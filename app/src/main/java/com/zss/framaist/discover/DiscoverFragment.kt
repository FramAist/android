package com.zss.framaist.discover

import android.content.Context
import android.view.LayoutInflater
import com.zss.base.ui.BaseFragment
import com.zss.framaist.databinding.FragmentDiscoverBinding
import com.zss.framaist.databinding.UserFragmentMineBinding


/**
 * @author :huchenxi
 * @createDate :2024/7/27 11:48
 * @description :我的（用户）
 */
class DiscoverFragment : BaseFragment<FragmentDiscoverBinding>() {

    companion object {
        fun newInstance(context: Context) =
            fragmentInstance<DiscoverFragment>(context) {

            }
    }


    override fun fragmentInflater(): ((inflater: LayoutInflater) -> FragmentDiscoverBinding) {
        return FragmentDiscoverBinding::inflate
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