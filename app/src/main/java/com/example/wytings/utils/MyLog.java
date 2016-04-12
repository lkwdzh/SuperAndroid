package com.example.wytings.utils;

import android.util.Log;

/**
 * Created by Rex on 2016/3/12.
 * https://github.com/wytings
 */
public class MyLog {
    private static final String TAG = "wytings";
    private static String className;
    private static String methodName;
    private static int lineNumber;

    public static void d(Object log) {
        getMethodInfo(new Throwable().getStackTrace());
        Log.d(TAG, "[" + className + "]" + "-[" + lineNumber + "]-[" + methodName + "] ----> " + log);
    }

    public static void e(Object log) {
        getMethodInfo(new Throwable().getStackTrace());
        Log.e(TAG, "[" + className + "]" + "-[" + lineNumber + "]-[" + methodName + "] ----> " + log);
    }

    private static void getMethodInfo(StackTraceElement[] sElements) {
        className = sElements[1].getFileName();
        lineNumber = sElements[1].getLineNumber();
        methodName = sElements[1].getMethodName();
    }
}
