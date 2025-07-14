package com.zss.base.mvvm

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zss.base.BaseApplication
import com.zss.base.util.LL
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.FileNotFoundException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


/**
 * 发起网络请求的协程（ViewModel生命周期）
 * @param block 请求体
 * @param onError 失败回调
 * @param onComplete 完成回调
 */
fun ViewModel.launch(
    block: suspend CoroutineScope.() -> Unit,
    onError: (msg: String) -> Unit = {},
    onComplete: () -> Unit = {},
    showToast: Boolean = true,
) {
    viewModelScope.launch(CoroutineExceptionHandler { _, e ->
        LL.e("xdd $e")
        e.printStackTrace()
        if (e.message?.contains("Unauthorized") == true) {
            try {
                val context = BaseApplication.instance
                val intent =
                    Intent().setClassName(context, "com.zss.framaist.login.LoginActivity")
                        .apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK // 关键
                        }
                context.startActivity(intent)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            return@CoroutineExceptionHandler
        }
        when (e) {
            is HttpException -> onError(e.message ?: "Http Exception, Code: ${e.code()}")
            is SocketException -> onError("SocketException, message: ${e.message}")
            is SocketTimeoutException -> onError("网络超时")
            is UnknownHostException -> onError("暂无网络")
            else -> onError(e.message ?: "Coroutine Exception")
        }
        if (showToast) {
            //toast("${e.message}")
        }
        //AppUtil.uploadErrorIfNeeded(e)
    }) {
        try {
            block.invoke(this)
        } finally {
            onComplete()
        }
    }
}
