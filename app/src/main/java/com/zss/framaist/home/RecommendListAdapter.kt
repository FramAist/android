package com.zss.framaist.home

import android.content.Context
import android.content.Intent
import android.view.ViewGroup
import com.chad.library.adapter4.BaseQuickAdapter
import com.jeremyliao.liveeventbus.LiveEventBus
import com.lxj.xpopup.util.XPopupUtils
import com.zss.base.glide.ImageLoader.loadImage
import com.zss.base.mvvm.ViewBindingHolder
import com.zss.base.mvvm.createViewBindingHolder
import com.zss.base.util.LL
import com.zss.base.util.dp2px
import com.zss.base.util.setOnSingleClickedListener
import com.zss.common.constant.EventConstant
import com.zss.common.constant.IntentKey
import com.zss.framaist.bean.RecommendModel
import com.zss.framaist.databinding.ItemRecommendListBinding
import com.zss.framaist.fram.ui.CameraActivity

class RecommendListAdapter() :
    BaseQuickAdapter<RecommendModel, ViewBindingHolder<ItemRecommendListBinding>>() {
    override fun onBindViewHolder(
        holder: ViewBindingHolder<ItemRecommendListBinding>,
        position: Int,
        item: RecommendModel?
    ) {
        holder.viewBinding?.apply {
            item ?: return
            root.post {
                val lp = ivImage.layoutParams
                lp.width = (XPopupUtils.getScreenWidth(root.context) - 68.dp2px()) / 2
                lp.height = (lp.width * 1.6).toInt()
                ivImage.layoutParams = lp
                ivImage.loadImage(context, item.image_url.toString(), 8.dp2px())
            }
            tvTakeSame.setOnSingleClickedListener {
                LiveEventBus.get<RecommendModel>(EventConstant.EVENT_CONFIRM_COMPOSITION).post(item)
            }
            ivImage.setOnSingleClickedListener {
                context.startActivity(Intent(context, RecommendDetailActivity::class.java).apply {
                    putExtra(IntentKey.DATA, item)
                    putExtra(IntentKey.SOURCE, SOURCE_LIST)
                })
            }

        }
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): ViewBindingHolder<ItemRecommendListBinding> {
        return createViewBindingHolder(parent, ItemRecommendListBinding::inflate)
    }

}