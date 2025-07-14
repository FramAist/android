package com.zss.base


import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.gyf.immersionbar.ktx.immersionBar
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.impl.LoadingPopupView
import com.zss.base.util.saveAs
import com.zss.base.util.saveAsUnChecked
import kotlinx.coroutines.Job
import me.jessyan.autosize.AutoSize
import me.jessyan.autosize.AutoSizeCompat
import java.lang.reflect.ParameterizedType


abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    var binding: VB? = null
    var loadingPop: LoadingPopupView? = null
    var loadingJob: Job? = null

    @SuppressLint("SourceLockedOrientationActivity")
    open override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        //overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        //AppGlobal.sCurrentActivity = this
        //预处理
        preInit()
        if (!childOverWriteBind()) {
            bindView()?.let(::setContentView)
        }
        //app仅支持竖屏
        //requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        supportActionBar?.hide()
        //初始化控件
        initView()
        //定义控件的各种事件
        bindingEvent()
        //初始化数据
        initData()
        //监听viewMode的数据
        observe()
        //默认的网络请求
        getData()
    }

    fun showLoading() {
        if (loadingPop == null) {
            loadingPop = XPopup.Builder(this)
                .dismissOnBackPressed(true)
                .dismissOnTouchOutside(false)
                .isLightNavigationBar(true)
                .asLoading(
                    null,
                    R.layout.layout_gz_loading_popup_view_by_open_box,
                    LoadingPopupView.Style.ProgressBar
                )
        }
        loadingJob?.cancel()
        loadingPop?.show()
    }

    fun dismissLoading() {
        loadingPop?.dismiss()
    }

    private fun bindView(): View? {
        immersionBar {
            transparentStatusBar()
            statusBarDarkFont(true)
        }
        return binding?.root
    }


    /**
     * 初始化viewBinding
     */
    private fun initBinding() {
        var currentClass = javaClass
        run out@{
            while (true) {
                if (currentClass.superclass == null || currentClass.superclass.typeParameters.isNotEmpty()) {
                    return@out
                }
                currentClass = currentClass.superclass as Class<BaseActivity<VB>>
            }
        }
        val type = currentClass.genericSuperclass
        val vbClass: Class<VB> = type!!.saveAs<ParameterizedType>().actualTypeArguments[0].saveAs()
        val method = vbClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
        binding = method.invoke(this, layoutInflater)?.saveAsUnChecked()
    }

    open fun getData() {

    }

    override fun onResume() {
        super.onResume()
        AutoSize.autoConvertDensityOfGlobal(this)
        //AppGlobal.sCurrentActivity = this
    }

    open fun preInit() {

    }


    abstract fun initView()

    abstract fun initData()

    protected open fun observe() {

    }

    protected open fun bindingEvent() {

    }

    // 字体大小不跟随系统
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase?.let { getConfigurationContext(it) })
    }

    private fun getConfigurationContext(context: Context): Context {
        val configuration = context.resources.configuration
        configuration.fontScale = 1f
        return context.createConfigurationContext(configuration)
    }

    protected open fun childOverWriteBind() = false

    override fun getResources(): Resources {
        //需要升级到 v1.1.2 及以上版本才能使用 AutoSizeCompat
        if (Looper.myLooper() == Looper.getMainLooper()) { //避免程序包崩溃
            AutoSizeCompat.autoConvertDensityOfGlobal(super.getResources()) //如果没有自定义需求用这个方法
        }
        return super.getResources()
    }

    override fun finish() {
        super.finish()
        //overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
    }
}