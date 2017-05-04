package com.example.leidong.windowmanagersample.floatview;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leidong.windowmanagersample.MyApplication;
import com.example.leidong.windowmanagersample.R;
import com.example.leidong.windowmanagersample.utils.ListViewAdapter;
import com.example.leidong.windowmanagersample.utils.NotificationListView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by leidong on 2017/4/12.
 */

@SuppressLint("ViewConstructor")
public class FloatView extends LinearLayout{
    private WindowManager.LayoutParams windowManagerParams;
    private WindowManager windowManager;

    //FloatView中的相关控件
    private TextView time1;
    private TextView time2;
    private ImageView image;
    private NotificationListView notificationListView;

    /**
     * 构造器1
     * @param context context
     * @param layoutId layoutId
     */
    public FloatView(Context context, int layoutId) {
        super(context);
        View view = LayoutInflater.from(getContext()).inflate(layoutId, null);
        //配置悬浮窗
        setWindowManagerParams(view);
        //获取并填充控件
        initAndOperate();
    }

    /**
     * 初始化控件
     * @param view view
     */
    private void setWindowManagerParams(View view) {
        //配置背景
        configBackground(view);

        windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        windowManagerParams = new WindowManager.LayoutParams();
        //设置你要添加控件的类型，TYPE_ALERT需要申明权限，Toast不需要，在某些定制系统中会禁止悬浮框显示，所以最后用TYPE_TOAST
        windowManagerParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        //设置控件在坐标计算规则，相当于屏幕左上角
        windowManagerParams.gravity =  Gravity.TOP | Gravity.LEFT;
        windowManagerParams.format = PixelFormat.RGBA_8888;
        windowManagerParams.flags = /*WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | */WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
        windowManagerParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        windowManagerParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        windowManagerParams.x = 0;
        windowManagerParams.y = 0;

        if (view != null) {
            addView(view);
        }
    }

    /**
     * 配置悬浮窗背景
     * @param view view
     */
    private void configBackground(View view) {
        WallpaperManager wallpaperManager = WallpaperManager
                .getInstance(MyApplication.getContext());
        //获取当前壁纸
        Drawable wallpaperDrawable = wallpaperManager.getDrawable();
        //设置背景
        view.setBackground(wallpaperDrawable);
    }

    /**
     * 获取控件并设置相关操作
     */
    private void initAndOperate() {
        time1 = (TextView) findViewById(R.id.time1);
        time2 = (TextView) findViewById(R.id.time2);
        image = (ImageView) findViewById(R.id.image);
        notificationListView = (NotificationListView) findViewById(R.id.listView);
    }

    /**
     * 填充FloatView的时间1
     * @param timeStr timeStr
     */
    public void configTime1(String timeStr, final String param){
        time1.setText(timeStr);
        time1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.baidu.com");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                MyApplication.getContext().startActivity(intent);
            }
        });
    }

    /**
     * 填充FloatView的时间2
     * @param curDayTime
     */
    public void comfigTime2(String curDayTime) {
        time2.setText(curDayTime);
    }

    /**
     * 填充FloatView的图片
     */
    public void configImage(String imageUri){
        ImageLoader.getInstance().displayImage(imageUri, image, MyApplication.getOptions());
        image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyApplication.getContext(), "image click操作写在这里", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 设置FloatView中的通知条目列表
     * @param listViewAdapter listViewAdapter
     */
    public void configNotificationList(ListViewAdapter listViewAdapter) {
        notificationListView.setAdapter(listViewAdapter);
        notificationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MyApplication.getContext(), "listView click操作写在这里", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 添加至窗口
     * @return 添加成功标志
     */
    public  boolean addToWindow(){
        if (windowManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (!isAttachedToWindow()) {
                    windowManager.addView(this, windowManagerParams);
                    return true;
                } else {
                    return false;
                }
            } else {
                try {
                    if (getParent() == null)
                        windowManager.addView(this, windowManagerParams);
                    return true;
                } catch (Exception e) {
                    return  false;
                }
            }
        } else {
            return false;
        }
    }

    /**
     * 从窗口移除悬浮窗
     * @return 移除成功标志
     */
    public boolean removeFromWindow() {
        if (windowManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (isAttachedToWindow()) {
                    windowManager.removeViewImmediate(this);
                    return true;
                } else {
                    return false;
                }
            } else {
                try {
                    if (getParent() != null) {
                        windowManager.removeViewImmediate(this);
                    }
                    return true;
                } catch (Exception e) {
                    return  false;
                }
            }
        } else {
            return false;
        }
    }

    /**
     *拦截触摸事件
     * @param ev MotionEvent
     * @return isAllowTouch
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        return super.onInterceptTouchEvent(ev);
    }
}
