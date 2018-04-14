package adapter;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import library.GetBitmap;
import library.ResolveJsonData;

public class MyPagerAdapter extends PagerAdapter implements View.OnTouchListener {
    private List<ImageView> mListViews;
    private TextView[] dot;
    Handler handler;
    Runnable runnable;
    ViewPager viewPager;
    TimerTask task;
    Timer timer;
    JSONObject json;
    public MyPagerAdapter( JSONObject json, final ViewPager viewPager, TextView... dot) {
        this.json = json;
        this.dot = dot;
        this.viewPager = viewPager;
        getImageView();
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        };
        timer = new Timer(true);
        task = new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                handler.post(runnable);
            }
        };
        timer.schedule(task, 4000, 4000);
        viewPager.addOnPageChangeListener(new MyPagerAdapter.MyPageChangeListener());
    }
public void getImageView(){

    new Thread(new Runnable() {
        @Override
        public void run() {
            ArrayList<Map<String, String>> bitmaps= ResolveJsonData.getJSONData(json);
            String[] urls=new String[bitmaps.size()];
            for(int i=0;i<bitmaps.size();i++) {
                urls[i] = bitmaps.get(i).get("image");
                ArrayList<Bitmap> bimaps = GetBitmap.getBitmaps(urls);

            }

        }
    }).start();

}
    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //container.removeView(mListViews.get(position));
    }

    @Override
    public void finishUpdate(ViewGroup container) {
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        /*
        position %= mListViews.size();
        if (position<0){
            position = mListViews.size()+position;
        }
        ImageView view = mListViews.get(position);
        //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
        ViewParent vp =view.getParent();
        if (vp!=null){
            ViewGroup parent = (ViewGroup)vp;
            parent.removeView(view);
        }
        container.addView(view);
        return view;
        */
        if (mListViews.size() > 0) {
            //position % view.size()是指虚拟的position会在[0，view.size()）之间循环
            View view = mListViews.get(position % mListViews.size());
            if (container.equals(view.getParent())) {
                container.removeView(view);
            }
            container.addView(view);
            view.setOnTouchListener(this);
            return view;
        }
        return null;


    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(ViewGroup container) {
        Log.e("startUpdate", "startUpdate");
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        timer.cancel();
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("ACTION_DOWN", "ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("ACTION_MOVE", "ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.e("ACTION_UP", "ACTION_UP");
                timer = new Timer(true);
                task = new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(runnable);
                    }
                };
                timer.schedule(task, 4000, 4000);
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.e("ACTION_CANCEL", "ACTION_CANCEL");
                timer = new Timer(true);
                task = new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(runnable);
                    }
                };
                timer.schedule(task, 4000, 4000);
                break;
        }

        return true;
    }

    class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            position = position % mListViews.size();
            switch (position) {
                case 0:
                    dot[0].setTextColor(Color.WHITE);
                    dot[1].setTextColor(Color.BLACK);
                    dot[2].setTextColor(Color.BLACK);
                    break;
                case 1:
                    dot[0].setTextColor(Color.BLACK);
                    dot[1].setTextColor(Color.WHITE);
                    dot[2].setTextColor(Color.BLACK);
                    break;
                case 2:
                    dot[0].setTextColor(Color.BLACK);
                    dot[1].setTextColor(Color.BLACK);
                    dot[2].setTextColor(Color.WHITE);
                    break;

            }
            Log.e("onPageSelected", "onPageSelected");
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }


    }
    public void setFilter(JSONObject json) {
        notifyDataSetChanged();

    }

}