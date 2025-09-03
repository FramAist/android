package com.zss.framaist.mine

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zss.framaist.R
import com.zss.framaist.compose.ConfirmButton
import com.zss.framaist.compose.ui.theme.FramAistTheme


@Composable
fun ResultScreen(
    errorReason: String?,
    onBackToMain: () -> Unit,
    onPopBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val success = errorReason.isNullOrEmpty()
    val submitText = if (success) "返回个人中心" else "重新尝试"
    val solidColor = if (success) Color(0xff283c2a) else Color(0xff412524)
    FramAistTheme {
        Column(
            modifier
                .background(Color.Black)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(if (success) R.drawable.ic_success_green else R.drawable.ic_failed_red),
                null,
                modifier
                    .width(60.dp)
                    .height(60.dp)
                    .clip(CircleShape)
                    .background(solidColor)
                    .padding(15.dp)
            )
            Text(
                text = if (success) "密码修改成功" else "密码修改失败",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(top = 10.dp)
            )
            Text(
                text = if (success) "您的密码已成功更新" else errorReason,
                color = colorResource(com.zss.base.R.color.gray_9da3ae),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(top = 10.dp, bottom = 20.dp)
            )
            ConfirmButton(
                title = submitText,
                onConfirm = {
                    if (success) {
                        onBackToMain()
                    }
                    onPopBack()
                },
                modifier = modifier,
                fillMaxWidth = false
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyPreview() {
    ResultScreen("重新尝试", {}, {})
}

