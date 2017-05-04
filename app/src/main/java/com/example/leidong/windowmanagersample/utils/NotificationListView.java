package com.example.leidong.windowmanagersample.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Scroller;

/**
 * Created by leidong on 2017/5/4.
 */

public class NotificationListView extends ListView {
    //当前滑动的ListView的Position
    private int slidePosition;
    //手指按下x的坐标
    private int downX;
    //手指按下y的坐标
    private int downY;
    //屏幕宽度
    private int screenWidth;
    //ListView的Item
    private View itemView;
    //滑动类
    private Scroller scroller;
    //最小滑动距离
    private static final int SNAP_VELOCITY = 600;
    //速度追踪对象
    private VelocityTracker velocityTracker;
    //是否应该滑动，默认为false
    private boolean isSlide = false;
    //用户滑动的最小距离
    private int mTouchSlop;
    //移除Item后的回调接口
    private RemoveListener removeListener;
    //滑动的方向(枚举)
    private RemoveDirection removeDirection;

    public NotificationListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NotificationListView(Context context) {
        this(context, null);
    }

    public NotificationListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        screenWidth = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        scroller = new Scroller(context);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    /**
     * 枚举
     */
    enum RemoveDirection{
        LEFT, RIGHT;
    }

    /**
     * 设置滑动删除的回调接口
     * @param removeListener removeListener
     */
    public void setRemoveListener(RemoveListener removeListener){
        this.removeListener = removeListener;
    }

    /**
     * 分发事件
     * @param event event
     * @return boolean
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //添加速度追踪器
                addVelocityTracker(event);
                //如果scroller的滚动还未结束，直接返回
                if(!scroller.isFinished()){
                    return super.dispatchTouchEvent(event);
                }
                downX = (int) event.getX();
                downY = (int) event.getY();

                slidePosition = pointToPosition(downX, downY);

                //位置无效直接返回
                if(slidePosition == AdapterView.INVALID_POSITION){
                    return super.dispatchTouchEvent(event);
                }

                //获取我们点击的item View
                itemView = getChildAt(slidePosition - getFirstVisiblePosition());
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(getScrollVelocity()) > SNAP_VELOCITY
                        || (Math.abs(event.getX() - downX) > mTouchSlop && Math
                        .abs(event.getY() - downY) < mTouchSlop)) {
                    isSlide = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                recycleVelocityTracker();
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     *移除用户速度跟踪器
     */
    private void recycleVelocityTracker() {
        if(velocityTracker != null){
            velocityTracker.recycle();
            velocityTracker = null;
        }
    }

    /**
     *获取X方向的滑动速度,大于0向右滑动，反之向左
     * @return 滑动速度
     */
    private int getScrollVelocity() {
        velocityTracker.computeCurrentVelocity(1000);
        return (int) velocityTracker.getXVelocity();
    }

    /**
     * 添加速度追踪器
     * @param event event
     */
    private void addVelocityTracker(MotionEvent event) {
        if(velocityTracker == null){
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);
    }

    /**
     *向右滑动
     */
    private void scrollRight(){
        removeDirection = RemoveDirection.RIGHT;
        final int delta = (screenWidth + itemView.getScrollX());
        //调用startScroller()方法来设置一些滚动的参数
        scroller.startScroll(itemView.getScrollX(), 0, -delta, 0, Math.abs(delta));
        //刷新itemView
        postInvalidate();
    }

    /**
     * 向右滑动
     */
    private void scrollLeft(){
        removeDirection = RemoveDirection.LEFT;
        final int delta = (screenWidth + itemView.getScrollX());
        //调用startScroller()方法来设置一些滚动的参数
        scroller.startScroll(itemView.getScrollX(), 0, delta, 0, Math.abs(delta));
        //刷新itemView
        postInvalidate();
    }

    /**
     * 根据手指x方向滚动的距离判断是滚动到开始位置还是向左向右滚动
     */
    private void scrollByDistanceX(){
        if(itemView.getScrollX() >= screenWidth/2){
            scrollLeft();
        }
        else if(itemView.getScrollX() <= -screenWidth/2){
            scrollRight();
        }
        else{
            itemView.scrollTo(0,0);
        }
    }

    /**
     * 处理拖动ListView item的逻辑
     * @return boolean
     */
    public boolean onTouchEvent(MotionEvent event){
        if(isSlide && slidePosition != AdapterView.INVALID_POSITION) {
            //防止父控件阻止事件分发
            requestDisallowInterceptTouchEvent(true);
            //添加速度探测器
            addVelocityTracker(event);
            int x = (int) event.getX();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    MotionEvent cancelEvent = MotionEvent.obtain(event);
                    cancelEvent.setAction(MotionEvent.ACTION_CANCEL
                            | (event.getActionIndex() << MotionEvent.ACTION_POINTER_INDEX_SHIFT));
                    onTouchEvent(cancelEvent);
                    int deltaX = downX - x;
                    downX = x;
                    //手指拖动itemView滚动，deltaX大于0向左滚动，小于0向右滚动
                    itemView.scrollBy(deltaX, 0);
                    return true;//拖动的时候ListView不滚动
                case MotionEvent.ACTION_UP:
                    int velocityX = getScrollVelocity();
                    if(velocityX > SNAP_VELOCITY){
                        scrollRight();
                    }
                    else if(velocityX < -SNAP_VELOCITY){
                        scrollLeft();
                    }
                    else{
                        scrollByDistanceX();
                    }
                    recycleVelocityTracker();
                    //手指离开的时候不响应左右滚动
                    isSlide = false;
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll(){
        if(scroller.computeScrollOffset()){
            //item根据当前的滚动偏移量进行滚动
            itemView.scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
            if(scroller.isFinished()){
                if(removeListener == null){
                    throw new NullPointerException("RemoveListener is null, we should called setRemoveListener()");
                }
                itemView.scrollTo(0, 0);
                removeListener.removeItem(removeDirection, slidePosition);
            }
        }
    }

    /**
     * 当ListView item滑出屏幕，回调这个接口
     * 我们需要在回调方法removeItem()中移除该Item,然后刷新ListView
     * @author xiaanming
     */
    interface RemoveListener {
        void removeItem(RemoveDirection direction, int position);
    }


}
