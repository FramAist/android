package com.zss.framaist.mine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.zss.common.bean.UserInfoBean
import com.zss.common.util.MMKVUtil
import com.zss.framaist.R
import com.zss.framaist.compose.MoreMessageCard
import com.zss.framaist.compose.RecentComposeList
import com.zss.framaist.compose.RecentListActivity
import com.zss.framaist.compose.TitleCard
import com.zss.framaist.compose.UserInfoCard
import com.zss.framaist.compose.ui.theme.FramAistTheme
import com.zss.framaist.fram.ui.navTo
import com.zss.framaist.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UserMineFragment @Inject constructor() : Fragment() {

    private val vm: MineVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vm.getRecentCompose()
        return ComposeView(requireActivity()).apply {
            setContent {
                MyScreen(MMKVUtil.getUserInfo())
            }
        }
    }

}


@Composable
fun ItemCard(item: ItemInfo, onClick: () -> Unit = {}) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .width(36.dp)
                .height(36.dp)
                .background(Color(item.imageBg ?: 0), CircleShape)
        ) {
            Image(
                painterResource(item.imageRes ?: 0),
                null,
                Modifier.padding(8.dp)
            )
        }
        Text(
            text = item.name,
            fontSize = 14.sp,
            color = colorResource(R.color.white),
            modifier = Modifier.padding(start = 20.dp)
        )
        Spacer(Modifier.weight(1f))
        Image(
            painterResource(R.drawable.ic_arrow_right_gray_9da3ae),
            null,
            modifier = Modifier
                .width(20.dp)
                .height(20.dp)
        )
    }
}

@Composable
fun LineCard() {
    Box(
        Modifier
            .height(1.dp)
            .fillMaxWidth()
            .background(Color.Black)
    )
}

@Composable
fun ItemCardBlock() {
    Column(
        modifier = Modifier
            .padding(top = 20.dp)
            .background(colorResource(com.zss.base.R.color.black_2d), RoundedCornerShape(8.dp))
    ) {
        ItemCard(
            ItemInfo(
                name = "我的收藏",
                imageRes = R.drawable.ic_my_collect,
                imageBg = 0xffdee9fc
            )
        )
        LineCard()
        ItemCard(
            ItemInfo(
                name = "拍摄历史",
                imageRes = R.drawable.ic_mine_history,
                imageBg = 0xffe2fbe8,
            )
        )
    }
}

@Composable
fun RecentComposeTitleCard(onClick: () -> Unit) {
    Column {
        MoreMessageCard("最近构图", onClick)
        RecentComposeList()
    }
}


@Composable
fun ItemCardBlock2(onLogout: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(top = 12.dp)
            .background(
                colorResource(com.zss.base.R.color.black_2d),
                RoundedCornerShape(8.dp)
            )
    ) {
        ItemCard(
            ItemInfo(
                name = "设置",
                imageRes = R.drawable.ic_setting,
                imageBg = 0xfff3f4f6,
            )
        )
        LineCard()
        ItemCard(
            ItemInfo(
                name = "帮助中心",
                imageRes = R.drawable.ic_mine_help,
                imageBg = 0xfff1e8fd,
            )
        )
        LineCard()
        ItemCard(
            ItemInfo(
                name = "退出登录",
                imageRes = R.drawable.ic_mine_logout,
                imageBg = 0xfff9e3e2
            )
        ) {
            onLogout()
        }
    }
}

@Composable
fun VersionCard() {
    Text(
        text = "FramAsit v0.0.1 (MVP)",
        color = colorResource(com.zss.base.R.color.gray_9da3ae),
        fontSize = 12.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(
                top = 20.dp,
                bottom = 30.dp
            )
            .fillMaxWidth(),
    )
}

@Composable
fun MyScreen(userInfo: UserInfoBean?) {
    FramAistTheme {
        Column(
            modifier = Modifier
                .background(Color.Black)
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = 26.dp)
                .verticalScroll(rememberScrollState())
        ) {
            val activity = LocalActivity.current
            TitleCard("我的")
            UserInfoCard(userInfo) {
                activity?.navTo<UserInfoActivity>()
            }
            ItemCardBlock()
            RecentComposeTitleCard {
                activity?.navTo<RecentListActivity>()
            }
            ItemCardBlock2 {
                activity?.navTo<LoginActivity>()
                activity?.finish()
                MMKVUtil.logout()
            }
            VersionCard()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserInfoPreview() {
    MoreMessageCard("111") {}
}

data class ItemInfo(
    val name: String,
    val desc: String? = null,
    val imageRes: Int? = null,
    val imageBg: Long? = null,
)