package com.zss.framaist.fram.ui

import android.content.Context
import android.view.LayoutInflater
import com.zss.base.ui.BaseFragment
import com.zss.framaist.databinding.FragmentFrameBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FramFragment @Inject constructor() : BaseFragment<FragmentFrameBinding>() {

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