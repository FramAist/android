package com.zss.common.util

import android.Manifest
import android.os.Build
import androidx.fragment.app.FragmentActivity
import com.lxj.xpopup.XPopup
import com.permissionx.guolindev.PermissionX
import com.zss.common.Dialog.CommonPermissionToOpenDialog

object PermissionUtil {
    val mediaPermissions = mutableListOf(
        Manifest.permission.CAMERA,
        //Manifest.permission.RECORD_AUDIO
    ).apply {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    fun requestPermission(activity: FragmentActivity, success: () -> Unit, failed: () -> Unit) {
        val toSettingDialog = XPopup.Builder(activity)
            .autoFocusEditText(false)
            .autoOpenSoftInput(false)
            .dismissOnTouchOutside(false)
            .asCustom(CommonPermissionToOpenDialog(activity))
        PermissionX.init(activity).permissions(mediaPermissions).request { allGranted, _, _ ->
            if (allGranted) {
                success()
            } else {
                toSettingDialog.dismiss()
                failed()
            }
        }
    }

    fun isCameraPermissionGranted(activity: FragmentActivity): Boolean {
        return PermissionX.isGranted(activity, Manifest.permission.CAMERA)
    }
}