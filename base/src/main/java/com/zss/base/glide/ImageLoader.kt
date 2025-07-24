package com.zss.base.glide

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.luck.picture.lib.utils.ActivityCompatHelper
import com.zss.base.R
import com.zss.base.util.dp2px

object ImageLoader {
    fun ImageView.loadImage(context: Context, any: Any, radius: Int = 20.dp2px()) {
        if (!ActivityCompatHelper.assertValidRequest(context)) return
        Glide.with(context).load(any).transform(CenterCrop(), RoundedCorners(radius)).into(this)
    }


    fun ImageView.load(context: Context, any: Any?) {
        if (!ActivityCompatHelper.assertValidRequest(context)) return
        Glide.with(context).load(any).placeholder(R.drawable.ic_holder_150_150).into(this)
    }


}