package com.zss.framaist.mine

import android.content.Context
import android.view.ViewGroup
import com.chad.library.adapter4.BaseQuickAdapter
import com.zss.base.glide.ImageLoader.load
import com.zss.base.mvvm.ViewBindingHolder
import com.zss.base.mvvm.createViewBindingHolder
import com.zss.base.util.clipOutline
import com.zss.framaist.bean.RecommendModel
import com.zss.framaist.databinding.ItemMineRecentFramBinding

class RecentFramAdapter : BaseQuickAdapter<RecommendModel, ViewBindingHolder<ItemMineRecentFramBinding>>() {
    override fun onBindViewHolder(
        holder: ViewBindingHolder<ItemMineRecentFramBinding>,
        position: Int,
        item: RecommendModel?
    ) {
        holder.viewBinding?.apply {
            clRoot.clipOutline(8)
            ivImage.load(context, item?.image_url)
        }
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): ViewBindingHolder<ItemMineRecentFramBinding> {
        return createViewBindingHolder(parent, ItemMineRecentFramBinding::inflate)
    }
}