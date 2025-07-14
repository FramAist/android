package com.zss.framaist.fram

import android.content.Context
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.core.content.ContextCompat
import com.zss.base.util.LL
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


suspend fun ImageCapture.startPicture(context: Context) = suspendCancellableCoroutine { con ->
    var isResumed = false
    LL.e("xdd startPicture")
    takePicture(
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageCapturedCallback() {
            @OptIn(ExperimentalGetImage::class)
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)
                LL.e("xdd onCaptureSuccess")
                LL.e("xdd ${image.image}  ${image.width} ${image.height} ${image.imageInfo} ")
                if (!isResumed) {
                    val res = image.toBitmap()
                    con.resume(res)
                    isResumed = true
                }
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                if (!isResumed) {
                    con.resumeWithException(exception)
                }
            }
        })
}
