package com.example.wytings.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import java.io.FileOutputStream;

/**
 * Created by Rex on 2016/3/12.
 * https://github.com/wytings
 */
public class ImageUtils {
    public static void saveBitmap(Context context, Bitmap bitmap, String name) {
        try {
            FileOutputStream out = context.openFileOutput(name, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Toast.makeText(context, "save successfully", Toast.LENGTH_SHORT).show();
            MyLog.d("已经保存");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
