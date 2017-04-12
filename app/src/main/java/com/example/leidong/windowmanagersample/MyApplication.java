package com.example.leidong.windowmanagersample;

import android.app.Application;
import android.content.Context;

/**
 * Created by leido on 2017/4/12.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate(){
        super.onCreate();

        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
