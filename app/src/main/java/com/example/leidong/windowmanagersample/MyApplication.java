package com.example.leidong.windowmanagersample;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by leidong on 2017/4/12.
 */

public class MyApplication extends Application {
    private static Context context;
    private static DisplayImageOptions options;
    private static int apiVersion;
    @Override
    public void onCreate(){
        super.onCreate();

        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);

        options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        context = getApplicationContext();

        apiVersion = Build.VERSION.SDK_INT;
    }

    /**
     * 获取全局Context
     * @return context
     */
    public static Context getContext(){
        return context;
    }

    /**
     * 获取DisplayImageOptions
     * @return options
     */
    public static DisplayImageOptions getOptions(){
        return options;
    }

    /**
     * 获取当前API版本号
     * @return apiVersion
     */
    public static int getAPIVersion(){
        return apiVersion;
    }
}
