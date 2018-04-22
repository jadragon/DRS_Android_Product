package adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.test.tw.wrokproduct.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import library.ImageLoaderUtils;
import library.ResolveJsonData;

public class MyPagerAdapter extends PagerAdapter implements View.OnTouchListener {
    private List<ImageView> mListViews;
    private List<TextView> dots;
    private ViewPager viewPager;
    private Timer timer;
    private Context ctx;
    private ImageView imageView;
    private ArrayList<Map<String, String>> bitmaps;
    private LinearLayout linearLayout;
    private TextView textView;
    private int mChildCount = 0;
    private  View view;

    public MyPagerAdapter(View view, JSONObject json) {
        this.view = view;
        this.ctx = view.getContext();
/*
   View view = View.inflate(ctx, R.layout.viewitem_homeheader, null);
        dot[0] = view.findViewById(R.id.dot1);
        dot[1] = ((Activity) ctx).findViewById(R.id.dot2);
*/
        viewPager = view.findViewById(R.id.adView);
        getImageView(json);
        //兩張圖以上才放dot及定時翻頁
        if (bitmaps.size() > 1) {
            initTimer();
            initDot();
            viewPager.addOnPageChangeListener(new MyPagerAdapter.MyPageChangeListener());
        }
    }

    private void initTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ((Activity) view.getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    }
                });

            }
        }, 4000, 4000);
    }

    public void getImageView(JSONObject json) {
        bitmaps = ResolveJsonData.getJSONData(json);
        mListViews = new ArrayList<>();
        for (int i = 0; i < bitmaps.size(); i++) {
            imageView = new ImageView(view.getContext());

            imageView.setTag(i);
            mListViews.add(imageView);
            final int finalI = i;
            ImageLoader.getInstance().loadImage(bitmaps.get(i).get("image"), ImageLoaderUtils.getWholeOptions(), new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view,
                                              Bitmap loadedImage) {
                    super.onLoadingComplete(imageUri, view, loadedImage);
                    mListViews.get(finalI).setImageBitmap(loadedImage);

                }

            });
            // ImageLoader.getInstance().displayImage(bitmaps.get(i).get("image"), imageView);


        }

    }

    private void initDot() {
        dots = new ArrayList<>();
        linearLayout = view.findViewById(R.id.dot_layout);
        linearLayout.removeAllViews();
        //添加第一個dot
        textView = new TextView(view.getContext());
        textView.setText(".");
        textView.setTextSize(50);
        textView.setTextColor(Color.BLACK);
        linearLayout.addView(textView);
        dots.add(textView);
        //添加後面的dots
        for (int i = 0; i < bitmaps.size() - 1; i++) {
            textView = new TextView(view.getContext());
            textView.setText(".");
            textView.setTextSize(50);
            textView.setTextColor(Color.WHITE);
            linearLayout.addView(textView);
            dots.add(textView);
        }
    }

    @Override
    public int getCount() {
        if (bitmaps.size() > 1)
            return Integer.MAX_VALUE;
        else
            return 1;

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
      //container.removeView(mListViews.get(position % mListViews.size()));
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
        //Log.e("startUpdate", "startUpdate");
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(bitmaps.size()>1) {
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
                    int position = (int) view.getTag();

                    Toast.makeText(ctx, "" + bitmaps.get(position).get("title"), Toast.LENGTH_SHORT).show();

                    initTimer();
                    break;
                case MotionEvent.ACTION_CANCEL:
                    Log.e("ACTION_CANCEL", "ACTION_CANCEL");
                    initTimer();
                    break;
            }
        }else{
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.e("ACTION_DOWN", "ACTION_DOWN");
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.e("ACTION_MOVE", "ACTION_MOVE");
                    break;
                case MotionEvent.ACTION_UP:
                    Log.e("ACTION_UP", "ACTION_UP");
                    int position = (int) view.getTag();
                    Toast.makeText(ctx, "" + bitmaps.get(position).get("title"), Toast.LENGTH_SHORT).show();
                    break;
                case MotionEvent.ACTION_CANCEL:
                    Log.e("ACTION_CANCEL", "ACTION_CANCEL");
                    break;
            }
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
            for (int i = 0; i < mListViews.size(); i++) {
                dots.get(i).setTextColor(Color.WHITE);
            }
            dots.get(position).setTextColor(Color.BLACK);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    }

    /**
     * 覆盖getItemPosition()方法，当调用notifyDataSetChanged时，让getItemPosition方法人为的返回POSITION_NONE，从而达到强迫viewpager重绘所有item的目的。
     */
    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        if (mChildCount > 0) {
            mChildCount--;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

    public void setFilter(JSONObject json) {
        timer.cancel();
        getImageView(json);
        initDot();
        notifyDataSetChanged();
    }

}