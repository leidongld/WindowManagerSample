package com.example.leidong.windowmanagersample.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by leidong on 2017/4/12.
 */

public class TimeUtil {
    private TimeUtil() throws InstantiationException {
        throw new InstantiationException("该类不能被实例化！");
    }

    /**
     * 返回时间
     * @return 返回的时间
     */
    public static String getCurTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date currentTime = new Date();
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 返回时间
     * @return 返回的时间
     */
    public static String getCurDayTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentTime = new Date();
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 返回时间
     * @return 返回的时间
     */
    public static String getTime() {
        String timeString = getCurTime();
        String[] timeArr = timeString.split(":");
        String dateString = timeArr[0] + ":" + timeArr[1];
        return dateString;
    }
}
