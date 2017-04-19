package com.example.leidong.windowmanagersample;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by leidong on 2017/4/12.
 */

@SuppressLint("ViewConstructor")
public class FloatView extends LinearLayout{
    private WindowManager.LayoutParams windowManagerParams;
    private WindowManager windowManager;
    private float mTouchStartX;
    private float mTouchStartY;
    private float x;
    private float y;
    private IFloatViewClick floatViewClickListener;
    private boolean isAllowTouch=true;

    //FloatView中的相关控件
    private TextView time;
    private ImageView image;
    private ListView listView;

    /**
     * 构造器1
     * @param context
     * @param layoutId
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
     * 构造器2
     * @param context
     * @param childView
     */
    public FloatView(Context context, View childView) {
        super(context);
        setWindowManagerParams(childView);
    }

    /**
     * 初始化控件
     * @param view
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
        windowManagerParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
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
     * @param view
     */
    private void configBackground(View view) {
        WallpaperManager wallpaperManager = WallpaperManager
                .getInstance(MyApplication.getContext());
        //获取当前壁纸
        Drawable wallpaperDrawable = wallpaperManager.getDrawable();
        // 将Drawable,转成Bitmap
        Bitmap bm = ((BitmapDrawable) wallpaperDrawable).getBitmap();
        // 设置 背景
        //view.setBackgroundDrawable(new BitmapDrawable(bm));
        view.setBackground(wallpaperDrawable);
    }

    /**
     * 获取控件并设置相关操作
     */
    private void initAndOperate() {
        time = (TextView) findViewById(R.id.time);
        image = (ImageView) findViewById(R.id.image);
        listView = (ListView) findViewById(R.id.listView);
    }

    /**
     * 填充FloatView的时间
     * @param timeStr
     */
    public void setTime(String timeStr){
        time.setText(timeStr);
    }

    /**
     * 填充FloatView的图片
     */
    public void setImage(String imageUri){
        ImageLoader.getInstance().displayImage(imageUri, image, MyApplication.getOptions());
    }

    /**
     * 通知填充ListView
     */
    public void setListView(){

    }

    /**
     * 添加至窗口
     * @return
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
     * 是否允许点击的标志
     * @param flag
     */
    public void setIsAllowTouch(boolean flag){
        isAllowTouch=flag;
    }

    /**
     * 从窗口移除悬浮窗
     * @return
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
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        return isAllowTouch;
    }

    /**
     * 触摸事件
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchStartX = (int) event.getRawX() - this.getMeasuredWidth() / 2;
                mTouchStartY = (int) event.getRawY() - this.getMeasuredHeight() / 2 - 25;
                return true;
            case MotionEvent.ACTION_MOVE:
                windowManagerParams.x = (int) event.getRawX() - this.getMeasuredWidth() / 2;
                // 减25为状态栏的高度
                windowManagerParams.y = (int) event.getRawY() - this.getMeasuredHeight() / 2 - 25;
                // 刷新
                windowManager.updateViewLayout(this, windowManagerParams);
                return true;
            /*case MotionEvent.ACTION_UP:
                y = (int) event.getRawY() - this.getMeasuredHeight() / 2 - 25;
                x = (int) event.getRawX() - this.getMeasuredWidth() / 2;
                if (Math.abs(y - mTouchStartY) > 10 || Math.abs(x - mTouchStartX) > 10) {
                    windowManager.updateViewLayout(this, windowManagerParams);
                }
                else {
                    if (floatViewClickListener != null) {
                        floatViewClickListener.onFloatViewClick();
                    }
                }
                return true;*/
            default:
                break;
        }
        return false;
    }

    /**
     * FloatView点击的监听
     */
    public interface IFloatViewClick {
        void onFloatViewClick();
    }

    public void setFloatViewClickListener(IFloatViewClick listener) {
        this.floatViewClickListener = listener;
    }
}
