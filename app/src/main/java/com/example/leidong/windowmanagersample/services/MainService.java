package com.example.leidong.windowmanagersample.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.leidong.windowmanagersample.Constants;
import com.example.leidong.windowmanagersample.FloatView;
import com.example.leidong.windowmanagersample.MyApplication;
import com.example.leidong.windowmanagersample.R;
import com.example.leidong.windowmanagersample.broadcasts.ListenCallsBroadcast;
import com.example.leidong.windowmanagersample.utils.ListViewAdapter;
import com.example.leidong.windowmanagersample.utils.NotificationInfos;
import com.example.leidong.windowmanagersample.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leidong on 2017/4/12.
 */

public class MainService extends Service{
    //通知接收广播
    private NotificationReceiver notificationReceiver;
    //通知List
    private List<NotificationInfos> list;
    //ListView适配器
    private ListViewAdapter listViewAdapter;

    @SuppressLint("StaticFieldLeak")
    public static FloatView floatView;

    //电话状态广播
    private ListenCallsBroadcast listenCallsBroadcast;

    //手指滑动起止坐标
    private float x0 = 0f;//x起始
    private float y0 = 0f;//y起始
    private float x1 = 0f;//x终止
    private float y1 = 0f;//y终止

    /**
     * 获取FloatView
     * @return floatView
     */
    public static FloatView getFloatView(){
        return floatView;
    }

    @Override
    public void onCreate(){
        super.onCreate();

        coreOperations();

        Toast.makeText(MyApplication.getContext(), "MainService onCreate", Toast.LENGTH_SHORT).show();
    }

    /**
     * 核心操作
     */
    private void coreOperations() {
        //初始化控件
        initView();
        //添加FloatView
        floatView.addToWindow();
        //注册电话状态监听广播
        registerListenCallsBroadcast();
    }

    @Override
    public int onStartCommand(Intent intent, int flags,int intemtId){
        return super.onStartCommand(intent, flags, intemtId);
    }

    /**
     * 打开LockScreenService
     */
    private void openLockScreenService() {
        Intent intent = new Intent(MainService.this, LockScreenService.class);
        startService(intent);
    }

    /**
     * 注册电话状态监听广播
     */
    private void registerListenCallsBroadcast() {
        listenCallsBroadcast = new ListenCallsBroadcast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.PHONE_CHANGED);
        intentFilter.setPriority(Integer.MAX_VALUE);
        registerReceiver(listenCallsBroadcast, intentFilter);
    }

    /**
     * 撤销电话状态监听广播
     */
    private void removeListenerCallsBroadcast(){
        if(listenCallsBroadcast != null){
            unregisterReceiver(listenCallsBroadcast);
        }
    }

    /**
     * 初始化FloatView
     */
    private void initView() {
        //创建FloatView
        floatView = new FloatView(getApplicationContext(), R.layout.floatview_layout);

        //初始化通知接收广播
        initNotificationReceiver();

        list = new ArrayList<>();
        listViewAdapter = new ListViewAdapter(getApplicationContext(), list);

        //填充FloatView中的各种控件
        fillComponentsOfFloatView();

        //FloatView长按
        floatView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Uri uri = Uri.parse("https://www.baidu.com");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                return false;
            }
        });

        floatView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    x0 = event.getX();
                    y0 = event.getY();
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    x1 = event.getX();
                    y1 = event.getY();
                    if(-1*(y1-y0) >= Constants.minFloatDistance && -1*(x1-x0) <= 100){
                        floatView.removeFromWindow();
                        /*****************************************/
                        openLockScreenService();
                        /*****************************************/
                    }
                }
                return false;
            }
        });
    }

    /**
     * 初始化通知接收广播
     */
    private void initNotificationReceiver() {
        notificationReceiver = new NotificationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.UPDATE);
        registerReceiver(notificationReceiver, filter);
    }

    /**
     * 填充FloatView中的各种控件
     */
    private void fillComponentsOfFloatView() {
        floatView.configTime(TimeUtil.getTime());
        floatView.configImage(Constants.IMAGE_URL);
        floatView.configNotificationList(listViewAdapter);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(floatView != null){
            floatView.removeFromWindow();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 内部类
     * 通知接收广播
     */
    public class NotificationReceiver extends BroadcastReceiver{
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onReceive(Context context, Intent intent) {
            NotificationInfos info = new NotificationInfos();
            Bundle bundle = intent.getExtras();
            info.title = bundle.getString(Notification.EXTRA_TITLE);
            info.text = bundle.getString(Notification.EXTRA_TEXT);
            list.add(info);
            listViewAdapter.notifyDataSetChanged();
        }
    }
}
