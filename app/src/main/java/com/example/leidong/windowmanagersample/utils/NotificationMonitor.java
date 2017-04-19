package com.example.leidong.windowmanagersample.utils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.util.Log;

/**
 * Created by leidong on 2017/4/13.
 */

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
@SuppressLint("OverrideAbstract")
public class NotificationMonitor extends NotificationListenerService {
    public static final String TAG = "NotificationMonitor";
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent,flags,startId);
    }

    //新的Notification到达
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
    }

    //新的Notification到达，api 21新增
    @Override
    public void onNotificationPosted(StatusBarNotification sbn, RankingMap rankingMap) {
        super.onNotificationPosted(sbn, rankingMap);
    }

    //Notification被移除
    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
        Log.i(TAG, "open >>>" + sbn.toString());
    }

    //Notification被移除，api 21新增
    @Override
    public void onNotificationRemoved(StatusBarNotification sbn, RankingMap rankingMap) {
        super.onNotificationRemoved(sbn, rankingMap);
        Log.i(TAG, "shut >>> " + sbn.toString());
    }

    //Notification排序变动，api 21新增
    @Override
    public void onNotificationRankingUpdate(RankingMap rankingMap) {
        super.onNotificationRankingUpdate(rankingMap);
    }

    //Service与系统通知栏完成绑定时回调，绑定后才能收到通知栏回调，api 21新增
    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
    }
}
