package com.zss.base.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    companion object {
        inline fun <reified T : Fragment> fragmentInstance(
            context: Context,
            argsFun: (Bundle.() -> Unit)
        ): T {
            val args = Bundle().apply(argsFun)
            val factory = object : FragmentFactory() {
                override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
                    return T::class.java.getDeclaredConstructor().newInstance().apply {
                        arguments = args
                    }
                }
            }
            return factory.instantiate(context.classLoader, T::class.java.name) as T
        }
    }

    private var isFirstLoad: Boolean = true

    private var isLocalHidden: Boolean = false

    /**
     * bing需要是可空的,因为onDestroyView之后哪怕是不可空的也会失效
     */
    protected var _binding: VB? = null
    protected open val binding get() = _binding!!

    abstract fun fragmentInflater(): ((inflater: LayoutInflater) -> VB)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = fragmentInflater().invoke(inflater)
        //初始化控件
        initView()
        //定义控件的各种事件
        bindingEvent()
        //初始化数据
        initData()
        return _binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //监听viewMode的数据
        observe()
    }

    open fun initView() {
        //init view
    }

    open fun bindingEvent() {
        //init event
    }

    open fun initData() {
        //init data
    }

    open fun observe() {
        //observe vm
    }


    open fun firstResume() {
        // firstTime resume
    }

    override fun onResume() {
        super.onResume()
        if (isFirstLoad) {
            firstResume()
            isFirstLoad = false
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        isLocalHidden = hidden
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}