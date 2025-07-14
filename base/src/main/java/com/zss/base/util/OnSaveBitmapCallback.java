package com.zss.base.util;

import java.io.File;

public interface OnSaveBitmapCallback {
    void onSuccess(File file);
    void onFail(String msg);
}
