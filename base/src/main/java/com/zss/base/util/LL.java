package com.zss.base.util;

import android.util.Log;

import com.zss.base.BuildConfig;


public final class LL {

    public static String mLinesStr = "\n=======";
    public static String mClassStr = "类class(";

    public static void e(String msg) {
        if (BuildConfig.DEBUG) {
            final String[] info = getAutoJumpLogInfo();
            final String TAG = mClassStr + info[0] + info[3] + ":" + info[2] + ")";
            Log.e(TAG, mLinesStr + msg + "\n");
        }
    }

    public static void w(String msg) {
        if (BuildConfig.DEBUG) {
            final String[] info = getAutoJumpLogInfo();
            final String TAG = mClassStr + info[0] + info[3] + ":" + info[2] + ")";
            Log.w(TAG, mLinesStr + msg + "\n");
        }
    }

    public static void d(String msg) {
        if (BuildConfig.DEBUG) {
            final String[] info = getAutoJumpLogInfo();
            final String TAG = mClassStr + info[0] + info[3] + ":" + info[2] + ")";
            Log.d(TAG, mLinesStr + msg + "\n");
        }
    }

    public static void v(String msg) {
        if (BuildConfig.DEBUG) {
            final String[] info = getAutoJumpLogInfo();
            final String TAG = mClassStr + info[0] + info[3] + ":" + info[2] + ")";
            Log.v(TAG, mLinesStr + msg + "\n");
        }
    }

    public static void i(String msg) {
        if (BuildConfig.DEBUG) {
            final String[] info = getAutoJumpLogInfo();
            final String TAG = mClassStr + info[0] + info[3] + ":" + info[2] + ")";
            Log.i(TAG, mLinesStr + msg + "\n");
        }
    }

    /**
     * 获取打印信息所在方法名，行号等信息
     */
    private static String[] getAutoJumpLogInfo() {
        final String[] info = new String[]{"", "", "", ""};
        final StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        if (elements.length < 5) {
            Log.e("MyLogger", "Stack is too shallow!!!");
        } else {
            if (elements[4].getClassName().contains("$")) { //用于内部类的名字解析
                info[0] = elements[4].getClassName().substring(elements[4].getClassName().lastIndexOf(".") + 1, elements[4].getClassName().indexOf("$"));
            } else {
                info[0] = elements[4].getClassName().substring(elements[4].getClassName().lastIndexOf(".") + 1);
            }

            info[1] = "=======" + elements[4].getMethodName() + "()";
            info[2] = elements[4].getLineNumber() + "";
            final String filename = elements[4].getFileName();
            if (filename != null) {
                info[3] = filename.contains(".kt") ? ".kt" : ".java";
            } else {
                info[3] = ".java";
            }
        }
        return info;
    }
}
