package com.example.leidong.windowmanagersample.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.example.leidong.windowmanagersample.Constants;
import com.example.leidong.windowmanagersample.services.MainService;

/**
 * Created by leidong on 2017/4/28.
 */

public class AutoStartBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Constants.AUTO_START_ACTION)) {
            Intent service = new Intent(context, MainService.class);
            context.startService(service);
        }
    }
}
