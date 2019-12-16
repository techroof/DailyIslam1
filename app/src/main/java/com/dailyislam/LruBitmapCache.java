package com.dailyislam;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

public class LruBitmapCache extends LruCache<String,Bitmap> implements ImageCache {

    public LruBitmapCache(int sizeInKiloBytes) {
        super(sizeInKiloBytes());
    }

    private static int sizeInKiloBytes() {

        return 0;
    }


    public LruBitmapCache() {
        this(getDefaultLruCacheSize());
    }

    private static int getDefaultLruCacheSize() {
        final int maxMemory=(int) (Runtime.getRuntime().maxMemory()/1024);
        final int cacheSize= maxMemory/8;
        return cacheSize;
    }

    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url,bitmap);
    }

    protected int sizeof(String key,Bitmap value){
        return value.getRowBytes()*value.getHeight()/1024;
    }
}
