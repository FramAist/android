package com.zss.framaist.mine

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zss.framaist.R
import com.zss.framaist.bean.ConfirmedSuggestionResp
import com.zss.framaist.compose.MoreMessageCard
import com.zss.framaist.compose.RecentComposeList
import com.zss.framaist.compose.TitleCard
import com.zss.framaist.compose.UserInfoCard
import com.zss.framaist.compose.ui.theme.FramAistTheme
import com.zss.framaist.entrance.EntranceSplashActivity
import com.zss.framaist.fram.ui.navTo
import com.zss.framaist.mine.settings.SettingDialog
import com.zss.framaist.util.MMKVUtil


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
fun RecentComposeTitleCard(items: List<ConfirmedSuggestionResp>, onClick: () -> Unit) {
    Column {
        MoreMessageCard("最近构图", onClick)
        RecentComposeList(items)
    }
}


@Composable
fun ItemCardBlock2(onLogout: () -> Unit, showSettingDialog: () -> Unit) {
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
        ) {
            showSettingDialog()
        }
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
fun MineScreen(
    vm: MineVM = hiltViewModel(),
    onUserInfoClick: () -> Unit,
    onRecentListClick: () -> Unit,
) {
    var userInfo by rememberSaveable { mutableStateOf(MMKVUtil.getUserInfo()) }
    var showLogoutDialog by rememberSaveable { mutableStateOf(false) }
    var showSettingsDialog by rememberSaveable { mutableStateOf(false) }
    val recent by vm.recentList.collectAsStateWithLifecycle()
    val activity = LocalActivity.current

    LaunchedEffect(Unit) {
        vm.getRecentCompose()
    }

    FramAistTheme {
        if (showLogoutDialog) {
            LogoutConfirmDialog(
                onConfirm = {
                    MMKVUtil.logout()
                    activity?.navTo<EntranceSplashActivity>()
                    activity?.finish()
                },
                onDismiss = {
                    showLogoutDialog = false
                }
            )
        }

        if (showSettingsDialog) {
            SettingDialog(onConfirm = {
                showSettingsDialog = false
            })
        }

        Column(
            modifier = Modifier
                .background(Color.Black)
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = 26.dp)
                .verticalScroll(rememberScrollState())
        ) {
            TitleCard("我的")
            UserInfoCard(userInfo, onUserInfoClick)
            ItemCardBlock()
            RecentComposeTitleCard(recent, onRecentListClick)
            ItemCardBlock2(
                onLogout = { showLogoutDialog = true },
                showSettingDialog = { showSettingsDialog = true }
            )
            VersionCard()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserInfoPreview() {
    MineScreen(onUserInfoClick = {}, onRecentListClick = {})
}

@Composable
fun LogoutConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        text = {
            Text(
                text = "确定要退出登录吗?",
                fontSize = 18.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
            )
        },
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "取消",
                    color = Color.Black
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = "确定",
                    color = Color.Black
                )
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(12.dp)
    )
}

data class ItemInfo(
    val name: String,
    val desc: String? = null,
    val imageRes: Int? = null,
    val imageBg: Long? = null,
)