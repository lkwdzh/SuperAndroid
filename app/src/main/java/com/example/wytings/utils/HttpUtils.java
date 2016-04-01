package com.example.wytings.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Rex on 2016/4/1.
 * https://github.com/wytings
 */
public class HttpUtils {

    public static String getHttpSource(String url) {
        try {
            URL uri = new URL(url);
            URLConnection connection = uri.openConnection();
            connection.connect();
            InputStream is = connection.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayOutputStream byteArrayInputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = bis.read(buffer, 0, buffer.length)) != -1) {
                byteArrayInputStream.write(buffer, 0, length);
            }
            return new String(byteArrayInputStream.toByteArray(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
