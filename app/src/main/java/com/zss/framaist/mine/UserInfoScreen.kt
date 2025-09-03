package com.zss.framaist.mine

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zss.common.bean.UserInfoBean
import com.zss.framaist.R
import com.zss.framaist.compose.BackTitleCard
import com.zss.framaist.compose.UserInfoCard
import com.zss.framaist.compose.ui.theme.FramAistTheme
import com.zss.framaist.util.MMKVUtil


@Composable
fun UserInfoScreen(onPopBack: () -> Boolean, onNavToPswManager: () -> Unit) {
    FramAistTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(horizontal = 26.dp)
        ) {
            BackTitleCard("个人信息管理", {
                onPopBack()
            })
            Spacer(Modifier.height(30.dp))
            InfoCard(onNavToPswManager = onNavToPswManager)
        }
    }
}

@Composable
fun InfoCard(onNavToPswManager: () -> Unit = {}, modifier: Modifier = Modifier) {
    var userInfo by rememberSaveable { mutableStateOf(MMKVUtil.getUserInfo()) }
    var phone = userInfo?.phone ?: ""
    if (phone.length > 4) {
        phone = phone.substring(0, 3) + "****" + phone.substring(7, phone.length)
    }

    Column(
        modifier = modifier
            .clip(shape = RoundedCornerShape(8.dp))
            .background(color = colorResource(com.zss.base.R.color.black_2d))
            .padding(bottom = 12.dp, start = 12.dp, end = 12.dp)
            .fillMaxWidth()

    ) {
        TopCard(userInfo)
        Spacer(modifier.height(20.dp))
        Box(
            modifier
                .background(Color(0xff373d48))
                .height(1.dp)
                .fillMaxWidth()
        )
        Spacer(modifier.height(16.dp))
        BaseItem(
            title = "昵称",
            titleColor = colorResource(com.zss.base.R.color.black_d2d5da),
            content = userInfo?.nickName ?: "",
        )
        BaseItem(
            title = "手机号",
            titleColor = colorResource(com.zss.base.R.color.black_d2d5da),
            content = phone
        )
        BaseItem(
            title = "密码管理",
            titleColor = colorResource(com.zss.base.R.color.black_d2d5da),
            content = "修改",
            contentColor = colorResource(com.zss.base.R.color.gray_9da3ae),
            showError = true,
            onItemClick = onNavToPswManager
        )
    }
}

@Composable
fun TopCard(userInfo: UserInfoBean?) {
    UserInfoCard(userInfo, null)
}

@Composable
fun BaseItem(
    title: String,
    titleColor: Color = Color.White,
    content: String,
    contentColor: Color = Color.White,
    modifier: Modifier = Modifier,
    showError: Boolean = false,
    onItemClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .clickable(onClick = onItemClick)
            .padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, color = titleColor)
        Spacer(modifier.weight(1f))
        Text(content, color = contentColor)
        if (showError) {
            Image(
                painterResource(R.drawable.ic_arrow_right_gray_9da3ae),
                null,
                modifier = Modifier
                    .width(20.dp)
                    .height(20.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScreenPreview() {
    UserInfoScreen(onNavToPswManager = {}, onPopBack = {
        true
    })
}