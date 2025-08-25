package com.zss.framaist.bean

import android.os.Parcelable
import com.zss.framaist.mine.settings.DarkThemeConfig
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserData(
    val darkThemeConfig: DarkThemeConfig = DarkThemeConfig.DARK
): Parcelable