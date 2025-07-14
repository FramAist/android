package com.example.depthestimation

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.io.IOException
import java.io.OutputStream

object DepthHelper {
    lateinit var depthManager: DepthEstimatorManager
    var isInitialized = false
    var isPredicting = false

    suspend fun init() {
        val modelPath = copyAssetToInternal("models/model.onnx")
        depthManager = DepthEstimatorManager(modelPath)
        if (depthManager.initialize()) {
            Log.i("xdd", "异步初始化成功")
            isInitialized = true
        } else {
            Log.e("xdd", "异步初始化失败")
            isInitialized = false
        }
    }

    fun saveBitmapToGallery(
        context: Context,
        bitmap: Bitmap,
        fileName: String,
        mimeType: String = "image/jpeg"
    ): Boolean {
        // 检查并请求权限 (仅适用于 Android 9 及以下)
        if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.P) { // Android 9 (API 28) 及以下
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                // 如果没有权限，需要请求权限。
                // 在 Activity 中：ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_WRITE_EXTERNAL_STORAGE)
                // 在 Fragment 中：requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_WRITE_EXTERNAL_STORAGE)
                // 这里我们只是Toast提示，实际应用中要处理权限请求回调
                Toast.makeText(context, "请授予存储权限以保存图片", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        val contentResolver = context.contentResolver
        val outputStream: OutputStream?
        val imageCollection =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) { // Android 10 (API 29) 及以上
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, mimeType)
            // Android 10 (API 29) 及以上，可以指定子目录
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                // 推荐存到 Pictures/YourAppName
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/YourAppName")
            }
        }
        try {
            val imageUri = contentResolver.insert(imageCollection, contentValues)
            if (imageUri == null) {
                Toast.makeText(context, "保存图片失败：无法创建 URI", Toast.LENGTH_SHORT).show()
                return false
            }
            outputStream = contentResolver.openOutputStream(imageUri)
            if (outputStream != null) {
                // 根据 mimeType 选择压缩格式
                val format =
                    if (mimeType == "image/png") Bitmap.CompressFormat.PNG else Bitmap.CompressFormat.JPEG
                val quality = if (mimeType == "image/png") 100 else 90 // PNG 质量为 100
                bitmap.compress(format, quality, outputStream)
                outputStream.flush()
                outputStream.close()
                //Toast.makeText(context, "图片已保存到相册", Toast.LENGTH_SHORT).show()
                // 通知媒体库扫描 (仅适用于 Android 9 及以下，Android 10+ MediaStore 自动处理)
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.Q) {
                    // 不再推荐直接使用 MediaScannerConnection，因为 MediaStore 已经处理
                    // 但如果需要兼容旧版本，并且是直接写入文件，可能仍需
                    // MediaScannerConnection.scanFile(context, arrayOf(filePath), null, null)
                    // 但由于我们这里是使用 MediaStore API，所以通常不需要额外扫描。
                }
                return true
            } else {
                Toast.makeText(context, "保存图片失败：无法打开输出流", Toast.LENGTH_SHORT).show()
                return false
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, "保存图片失败：${e.message}", Toast.LENGTH_SHORT).show()
            return false
        }
    }

}

