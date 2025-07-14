package com.zss.framaist.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SuggestionResp(
    val status: String,
    val suggestions: List<RecommendModel>?,
    var taskId: String?
): Parcelable


