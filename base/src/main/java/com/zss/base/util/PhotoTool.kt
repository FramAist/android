package com.zss.base.util

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.luck.picture.lib.utils.PictureFileUtils
import com.permissionx.guolindev.PermissionX
import com.zss.base.BaseApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

/**
 * @author :chenjie
 * @createDate :2024/8/3 16:44
 * @description :  保存 bitmap  如：海报
 */
class PhotoTool {
    companion object {
        private var instance: PhotoTool? = null

        const val KEY_URI = "uri"
        const val KEY_ERR_MSG = "errMsg"

        @JvmStatic
        fun getInstance(): PhotoTool {
            if (instance == null) {
                synchronized(PhotoTool::class.java) {
                    if (instance == null) {
                        instance = PhotoTool()
                    }
                }
            }
            return instance!!
        }
    }

    /**
     * 保持图片到手机相册
     * @param fileName  带后缀名.png  如：img_${System.currentTimeMillis()}.png
     */
    fun saveToPhoneAlbum(
        fragment: Fragment?,
        bitmap: Bitmap?,
        fileName: String? = null,
        callback: OnSaveBitmapCallback? = null,
        isShowToast: Boolean = false
    ) {
        if (fragment == null) {
            return
        }
        saveToPhoneAlbum(fragment.requireActivity(), bitmap, fileName, callback, isShowToast)

    }

    /**
     * 保持图片到手机相册
     * @param fileName  带后缀名.png  如：img_${System.currentTimeMillis()}.png
     */
    fun saveToPhoneAlbum(
        activity: FragmentActivity?,
        bitmap: Bitmap?,
        fileName: String? = null,
        callback: OnSaveBitmapCallback? = null,
        isShowToast: Boolean = false
    ) {
        if (activity == null) {
            return
        }
        val permissionMediator = PermissionX.init(activity)
        (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            //**Android 13（API 级别 33），检测并申请对应权限**
            permissionMediator.permissions(
                Manifest.permission.READ_MEDIA_IMAGES,
            )
        } else {
            permissionMediator.permissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
            )
        }).onExplainRequestReason { scope, deniedList ->
        }.request { allGranted, grantedList, deniedList ->
            if (allGranted) {
                saveBitmap(activity, bitmap, fileName, object : OnSaveBitmapCallback {
                    override fun onSuccess(file: File?) {
                        if (activity.isFinishing || activity.isDestroyed) {
                            return
                        }
                        if (isShowToast) {
                            //ToastUtils.showSystem(activity.getString(R.string.common_saved_to_album))
                        }
                        callback?.onSuccess(file)
                    }

                    override fun onFail(msg: String?) {
                        if (activity.isFinishing || activity.isDestroyed) {
                            return
                        }
                        if (isShowToast) {
                            // ToastUtils.showSystem(msg)
                        }
                        callback?.onFail(msg)
                    }
                })
            } else {
                if (isShowToast) {
                    //ToastUtils.showSystem(activity.getString(R.string.common_please_open_read_write_storage_permission))
                }
                callback?.onFail("请允许授权")
            }
        }

    }

    /**
     * 保持图片到手机相册
     * @param fileName  带后缀名.png  如：img_${System.currentTimeMillis()}.png
     */
    private fun saveBitmap(
        context: Context?,
        bitmap: Bitmap?,
        fileName: String? = "share_image_" + System.currentTimeMillis() + ".jpg",
        callback: OnSaveBitmapCallback?
    ) {

        GlobalScope.launch {
            val hashMap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                saveImageInQ(context, bitmap, fileName)
            } else {
                saveTheImageLegacyStyle(context, bitmap, fileName)
            }
            var finalErrMsg = hashMap.get(KEY_ERR_MSG) as String?
            var imageFile: File? = null
            try {

                imageFile = File(PictureFileUtils.getPath(context, hashMap.get(KEY_URI) as Uri?))
            } catch (e: Exception) {
                e.printStackTrace()
                finalErrMsg = e.message
            }
            withContext(context = Dispatchers.Main) {
                if (callback != null) {
                    if (imageFile != null) {
                        callback.onSuccess(imageFile)
                    } else {
                        if (!TextUtils.isEmpty(finalErrMsg)) {
                            callback.onFail(finalErrMsg)
                        } else {
                            callback.onFail("File save failed")
                        }
                    }
                }
            }
        }
    }

    private fun saveImageInQ(
        context: Context?,
        bitmap: Bitmap?,
        fileName: String?
    ): HashMap<String, Any?> {
        var fos: OutputStream? = null
        var imageUri: Uri? = null
        var hashMap: HashMap<String, Any?> = hashMapOf()
        try {
            if (bitmap != null) {
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                    put(MediaStore.MediaColumns.IS_PENDING, 1)
                }
                //use application context to get contentResolver
                context?.contentResolver?.let { resolver ->
                    imageUri =
                        resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                    try {
                        fos = imageUri?.let { resolver.openOutputStream(it) }
                        fos?.use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
                        fos?.flush()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        hashMap.put(KEY_ERR_MSG, "${e.message}")
                    } finally {
                        fos?.close()
                    }
                    contentValues.clear()
                    contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                    resolver.update(imageUri!!, contentValues, null, null)
                    context?.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, imageUri))
                }
            } else {
                hashMap.put(KEY_ERR_MSG, "bitmap is null")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            hashMap.put(KEY_ERR_MSG, "${e.message}")
        }
        hashMap.put(KEY_URI, imageUri)
        return hashMap
    }

    private fun saveTheImageLegacyStyle(
        context: Context?,
        bitmap: Bitmap?,
        fileName: String?
    ): HashMap<String, Any?> {
        var imageUri: Uri? = null
        var hashMap: HashMap<String, Any?> = hashMapOf()
        try {
            var fos: OutputStream? = null
            var imageFile: File? = null
            if (bitmap != null) {
                try {
                    val imagesDir =
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    imageFile = File(imagesDir, fileName)
                    fos = FileOutputStream(imageFile)
                    fos.use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
                    fos.flush()
                } catch (e: Exception) {
                    e.printStackTrace()
                    hashMap.put(KEY_ERR_MSG, "${e.message}")
                } finally {
                    fos?.close()
                }
                if (imageFile != null) {
                    imageUri = Uri.parse("file://" + imageFile.absolutePath)
                    context?.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, imageUri))
                }
            } else {
                hashMap.put(KEY_ERR_MSG, "bitmap is null")
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            hashMap.put(KEY_ERR_MSG, "${e.message}")
        }
        hashMap.put(KEY_URI, imageUri)
        return hashMap
    }

    /**
     * 保持图片到缓存
     * @param fileName  带后缀名.png  如：img_${System.currentTimeMillis()}.png
     */
    fun saveBitmap(bitmap: Bitmap?, fileName: String, callback: OnSaveBitmapCallback?) {

        val imageFile: File = File(BaseApplication.instance.cacheDir, fileName)
        GlobalScope.launch {
            var saveFileSuccess = false
            var fos: FileOutputStream? = null
            var errMsg: String? = null
            try {
                fos = FileOutputStream(imageFile)
                bitmap?.compress(Bitmap.CompressFormat.PNG, 100, fos)
                fos.flush()
                saveFileSuccess = true
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                errMsg = e.message
                saveFileSuccess = false
            } finally {
                if (fos != null) {
                    try {
                        fos.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
            val finalSaveFileSuccess = saveFileSuccess
            val finalErrMsg = errMsg
            withContext(context = Dispatchers.Main) {
                if (callback != null) {
                    if (imageFile != null) {
                        if (finalSaveFileSuccess) {
                            callback.onSuccess(imageFile)
                        } else {
                            callback.onFail("file is not exist")
                        }
                    } else {
                        callback.onFail(finalErrMsg)
                    }
                }
            }
        }
    }

}