package com.zss.base

import android.app.Application
import android.webkit.WebView
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.bumptech.glide.Glide
import com.tencent.mmkv.MMKV
import dagger.hilt.android.HiltAndroidApp


open class BaseApplication : Application(), ViewModelStoreOwner {
    override fun onLowMemory() {
        super.onLowMemory()
        //内存低的，glide清理内存
        Glide.get(this).clearMemory()
    }

    companion object {
        lateinit var instance: BaseApplication
    }

    private lateinit var mAppViewModelStore: ViewModelStore

    override fun onCreate() {
        super.onCreate()
        mAppViewModelStore = ViewModelStore()
        instance = this
        MMKV.initialize(this)
        //初始化的时候设置webView目录
        val processName = getProcessName()
        //非主进程使用其他目录
        if (processName != instance.packageName) {
            WebView.setDataDirectorySuffix(processName)
        }
    }

    override val viewModelStore: ViewModelStore
        get() = mAppViewModelStore
}