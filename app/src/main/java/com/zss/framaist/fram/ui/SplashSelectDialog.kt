package com.zss.framaist.fram.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import com.blankj.utilcode.util.ToastUtils
import com.lxj.xpopup.core.BubbleAttachPopupView
import com.zss.base.util.setOnSingleClickedListener
import com.zss.framaist.R
import com.zss.framaist.bean.LightMode
import com.zss.framaist.databinding.DialogCameraSplashMenuBinding

@SuppressLint("ViewConstructor")
class SplashSelectDialog(context: Context, val onSplashSelect: (mode: LightMode) -> Unit) :
    BubbleAttachPopupView(context) {

    /**
     * BubbleAttachPopupView的布局左右两边会被裁剪掉一部分5dp左右,是留出了距离设置了圆角?
     */
    var binding: DialogCameraSplashMenuBinding? = null


    override fun getImplLayoutId(): Int {
        return R.layout.dialog_camera_splash_menu
    }

    override fun addInnerContent() {
        val contentView = LayoutInflater.from(this.context)
            .inflate(this.implLayoutId, this.bubbleContainer, false)
        binding = DialogCameraSplashMenuBinding.bind(contentView)
        bubbleContainer.lookWidth
        bubbleContainer.addView(contentView)
    }

    override fun onCreate() {
        super.onCreate()
        binding?.apply {
            tvOpen.setOnSingleClickedListener {
                onSplashSelect.invoke(LightMode.OPEN)
                dismiss()
            }
            tvClose.setOnSingleClickedListener {
                onSplashSelect.invoke(LightMode.CLOSE)
                dismiss()
            }
            tvAlwaysOn.setOnSingleClickedListener {
                onSplashSelect.invoke(LightMode.AUTO)
                dismiss()
            }
        }
    }

}