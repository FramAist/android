package com.zss.framaist.mine

import android.content.Context
import android.view.ViewGroup
import com.chad.library.adapter4.BaseQuickAdapter
import com.zss.base.glide.ImageLoader.loadImage
import com.zss.base.mvvm.ViewBindingHolder
import com.zss.base.mvvm.createViewBindingHolder
import com.zss.framaist.databinding.ItemMineRecentFramBinding

class RecentFramAdapter : BaseQuickAdapter<String, ViewBindingHolder<ItemMineRecentFramBinding>>() {
    override fun onBindViewHolder(
        holder: ViewBindingHolder<ItemMineRecentFramBinding>,
        position: Int,
        item: String?
    ) {
        holder.viewBinding?.apply {
            ivImage.loadImage(context, item.toString())
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