package com.zss.framaist.home

import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.angcyo.tablayout.delegate2.ViewPager2Delegate
import com.hjq.shape.view.ShapeTextView
import com.jeremyliao.liveeventbus.LiveEventBus
import com.zss.base.BaseActivity
import com.zss.base.R
import com.zss.base.recyclerView.BaseNoLeakVPAdapter
import com.zss.base.util.setOnSingleClickedListener
import com.zss.common.constant.EventConstant
import com.zss.common.constant.IntentKey
import com.zss.common.net.safeLaunch
import com.zss.framaist.bean.RecommendModel
import com.zss.framaist.bean.SceneTypeEnum
import com.zss.framaist.databinding.ActivityRecommendListBinding
import com.zss.framaist.fram.CameraVM
import com.zss.framaist.fram.ui.CameraActivity
import com.zss.framaist.fram.ui.navTo

class RecommendListActivity : BaseActivity<ActivityRecommendListBinding>() {

    var taskId: String? = null
    private val adapterVp: BaseNoLeakVPAdapter by lazy {
        BaseNoLeakVPAdapter(supportFragmentManager, lifecycle)
    }
    val vm: CameraVM by viewModels()

    @Suppress("DEPRECATION")
    override fun initView() {
        val data =
            intent.getParcelableArrayListExtra<RecommendModel>(IntentKey.RECOMMEND_LIST) ?: return
        taskId = intent.getStringExtra(IntentKey.TASK_ID)
        binding?.apply {
            vpContent.adapter = adapterVp
            vpContent.offscreenPageLimit = 3
            ViewPager2Delegate.install(binding!!.vpContent, tabLayout, false)
            tabLayout.setCurrentItem(0)
            vpContent.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    tabRecommend.setTabSelected(position == 0)
                    tabClose.setTabSelected(position == 1)
                    tabMedium.setTabSelected(position == 2)
                    tabFar.setTabSelected(position == 3)
                }
            })
            adapterVp.add { RecommendListFragment(data) }
            adapterVp.add { RecommendListFragment(data.filter { it.scene_type == SceneTypeEnum.FULL.value }) }
            adapterVp.add { RecommendListFragment(data.filter { it.scene_type == SceneTypeEnum.MEDIUM.value }) }
            adapterVp.add { RecommendListFragment(data.filter { it.scene_type == SceneTypeEnum.LONG.value }) }
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
        }
    }


    private fun ShapeTextView.setTabSelected(isSelected: Boolean) {
        setBackgroundColor(getColor(R.color.trans))
        shapeDrawableBuilder.apply {
            if (isSelected) {
                solidColor = getColor(R.color.blue_375af6)
                intoBackground()
            }
        }
    }

    override fun observe() {
        super.observe()
        LiveEventBus.get<RecommendModel>(EventConstant.EVENT_CONFIRM_COMPOSITION).observe(this) {
            safeLaunch {
                requireNotNull(taskId)
                vm.confirmSuggestion(taskId, it.id)
            }
            navTo<CameraActivity> { intent ->
                intent.putExtra(IntentKey.DATA, it)
            }
        }
    }
}