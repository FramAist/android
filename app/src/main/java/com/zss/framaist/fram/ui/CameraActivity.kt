package com.zss.framaist.fram.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.RenderEffect
import android.graphics.Shader
import android.net.Uri
import android.os.Build
import android.os.CountDownTimer
import android.provider.Settings
import android.view.MotionEvent
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.SeekBar
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.camera.core.AspectRatio
import androidx.camera.core.ExperimentalGetImage
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.IntentCompat.getParcelableExtra
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.depthestimation.DepthHelper
import com.lxj.xpopup.core.CenterPopupView
import com.lxj.xpopup.util.XPopupUtils
import com.zss.base.BaseActivity
import com.zss.base.glide.ImageLoader.load
import com.zss.base.util.LL
import com.zss.base.util.clipOutline
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
import com.zss.framaist.bean.SuggestionStatus
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
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

class CameraActivity : BaseActivity<ActivityCameraBinding>() {

    val vm: CameraVM by viewModels()
    private var mCountDownTimer: CountDownTimer? = null

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
                cameraControl?.initCamera()
            }
        }
    var cameraControl: CameraControl? = null

    override fun initView() {
        val recommendData = getParcelableExtra(intent, IntentKey.DATA, RecommendModel::class.java)
        vm.setRecommendData(recommendData)
        cameraControl = CameraControl(this@CameraActivity)
        binding?.apply {
            layoutCamera.apply {
                cameraControl?.init(viewFinder)
                cameraControl?.initDefaultUseCaseGroup(viewFinder)
                ivRecommendPic.isVisible = recommendData != null
                if (recommendData != null) {
                    ivRecommendPic.load(this@CameraActivity, recommendData.image_url)
                    ivRecommendPic.clipOutline(8)
                }
            }
        }
    }

    override fun initData() {
        PermissionUtil.requestPermission(this, {
            cameraControl?.initCamera()
        }, {
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
        vm.submitPic.collectResumed(this) { resp ->
            resp ?: return@collectResumed
            val list = resp.suggestions
            if (resp.status == SuggestionStatus.FAILED.desc || resp.status == SuggestionStatus.TIMEOUT.desc || list.isNullOrEmpty()) {
                val errorInfo = resp.message ?: "构图失败!"
                toast(errorInfo)
                return@collectResumed
            }
            navTo<RecommendListActivity> {
                it.putParcelableArrayListExtra(IntentKey.RECOMMEND_LIST, ArrayList(list))
                it.putExtra(IntentKey.TASK_ID, resp.taskId)
            }
        }
        vm.aspectRatio.collectResumed(this) {
            binding?.apply {
                val width = XPopupUtils.getScreenWidth(this@CameraActivity)
                cameraControl?.updateImageCapture(it)
                val height = when (it) {
                    AspectRatio.RATIO_16_9 -> {
                        layoutCamera.tvRatio.text = "9:16"
                        (width * 16 / 9.0).toInt()
                    }

                    AspectRatio.RATIO_4_3 -> {
                        layoutCamera.tvRatio.text = "3:4"
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
                cameraControl?.initDefaultUseCaseGroup(layoutCamera.viewFinder)
                cameraControl?.bindAnalyze()
            }
        }
        vm.picDepth.collectResumed(this) {
            if (it == null) {
                showTooCloseTips(false)
                return@collectResumed
            }
            isTakingPhoto = false
            showTooCloseTips(it.isTooClose)
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
                        cameraControl?.bindAnalyze()
                    }

                    UiMode.PICTURE -> {
                        layoutPicture.root.isVisible = true
                        layoutLoading.root.isGone = true
                        if (DataHelper.tempPicture != null) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                layoutPicture.ivPreview.setRenderEffect(null)
                            } else {
                                Glide.with(this@CameraActivity)
                                    .load(DataHelper.tempPicture)
                                    .into(layoutPicture.ivPreview)
                            }
                        }
                    }

                    UiMode.LOADING -> {
                        layoutLoading.root.isVisible = true
                        if (DataHelper.tempPicture != null) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                val blurEffect = RenderEffect.createBlurEffect(
                                    20f, // 模糊半径X
                                    20f, // 模糊半径Y
                                    Shader.TileMode.CLAMP
                                )
                                layoutPicture.ivPreview.setRenderEffect(blurEffect)
                            } else {
                                Glide.with(this@CameraActivity)
                                    .load(DataHelper.tempPicture)
                                    .apply(RequestOptions.bitmapTransform(BlurTransformation(25)))
                                    .into(layoutPicture.ivPreview)
                            }
                        }
                    }
                }
            }
        }
        vm.recommendData.collectResumed(this) {
            binding?.layoutPicture?.tvSubmit?.text = if (it == null) "提交构图" else "保存到相册"
        }
        vm.lightMode.collectResumed(this) {
            val ivLight = binding?.layoutCamera?.ivLight ?: return@collectResumed
            cameraControl?.setLightMode(it)
            when (it) {
                LightMode.CLOSE -> {
                    ivLight.setImageResource(R.drawable.ic_close_flash)
                }

                LightMode.AUTO -> {
                    ivLight.setImageResource(R.drawable.ic_auto_flash)
                }

                LightMode.OPEN -> {
                    ivLight.setImageResource(R.drawable.ic_open_flash)
                }
            }
        }
    }

    @OptIn(ExperimentalGetImage::class)
    private fun takePhoto() {
        if (isTakingPhoto) {
            LL.e("xdd isTakingPhoto return")
            return
        }
        isTakingPhoto = true
        vm.clearPicture()
        vm.cancelAnalyze()
        LL.e("xdd 开始拍照  停止分析景深")
        try {
            playShutterEffect()
            cameraControl?.takePicture {
                vm.setUiMode(UiMode.PICTURE)
                vm.setPicture(it)
                vm.analyzeImage(it)
            }

        } catch (e: Exception) {
            isTakingPhoto = false
            vm.setUiMode(UiMode.CAMERA)
            LL.e("xdd $e")
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

    fun handleViewFinderOnTouch() {
        binding?.layoutCamera?.viewFinder?.setOnTouchListener { _, event ->
            binding?.layoutCamera?.viewFinder?.performClick()
            cameraControl?.scaleDetector?.onTouchEvent(event)
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    cameraControl?.doFocus(event.x, event.y)
                    showFocusAnimation(event.x, event.y)
                    showExposureSeekbar()
                    true
                }

                else -> false
            }
            true
        }
    }

    private fun bindCameraLayout() {
        binding?.layoutCamera?.apply {
            ivRecommendPic.setOnSingleClickedListener {
                navTo<RecommendDetailActivity> {
                    it.putExtra(IntentKey.DATA, vm.recommendData.value)
                    it.putExtra(IntentKey.SOURCE, SOURCE_CAMERA)
                }
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
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    p0: SeekBar?,
                    progress: Int,
                    p2: Boolean
                ) {
                    val camera = cameraControl?.camera ?: return
                    val cameraInfo = camera.cameraInfo
                    val range = cameraInfo.exposureState.exposureCompensationRange
                    val currentIndex =
                        (range.upper - range.lower) * progress * 1.0 / 100 - range.upper
                    camera.cameraControl.setExposureCompensationIndex(currentIndex.toInt())
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                    showExposureSeekbar()
                }
            })
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
                vm.setUiMode(UiMode.LOADING)
                //生成了图片,但是没分析完景深
                if (vm.tempPic.value != null && vm.picDepth.value == null) {
                    isPreSubmit = true
                    return@setOnSingleClickedListener
                }
                vm.submitPicture()
            }
        }
    }

    private fun playShutterEffect() {
        val anim = ScaleAnimation(
            1f, 0.9f, 1f, 0.9f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 100
            repeatCount = 1
            repeatMode = Animation.REVERSE
        }
        binding?.layoutCamera?.btnTakePhoto?.startAnimation(anim)

        // 播放系统快门声
//        val soundPool = SoundPool.Builder().build()
//        soundPool.load(this, android.provider.Settings.System.DEFAULT_CAMERA_SOUND_URI, 1)
    }


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

    fun showExposureSeekbar() {
        exposureJob?.cancel()
        binding?.layoutCamera?.seekBar?.isVisible = true
        exposureJob = safeLaunch {
            delay(3000)
            binding?.layoutCamera?.seekBar?.isVisible = false
        }
    }

}

inline fun <reified T : Any> Context.navTo(block: (intent: Intent) -> Unit = {}) {
    startActivity(Intent(this@navTo, T::class.java).apply(block))
}
