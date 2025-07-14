package com.example.depthestimation

import android.content.Context
import android.os.Environment
import com.zss.base.BaseApplication
import com.zss.base.util.toast
import java.io.File

// 复制assets文件到内部存储
 fun copyAssetToInternal(assetPath: String): String {
    val fileName = System.currentTimeMillis().toString() + "upload.jpg";
    val cameraFilePath =
        BaseApplication.instance.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + fileName;

    val internalFile = File(cameraFilePath, assetPath)
    internalFile.parentFile?.mkdirs()

    BaseApplication.instance.assets.open(assetPath).use { input ->
        internalFile.outputStream().use { output ->
            input.copyTo(output)
        }
    }

    return internalFile.absolutePath
}

// 从相机获取图片
//private fun captureImage() {
//    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//    if (intent.resolveActivity(packageManager) != null) {
//        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
//    }
//}
//
//override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//    super.onActivityResult(requestCode, resultCode, data)
//
//    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//        val imageBitmap = data?.extras?.get("data") as? Bitmap
//        imageBitmap?.let { predictFromBitmap(it) }
//    }
//}

// 扩展函数：简化结果处理
fun DepthAnalysisResult.showAlert(context: Context) {
    if (isTooClose) {
        toast("检测到距离过近！过近像素占比：${getPercentageString()}")
//        AlertDialog.Builder(context)
//            .setTitle("距离警告")
//            .setMessage("检测到距离过近！过近像素占比：${getPercentageString()}")
//            .setPositiveButton("确定", null)
//            .show()
    }
}