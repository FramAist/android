package com.zss.framaist.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zss.framaist.R


@Composable
fun GoCameraView(onClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .background(
                Color(0xFF1d2130),
                shape = RoundedCornerShape(8.dp)
            )
            .fillMaxWidth()
            .padding(vertical = 20.dp)
            .clickable {
                onClick()
            }

    ) {
        Row {
            Spacer(modifier = Modifier.size(12.dp))
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(colorResource(com.zss.base.R.color.blue_375af6))
                    .width(40.dp)
                    .height(40.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_camera_white), // 替换为实际资源 ID
                    contentDescription = "描述文本",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(10.dp)
                )
            }
            Spacer(modifier = Modifier.size(12.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(vertical = 2.dp)
            ) {
                Text(
                    text = "立即体验AI构图",
                    color = colorResource(R.color.white),
                    fontSize = 14.sp,
                )
                Text(
                    text = "点击立即进入相机模式",
                    color = colorResource(com.zss.base.R.color.gray_9da3ae),
                    fontSize = 12.sp,
                )
            }
        }
    }

}

@Preview
@Composable
fun PreviewWidget() {
    GoCameraView()
}

