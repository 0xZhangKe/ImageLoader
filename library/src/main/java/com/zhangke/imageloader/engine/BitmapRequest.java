package com.zhangke.imageloader.engine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ZhangKe on 2017/9/24.
 */

public class BitmapRequest {

    private static final String TAG = "BitmapRequest";
    private static final int TIME_OUT = 10 * 1000;

    /**
     * 通过 URL 获取 Bitmap
     *
     * @param imageStringUrl URL
     * @return Bitmap
     * @throws IOException
     */
    public static Bitmap getBitmap(String imageStringUrl, BitmapFactory.Options options) throws IOException {
        URL imgUrl = null;
        Bitmap bitmap = null;
        imgUrl = new URL(imageStringUrl);
        HttpURLConnection urlConn = (HttpURLConnection) imgUrl.openConnection();
        InputStream inputStream = null;
        try {
            urlConn.setDoInput(true);
            urlConn.setRequestMethod("GET");
            urlConn.setConnectTimeout(TIME_OUT);
            urlConn.connect();
            inputStream = urlConn.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream, null, options);
        } finally {
            if (inputStream != null) {
                inputStream.close();
                inputStream = null;
            }
            urlConn = null;
        }
        return bitmap;
    }

}
