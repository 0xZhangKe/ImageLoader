package com.zhangke.imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.Toast;

import com.zhangke.imageloader.engine.BitmapRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ZhangKe on 2017/9/24.
 */

public class ImageLoader {

    private static final String TAG = "ImageLoader";
    private static final int TIME_OUT = 10 * 1000;

    public static void load(final ImageView imageView, final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL imgUrl = null;
                    imgUrl = new URL(url);
                    HttpURLConnection urlConn = (HttpURLConnection) imgUrl.openConnection();
                    InputStream inputStream = null;
                    try {
                        urlConn.setDoInput(true);
                        urlConn.setRequestMethod("GET");
                        urlConn.setConnectTimeout(TIME_OUT);
                        urlConn.connect();
                        inputStream = urlConn.getInputStream();
                        final BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeStream(inputStream, null, options);
                        options.inSampleSize = calculateInSampleSize(options, imageView.getWidth(), imageView.getHeight());
                        options.inJustDecodeBounds = false;
                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null ,options);
                        imageView.post(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageBitmap(bitmap);
                            }
                        });
                    } finally {
                        if (inputStream != null) {
                            inputStream.close();
                            inputStream = null;
                        }
                        urlConn = null;
                    }

                }catch(IOException e){
                    e.printStackTrace();
                    Toast.makeText(imageView.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).start();
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth,
                                            int reqHeight){
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if(height > reqHeight || width > reqWidth){
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth){
                inSampleSize *= 2;
            }
        }
        return  inSampleSize;
    }
}
