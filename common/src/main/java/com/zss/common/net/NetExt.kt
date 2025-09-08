package com.zss.common.net

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.ActivityUtils
import com.zss.base.util.LL
import com.zss.base.util.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.cancellation.CancellationException

fun JSONObject.toRequestBody(): RequestBody {
    return this.toString().toRequestBody("application/json".toMediaTypeOrNull())
}

fun JSONArray.toRequestBody(): RequestBody {
    return this.toString().toRequestBody("application/json".toMediaTypeOrNull())
}

fun <T> ApiResponse<T>.getOrNull(): T? {
    if (message == "success") {
        return data
    } else {
        throw IMException(errCode = code.toString(), errMsg = message)
    }
}

fun toastErr(e: Throwable, show: Boolean) {
    if (show) {
        if (e.message.isNullOrEmpty()) {
            toast("操作失败!")
        } else {
            toast("${e.message}")
        }
    }
}


fun CoroutineScope.safeLaunch(
    //使用时注意线程切换
    context: CoroutineContext = Dispatchers.IO,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    onError: (Throwable) -> Unit = {},
    showDefaultToast: Boolean = false,
    block: suspend CoroutineScope.() -> Unit
): Job {
    return launch(context, start) {
        try {
            block()
        } catch (e: Throwable) {
            if (e is CancellationException) {
                return@launch
            }
            //TODO 401直接到登录页不再刷TOKEN
            if (e.message?.contains("HTTP 401") == true) {
                LL.e("xdd token失效")
                val topActivity = ActivityUtils.getTopActivity()
                topActivity.startActivity(
                    Intent(
                        topActivity,
                        Class.forName("com.zss.framaist.entrance.EntranceSplashActivity")
                    )
                )
                return@launch
//                val res = refreshToken(this)
//                if (res != null) {
//                    MMKV.defaultMMKV().encode(MMKVConstants.REFRESH_TOKEN, res.refresh_token)
//                    MMKV.defaultMMKV().encode(MMKVConstants.TOKEN, res.access_token)
//                    LL.e("xdd ${res.toJson()}")
//                    block()
//                    return@launch
//                } else {
//                    toast("刷新了token")
//                }
            }
            onError(e)
            toastErr(e, showDefaultToast)
            LL.e("协程异常${e}")
        }
    }
}


fun AppCompatActivity.safeLaunch(
    //使用时注意线程切换
    context: CoroutineContext = Dispatchers.Main,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    onError: (Throwable) -> Unit = {},
    showDefaultToast: Boolean = true,
    block: suspend CoroutineScope.() -> Unit
): Job {
    return lifecycleScope.safeLaunch(
        context = context,
        start = start,
        onError = onError,
        showDefaultToast,
        block = block
    )
}