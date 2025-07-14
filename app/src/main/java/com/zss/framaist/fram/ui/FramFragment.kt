package com.zss.framaist.fram.ui

import android.content.Context
import android.view.LayoutInflater
import com.zss.base.ui.BaseFragment
import com.zss.framaist.databinding.FragmentFrameBinding

/**
 * @author :huchenxi
 * @createDate :2024/7/27 11:48
 * @description :我的（用户）
 */
class FramFragment : BaseFragment<FragmentFrameBinding>() {

    companion object {
        fun newInstance(context: Context) =
            fragmentInstance<FramFragment>(context) {

            }
    }


    override fun fragmentInflater(): ((inflater: LayoutInflater) -> FragmentFrameBinding) {
        return FragmentFrameBinding::inflate
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