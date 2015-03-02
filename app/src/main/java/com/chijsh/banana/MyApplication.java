package com.chijsh.banana;

import android.app.Application;
import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.signature.StringSignature;

/**
 * Created by chijsh on 11/10/14.
 */
public class MyApplication extends Application {
    private static Context sContext;

    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 50;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();

        MemorySizeCalculator calculator = new MemorySizeCalculator(this);

        Glide.setup(new GlideBuilder(this)
            .setMemoryCache(new LruResourceCache(calculator.getMemoryCacheSize()))
            .setDiskCache(DiskLruCacheWrapper.get(Glide.getPhotoCacheDir(this), DISK_CACHE_SIZE))
            .setBitmapPool(new LruBitmapPool(calculator.getBitmapPoolSize()))
            .setDecodeFormat(DecodeFormat.ALWAYS_ARGB_8888));

    }

    public static Context getAppContext() {
        return sContext;
    }
}
