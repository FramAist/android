package com.zss.framaist.home

import android.content.Context
import android.view.ViewGroup
import com.chad.library.adapter4.BaseQuickAdapter
import com.lxj.xpopup.util.XPopupUtils
import com.zss.base.glide.ImageLoader.loadImage
import com.zss.base.mvvm.ViewBindingHolder
import com.zss.base.mvvm.createViewBindingHolder
import com.zss.base.util.dp2px
import com.zss.framaist.bean.RecommendModel
import com.zss.framaist.databinding.ItemHomeRecommendBinding

class RecommendAdapter :
    BaseQuickAdapter<RecommendModel, ViewBindingHolder<ItemHomeRecommendBinding>>() {
    override fun onBindViewHolder(
        holder: ViewBindingHolder<ItemHomeRecommendBinding>,
        position: Int,
        item: RecommendModel?
    ) {
        holder.viewBinding?.apply {
            root.post {
                val lp = ivImage.layoutParams
                lp.width = (XPopupUtils.getScreenWidth(root.context) - 68.dp2px()) / 2
                lp.height = (lp.width * 1.6).toInt()
                ivImage.layoutParams = lp
                ivImage.loadImage(context, item?.image_url.toString(), 8.dp2px())
            }

        }
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): ViewBindingHolder<ItemHomeRecommendBinding> {
        return createViewBindingHolder(parent, ItemHomeRecommendBinding::inflate)
    }

}