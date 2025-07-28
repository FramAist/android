package com.zss.framaist.fram.ui

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.util.Rational
import android.view.ScaleGestureDetector
import androidx.annotation.OptIn
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalZeroShutterLag
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.core.UseCase
import androidx.camera.core.UseCaseGroup
import androidx.camera.core.ViewPort
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.example.depthestimation.DepthHelper
import com.google.common.util.concurrent.ListenableFuture
import com.zss.base.util.LL
import com.zss.framaist.bean.LightMode
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class CameraControl(val activity: CameraActivity) {
    var camera: Camera? = null
    var imageCapture: ImageCapture? = null

    var preview: Preview = Preview.Builder().build()

    var viewFinder: PreviewView? = null

    var viewPort: ViewPort? = null


    val imageAnalysis: ImageAnalysis by lazy {
        ImageAnalysis.Builder()
            .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_YUV_420_888)
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
    }

    var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>? = null

    var commonUseCaseGroup: UseCaseGroup? = null

    val scaleDetector by lazy {
        ScaleGestureDetector(
            activity,
            object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
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

    fun init(viewFinder: PreviewView) {
        this.viewFinder = viewFinder
        preview.surfaceProvider = viewFinder.surfaceProvider
        cameraProviderFuture = ProcessCameraProvider.getInstance(activity)
    }

    @OptIn(ExperimentalZeroShutterLag::class)
    @SuppressLint("RestrictedApi")
    fun initCamera() {
        cameraProviderFuture?.addListener(
            { bindAnalyze() },
            ContextCompat.getMainExecutor(activity)
        )
    }

    /**
     * 绑定imageAnalysis
     */
    fun bindAnalyze() {
        //有推荐构图后不再检测景深
        if (activity.vm.recommendData.value != null) return
        imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor()) { imageProxy ->
            //检测景深,画面等,目前仅支持景深
            if (DepthHelper.isInitialized && !DepthHelper.isPredicting) {
                activity.vm.analyzeImage(imageProxy.toBitmap())
            }
            imageProxy.close()
        }
        cameraProviderFuture?.get()?.unbind(imageCapture)
        bindUseCase(imageAnalysis)
    }

    fun bindUseCase(useCase: UseCase) {
        cameraProviderFuture?.get()?.bindToLifecycle(
            activity, CameraSelector.DEFAULT_BACK_CAMERA, useCase
        )
    }

    fun initDefaultUseCaseGroup(viewFinder: PreviewView) {
        this.viewFinder = viewFinder
        viewFinder.apply {
            post {
                val rational = Rational(width, height)
                this@CameraControl.viewPort = ViewPort.Builder(rational, display.rotation).build()
                commonUseCaseGroup =
                    UseCaseGroup.Builder()
                        .addUseCase(preview)
                        .setViewPort(viewPort!!)
                        .build()
                camera = cameraProviderFuture?.get()?.bindToLifecycle(
                    activity, CameraSelector.DEFAULT_BACK_CAMERA, commonUseCaseGroup!!
                )
            }
        }

    }

    fun updateImageCapture(ratio: Int) {
        when (ratio) {
            AspectRatio.RATIO_16_9 -> {
                imageCapture =
                    getImageCapture(AspectRatioStrategy.RATIO_16_9_FALLBACK_AUTO_STRATEGY)
            }

            AspectRatio.RATIO_4_3 -> {
                imageCapture =
                    getImageCapture(AspectRatioStrategy.RATIO_4_3_FALLBACK_AUTO_STRATEGY)
            }

            else -> {
                imageCapture =
                    getImageCapture(AspectRatioStrategy.RATIO_16_9_FALLBACK_AUTO_STRATEGY)
            }
        }

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

    fun setLightMode(mode: LightMode) {
        when (mode) {
            LightMode.CLOSE -> {
                imageCapture?.flashMode = ImageCapture.FLASH_MODE_OFF
            }

            LightMode.AUTO -> {
                imageCapture?.flashMode = ImageCapture.FLASH_MODE_AUTO
            }

            LightMode.OPEN -> {
                imageCapture?.flashMode = ImageCapture.FLASH_MODE_ON
            }
        }

    }

    fun takePicture(onSuccess: (bitmap: Bitmap) -> Unit) {
        cameraProviderFuture?.get()?.unbind(imageAnalysis)
        bindUseCase(imageCapture!!)
        val before = System.currentTimeMillis()
        imageCapture?.takePicture(
            Executors.newSingleThreadExecutor(),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureStarted() {
                    super.onCaptureStarted()
                    LL.e("xdd onCaptureStarted ${System.currentTimeMillis() - before}")
                }

                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)
                    val end = System.currentTimeMillis()
                    LL.e("xdd 总拍摄时间: ${end - before}")
                    val rotationDegrees = image.imageInfo.rotationDegrees
                    val bitmap = image.toBitmap()
                    val correctedBitmap = rotateBitmap(bitmap, rotationDegrees) // 旋转修正
                    onSuccess(correctedBitmap)
                    image.close() // 必须手动释放资源！
                }
            })
    }

    // 旋转 Bitmap 的方法
    fun rotateBitmap(bitmap: Bitmap, degrees: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }


    fun doFocus(x: Float, y: Float) {
        // 创建 MeteringPoint
        val factory = viewFinder?.meteringPointFactory ?: return
        val point = factory.createPoint(x, y)
        // 创建对焦动作
        val action = FocusMeteringAction.Builder(point)
            .setAutoCancelDuration(3, TimeUnit.SECONDS)
            .build()
        camera?.cameraControl?.startFocusAndMetering(action)
    }

}