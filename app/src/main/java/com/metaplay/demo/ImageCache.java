package com.metaplay.demo;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by wilbur.yu on 10/16/18.
 */

public class ImageCache {
    private LruCache<String, Bitmap> mMemoryCache;

    final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    final int cacheSize = maxMemory / 4;

    private static ImageCache mInstance;

    public static ImageCache getInstance() {
        if (mInstance == null) {
            mInstance = new ImageCache();
        }
        return mInstance;
    }

    public ImageCache() {
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        synchronized (mMemoryCache) {
            if (mMemoryCache.get(key) == null) {
                mMemoryCache.put(key, bitmap);
            }
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    public void clean() {
        mMemoryCache.evictAll();
    }
}
