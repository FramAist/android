package com.zss.framaist.fram

import android.graphics.Bitmap
import androidx.camera.core.AspectRatio
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
import com.zss.common.util.MMKVUtil
import com.zss.framaist.bean.LightMode
import com.zss.framaist.bean.RecommendModel
import com.zss.framaist.bean.SuggestionResp
import com.zss.framaist.bean.SuggestionStatus
import com.zss.framaist.bean.UiMode
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

const val ANALYZE_INTERNAL = 40

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

    /**
     * 拍摄的构图场景图片
     */
    private val _tempPic: MutableStateFlow<Pair<Bitmap?, Int>> = MutableStateFlow(Pair(null, 0))
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

    fun setPicture(bitmap: Bitmap?, orientation: Int) {
        _tempPic.value = Pair(bitmap, orientation)
        DataHelper.tempPicture = bitmap
    }

    fun clearPicture() {
        _tempPic.value = Pair(null, 0)
        DataHelper.tempPicture = null
        _picDepth.value = null
    }

    fun setUiMode(mode: UiMode) {
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
    suspend fun analyze(data: Bitmap, orientation: Int): SuggestionResp? =
        suspendCancellableCoroutine<SuggestionResp?> { con ->
            var isResumed = false
            var isPortrait = when (orientation) {
                in 45 until 135 -> false
                in 135 until 225 -> true
                in 225 until 315 -> false
                else -> true
            }
            launch({
                val ratio = when (aspectRatio.value) {
                    AspectRatio.RATIO_16_9 -> getFixedRatio(9, 16, isPortrait)
                    AspectRatio.RATIO_4_3 -> getFixedRatio(3, 4, isPortrait)
                    else -> getFixedRatio(
                        ScreenUtils.getScreenWidth(),
                        ScreenUtils.getScreenHeight(),
                        isPortrait
                    )
                }
                val res = repo.analyzeRemote(data, ratio)
                LL.e("xdd $res $ratio")
                requireNotNull(res) { "解析构图失败!" }
                var repeatTime = 1
                while (repeatTime < ANALYZE_INTERNAL) {
                    delay(1000)
                    val suggestionRes = repo.getSuggestion(res.task_id)
                    suggestionRes?.taskId = res.task_id
                    when (suggestionRes?.status) {
                        SuggestionStatus.PENDING.desc, SuggestionStatus.PROCESSING.desc -> {
                            delay(1000)
                        }

                        null -> {
                            if (!isResumed) {
                                isResumed = true
                                con.resumeWithException(IllegalArgumentException("获取构图结果失败!"))
                            }
                        }

                        else -> {
                            if (!isResumed) {
                                isResumed = true
                                con.resume(suggestionRes)
                            }
                        }
                    }
                    repeatTime++
                }
                if (!isResumed) {
                    isResumed = true
                    con.resumeWithException(IllegalArgumentException("构图超时!"))
                }
            }, {
                if (!isResumed) {
                    isResumed = true
                    con.resumeWithException(IllegalArgumentException(it))
                }
            })
        }

    suspend fun confirmSuggestion(taskId: String?, suggestionId: String) =
        suspendCancellableCoroutine<String?> { con ->
            val jb = JSONObject().apply {
                put("task_id", taskId)
                put("suggestion_id", suggestionId)
            }
            launch({
                val res = repo.api.compositionConfirm(jb.toRequestBody()).getOrNull()
                LL.e("xdd $res $taskId $suggestionId ${MMKVUtil.getToken()}")
                con.resume(res.toString())
            }, {
                LL.e("xdd $it")
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
            //TODO  先关闭景深检测
            val result = depthManager.predictFromBitmapAsync(bitmap)?.copy(isTooClose = false)
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
            val bitmap = tempPic.value.first
            requireNotNull(bitmap) { "获取图片失败!" }
            if (_picDepth.value?.isTooClose == true) {
                ToastUtils.showShort("距离过近!")
                setUiMode(UiMode.PICTURE)
                return@launch
            }
            val res = analyze(bitmap, tempPic.value.second)
            _submitPic.value = res
            //接口返回失败时允许再次提交
            if (res?.status == SuggestionStatus.FAILED.desc || res?.status == SuggestionStatus.TIMEOUT.desc) {
                setUiMode(UiMode.PICTURE)
            } else {
                setUiMode(UiMode.CAMERA)
            }
        }, {
            ToastUtils.showShort(it)
            setUiMode(UiMode.PICTURE)
        })
    }

    fun getFixedRatio(width: Int, height: Int, isPortrait: Boolean): String {
        return if (isPortrait) {
            "${width}:$height"
        } else {
            "${height}:$width"
        }
    }
}