package com.example.leidong.windowmanagersample;

import android.telephony.TelephonyManager;

/**
 * Created by leidong on 2017/4/13.
 */

public class Constants {
    public static final String LOCK_SCREEN_ACTION = "android.intent.action.LOCK_SCREEN";
    public static final String AUTO_START_ACTION = "android.intent.action.BOOT_COMPLETED";

    public static final String IMAGE_URL = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493807752629&di=38451ea9a55b0d74b87d98daf061855d&imgtype=0&src=http%3A%2F%2Fpic31.photophoto.cn%2F20140603%2F0011024356959453_b.jpg";
    public static final String PHONE_CHANGED = TelephonyManager.ACTION_PHONE_STATE_CHANGED;

    //FloatView滑动的最短距离
    public static final int minFloatDistance = 200;

    public static final String UPDATE = "com.example.leidong.windowmanagersample.NOTIFICATION_LISTENER_EXAMPLE";

    public static final String COMMAND = "com.example.leidong.windowmanagersample.COMMAND_NOTIFICATION_LISTENER_SERVICE";
    public static final String COMMAND_EXTRA = "command";
    public static final String CLEAR_ALL = "clear";
    public static final String SHOW_ALL = "show";
}
