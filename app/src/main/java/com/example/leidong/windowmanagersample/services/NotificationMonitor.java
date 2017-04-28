package com.example.leidong.windowmanagersample.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;

import com.example.leidong.windowmanagersample.Constants;

/**
 * Created by leidong on 2017/4/13.
 */

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
@SuppressLint("OverrideAbstract")
public class NotificationMonitor extends NotificationListenerService {
    public static final String TAG = "NotificationMonitor";
    private NotificationMonitorReceiver notificationMonitorReceiver;

    @Override
    public void onCreate(){
        super.onCreate();
        //Toast.makeText(MyApplication.getContext(), "NotificationMonitor --->>> created", Toast.LENGTH_SHORT).show();
        notificationMonitorReceiver = new NotificationMonitorReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.COMMAND);
        registerReceiver(notificationMonitorReceiver, filter);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(notificationMonitorReceiver);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn){
        Notification notification = sbn.getNotification();
        Intent intent = new Intent(Constants.UPDATE);
        intent.putExtras(notification.extras);
        sendBroadcast(intent);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn){
    }

    /**
     * 内部类
     */
    private class NotificationMonitorReceiver extends BroadcastReceiver{
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onReceive(Context context, Intent intent) {
            String command = intent.getStringExtra(Constants.COMMAND_EXTRA);
            //Toast.makeText(MyApplication.getContext(), "command", Toast.LENGTH_SHORT).show();
            //清除所有通知
            if(command.equals(Constants.CLEAR_ALL)){
                NotificationMonitor.this.cancelAllNotifications();
            }
            //显示所有通知
            else if(command.equals(Constants.SHOW_ALL)){
                StatusBarNotification[] sbns = NotificationMonitor.this.getActiveNotifications();
                for(int i = 0; i < sbns.length; i++){
                    Intent intent1 = new Intent(Constants.UPDATE);
                    Notification notification = sbns[i].getNotification();
                    intent1.putExtras(notification.extras);
                    sendBroadcast(intent);
                }
            }
        }
    }
}
