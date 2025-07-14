package com.zss.framaist.home

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.zss.base.ui.BaseFragment
import com.zss.framaist.bean.RecommendModel
import com.zss.framaist.databinding.FragmentRecommendListBinding


class RecommendListFragment(val list: List<RecommendModel>) :
    BaseFragment<FragmentRecommendListBinding>() {

    private val mAdapter = RecommendListAdapter()

    companion object {
        fun newInstance(context: Context) =
            fragmentInstance<RecommendListFragment>(context) {

            }
    }


    override fun fragmentInflater(): ((inflater: LayoutInflater) -> FragmentRecommendListBinding) {
        return FragmentRecommendListBinding::inflate
    }

    override fun initData() {


    }

    override fun initView() {
        _binding?.apply {
            rvContent.apply {
                // 设置布局管理器为瀑布流
                val staggeredGridLayoutManager =
                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                layoutManager = staggeredGridLayoutManager
                adapter = mAdapter
            }
            mAdapter.submitList(list)
        }
    }

    override fun bindingEvent() {

    }

    override fun observe() {


    }


}