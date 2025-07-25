package com.zss.framaist.home

import androidx.core.content.IntentCompat.getParcelableExtra
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.jeremyliao.liveeventbus.LiveEventBus
import com.zss.base.BaseActivity
import com.zss.base.glide.ImageLoader.load
import com.zss.base.util.setOnSingleClickedListener
import com.zss.common.constant.EventConstant
import com.zss.common.constant.IntentKey
import com.zss.framaist.bean.RecommendModel
import com.zss.framaist.common.showNotSupportedDialog
import com.zss.framaist.databinding.ActivityRecommendDetailBinding
import com.zss.framaist.fram.ui.CameraActivity
import com.zss.framaist.fram.ui.navTo


const val SOURCE_CAMERA = 1
const val SOURCE_LIST = 0

/**
 * 构图详情页面
 * 入口:
 *  1. 构图列表
 *  2. 拍摄页 -> 推荐构图缩略图
 */
class RecommendDetailActivity : BaseActivity<ActivityRecommendDetailBinding>() {

    var recommendModel: RecommendModel? = null

    // 标签和按钮是否收起
    var wrapped = false

    //只有从拍摄页面缩略图进入不展示拍同款
    var enterSource: Int = SOURCE_LIST


    override fun initView() {
        enterSource = intent.getIntExtra(IntentKey.SOURCE, 0)
        recommendModel = getParcelableExtra(intent, IntentKey.DATA, RecommendModel::class.java)
        binding?.apply {
            tvTitle.text = recommendModel?.title
            tvDesc.text = recommendModel?.desc
            tvGoToCamera.isGone = enterSource == SOURCE_CAMERA
            ivPic.load(this@RecommendDetailActivity, recommendModel?.image_url)
        }
    }


    override fun initData() {
    }

    override fun bindingEvent() {
        super.bindingEvent()
        binding?.apply {
            ivBack.setOnSingleClickedListener {
                finish()
            }
            ivCollect.setOnSingleClickedListener {
                showNotSupportedDialog(this@RecommendDetailActivity)
            }
            tvGoToCamera.setOnSingleClickedListener {
                recommendModel ?: return@setOnSingleClickedListener
                LiveEventBus.get<RecommendModel>(EventConstant.EVENT_CONFIRM_COMPOSITION)
                    .post(recommendModel)
                navTo<CameraActivity> {
                    it.putExtra(IntentKey.DATA, recommendModel)
                }
            }
            ivPic.setOnClickListener {
                wrapped = !wrapped
                widgetsGroup.isVisible = wrapped
            }
        }
    }
}