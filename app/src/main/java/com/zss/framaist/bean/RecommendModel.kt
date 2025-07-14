package com.zss.framaist.bean

import android.os.Parcelable
import com.blankj.utilcode.util.LogUtils.a
import kotlinx.parcelize.Parcelize
import kotlin.math.abs

@Parcelize
data class RecommendModel(
    val id: String,
    val image_code: String,
    val image_url: String,
    val scene_type: String,//full,
    val score: Double,
    val rank: Int,
    val model_version: String,

    val title: String?,
    val desc: String?,
) : Parcelable
