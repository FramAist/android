package com.zss.base

import android.app.Application
import android.os.Build
import android.webkit.WebView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.bumptech.glide.Glide
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import me.jessyan.autosize.AutoSize
import me.jessyan.autosize.AutoSizeConfig
import me.jessyan.autosize.DefaultAutoAdaptStrategy

val appViewModel: AppViewModel by lazy { BaseApplication.appViewModelInstance }

class BaseApplication : Application(), ViewModelStoreOwner {
    val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    override fun onLowMemory() {
        super.onLowMemory()
        //内存低的，glide清理内存
        Glide.get(this).clearMemory()
    }

    companion object {
        lateinit var instance: BaseApplication
        lateinit var appViewModelInstance: AppViewModel
    }

    private lateinit var mAppViewModelStore: ViewModelStore

    override fun onCreate() {
        super.onCreate()
        mAppViewModelStore = ViewModelStore()
        instance = this
        appViewModelInstance = ViewModelProvider(this)[AppViewModel::class.java]
        //字体适配初始化
        AutoSize.checkAndInit(this)
        AutoSizeConfig.getInstance()
            .setAutoAdaptStrategy(DefaultAutoAdaptStrategy())
            .setLog(true)
            .setExcludeFontScale(true)
        MMKV.initialize(this)
        //初始化的时候设置webView目录
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val processName = getProcessName()
            //非主进程使用其他目录
            if (processName != BaseApplication.instance.packageName) {
                WebView.setDataDirectorySuffix(processName)
            }
        }
    }

    override val viewModelStore: ViewModelStore
        get() = mAppViewModelStore
}