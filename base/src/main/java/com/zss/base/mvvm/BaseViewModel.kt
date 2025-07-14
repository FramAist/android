package com.zss.base.mvvm

import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {

    fun toast(msg: String) {
        if (msg.isNotEmpty()) {
            //ToastUtils.showSystem(msg)
        }
    }

}