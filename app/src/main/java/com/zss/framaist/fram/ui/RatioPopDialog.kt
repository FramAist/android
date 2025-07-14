package com.zss.framaist.fram.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import com.lxj.xpopup.core.BubbleAttachPopupView
import com.zss.base.util.setOnSingleClickedListener
import com.zss.framaist.R
import com.zss.framaist.databinding.DialogCameraRatioMenuBinding
import com.zss.framaist.databinding.DialogCameraTimeMenuBinding

@SuppressLint("ViewConstructor")
class RatioPopDialog(context: Context, val onRatioSelect: (position: Int) -> Unit) :
    BubbleAttachPopupView(context) {

    /**
     * BubbleAttachPopupView的布局左右两边会被裁剪掉一部分5dp左右,是留出了距离设置了圆角?
      */
    var binding: DialogCameraRatioMenuBinding? = null


    override fun getImplLayoutId(): Int {
        return R.layout.dialog_camera_ratio_menu
    }

    override fun addInnerContent() {
        val contentView = LayoutInflater.from(this.context)
            .inflate(this.implLayoutId, this.bubbleContainer, false)
        binding = DialogCameraRatioMenuBinding.bind(contentView)
        bubbleContainer.lookWidth
        bubbleContainer.addView(contentView)
    }

    override fun onCreate() {
        super.onCreate()
        binding?.apply {
            tvFull.setOnSingleClickedListener {
                onRatioSelect.invoke(0)
                dismiss()
            }
            tv43.setOnSingleClickedListener {
                onRatioSelect.invoke(1)
                dismiss()
            }
            tv169.setOnSingleClickedListener {
                onRatioSelect.invoke(2)
                dismiss()
            }
        }
    }

}