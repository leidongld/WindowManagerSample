package com.example.leidong.windowmanagersample;

import android.telephony.TelephonyManager;

/**
 * Created by leidong on 2017/4/13.
 */

public class Constants {
    public static final String IMAGE_URL = "http://rescdn.qqmail.com/dyimg/20140630/7D38689E0A7A.jpg";
    public static final String PHONE_CHANGED = TelephonyManager.ACTION_PHONE_STATE_CHANGED;

    //FloatView滑动的最短距离
    public static final int minFloatDistance = 200;

    public static final String UPDATE = "com.example.leidong.windowmanagersample.NOTIFICATION_LISTENER_EXAMPLE";

    public static final String COMMAND = "com.example.leidong.windowmanagersample.COMMAND_NOTIFICATION_LISTENER_SERVICE";
    public static final String COMMAND_EXTRA = "command";
    public static final String CANCEL_ALL = "clear";
    public static final String GET_LIST = "show";
}
