package com.zss.framaist.home

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zss.framaist.compose.MoreMessageCard
import com.zss.framaist.compose.TitleCard
import com.zss.framaist.compose.ui.theme.FramAistTheme
import com.zss.framaist.fram.ui.CameraActivity
import com.zss.framaist.fram.ui.navTo

@Composable
fun HomeScreen() {
    FramAistTheme {
        val activity = LocalActivity.current
        Column(
            modifier = Modifier
                .background(Color.Black)
                .fillMaxSize()
                .padding(horizontal = 26.dp)
        ) {
            TitleCard("首页")
            Spacer(modifier = Modifier.height(20.dp))
            MoreMessageCard("推荐构图") { }
            MoreMessageCard("热门景点") { }
            BannerCard()
            Spacer(modifier = Modifier.height(30.dp))
            GoCameraView {
                activity?.navTo<CameraActivity>()
            }
        }
    }
}

@Composable
fun BannerCard(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color = colorResource(com.zss.base.R.color.black_33))
            .padding(20.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(com.zss.base.R.drawable.ic_holder_150_150),
            null,
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
        )
        Spacer(modifier.width(20.dp))
        Column {
            Text(
                text = "独库公路",
                fontSize = 14.sp,
                color = Color.White
            )
            Text(
                text = "新疆维吾尔族自治区",
                fontSize = 12.sp,
                color = colorResource(com.zss.base.R.color.gray_9da3ae),
                modifier = modifier.padding(top = 20.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    HomeScreen()
}