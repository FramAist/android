package com.zss.base.util

import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.zss.base.BaseApplication

private const val TAG_KEY = 1766613352

var <T : View> T.lastClickTime: Long
    set(value) = setTag(TAG_KEY, value)
    get() = getTag(TAG_KEY) as? Long ?: 0

inline fun <T : View> T.setOnSingleClickedListener(
    time: Long = 800,
    crossinline block: (T) -> Unit
) {
    setOnClickListener {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - lastClickTime > time) {
            lastClickTime = currentTimeMillis
            block(this)
        }
    }
}

fun getResDrawable(id: Int): Drawable {
    return ResourcesCompat.getDrawable(BaseApplication.instance.resources, id, null)
}