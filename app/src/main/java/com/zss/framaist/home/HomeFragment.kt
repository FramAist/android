package com.zss.framaist.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter4.util.setOnDebouncedItemClick
import com.zss.base.glide.ImageLoader.loadImage
import com.zss.base.ui.BaseFragment
import com.zss.base.util.setOnSingleClickedListener
import com.zss.common.constant.IntentKey
import com.zss.framaist.R
import com.zss.framaist.common.showNotSupportedDialog
import com.zss.framaist.databinding.FragmentHomeBinding
import com.zss.framaist.fram.ui.CameraActivity


class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    val recommendAdapter = RecommendAdapter()

    companion object {
        fun newInstance(context: Context) =
            fragmentInstance<HomeFragment>(context) {

            }
    }


    override fun fragmentInflater(): ((inflater: LayoutInflater) -> FragmentHomeBinding) {
        return FragmentHomeBinding::inflate
    }

    override fun initData() {


    }

    override fun initView() {
        _binding?.apply {
            rvRecommend.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            rvRecommend.adapter = recommendAdapter
            recommendAdapter.submitList(listOf())
            recommendAdapter.setOnDebouncedItemClick { adapter, view, position ->
                startActivity(Intent(requireActivity(), RecommendDetailActivity::class.java).apply {
                    putExtra(IntentKey.DATA, adapter.getItem(position))
                    putExtra(IntentKey.SOURCE, SOURCE_LIST)
                })
            }
            ivScenery.loadImage(requireActivity(), com.zss.base.R.drawable.ic_holder_150_150, 12)
        }
    }

    override fun bindingEvent() {
        _binding?.apply {
            tvMoreRecommend.setOnSingleClickedListener {
                showNotSupportedDialog(requireActivity())
                return@setOnSingleClickedListener
                startActivity(Intent(requireActivity(), RecommendListActivity::class.java).apply {
//                    putParcelableArrayListExtra(
//                        IntentKey.RECOMMEND_LIST,
//                        kotlin.collections.ArrayList(list)
//                    )
                })
            }
            clTakePhoto.setOnSingleClickedListener {
                startActivity(Intent(requireActivity(), CameraActivity::class.java))
            }
        }
    }

    override fun observe() {


    }


}