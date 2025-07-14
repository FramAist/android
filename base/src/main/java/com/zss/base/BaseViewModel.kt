package com.zss.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

open class BaseViewModel : ViewModel() {

    private val _showWaiting = MutableSharedFlow<Boolean>()
    val showWaiting: SharedFlow<Boolean> = _showWaiting

    fun toast(msg: String) {
        if (msg.isNotEmpty()) {
            //ToastUtils.showSystem(msg)
        }
    }

    suspend fun showWaiting(isShow: Boolean) {
        _showWaiting.emit(isShow)
        delay(10_000)
        _showWaiting.emit(false)
    }

}