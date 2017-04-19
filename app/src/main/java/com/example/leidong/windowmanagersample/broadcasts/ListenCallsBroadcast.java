package com.example.leidong.windowmanagersample.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import com.example.leidong.windowmanagersample.Constants;
import com.example.leidong.windowmanagersample.services.MainService;

/**
 * Created by leidong on 2017/4/13.
 */

public class ListenCallsBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals(Constants.PHONE_CHANGED)){
            doReceivePhone(context, intent);
        }
    }

    /**
     *处理电话广播
     * @param context
     * @param intent
     */
    public void doReceivePhone(Context context, Intent intent) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int callState = telephonyManager.getCallState();
        switch (callState){
            case TelephonyManager.CALL_STATE_RINGING:
                Toast.makeText(context, "有电话啦", Toast.LENGTH_SHORT).show();
                MainService.getFloatView().removeFromWindow();
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                Toast.makeText(context, "电话打完啦", Toast.LENGTH_SHORT).show();
                MainService.getFloatView().addToWindow();
                break;
            default:
                break;
        }
    }
}
