package library.Component;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @ explain:这个 ViewPager是用来解决ScrollView里面嵌套ViewPager的 内部解决法的
 * @ author：xujun on 2016/10/25 16:38
 * @ email：gdutxiaoxu@163.com
 */
public class MyViewPager extends ViewPager {
    int lastX = -1;
    int lastY = -1;

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getRawX();
        int y = (int) ev.getRawY();
        int dealtX = 0;
        int dealtY = 0;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dealtX = 0;
                dealtY = 0;
                // 保证子View能够接收到Action_move事件
               // getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                dealtX += Math.abs(x - lastX);
                dealtY += Math.abs(y - lastY);
                // 这里是否拦截的判断依据是左右滑动，读者可根据自己的逻辑进行是否拦截
                /*
                if (dealtX >= dealtY) { // 左右滑动请求父 View 不要拦截
                    getParent().requestDisallowInterceptTouchEvent(true);
                }else{
                   getParent().requestDisallowInterceptTouchEvent(false);
                }
                */
/*
                int[] location = new int[2];
                getLocationOnScreen(location);
                Log.e("webviewpager", "X:" + location[0] + "Y:" + location[1]);
                if (location[1] == -56) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
*/
                lastX = x;
                lastY = y;

                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_UP:
                break;

        }
        return super.dispatchTouchEvent(ev);
    }
}

