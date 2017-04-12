package com.example.leidong.windowmanagersample.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.example.leidong.windowmanagersample.R;
import com.example.leidong.windowmanagersample.services.MainService;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(MainActivity.this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent,10);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10) {
            if (Build.VERSION.SDK_INT >= 23) {
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
}
