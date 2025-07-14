package com.zss.framaist.fram

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.impl.ConfirmPopupView

object CameraDialogHelper {
    fun getGoToPermissionDialog(
        context: Context,
        onConfirm: () -> Unit
    ): ConfirmPopupView {
        return XPopup.Builder(context)
            .dismissOnBackPressed(false)
            .dismissOnTouchOutside(false)
            .autoDismiss(false)
            .asConfirm("请先允许相机权限", "", {
                onConfirm()
            }, {
                (context as? AppCompatActivity)?.finish()
            })
    }
}