package Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.test.tw.wrokproduct.R;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import adapter.ShopRecyclerViewAdapter;
import library.ResolveJsonData;

public class Fragment_shop_content extends Fragment {
    RecyclerView recyclerView;
    ShopRecyclerViewAdapter myRecyclerAdapter1;
    ViewPager viewPager;
    JSONObject json1, json2;
    View v;
    Handler handler;
    DisplayMetrics dm;
    com.nostra13.universalimageloader.core.ImageLoader imageLoader= com.nostra13.universalimageloader.core.ImageLoader.getInstance();
    public Fragment_shop_content() {
    }

    @SuppressLint("ValidFragment")
    public Fragment_shop_content(JSONObject json1, JSONObject json2) {
        this.json1 = json1;
        this.json2 = json2;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_shop_content_layout, container, false);
        initViewPagerAndRecyclerView();
        return v;
    }

    private void initViewPagerAndRecyclerView() {
        recyclerView = v.findViewById(R.id.shop_conttent_re);
        viewPager = v.findViewById(R.id.adView);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //recycleView
                               dm = getResources().getDisplayMetrics();
                                int real_heigh = (int) ((dm.widthPixels - 10 * dm.density) / (float)2);
                                myRecyclerAdapter1 = new ShopRecyclerViewAdapter(getContext(), json1,json2, real_heigh, (int)(real_heigh+(110 * dm.density)));
                                GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
                                recyclerView.setLayoutManager(layoutManager);
                                /*
                                recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                                    @Override
                                    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                                        outRect.left = 50;
                                        outRect.right = 50;
                                        outRect.bottom = 50;

                                        // Add top margin only for the first item to avoid double space between items
                                        if (parent.getChildPosition(view) == 0)
                                            outRect.top = 50;
                                    }
                                });
                                */
                                recyclerView.setAdapter(myRecyclerAdapter1);
                                setHeaderView(myRecyclerAdapter1);
                                setFooterView(myRecyclerAdapter1);
                            }
                        });
                    }
                });
                Looper.loop();
            }
        }).start();


    }

    private void setHeaderView(ShopRecyclerViewAdapter adapter) {
        Banner header = (Banner)LayoutInflater.from(getActivity()).inflate(R.layout.testbanner, recyclerView, false);
       // Banner header=new Banner(getActivity());
        header.setLayoutParams(new LinearLayout.LayoutParams(dm.widthPixels, dm.widthPixels * 19 / 54));
        header.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        header.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                imageLoader.displayImage(path.toString(), imageView);
            }
        });
        List<String> images=new ArrayList<>();
        for(Map<String,String> map: ResolveJsonData.getJSONData1(json1))
            images.add(map.get("image"));
        header.setImages(images);
        //banner设置方法全部调用完毕时最后调用
        header.start();

        adapter.setmHeaderView(header);
    }
    private void setFooterView(ShopRecyclerViewAdapter adapter) {
        View footer = new LinearLayout(getActivity());
        footer.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,50));
        adapter.setmFooterViewView(footer);
    }


    public void setFilter(JSONObject json) {
        if(myRecyclerAdapter1!=null)
     myRecyclerAdapter1.setFilter(json);
    }
}
