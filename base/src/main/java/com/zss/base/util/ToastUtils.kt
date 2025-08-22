//package com.zss.base.util
//
//import android.os.Looper
//import android.view.Gravity
//import android.view.LayoutInflater
//import android.view.View
//import android.widget.ImageView
//import android.widget.TextView
//import android.widget.Toast
//import androidx.core.view.isVisible
//import com.zss.framaist.BaseApplication
//import com.zss.base.R
//import java.lang.ref.WeakReference
//
//object ToastUtils {
//    private var toast: WeakReference<Toast>? = null
//
//    fun showPicToast(msg: String?, pic: Int) {
//        val context = BaseApplication.instance
//        Looper.getMainLooper()
//
//        runOnUiThread {
//            cancel()
//            val view: View =
//                LayoutInflater.from(BaseApplication.instance)
//                    .inflate(com.zss.base.R.layout.layout_pic_toast, null)
//            val imageView = view.findViewById<ImageView>(R.id.img_toast)
//            val textView = view.findViewById<TextView>(R.id.text_toast)
//            textView.text = msg
//            imageView.setImageResource(pic)
//            toast = WeakReference(Toast(BaseApplication.instance))
//            toast?.get()?.let {
//                it.view = view
//                it.setGravity(Gravity.CENTER, 0, 0)
//                it.show()
//            }
//        }
//    }
//
//    fun showPicToastStatus(msg: String?, toastType: Int = ToastTypeInfo.SUCCESS) {
//        runOnUiThread {
//            cancel()
//            val view: View =
//                LayoutInflater.from(BaseApplication.instance).inflate(com.zss.base.R.layout.layout_pic_toast, null)
//            val imageView = view.findViewById<ImageView>(R.id.img_toast)
//            val textView = view.findViewById<TextView>(R.id.text_toast)
//            textView.text = msg
//            when (toastType) {
//                ToastTypeInfo.SUCCESS -> {
//                    imageView.isVisible = true
//                    imageView.setImageResource(R.drawable.icon_toast_sucess)
//                }
//
//                ToastTypeInfo.ERROR -> {
//                    imageView.isVisible = true
//                    imageView.setImageResource(R.drawable.icon_toast_failed)
//                }
//
//                ToastTypeInfo.WARNING -> {
//                    imageView.isVisible = true
//                    imageView.setImageResource(R.drawable.icon_toast_warning)
//                }
//
//                ToastTypeInfo.SYSTEM -> {
//                    imageView.isVisible = false
//                    imageView.setImageResource(0)
//                }
//            }
//            toast = WeakReference(Toast(AppGlobal.sApplication))
//            toast?.get()?.let {
//                it.view = view
//                it.setGravity(Gravity.CENTER, 0, 0)
//                it.show()
//            }
//        }
//    }
//
//    /**
//     * 居中图片toast
//     */
//    fun showCenterPicToastStatus(msg: String?, toastType: Int = ToastTypeInfo.SUCCESS) {
//        runOnUiThread {
//            cancel()
//            val view: View = LayoutInflater.from(AppGlobal.sApplication)
//                .inflate(R.layout.layout_center_pic_toast, null)
//            val imageView = view.findViewById<ImageView>(R.id.iv_image)
//            val textView = view.findViewById<TextView>(R.id.tv_message)
//            textView.text = msg
//            when (toastType) {
//                ToastTypeInfo.SUCCESS -> {
//                    imageView.setImageResource(R.drawable.icon_toast_sucess)
//                }
//
//                ToastTypeInfo.ERROR -> {
//                    imageView.setImageResource(R.drawable.icon_toast_failed)
//                }
//
//                ToastTypeInfo.WARNING -> {
//                    imageView.setImageResource(R.drawable.icon_toast_warning)
//                }
//            }
//            toast = WeakReference(Toast(AppGlobal.sApplication))
//            toast?.get()?.let {
//                it.view = view
//                it.setGravity(Gravity.CENTER, 0, 0)
//                it.show()
//            }
//        }
//    }
//
//
//    fun showSuccess(msg: String?) {
//        showPicToastStatus(msg, ToastTypeInfo.SUCCESS)
//    }
//
//    fun showError(msg: String?) {
//        showPicToastStatus(msg, ToastTypeInfo.ERROR)
//    }
//
//    fun showAlert(msg: String?) {
//        showPicToastStatus(msg, ToastTypeInfo.WARNING)
//    }
//
//    fun showSystem(msg: String?) {
//        showPicToastStatus(msg, ToastTypeInfo.SYSTEM)
//    }
//
//    fun showSystem(resourceId: Int) {
//        val message = BaseApplication.instance.getString(resourceId)
//        showPicToastStatus(message, ToastTypeInfo.SYSTEM)
//    }
//
//    fun cancel() {
//        toast?.get()?.cancel()
//        toast?.clear()
//        toast = null
//    }
//}