package com.zss.framaist.fram.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import com.lxj.xpopup.core.BubbleAttachPopupView
import com.zss.base.util.setOnSingleClickedListener
import com.zss.framaist.R
import com.zss.framaist.databinding.DialogCameraTimeMenuBinding

@SuppressLint("ViewConstructor")
class TimePopDialog(context: Context, val onTimeSelect: (time: Int) -> Unit) :
    BubbleAttachPopupView(context) {

    /**
     * BubbleAttachPopupView的布局左右两边会被裁剪掉一部分5dp左右,是留出了距离设置了圆角?
      */
    var binding: DialogCameraTimeMenuBinding? = null


    override fun getImplLayoutId(): Int {
        return R.layout.dialog_camera_time_menu
    }

    override fun addInnerContent() {
        val contentView = LayoutInflater.from(this.context)
            .inflate(this.implLayoutId, this.bubbleContainer, false)
        binding = DialogCameraTimeMenuBinding.bind(contentView)
        bubbleContainer.lookWidth
        bubbleContainer.addView(contentView)
    }

    override fun onCreate() {
        super.onCreate()
        binding?.apply {
            tv2s.setOnSingleClickedListener {
                onTimeSelect.invoke(2)
                dismiss()
            }
            tv5s.setOnSingleClickedListener {
                onTimeSelect.invoke(5)
                dismiss()
            }
            tv10s.setOnSingleClickedListener {
                onTimeSelect.invoke(10)
                dismiss()
            }
            tv0s.setOnSingleClickedListener {
                onTimeSelect.invoke(0)
                dismiss()
            }
        }
    }

}