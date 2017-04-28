package com.example.leidong.windowmanagersample.broadcasts;

import android.content.Context;
import android.content.Intent;

import com.example.leidong.windowmanagersample.Constants;

/**
 * Created by leidong on 2017/4/27.
 */

public class LockScreenReceiver extends android.content.BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
            Intent lockIntent = new Intent(Constants.LOCK_SCREEN_ACTION);
            lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            context.startService(lockIntent);
        }
    }
}