package com.zss.framaist.entrance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.viewpager2.widget.ViewPager2
import com.example.depthestimation.DepthHelper
import com.zss.base.BaseActivity
import com.zss.base.recyclerView.BaseNoLeakVPAdapter
import com.zss.common.net.safeLaunch
import com.zss.framaist.R
import com.zss.framaist.common.showNotSupportedDialog
import com.zss.framaist.databinding.EntranceActivityMainEntranceBinding
import com.zss.framaist.discover.DiscoverFragment
import com.zss.framaist.factory.FactoryFragment
import com.zss.framaist.fram.ui.CameraActivity
import com.zss.framaist.fram.ui.FramFragment
import com.zss.framaist.fram.ui.navTo
import com.zss.framaist.home.HomeFragment
import com.zss.framaist.mine.UserMineFragment
import kotlinx.coroutines.Dispatchers

class EntranceMainActivity : BaseActivity<EntranceActivityMainEntranceBinding>() {

    private var adapterVp: BaseNoLeakVPAdapter? = null
    val homeFragment by lazy { HomeFragment() }
    val discoverFragment by lazy { DiscoverFragment() }
    val framFragment by lazy { FramFragment() }
    val factoryFragment by lazy { FactoryFragment() }
    val mineFragment by lazy { UserMineFragment() }

    override fun initView() {
        initFragments()
    }

    override fun initData() {
        safeLaunch(Dispatchers.IO) {
            DepthHelper.init()
        }
    }

    private fun initFragments() {
        adapterVp = BaseNoLeakVPAdapter(
            supportFragmentManager,
            lifecycle
        ).apply {
            add { homeFragment }
            add { discoverFragment }
            add { framFragment }
            add { factoryFragment }
            add { mineFragment }
        }
        binding?.vpContent?.setCurrentItem(0, false)
        binding?.vpContent?.apply {
            isUserInputEnabled = false
            adapter = adapterVp
            // 加载全部fragment（不全部加载可能会导致首次进入app点击我的页面卡顿）
            //offscreenPageLimitSafe(adapterVp!!.itemCount - 1)
        }
        binding?.vpContent?.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })
    }

    override fun bindingEvent() {
        super.bindingEvent()
        binding?.apply {
            vTabHome.configTabLayoutConfig {
                onSelectIndexChange = { index, list, _, _ ->
                    when (list.first()) {
                        0 -> {
                            vpContent.setCurrentItem(list.first(), false)
                            ivTabHome.isSelected = true
                            ivTabDiscover.isSelected = false
                            ivTabFactory.isSelected = false
                            ivTabMine.isSelected = false

                            tvHome.isSelected = true
                            tvDiscover.isSelected = false
                            tvFactory.isSelected = false
                            tvMine.isSelected = false
                        }

                        1 -> {

                            ivTabHome.isSelected = false
                            ivTabDiscover.isSelected = true
                            ivTabFactory.isSelected = false
                            ivTabMine.isSelected = false

                            tvHome.isSelected = false
                            tvDiscover.isSelected = true
                            tvFactory.isSelected = false
                            tvMine.isSelected = false
                            // 本期不开放发现
                            showNotSupportedDialog(this@EntranceMainActivity)
                            vTabHome.setCurrentItem(index)

                        }

                        2 -> {
                            vTabHome.setCurrentItem(index)
                            navTo<CameraActivity>()
                        }

                        3 -> {
                            ivTabHome.isSelected = false
                            ivTabDiscover.isSelected = false
                            ivTabFactory.isSelected = true
                            ivTabMine.isSelected = false

                            tvHome.isSelected = false
                            tvDiscover.isSelected = false
                            tvFactory.isSelected = true
                            tvMine.isSelected = false
                            // 本期不开放工坊
                            showNotSupportedDialog(this@EntranceMainActivity)
                            vTabHome.setCurrentItem(index)
                        }

                        4 -> {
                            vpContent.setCurrentItem(list.first(), false)
                            ivTabHome.isSelected = false
                            ivTabDiscover.isSelected = false
                            ivTabFactory.isSelected = false
                            ivTabMine.isSelected = true

                            tvHome.isSelected = false
                            tvDiscover.isSelected = false
                            tvFactory.isSelected = false
                            tvMine.isSelected = true
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun SmoothBottomNav(modifier: Modifier = Modifier) {
    NavigationBar(
        containerColor = colorResource(com.zss.base.R.color.black_2d),
        modifier = modifier
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    painterResource(R.drawable.ic_home_blue), null, modifier = Modifier.width(24.dp)
                )
            },
            selected = true,
            label = {
                Text(
                    text = "首页",
                    color = Color.White
                )
            },
            onClick = {},
            modifier = Modifier.background(color = colorResource(com.zss.base.R.color.black_2d))
        )
        NavigationBarItem(
            icon = {
                Icon(painterResource(R.drawable.ic_home_blue), null)
            },
            selected = true,
            label = {
                Text(
                    text = "发现"
                )
            },
            onClick = {}
        )
        NavigationBarItem(
            icon = {
                Icon(painterResource(R.drawable.ic_home_blue), null)
            },
            selected = true,
            label = null,
            onClick = {}
        )
        NavigationBarItem(
            icon = {
                Icon(painterResource(R.drawable.ic_home_blue), null)
            },
            selected = true,
            label = {
                Text(
                    text = "工坊"
                )
            },
            onClick = {}
        )
        NavigationBarItem(
            icon = {
                Icon(painterResource(R.drawable.ic_home_blue), null)
            },
            selected = true,
            label = {
                Text(
                    text = "我的"
                )
            },
            onClick = {}
        )
    }
}

@Preview
@Composable
fun NavPreview(modifier: Modifier = Modifier) {
    SmoothBottomNav()
}