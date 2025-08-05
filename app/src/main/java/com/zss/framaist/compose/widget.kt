package com.zss.framaist.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import com.zss.base.util.toast
import com.zss.common.bean.UserInfoBean
import com.zss.framaist.R
import com.zss.framaist.compose.ui.theme.FramAistTheme

@Composable
fun ConfirmBtn(confirmText: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    MaterialTheme {

    }
}


@Preview
@Composable
fun SearchBarPreview(modifier: Modifier = Modifier) {
    MaterialTheme {
        SearchBar {
            toast(it)
        }
    }
}

@Composable
fun SearchBar(modifier: Modifier = Modifier, onValueChange: (stringVal: String) -> Unit) {
    MaterialTheme {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            TextField(
                "fadsf",
                shape = RoundedCornerShape(8.dp),
                onValueChange = {

                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.heightIn(56.dp)
            )

        }

    }
}

@Composable
fun TitleCard(title: String) {
    Box(
        modifier = Modifier
            .padding(top = 78.dp)
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        Text(
            text = title,
            fontSize = 26.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * title + 更多
 */
@Composable
fun MoreMessageCard(title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(top = 12.dp, bottom = 12.dp)
            .fillMaxWidth()
            .clickable(onClick = {
                onClick.invoke()
            })
    ) {
        Text(
            text = title,
            color = Color.White,
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "更多",
            color = Color.Blue
        )
    }
}

@Composable
fun ConfirmButton(title: String, onConfirm: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = {
            onConfirm()
        },
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Blue),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.White
        ),
        contentPadding = PaddingValues(vertical = 10.dp)
    ) {
        Text(text = title, fontSize = 18.sp)
    }
}

@Composable
fun BackTitleCard(title: String, onBack: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(top = 78.dp)
            .clickable(onClick = onBack),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.ic_arrow_left_white), null,
            modifier = modifier
                .background(
                    color = Color.DarkGray,
                    shape = CircleShape,
                )
                .size(28.dp)
                .padding(5.dp)
        )
        Spacer(modifier.width(16.dp))
        Text(text = title, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun UserInfoCard(user: UserInfoBean?, onClick: (() -> Unit)?) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .clickable(onClick != null, onClick = {
                onClick?.invoke()
            })
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(65.dp)
                .height(65.dp)
                .background(Color(0xFFBEBEBE), CircleShape)
                .padding(vertical = 12.dp, horizontal = 10.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_account_b1),
                contentDescription = null
            )
        }
        val name = if (user == null) "" else user.nickName ?: "用户${
            user.user_id.substring(
                user.user_id.length - 6,
                user.user_id.length
            )
        }"

        Column {
            Text(
                text = name,
                fontSize = 18.sp,
                color = colorResource(R.color.white),
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(start = 12.dp)

            )
            Text(
                "内测用户",
                color = colorResource(com.zss.base.R.color.gray_9da3ae),
                fontSize = 14.sp,
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(start = 12.dp, 4.dp)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        if (onClick != null) {
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
fun TitlePreview() {
    FramAistTheme {
        Column {
            UserInfoCard(null, null)
        }
    }
}

