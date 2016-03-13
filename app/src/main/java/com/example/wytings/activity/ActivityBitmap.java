package com.example.wytings.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.view.View;
import android.widget.ImageView;

import com.example.wytings.R;
import com.example.wytings.utils.ImageUtils;
import com.example.wytings.utils.MyLog;

/**
 * Created by Rex on 2016/3/12.
 * https://github.com/wytings
 */
public class ActivityBitmap extends BaseActivity {

    private Bitmap bitmap;
    private ImageView imageView;

    @Override
    protected void initialize() {

        setOnButtonClickListener("check bitmap", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBitmap();
            }
        });
        setOnButtonClickListener("change bitmap", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBitmap();
            }
        });

        imageView = new ImageView(this);
        setExtraContent(imageView);
    }

    private void checkBitmap() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        //options.inJustDecodeBounds = true;
        options.inSampleSize = 4;
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.big, options);
        MyLog.d(options.outHeight + "," + options.outWidth + "," + options.inDensity);
    }

    @SuppressLint("NewApi")
    private void showBitmap() {
        Bitmap resizeBitmap = ThumbnailUtils.extractThumbnail(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        MyLog.d(bitmap.getByteCount());
        MyLog.d(resizeBitmap.getByteCount());
        imageView.setImageBitmap(null);
        imageView.setImageBitmap(resizeBitmap);
        ImageUtils.saveBitmap(this, bitmap, "bitmap1.png");
        ImageUtils.saveBitmap(this, resizeBitmap, "bitmap2.png");
    }
}
