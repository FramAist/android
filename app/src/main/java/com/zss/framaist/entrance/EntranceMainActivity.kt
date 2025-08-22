package com.zss.framaist.entrance

import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
import com.zss.framaist.compose.component.FaNavigationBarItem
import com.zss.framaist.databinding.EntranceActivityMainEntranceBinding
import com.zss.framaist.discover.DiscoverFragment
import com.zss.framaist.factory.FactoryFragment
import com.zss.framaist.fram.ui.CameraActivity
import com.zss.framaist.fram.ui.FramFragment
import com.zss.framaist.fram.ui.navTo
import com.zss.framaist.home.HomeFragment
import com.zss.framaist.mine.UserMineFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@AndroidEntryPoint
class EntranceMainActivity : BaseActivity<EntranceActivityMainEntranceBinding>() {

    @Inject
    lateinit var adapterVp: BaseNoLeakVPAdapter

    @Inject
    lateinit var homeFragment: HomeFragment

    @Inject
    lateinit var discoverFragment: DiscoverFragment

    @Inject
    lateinit var framFragment: FramFragment

    @Inject
    lateinit var factoryFragment: FactoryFragment

    @Inject
    lateinit var mineFragment: UserMineFragment


    override fun initView() {
        initFragments()
    }

    override fun initData() {
        safeLaunch(Dispatchers.IO) {
            DepthHelper.init()
        }
    }

    private fun initFragments() {
        adapterVp.addAll(
            listOf(
                homeFragment,
                discoverFragment,
                framFragment,
                factoryFragment,
                mineFragment
            )
        )
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
    var selectedItem by rememberSaveable { mutableIntStateOf(0) }
    val items = listOf("首页", "发现", "", "工坊", "我的")
    val iconsRes = listOf(
        R.drawable.ic_home_gray_9da3ae,
        R.drawable.ic_search_9da3ae,
        R.drawable.ic_camera_blue,
        R.drawable.ic_factory_gray_9da3ae,
        R.drawable.ic_mine_gray_9da3ae
    )
    val selectedIconsRes = listOf(
        R.drawable.ic_home_blue,
        R.drawable.ic_search_blue,
        R.drawable.ic_camera_blue,
        R.drawable.ic_factory_blue,
        R.drawable.ic_mine_blue
    )
    NavigationBar(
        containerColor = colorResource(com.zss.base.R.color.black_2d),
        modifier = modifier
    ) {
        items.forEachIndexed { index, string ->
            FaNavigationBarItem(
                index = index,
                icon = {
                    Icon(
                        painter = painterResource(iconsRes[index]),
                        contentDescription = null,
                        modifier = Modifier.width(24.dp)
                    )
                },
                selectedIcon = {
                    Icon(
                        painter = painterResource(selectedIconsRes[index]),
                        contentDescription = null,
                        modifier = Modifier.width(24.dp)
                    )
                },
                label = { Text(string) },
                selected = selectedItem == index,
                onClick = {

                },
            )
        }
    }
}

@Preview
@Composable
fun NavPreview(modifier: Modifier = Modifier) {
    SmoothBottomNav(modifier)
}