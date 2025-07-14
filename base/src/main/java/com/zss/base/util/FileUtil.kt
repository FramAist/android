//package com.zss.base.util
//
//import android.Manifest
//import android.content.Context
//import android.graphics.Bitmap
//import android.net.Uri
//import android.os.Build
//import android.text.TextUtils
//import androidx.fragment.app.FragmentActivity
//import com.luck.picture.lib.utils.PictureFileUtils
//import com.luck.picture.lib.utils.ToastUtils
//import com.permissionx.guolindev.PermissionX
//import com.zss.base.util.PhotoTool.Companion.KEY_ERR_MSG
//import com.zss.base.util.PhotoTool.Companion.KEY_URI
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//import java.io.File
//
//object FileUtil {
//}
//
//fun saveToPhoneAlbum(activity: FragmentActivity?, bitmap: Bitmap?, fileName: String? = null, callback: OnSaveBitmapCallback? = null, isShowToast: Boolean = false) {
//    if (activity==null){
//        return
//    }
//    val permissionMediator = PermissionX.init(activity)
//    (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//        //**Android 13（API 级别 33），检测并申请对应权限**
//        permissionMediator.permissions(
//            Manifest.permission.READ_MEDIA_IMAGES,
//        )
//    } else {
//        permissionMediator.permissions(
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//        )
//    }).onExplainRequestReason { scope, deniedList ->
//    }.request { allGranted, grantedList, deniedList ->
//        if (allGranted) {
//            saveBitmap(activity, bitmap, fileName, object : OnSaveBitmapCallback {
//                override fun onSuccess(file: File?) {
//                    if (activity.isActivityFinishingOrDestroyed()) {
//                        return
//                    }
//                    if (isShowToast) {
//                        ToastUtils.showSystem(activity.getString(R.string.common_saved_to_album))
//                    }
//                    callback?.onSuccess(file)
//                }
//
//                override fun onFail(msg: String?) {
//                    if (activity.isActivityFinishingOrDestroyed()) {
//                        return
//                    }
//                    if (isShowToast) {
//                        ToastUtils.showSystem(msg)
//                    }
//                    callback?.onFail(msg)
//                }
//            })
//        } else {
//            if (isShowToast) {
//                ToastUtils.showSystem(activity.getString(R.string.common_please_open_read_write_storage_permission))
//            }
//            callback?.onFail(activity.getString(R.string.common_please_open_read_write_storage_permission))
//        }
//    }
//
//}
//
//private fun saveBitmap(context: Context?, bitmap: Bitmap?, fileName: String? = "share_image_" + System.currentTimeMillis() + ".jpg", callback: OnSaveBitmapCallback?) {
//
//    GlobalScope.launch {
//        val hashMap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            saveImageInQ(context, bitmap, fileName)
//        } else {
//            saveTheImageLegacyStyle(context, bitmap, fileName)
//        }
//        var finalErrMsg = hashMap.get(KEY_ERR_MSG) as String?
//        var imageFile: File? = null
//        try {
//
//            imageFile = File(PictureFileUtils.getPath(context, hashMap.get(KEY_URI) as Uri?))
//        } catch (e: Exception) {
//            e.printStackTrace()
//            finalErrMsg = e.message
//        }
//        withContext(context = Dispatchers.Main) {
//            if (callback != null) {
//                if (imageFile != null) {
//                    callback.onSuccess(imageFile)
//                } else {
//                    if (!TextUtils.isEmpty(finalErrMsg)) {
//                        callback.onFail(finalErrMsg)
//                    } else {
//                        callback.onFail("File save failed")
//                    }
//                }
//            }
//        }
//    }
//}
//
