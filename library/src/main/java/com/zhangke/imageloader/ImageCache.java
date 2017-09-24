package com.zhangke.imageloader;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by ZhangKe on 2017/9/24.
 */

public class ImageCache {

    private ImageCache mImageCache;
    private LruCache<String, Bitmap> mMemoryCache;

    private ImageCache(){
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };
    }

    public ImageCache getInstance(){
        if(mImageCache == null){
            mImageCache = new ImageCache();
        }
        return mImageCache;
    }

    public LruCache<String, Bitmap> getmMemoryCache() {
        return mMemoryCache;
    }
}
