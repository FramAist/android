package com.zss.framaist.entrance

import android.os.Bundle
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
import androidx.compose.ui.unit.dp
import com.example.depthestimation.DepthHelper
import com.zss.common.net.safeLaunch
import com.zss.framaist.R
import com.zss.framaist.compose.BaseComposeActivity
import com.zss.framaist.compose.FaApp
import com.zss.framaist.compose.component.FaNavigationBarItem
import com.zss.framaist.compose.rememberFaAppState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers

@AndroidEntryPoint
class EntranceMainActivity : BaseComposeActivity() {

    @Composable
    override fun SetScreen() {
        val appState = rememberFaAppState()
        FaApp(appState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        safeLaunch(Dispatchers.IO) {
            DepthHelper.init()
        }
    }
}

@Composable
fun SmoothBottomNav(modifier: Modifier = Modifier, onClick: (index: Int) -> Unit) {
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
                    onClick(index)
                    selectedItem = index
                },
            )
        }
    }
}
