package com.zss.framaist.fram

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.OptIn
import androidx.camera.core.AspectRatio
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.ToastUtils
import com.example.depthestimation.DepthAnalysisResult
import com.example.depthestimation.DepthHelper
import com.example.depthestimation.DepthHelper.depthManager
import com.zss.base.mvvm.BaseVM
import com.zss.base.mvvm.launch
import com.zss.base.util.LL
import com.zss.common.net.getOrNull
import com.zss.common.net.toRequestBody
import com.zss.framaist.bean.LightMode
import com.zss.framaist.bean.RecommendModel
import com.zss.framaist.bean.SuggestionResp
import com.zss.framaist.bean.UiMode
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


class CameraVM : BaseVM<CameraRepo>() {

    /**
     * 倒计时
     */
    private val _countDownTime: MutableStateFlow<Int> = MutableStateFlow(0)
    val countDownTime = _countDownTime.asStateFlow()

    /**
     * 闪光灯模式
     */
    private val _lightMode: MutableStateFlow<LightMode> = MutableStateFlow(LightMode.CLOSE)
    val lightMode = _lightMode.asStateFlow()

    /**
     * 提交构图结果
     */
    private val _submitPic: MutableStateFlow<SuggestionResp?> = MutableStateFlow(null)
    val submitPic = _submitPic.asStateFlow()

    private val _uiMode: MutableStateFlow<UiMode> = MutableStateFlow(UiMode.CAMERA)
    val uiMode = _uiMode.asStateFlow()

    private val _aspectRatio: MutableStateFlow<Int> = MutableStateFlow(AspectRatio.RATIO_16_9)
    val aspectRatio = _aspectRatio.asStateFlow()

    /**
     * 推荐构图数据
     */
    private val _recommendData: MutableStateFlow<RecommendModel?> = MutableStateFlow(null)
    val recommendData = _recommendData.asStateFlow()

    private val _tempPic: MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    val tempPic = _tempPic.asStateFlow()

    private val _picDepth: MutableStateFlow<DepthAnalysisResult?> = MutableStateFlow(null)
    val picDepth = _picDepth.asStateFlow()

    var job: Job? = null

    fun setCountDown(time: Int) {
        _countDownTime.value = time
    }

    fun setAspectRatio(ratio: Int) {
        _aspectRatio.value = ratio
    }

    fun setPicture(bitmap: Bitmap?) {
        _tempPic.value = bitmap
        DataHelper.tempPicture = bitmap
    }

    fun clearPicture() {
        _tempPic.value = null
        DataHelper.tempPicture = null
        _picDepth.value = null
    }

    fun setUiMode(mode: UiMode) {
        LL.e("xdd 设置UI模式$mode")
        _uiMode.value = mode
    }

    fun setLightMode(mode: LightMode) {
        _lightMode.value = mode
    }

    fun setRecommendData(data: RecommendModel?) {
        _recommendData.value = data
    }

    /**
     * 提交构图,获取构图方案
     */
    suspend fun analyze(data: Bitmap): SuggestionResp? =
        suspendCancellableCoroutine<SuggestionResp?> { con ->
            launch({
                val radio = when (aspectRatio.value) {
                    AspectRatio.RATIO_16_9 -> "16:9"
                    AspectRatio.RATIO_4_3 -> "4:3"
                    else -> "${ScreenUtils.getScreenHeight()}:${ScreenUtils.getScreenWidth()}"
                }
                val res = repo.analyzeRemote(data, radio)
                LL.e("xdd $res")
                requireNotNull(res) { "解析构图失败!" }
                var suggestionRes: SuggestionResp? = null
                var repeatTime = 1
                var status = ""
                while (repeatTime < 40) {
                    delay(1000)
                    suggestionRes = repo.getSuggestion(res.task_id)
                    LL.e("xdd $suggestionRes")
                    status = suggestionRes?.status.toString()
                    suggestionRes?.taskId = res.task_id
                    if (status != "pending" && status != "processing") {
                        con.resume(suggestionRes)
                        return@launch
                    }
                    delay(1000)
                    repeatTime++
                    LL.e("xdd 循环次数:$repeatTime")
                }
                con.resumeWithException(IllegalArgumentException("构图超时!"))
            }, {
                LL.e("xdd 构图失败!$it")
                con.resumeWithException(IllegalArgumentException(it))
            })
        }


    suspend fun startPicture(imageCapture: ImageCapture, context: Context) =
        suspendCancellableCoroutine<Bitmap> { con ->
            var isResumed = false
            imageCapture.takePicture(
                ContextCompat.getMainExecutor(context),
                object : ImageCapture.OnImageCapturedCallback() {
                    @OptIn(ExperimentalGetImage::class)
                    override fun onCaptureSuccess(image: ImageProxy) {
                        super.onCaptureSuccess(image)
                        LL.e("xdd ${image.image}  ${image.width} ${image.height} ${image.imageInfo} ")
                        if (!isResumed) {
                            val res = image.toBitmap()
                            con.resume(res)
                            isResumed = true
                        }
                    }

                    override fun onError(exception: ImageCaptureException) {
                        super.onError(exception)
                        if (!isResumed) {
                            con.resumeWithException(exception)
                        }
                    }
                })
        }


    fun getOutputOptions(context: Context) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, System.currentTimeMillis().toString())
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/FramAist")
            }
        }
        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            context.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ).build()
    }

    suspend fun confirmSuggestion(taskId: String?, suggestionId: String) =
        suspendCancellableCoroutine<String?> { con ->
            val jb = JSONObject().apply {
                put("task_id", taskId)
                put("suggestion_id", suggestionId)
            }
            launch({
                val res = repo.api.compositionConfirm(jb.toRequestBody()).getOrNull()
                con.resume(res)
            }, {
                con.resumeWithException(java.lang.IllegalArgumentException(it))
            })
        }

    fun cancelAnalyze() {
        job?.cancel()
    }

    fun analyzeImage(bitmap: Bitmap) {
        job?.cancel()
        job = launch({
            DepthHelper.isPredicting = true
            val result = depthManager.predictFromBitmapAsync(bitmap)
            LL.e("xdd 分析景深 ${result?.isTooClose}")
            DepthHelper.isPredicting = false
            result?.bitmap = bitmap
            _picDepth.value = result
        }, {
            DepthHelper.isPredicting = false
            LL.e("xdd $it")
        })
    }

    fun submitPicture() {
        launch({
            val bitmap = DataHelper.tempPicture
            LL.e("xdd $bitmap")
            requireNotNull(bitmap) { "获取图片失败!" }
            if (_picDepth.value?.isTooClose == true) {
                ToastUtils.showShort("距离过近!")
                return@launch
            }
            setUiMode(UiMode.LOADING)
            val res = analyze(bitmap)
            _submitPic.value = res
            //接口返回失败时允许再次提交
            if (res?.status == "failed") {
                setUiMode(UiMode.PICTURE)
            } else {
                setUiMode(UiMode.CAMERA)
            }
        }, {
            ToastUtils.showShort(it)
            setUiMode(UiMode.PICTURE)
        })
    }
}