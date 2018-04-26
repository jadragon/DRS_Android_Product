package adapter;

import android.content.Context;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.test.tw.wrokproduct.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import library.ResolveJsonData;

public class PcContentPagerAdapter extends PagerAdapter implements View.OnTouchListener {
    private List<View> mListViews;
    private List<TextView> dots;
    private ViewPager viewPager;
    private Context ctx;
    private ImageView imageView;
    private ArrayList<Map<String, String>> bitmaps;
    private LinearLayout linearLayout;
    private TextView textView;
    private int mChildCount = 0;
    private RelativeLayout relativeLayout;

    public PcContentPagerAdapter(View current_view, JSONObject json) {
        this.ctx = current_view.getContext();
        this.linearLayout = current_view.findViewById(R.id.dot_layout);
        this.viewPager = current_view.findViewById(R.id.adView);
        this.relativeLayout=current_view.findViewById(R.id.RelateView);
        getImageView(json);
        initDot();
        viewPager.addOnPageChangeListener(new PageChangeListener());
        //兩張圖以上才放dot及定時翻頁
        if (bitmaps.size() > 1) {
        }
    }
    public PcContentPagerAdapter(View current_view, JSONObject json, int width, int heigh) {
        this.ctx = current_view.getContext();
        this.linearLayout = current_view.findViewById(R.id.dot_layout);
        this.viewPager = current_view.findViewById(R.id.adView);
        this.relativeLayout=current_view.findViewById(R.id.RelateView);
        relativeLayout.setLayoutParams(new LinearLayout.LayoutParams(width,heigh));
        getImageView(json);
        initDot();
        viewPager.addOnPageChangeListener(new PageChangeListener());
        //兩張圖以上才放dot及定時翻頁
        if (bitmaps.size() > 1) {
        }
    }

    public void getImageView(JSONObject json) {
        bitmaps = ResolveJsonData.getPcContent(json);
        Log.e("Image",bitmaps+"");
        mListViews = new ArrayList<>();
        for (int i = 0; i < bitmaps.size(); i++) {
            imageView = new ImageView(ctx);
            ImageLoader.getInstance().displayImage(bitmaps.get(i).get("img"), imageView);
            imageView.setTag(i);
            mListViews.add(imageView);
        }
    }


    private void initDot() {
        dots = new ArrayList<>();
        //添加第一個dot
        textView = new TextView(ctx);
        textView.setText(".");
        textView.setTextSize(50);
        textView.setTextColor(Color.BLACK);
        linearLayout.addView(textView);
        dots.add(textView);
        //添加後面的dots
        for (int i = 0; i < bitmaps.size() - 1; i++) {
            textView = new TextView(ctx);
            textView.setText(".");
            textView.setTextSize(50);
            textView.setTextColor(Color.WHITE);
            linearLayout.addView(textView);
            dots.add(textView);
        }
    }

    @Override
    public int getCount() {
        return mListViews.size();

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mListViews.get(position));
    }

    @Override
    public void finishUpdate(ViewGroup container) {
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mListViews.get(position), 0);
        return mListViews.get(position);
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
        if (bitmaps.size() > 1) {
            // timer.cancel();
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

                    //   initTimer();
                    break;
                case MotionEvent.ACTION_CANCEL:
                    Log.e("ACTION_CANCEL", "ACTION_CANCEL");
                    //  initTimer();
                    break;
            }
        } else {
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

    private class PageChangeListener implements ViewPager.OnPageChangeListener {
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
        //  timer.cancel();
        getImageView(json);
        // initDot();
        notifyDataSetChanged();
    }

}