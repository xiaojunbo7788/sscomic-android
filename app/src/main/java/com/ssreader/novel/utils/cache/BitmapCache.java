package com.ssreader.novel.utils.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * 用于缓存漫画的bitmap
 */
public class BitmapCache {

    private static BitmapCache bitmapCache;

    // 内存缓存，key可以是网络路径，可以是本地路径
    private LruCache<String, Bitmap> lruCache;

    public static BitmapCache getInstance() {
        if (bitmapCache == null ) {
            try {
                bitmapCache = new BitmapCache();
            } catch (Exception e) {
            }
        }
        return bitmapCache;
    }

    public BitmapCache() {
        // 使用Runtime类获取最大可用内存缓存（计量单位为Byte）
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        // 设置为可用内存的1/4（按Byte计算）
        int cacheSize = maxMemory / 8;
        lruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                // 在每次存入缓存时进行调用
                return value.getByteCount();
            }
        };
    }

    /**
     * 添加缓存
     *
     * @param key   键
     * @param value 值
     */
    public void addBitmapToCache(String key, Bitmap value) {
        if (value != null) {
            lruCache.put(key, value);
        }
    }

    /**
     * 取出Bitmap
     * @param key 键
     */
    public Bitmap getBitmapFromCache(String key) {
        return lruCache.get(key);
    }

    /**
     * 移除Bitmap
     * @param key 键
     */
    public void removeBitmapFromCache(String key) {
        lruCache.remove(key);
    }

    /**
     * 清空缓存
     */
    public void clearCache() {
        lruCache.evictAll();
    }
}
