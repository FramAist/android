package com.zss.framaist.fram

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object DataHelper {
    /**
     * 拍摄页面临时保存的图片
     */
    var tempPicture: Bitmap? = null

    var bitmap by mutableStateOf<Bitmap?>(null)

}