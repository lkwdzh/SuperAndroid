package com.example.wytings.utils;

/**
 * Created by Rex on 2016/3/12.
 * https://github.com/wytings
 */
public class StringUtils {
    public static boolean isEmpty(String string) {
        if (string == null || string.trim().length() == 0) {
            return true;
        } else {
            return false;
        }
    }
}
