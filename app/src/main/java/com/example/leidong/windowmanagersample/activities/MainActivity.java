package com.example.leidong.windowmanagersample.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.leidong.windowmanagersample.MyApplication;
import com.example.leidong.windowmanagersample.R;
import com.example.leidong.windowmanagersample.services.MainService;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class MainActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //检查并配置相关权限
        checkAndConfigurePermissions();
    }

    /**
     * 检查并配置相关权限
     */
    private void checkAndConfigurePermissions() {
        //打开SYSTEM_OVERLAY_WINDOW的权限
        openOverlayWindow();
        //引导用户关闭系统锁屏
        closeSystemLockScreen();
        //打开NotificationListen功能，引导用户打开ACTION_NOTIFICATION_LISTENER_SETTINGS权限
        //openNotificationListen();
        //
    }

    /**
     * 打开NotificationListen功能，引导用户打开ACTION_NOTIFICATION_LISTENER_SETTINGS权限
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private void openNotificationListen() {
        //检查到通知栏使用权是否已经拿到
        boolean isNotificationListen = isNotificationListenEnabled();
        startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
    }

    /**
     * 引导用户关闭系统锁屏
     */
    private void closeSystemLockScreen() {
        boolean hasKeyGuard = false;
        //判断用户是否设置了系统锁屏
        if(MyApplication.getAPIVersion() >= 15){
            hasKeyGuard = ((KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE)).isKeyguardSecure();
        }
        else{
            Class<?> clazz = null;
            try {
                clazz = Class.forName("com.android.internal.widget.LockPatternUtils");
                Constructor<?> constructor = clazz.getConstructor(Context.class);
                constructor.setAccessible(true);
                Object utils = constructor.newInstance(this);
                Method method = clazz.getMethod("isSecure");
                hasKeyGuard = (Boolean) method.invoke(utils);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //引导用户关闭系统锁屏
        if(hasKeyGuard){
            Intent in = new Intent(Settings.ACTION_SECURITY_SETTINGS);
            startActivity(in);
        }
    }

    /**
     * 引导用户打开SYSTEM_OVERLAY_WINDOW的权限
     */
    @SuppressLint("NewApi")
    private void openOverlayWindow() {
        if (MyApplication.getAPIVersion() >= 23) {
            if (!Settings.canDrawOverlays(MainActivity.this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent,10);
            }
        }
    }

    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10) {
            if (MyApplication.getAPIVersion() >= 23) {
                if (!Settings.canDrawOverlays(this)) {
                    // SYSTEM_ALERT_WINDOW permission not granted...
                    Toast.makeText(MainActivity.this,"not granted",Toast.LENGTH_SHORT);
                }
            }
        }
    }

    /**
     * 主布局中按钮点击监听
     * @param view
     */
    public void onClick(View view){
        switch (view.getId()){
            case R.id.bt_remove:
                Intent stopIntent = new Intent(MainActivity.this, MainService.class);
                stopService(stopIntent);
                //floatView.removeFromWindow();
                break;
            case R.id.bt_create:
                Intent startIntent = new Intent(MainActivity.this, MainService.class);
                startService(startIntent);
                //floatView.addToWindow();
                break;
            default:
                break;
        }
    }

    /**
     * 检查到通知栏使用权是否已经拿到
     * @return
     */
    public boolean isNotificationListenEnabled() {
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(),
                "enabled_notification_listeners");
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
