package com.zss.framaist.factory

import android.view.LayoutInflater
import com.zss.base.ui.BaseFragment
import com.zss.framaist.databinding.FragmentFactoryBinding


/**
 * @author :huchenxi
 * @createDate :2024/7/27 11:48
 * @description :我的（用户）
 */
class FactoryFragment : BaseFragment<FragmentFactoryBinding>() {
    override fun fragmentInflater(): ((inflater: LayoutInflater) -> FragmentFactoryBinding) {
        return FragmentFactoryBinding::inflate
    }
}