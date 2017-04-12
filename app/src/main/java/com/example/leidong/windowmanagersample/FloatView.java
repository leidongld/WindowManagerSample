package com.example.leidong.windowmanagersample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    private IFloatViewClick listener;
    private boolean isAllowTouch=true;

    //FloatView中的相关控件
    private TextView time;
    private ImageView image;
    private Button bt;

    //构造器1
    public FloatView(Context context, int layoutId) {
        super(context);
        View view = LayoutInflater.from(getContext()).inflate(layoutId, null);
        setWindowManagerParams(view);
        initAndOperate();
    }

    //构造器2
    public FloatView(Context context, View childView) {
        super(context);
        setWindowManagerParams(childView);
    }

    /**
     * 初始化控件
     * @param view
     */
    private void setWindowManagerParams(View view) {
        windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        windowManagerParams = new WindowManager.LayoutParams();
        //设置你要添加控件的类型，TYPE_ALERT需要申明权限，Toast不需要，在某些定制系统中会禁止悬浮框显示，所以最后用TYPE_TOAST
        windowManagerParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        //设置控件在坐标计算规则，相当于屏幕左上角
        windowManagerParams.gravity =  Gravity.TOP | Gravity.LEFT;
        windowManagerParams.format = PixelFormat.RGBA_8888;
        windowManagerParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        windowManagerParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        windowManagerParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        windowManagerParams.x = 0;
        windowManagerParams.y = 0;
        if (view != null) {
            addView(view);
        }
    }

    /**
     * 获取控件并设置相关操作
     */
    private void initAndOperate() {
        time = (TextView) findViewById(R.id.time);
        image = (ImageView) findViewById(R.id.image);
        bt = (Button) findViewById(R.id.bt);
    }

    /**
     * 悬浮窗点击监听
     * @param listener
     */
    public void setFloatViewClickListener(IFloatViewClick listener) {
        this.listener = listener;
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
            case MotionEvent.ACTION_UP:
                y = (int) event.getRawY() - this.getMeasuredHeight() / 2 - 25;
                x = (int) event.getRawX() - this.getMeasuredWidth() / 2;
                if (Math.abs(y - mTouchStartY) > 10 || Math.abs(x - mTouchStartX) > 10) {
                    windowManager.updateViewLayout(this, windowManagerParams);
                }
                else {
                    if (listener != null) {
                        listener.onFloatViewClick();
                    }
                }
                return true;
            default:
                break;
        }
        return false;
    }

    public interface IFloatViewClick {
        void onFloatViewClick();
    }
}
