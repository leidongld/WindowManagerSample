package com.example.leidong.windowmanagersample.services;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.leidong.windowmanagersample.FloatView;
import com.example.leidong.windowmanagersample.R;

/**
 * Created by leidong on 2017/4/12.
 */

public class MainService extends Service{
    FloatView floatView;

    @Override
    public void onCreate(){
        super.onCreate();
        initView();
        floatView.addToWindow();
        Toast.makeText(getApplicationContext(), "HAHAHA", Toast.LENGTH_SHORT).show();
    }

    /**
     * 初始化FloatView
     */
    private void initView() {
        floatView = new FloatView(getApplicationContext(), R.layout.floatview_layout);
        floatView.setFloatViewClickListener(new FloatView.IFloatViewClick() {
            @Override
            public void onFloatViewClick() {
                Toast.makeText(getApplicationContext(), "drsegfeoregafoc", Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse("https://www.baidu.com");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy(){
        if(floatView != null){
            floatView.removeFromWindow();
        }
    }
}
