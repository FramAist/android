package com.zss.framaist.fram.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.net.Uri
import android.os.CountDownTimer
import android.provider.Settings
import android.util.Rational
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ExperimentalZeroShutterLag
import androidx.camera.core.ExposureState
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.core.UseCaseGroup
import androidx.camera.core.ViewPort
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.takePicture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.IntentCompat.getParcelableExtra
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.depthestimation.DepthHelper
import com.lxj.xpopup.core.CenterPopupView
import com.lxj.xpopup.util.XPopupUtils
import com.zss.base.BaseActivity
import com.zss.base.BaseApplication
import com.zss.base.glide.ImageLoader.loadImage
import com.zss.base.util.LL
import com.zss.base.util.collectResumed
import com.zss.base.util.dp2px
import com.zss.base.util.setOnSingleClickedListener
import com.zss.base.util.toast
import com.zss.common.constant.IntentKey
import com.zss.common.net.safeLaunch
import com.zss.common.util.PermissionUtil
import com.zss.framaist.R
import com.zss.framaist.bean.LightMode
import com.zss.framaist.bean.RecommendModel
import com.zss.framaist.bean.UiMode
import com.zss.framaist.databinding.ActivityCameraBinding
import com.zss.framaist.fram.CameraDialogHelper
import com.zss.framaist.fram.CameraVM
import com.zss.framaist.fram.DataHelper
import com.zss.framaist.home.RecommendDetailActivity
import com.zss.framaist.home.RecommendListActivity
import com.zss.framaist.home.SOURCE_CAMERA
import com.zss.framaist.util.DialogHelper.showClockPopView
import com.zss.framaist.util.DialogHelper.showRatioPopView
import com.zss.framaist.util.DialogHelper.showSplashPopView
import com.zss.framaist.util.DialogHelper.showTooCloseTips
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.math.roundToInt

class CameraActivity : BaseActivity<ActivityCameraBinding>() {

    val vm: CameraVM by viewModels()
    private var mCountDownTimer: CountDownTimer? = null
    private var currentExposureIndex = 0

    private var camera: Camera? = null
    var imageCapture: ImageCapture? = null

    val preview: Preview by lazy {
        Preview.Builder().build().apply {
            binding ?: return@apply
            surfaceProvider = binding!!.layoutCamera.viewFinder.surfaceProvider
        }
    }

    val imageAnalysis: ImageAnalysis by lazy {
        ImageAnalysis.Builder()
            .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_YUV_420_888)
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
    }

    var enableExposure = true

    val cameraProviderFuture by lazy {
        ProcessCameraProvider.getInstance(this)
    }

    //是否正在拍照
    private var isTakingPhoto = false

    // analyse图片时,是否点击了提交构图
    private var isPreSubmit = false

    private var exposureJob: Job? = null

    private val requestPermissionDialog: CenterPopupView by lazy {
        CameraDialogHelper.getGoToPermissionDialog(this, onConfirm = {
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            intent.data = Uri.fromParts("package", packageName, null)
            resultLauncher.launch(intent)
        })
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ ->
            if (PermissionUtil.isCameraPermissionGranted(this)) {
                requestPermissionDialog.dismiss()
                initCamera()
            }
        }

    /**
     * 双指缩放手势
     */
    private val scaleDetector by lazy {
        ScaleGestureDetector(this, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            var currentZoom: Float = 1.0f
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                camera ?: return true
                currentZoom = camera!!.cameraInfo.zoomState.value?.zoomRatio ?: 1.0f
                val newZoom = currentZoom * detector.scaleFactor
                camera!!.cameraControl.setZoomRatio(newZoom.coerceIn(1.0f, 10.0f)) // 限制缩放范围
                return true
            }
        })
    }

    override fun initView() {
        val recommendData = getParcelableExtra(intent, IntentKey.DATA, RecommendModel::class.java)
        vm.setRecommendData(recommendData)
        binding?.apply {
            layoutCamera.apply {
                ivRecommendPic.isVisible = recommendData != null
                if (recommendData != null) {
                    val lp = ivRecommendPic.layoutParams
                    lp.width = 100.dp2px()
                    lp.height = 150.dp2px()
                    ivRecommendPic.layoutParams = lp
                    ivRecommendPic.loadImage(
                        this@CameraActivity,
                        recommendData.image_url.toString()
                    )
                }
            }
        }
    }

    override fun initData() {
        PermissionUtil.requestPermission(this, { initCamera() }, {
            requestPermissionDialog.show()
        })
    }

    override fun bindingEvent() {
        super.bindingEvent()
        bindPictureLayout()
        bindCameraLayout()
    }

    @SuppressLint("SetTextI18n")
    override fun observe() {
        super.observe()
        vm.submitPic.collectResumed(this) {
            it ?: return@collectResumed
            val list = it.suggestions
            if (it.status == "failed" || list.isNullOrEmpty()) {
                toast("构图失败!")
                return@collectResumed
            }
            val intent = Intent(BaseApplication.instance, RecommendListActivity::class.java)
            intent.putParcelableArrayListExtra(IntentKey.RECOMMEND_LIST, ArrayList(list))
            intent.putExtra(IntentKey.TASK_ID, it.taskId)
            startActivity(intent)
        }
        vm.aspectRatio.collectResumed(this) {
            binding?.apply {
                val width = XPopupUtils.getScreenWidth(this@CameraActivity)
                val height = when (it) {
                    AspectRatio.RATIO_16_9 -> {
                        layoutCamera.tvRatio.text = "16:9"
                        (width * 16 / 9.0).toInt()
                    }

                    AspectRatio.RATIO_4_3 -> {
                        layoutCamera.tvRatio.text = "4:3"
                        (width * 4 / 3.0).toInt()
                    }

                    else -> {
                        layoutCamera.tvRatio.text = "全屏"
                        XPopupUtils.getScreenHeight(this@CameraActivity)
                    }
                }
                val lp = layoutCamera.viewFinder.layoutParams
                lp.width = width
                lp.height = height
                layoutCamera.viewFinder.layoutParams = lp
                bindAnalyze()
            }
        }
        vm.picDepth.collectResumed(this) {
            if (it == null) {
                showTooCloseTips(false)
                return@collectResumed
            }
            isTakingPhoto = false
            showTooCloseTips(it.isTooClose)
            dismissLoading()
            if (isPreSubmit) {
                isPreSubmit = false
                vm.submitPicture()
            }
        }
        vm.tempPic.collectResumed(this) {
            it ?: return@collectResumed
            binding ?: return@collectResumed
            Glide.with(this@CameraActivity).load(it).into(binding!!.layoutPicture.ivPreview)
        }
        vm.uiMode.collectResumed(this) {
            binding?.apply {
                when (it) {
                    UiMode.CAMERA -> {
                        layoutPicture.root.isGone = true
                        layoutLoading.root.isGone = true
                        bindAnalyze()
                    }

                    UiMode.PICTURE -> {
                        layoutPicture.root.isVisible = true
                        layoutLoading.root.isGone = true
                    }

                    UiMode.LOADING -> {
                        layoutPicture.root.isGone = true
                        layoutLoading.root.isVisible = true
                    }
                }
            }
        }
        vm.recommendData.collectResumed(this) {
            binding?.layoutPicture?.tvSubmit?.text = if (it == null) "提交构图" else "保存到相册"
        }
        vm.lightMode.collectResumed(this) {
            val ivLight = binding?.layoutCamera?.ivLight ?: return@collectResumed
            when (it) {
                LightMode.CLOSE -> ivLight.setImageResource(R.drawable.ic_close_flash)
                LightMode.AUTO -> ivLight.setImageResource(R.drawable.ic_auto_flash)
                LightMode.OPEN -> ivLight.setImageResource(R.drawable.ic_open_flash)
            }
        }
    }

    @OptIn(ExperimentalZeroShutterLag::class)
    @SuppressLint("RestrictedApi")
    private fun initCamera() {
        binding?.apply {
            cameraProviderFuture.addListener(
                { bindAnalyze() },
                ContextCompat.getMainExecutor(this@CameraActivity)
            )
        }
    }

    @OptIn(ExperimentalGetImage::class)
    private fun takePhoto() {
        if (isTakingPhoto) return
        isTakingPhoto = true
        vm.clearPicture()
        vm.cancelAnalyze()
        binding?.layoutCamera?.apply {
            viewFinder.post {
                imageCapture = when (vm.aspectRatio.value) {
                    AspectRatio.RATIO_DEFAULT ->
                        getImageCapture(AspectRatioStrategy.RATIO_16_9_FALLBACK_AUTO_STRATEGY)

                    AspectRatio.RATIO_16_9 -> getImageCapture(AspectRatioStrategy.RATIO_16_9_FALLBACK_AUTO_STRATEGY)
                    AspectRatio.RATIO_4_3 -> getImageCapture(AspectRatioStrategy.RATIO_4_3_FALLBACK_AUTO_STRATEGY)
                    else -> getImageCapture(AspectRatioStrategy.RATIO_16_9_FALLBACK_AUTO_STRATEGY)
                }
                try {
                    cameraProviderFuture.get().unbind(preview, imageAnalysis)
                    camera = cameraProviderFuture.get().bindToLifecycle(
                        this@CameraActivity,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        imageCapture
                    )
                    //设置闪光灯会让拍照时间  从800ms到1000ms
                    imageCapture?.flashMode = when (vm.lightMode.value) {
                        LightMode.OPEN -> ImageCapture.FLASH_MODE_ON
                        LightMode.CLOSE -> ImageCapture.FLASH_MODE_OFF
                        LightMode.AUTO -> ImageCapture.FLASH_MODE_AUTO
                    }
                    safeLaunch {
                        //TODO  有延迟,等待相机初始化,  待优化
                        delay(100)
                        val before = System.currentTimeMillis()
                        val res = imageCapture?.takePicture()
                        val end = System.currentTimeMillis()
                        LL.e("xdd 总拍摄时间: ${end - before}")
                        val rotationDegrees = res?.imageInfo?.rotationDegrees ?: return@safeLaunch
                        val bitmap = res.toBitmap() ?: return@safeLaunch
                        val correctedBitmap = rotateBitmap(bitmap, rotationDegrees) // 旋转修正
                        vm.setUiMode(UiMode.PICTURE)
                        vm.setPicture(correctedBitmap)
                        vm.analyzeImage(correctedBitmap)
                        res.close() // 必须手动释放资源！
                    }
                } catch (e: Exception) {
                    LL.e("xdd $e")
                }
            }
        }
    }

    fun startCountDown(time: Int) {
        mCountDownTimer?.cancel()
        val tvTime = binding?.layoutCamera?.tvCountDownTime ?: return
        tvTime.text = time.toString()
        mCountDownTimer = object : CountDownTimer(1000L * time, 1000) {
            override fun onTick(p0: Long) {
                tvTime.text = (p0 / 1000.00).roundToInt().toString()
            }

            override fun onFinish() {
                takePhoto()
                tvTime.text = ""
            }
        }
        mCountDownTimer?.start()
    }

    /**
     * 保存图片
     */
    private fun savePicture() {
        val image = DataHelper.tempPicture ?: return
        showLoading()
        val res =
            DepthHelper.saveBitmapToGallery(this, image, System.currentTimeMillis().toString())
        dismissLoading()
        if (res) {
            resetPicture()
            startActivity(Intent(this@CameraActivity, PictureSavedActivity::class.java))
        }
    }

    fun resetPicture() {
        vm.setUiMode(UiMode.CAMERA)
        binding?.layoutCamera?.ivRecommendPic?.isVisible = false
        isTakingPhoto = false
        isPreSubmit = false
        dismissLoading()
        vm.setRecommendData(null)
        vm.setPicture(null)
    }

    override fun onDestroy() {
        resetPicture()
        super.onDestroy()
    }

    /**
     * 设置曝光条进度
     */
    private fun setUpExposureProgress(current: Int, state: ExposureState) {
        binding?.layoutCamera?.groupProgress?.isVisible = true
        val range = state.exposureCompensationRange
        val progress = ((current - range.lower).toFloat() / (range.upper - range.lower)) * 100
        binding?.layoutCamera?.exposureProgress?.progress = progress.toInt()
        exposureJob?.cancel()
        exposureJob = lifecycleScope.launch {
            delay(2000)
            binding?.layoutCamera?.groupProgress?.isGone = true
        }
        exposureJob?.start()
    }

    /**
     * 聚焦动画
     */
    private fun showFocusAnimation(xF: Float, yF: Float) {
        val layoutCamera = binding?.layoutCamera ?: return
        layoutCamera.ivFocus.apply {
            isVisible = true
            alpha = 1f
            val lp = layoutParams as ConstraintLayout.LayoutParams
            lp.startToStart = layoutCamera.viewFinder.id
            lp.topToTop = layoutCamera.viewFinder.id
            lp.leftMargin = xF.toInt() - 28.dp2px()
            lp.topMargin = yF.toInt() - 28.dp2px()
            layoutParams = lp
            animate().scaleX(1f).scaleY(1f).setDuration(0).start()
            requestLayout()
            animate().scaleX(0.8f).scaleY(0.8f).setDuration(100).start()
            postDelayed({
                animate().alpha(0f).setDuration(300).start()
            }, 1000)
        }
    }

    fun handleViewFinderOnTouch() {
        var lastX = 0f
        var lastY = 0f
        binding?.layoutCamera?.viewFinder?.setOnTouchListener { _, event ->
            binding?.layoutCamera?.viewFinder?.performClick()
            scaleDetector.onTouchEvent(event)
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // 距离上次触摸点300内激活曝光调整
                    enableExposure = !(abs(event.x - lastX) > 300 || abs(event.y - lastY) > 300)
                    lastX = event.x
                    lastY = event.y
                    doFocus(event.x, event.y)
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    if (!enableExposure) return@setOnTouchListener false
                    val cameraInfo = camera?.cameraInfo ?: return@setOnTouchListener false
                    val dx = event.x - lastX
                    val dy = event.y - lastY
                    //拦截距离上次对焦位置100内的触摸事件
                    if (abs(dx) + abs(dy) > 5 && abs(dx) < abs(dy)) {
                        if (dy > 3) currentExposureIndex -= 1
                        if (dy < 3) currentExposureIndex += 1
                        camera?.cameraControl?.setExposureCompensationIndex(currentExposureIndex)
                        setUpExposureProgress(currentExposureIndex, cameraInfo.exposureState)
                    }
                    lastX = event.x
                    lastY = event.y
                    true
                }

                else -> false
            }
            true
        }
    }

    private fun doFocus(x: Float, y: Float) {
        // 创建 MeteringPoint
        val factory = binding!!.layoutCamera.viewFinder.meteringPointFactory
        val point = factory.createPoint(x, y)
        // 创建对焦动作
        val action = FocusMeteringAction.Builder(point)
            .setAutoCancelDuration(3, TimeUnit.SECONDS)
            .build()
        val cameraControl = camera?.cameraControl ?: return
        // 触发对焦
        cameraControl.startFocusAndMetering(action)
        showFocusAnimation(x, y)
    }

    private fun bindCameraLayout() {
        binding?.layoutCamera?.apply {
            ivRecommendPic.setOnSingleClickedListener {
                startActivity(
                    Intent(
                        this@CameraActivity,
                        RecommendDetailActivity::class.java
                    ).apply {
                        putExtra(IntentKey.DATA, vm.recommendData.value)
                        putExtra(IntentKey.SOURCE, SOURCE_CAMERA)
                    })
            }
            handleViewFinderOnTouch()
            btnTakePhoto.setOnSingleClickedListener {
                mCountDownTimer?.cancel()
                if (vm.countDownTime.value > 0) {
                    startCountDown(vm.countDownTime.value)
                } else {
                    takePhoto()
                }
            }
            ivClose.setOnSingleClickedListener {
                if (binding?.layoutPicture?.root?.isVisible == true) {
                    binding?.layoutPicture?.root?.isGone = true
                } else {
                    finish()
                }
            }
            ivLight.setOnSingleClickedListener {
                showSplashPopView()
            }
            ivClock.setOnClickListener {
                showClockPopView()
            }
            tvRatio.setOnClickListener {
                showRatioPopView()
            }
        }

    }

    private fun bindPictureLayout() {
        binding?.layoutPicture?.apply {
            btnRePicture.setOnSingleClickedListener {
                vm.setUiMode(UiMode.CAMERA)
            }
            btnSave.setOnSingleClickedListener {
                // 保存图片
                if (vm.recommendData.value != null) {
                    savePicture()
                    return@setOnSingleClickedListener
                }
                //提交构图
                if (vm.picDepth.value?.isTooClose == true) {
                    toast("距离过近!")
                    return@setOnSingleClickedListener
                }
                //生成了图片,但是没分析完景深
                if (vm.tempPic.value != null && vm.picDepth.value == null) {
                    showLoading()
                    isPreSubmit = true
                    return@setOnSingleClickedListener
                }
                vm.submitPicture()
            }
        }

    }

    // 旋转 Bitmap 的方法
    fun rotateBitmap(bitmap: Bitmap, degrees: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }


    @SuppressLint("RestrictedApi")
    fun getImageCapture(ratio: AspectRatioStrategy): ImageCapture {
        return ImageCapture.Builder()
            .setBufferFormat(ImageFormat.YUV_420_888)
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY) // 优先降低延迟
            .setResolutionSelector(
                ResolutionSelector.Builder()
                    .setAspectRatioStrategy(ratio)
                    .build()
            )
            .build()
    }

    /**
     * 绑定imageAnalysis
     */
    fun bindAnalyze() {
        // 设置 ViewPort
        binding?.layoutCamera?.apply {
            viewFinder.post {
                val rational = Rational(viewFinder.width, viewFinder.height)
                val viewPort =
                    ViewPort.Builder(rational, viewFinder.display.rotation).build()
                try {
                    //默认最多支持3个UseCase,take photo时再手动解绑
                    val useCaseGroup = UseCaseGroup.Builder()
                        .addUseCase(preview)
                        .addUseCase(imageAnalysis)
                        .setViewPort(viewPort)
                        .build()
                    imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor()) { imageProxy ->
                        //检测景深,画面等,目前仅支持景深
                        if (DepthHelper.isInitialized && !DepthHelper.isPredicting) {
                            vm.analyzeImage(imageProxy.toBitmap())
                        }
                        imageProxy.close()
                    }
                    cameraProviderFuture.get().unbindAll()
                    camera = cameraProviderFuture.get().bindToLifecycle(
                        this@CameraActivity,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        useCaseGroup
                    )
                } catch (e: Exception) {
                    LL.e("xdd $e")
                }
            }
        }


    }

}