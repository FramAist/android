package com.zss.framaist.util

import android.app.Activity
import androidx.camera.core.AspectRatio
import androidx.core.view.isVisible
import com.lxj.xpopup.XPopup
import com.zss.framaist.fram.ui.CameraActivity
import com.zss.framaist.fram.ui.RatioPopDialog
import com.zss.framaist.fram.ui.SplashSelectDialog
import com.zss.framaist.fram.ui.TimePopDialog

object DialogHelper {
    fun CameraActivity.showTooCloseTips(isShow: Boolean) {
        this.binding?.layoutCamera?.tvTips?.apply {
            if (isShow) {
                text = "距离过近!"
                isVisible = true
            } else {
                isVisible = false
            }
        }
    }

    fun CameraActivity.showClockPopView() {
        this.binding?.layoutCamera?.ivClock?.let {
            val dialog = XPopup.Builder(this)
                .dismissOnBackPressed(true)
                .dismissOnTouchOutside(true)
                .isCenterHorizontal(true)
                .atView(it)
                .asCustom(TimePopDialog(this) {
                    vm.setCountDown(it)
                })
            dialog.show()
        }
    }

    fun CameraActivity.showRatioPopView() {
        this.binding?.layoutCamera?.tvRatio?.let {
            val dialog = XPopup.Builder(this)
                .dismissOnBackPressed(true)
                .dismissOnTouchOutside(true)
                .atView(it)
                .asCustom(RatioPopDialog(this) {
                    when (it) {
                        0 -> vm.setAspectRatio(AspectRatio.RATIO_DEFAULT)
                        1 -> vm.setAspectRatio(AspectRatio.RATIO_4_3)
                        2 -> vm.setAspectRatio(AspectRatio.RATIO_16_9)
                    }
                })
            dialog.show()
        }
    }

    fun CameraActivity.showSplashPopView() {
        this.binding?.layoutCamera?.ivLight?.let {
            val dialog = XPopup.Builder(this)
                .dismissOnBackPressed(true)
                .dismissOnTouchOutside(true)
                .atView(it)
                .asCustom(SplashSelectDialog(this) {
                    vm.setLightMode(it)
                })
            dialog.show()
        }
    }

    fun Activity.showLoading() {
        XPopup.Builder(this).asLoading().show()
    }

}