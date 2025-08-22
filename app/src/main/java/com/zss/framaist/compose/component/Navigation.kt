package com.zss.framaist.compose.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.zss.base.R

@Composable
fun RowScope.FaNavigationBarItem(
    index: Int,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    selectedIcon: @Composable () -> Unit,
    label: @Composable (() -> Unit)? = null,
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = if (selected) selectedIcon else icon,
        modifier = modifier,
        label = label,
        alwaysShowLabel = index != 2,
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = colorResource(R.color.blue_2a5bff),
            unselectedIconColor = colorResource(R.color.gray_9da3ae),
            selectedTextColor = colorResource(R.color.blue_2a5bff),
            unselectedTextColor = colorResource(R.color.gray_9da3ae),
            indicatorColor = colorResource(R.color.black_2d),
        ),
    )
}
