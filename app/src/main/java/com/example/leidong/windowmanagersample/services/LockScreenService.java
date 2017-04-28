package com.example.leidong.windowmanagersample.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.leidong.windowmanagersample.MyApplication;
import com.example.leidong.windowmanagersample.broadcasts.LockScreenReceiver;

/**
 * Created by leidong on 2017/4/27.
 */

public class LockScreenService extends Service {
    private BroadcastReceiver broadcastReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);

        broadcastReceiver = new LockScreenReceiver();
        registerReceiver(broadcastReceiver, filter);

        Toast.makeText(MyApplication.getContext(), "LockScreenService onCreate", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int intentId){
        return super.onStartCommand(intent, flags, intentId);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
