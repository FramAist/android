package com.zss.framaist.factory

import android.content.Context
import android.view.LayoutInflater
import com.zss.base.ui.BaseFragment
import com.zss.framaist.databinding.FragmentDiscoverBinding
import com.zss.framaist.databinding.FragmentFactoryBinding
import com.zss.framaist.databinding.UserFragmentMineBinding


/**
 * @author :huchenxi
 * @createDate :2024/7/27 11:48
 * @description :我的（用户）
 */
class FactoryFragment : BaseFragment<FragmentFactoryBinding>() {

    companion object {
        fun newInstance(context: Context) =
            fragmentInstance<FactoryFragment>(context) {

            }
    }


    override fun fragmentInflater(): ((inflater: LayoutInflater) -> FragmentFactoryBinding) {
        return FragmentFactoryBinding::inflate
    }

    override fun initData() {


    }

    override fun initView() {

    }

    override fun bindingEvent() {

    }

    override fun observe() {


    }


}