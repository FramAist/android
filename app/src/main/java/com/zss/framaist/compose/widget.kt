package com.zss.framaist.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zss.base.util.toast

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

@Preview
@Composable
fun TitlePreview() {
    TitleCard("fsdf")
}
