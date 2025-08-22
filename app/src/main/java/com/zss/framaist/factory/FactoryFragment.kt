package com.zss.framaist.factory

import android.view.LayoutInflater
import com.zss.base.ui.BaseFragment
import com.zss.framaist.databinding.FragmentFactoryBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FactoryFragment @Inject constructor() : BaseFragment<FragmentFactoryBinding>() {
    override fun fragmentInflater(): ((inflater: LayoutInflater) -> FragmentFactoryBinding) {
        return FragmentFactoryBinding::inflate
    }
}