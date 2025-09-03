package com.zss.framaist.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.zss.framaist.bean.ConfirmedSuggestionResp
import com.zss.framaist.compose.ui.theme.FramAistTheme
import com.zss.framaist.mine.MineVM

class RecentListActivity : ComponentActivity() {
    private val vm: MineVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm.getRecentCompose()
        enableEdgeToEdge()
        setContent {
            FramAistTheme {
                Column(
                    modifier = Modifier
                        .background(Color.Black)
                        .padding(horizontal = 12.dp)
                ) {
                    TitleCard("最近构图记录")
                    RecentComposeGrid(modifier = Modifier)
                }
            }
        }
    }
}


/**
 * 最近构图
 */
@Composable
fun RecentComposeCard(suggestion: ConfirmedSuggestionResp) {
    Box(
        contentAlignment = Alignment.BottomStart,
        modifier = Modifier
            .width(120.dp)
            .aspectRatio(1f / 1.5f)
            .clip(shape = RoundedCornerShape(8.dp))
            .background(colorResource(com.zss.base.R.color.black_2d))
    ) {
        NetWorkImage(url = suggestion.image_url.toString())
    }

}

@Composable
fun NetWorkImage(url: String, modifier: Modifier = Modifier) {
    Image(
        painter = rememberAsyncImagePainter(
            model = ImageRequest
                .Builder(LocalContext.current)
                .data(url)
                .crossfade(true)
                .build()
        ),
        contentDescription = null,
        contentScale = ContentScale.Inside,
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(8.dp)),
    )
}


@Composable
fun RecentComposeList(list:List<ConfirmedSuggestionResp>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(list) { suggestion ->
            RecentComposeCard(suggestion)
        }
    }
}

@Composable
fun RecentComposeGrid(modifier: Modifier = Modifier, vm: MineVM = viewModel()) {
    val list by vm.recentList.collectAsStateWithLifecycle(listOf())
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
            .padding(top = 100.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(list) { suggestion ->
            RecentComposeCard(suggestion)
        }
    }
}

@Preview
@Composable
fun ComposeListPreview() {
    RecentComposeCard(
        suggestion = ConfirmedSuggestionResp(
            "3",
            "3",
            "",
            "中景",
            null,
            null,
            "",
            "2025-05-07 12:00:00"
        ),
    )
}






