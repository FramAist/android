package com.zss.common.Dialog

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.LayoutInflater
import com.lxj.xpopup.impl.FullScreenPopupView
import com.zss.base.util.setOnSingleClickedListener
import com.zss.common.R
import com.zss.common.databinding.CommonDialogPermissionToOpenBinding


/**
 * @author :chenjie
 * @createDate :2024/8/20 14:35
 * @description :权限说明
 */
class CommonPermissionToOpenDialog(
    context: Context,
    var title: String? = null,
    var content: String? = null
) : FullScreenPopupView(context) {
    var binding: CommonDialogPermissionToOpenBinding? = null
    override fun getImplLayoutId(): Int {
        return R.layout.common_dialog_permission_to_open
    }

    override fun addInnerContent() {
        val contentView = LayoutInflater.from(this.context)
            .inflate(this.implLayoutId, this.fullPopupContainer, false)
        binding = CommonDialogPermissionToOpenBinding.bind(contentView)
        fullPopupContainer.addView(contentView)

    }

    override fun onCreate() {
        super.onCreate()
        binding?.apply {
            ivClose.setOnClickListener {
                dismiss()
            }
            tvSubmit.setOnSingleClickedListener {
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", context.packageName, null)
                intent.data = uri
                context.startActivity(intent)
            }
        }
    }
}