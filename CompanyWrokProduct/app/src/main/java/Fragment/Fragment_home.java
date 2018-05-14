package Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.test.tw.wrokproduct.PtypeActivity;
import com.test.tw.wrokproduct.R;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import adapter.recyclerview.MyRecyclerAdapter;
import library.GetInformationByPHP;
import library.ResolveJsonData;
import library.component.MySwipeRefreshLayout;

/**
 * Created by user on 2017/5/30.
 */

public class Fragment_home extends Fragment {
    DisplayMetrics dm;
    List<ImageView> list;
    RelativeLayout relativeLayout;
    ViewPager viewPager;
    int real_heigh;
    MySwipeRefreshLayout mSwipeLayout;
    RecyclerView recyclerView, recyclerView2, recyclerView3;
    JSONObject json, json1, json2, json3;
    MyRecyclerAdapter myRecyclerAdapter1, myRecyclerAdapter2, myRecyclerAdapter3;
    Handler handler;
    View v;
    Banner header;
    int what = 0;
    //宣告特約工人的經紀人

    private Handler mThreadHandler;

    //宣告特約工人

    private HandlerThread mThread;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_home_layout, container, false);
        //取得ID
        getID(v);
        //起始方法
        init();
        handler = new Handler(Looper.getMainLooper());
        mThread = new HandlerThread("name");

        //讓Worker待命，等待其工作 (開啟Thread)

        mThread.start();

        //找到特約工人的經紀人，這樣才能派遣工作 (找到Thread上的Handler)

        mThreadHandler = new Handler(mThread.getLooper());
        initSwipeLayout();
        initViewPagerAndRecyclerView();

        return v;
    }

    private void initSwipeLayout() {
        mSwipeLayout.setColorSchemeColors(Color.RED);
        //設定靈敏度
        mSwipeLayout.setTouchSlop(400);
        //設定刷新動作
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                what = 1;
                mThreadHandler.post(r1);
            }

        });
    }

    private void initViewPagerAndRecyclerView() {
        what = 0;
        mThreadHandler.post(r1);
    }

    private void init() {
        list = new ArrayList<>();
        dm = getActivity().getResources().getDisplayMetrics();
        real_heigh = (int) ((dm.widthPixels - 20 * dm.density) / (float) 3.5 / 4 * 3);
        recyclerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, real_heigh));
        recyclerView.setHasFixedSize(true);
        real_heigh = (dm.widthPixels) / 2;
        recyclerView2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, real_heigh));
        recyclerView2.setHasFixedSize(true);
        real_heigh = (int) ((dm.widthPixels - 40 * dm.density) / (float) 4 / 4 * 3 + 10 * dm.density) * 2;
        recyclerView3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, real_heigh));
        recyclerView3.setHasFixedSize(true);

    }

    private void getID(View v) {
        mSwipeLayout = v.findViewById(R.id.swipe_container);
        recyclerView = v.findViewById(R.id.reView);
        recyclerView2 = v.findViewById(R.id.reView2);
        recyclerView3 = v.findViewById(R.id.reView3);
        viewPager = v.findViewById(R.id.adView);
        relativeLayout = v.findViewById(R.id.RelateView);
    }

    private Runnable r1 = new Runnable() {

        public void run() {
            /**
             * 取得Slider圖片
             * */
            json = new GetInformationByPHP().getSlider();
            /**
             * 取得HotkeyWords圖片
             * */
            json1 = new GetInformationByPHP().getHotkeywords();

            /**
             * 取得Ptype圖片
             * */
            json2 = new GetInformationByPHP().getPtype();

            /**
             * 取得Brands圖片
             * */
            json3 = new GetInformationByPHP().getBrands();
            if (what == 0)
                //初始化面
                handler.post(r2);
            if (what == 1)
                //刷新畫面
                handler.post(r3);
        }
    };

    //工作名稱 r2 的工作內容

    private Runnable r2 = new Runnable() {
        public void run() {
            //取得Slider圖片
            //高度等比縮放[   圖片高度/(圖片寬度/手機寬度)    ]
            // float real_heigh = bitmaps1.get(0).getImage().getHeight() / (bitmaps1.get(0).getImage().getWidth() / (float) dm.widthPixels);
            header = v.findViewById(R.id.testbanner);
            // Banner header=new Banner(getActivity());
            header.setLayoutParams(new LinearLayout.LayoutParams(dm.widthPixels, dm.widthPixels * 19 / 54));
            header.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
            header.setImageLoader(new ImageLoader() {
                @Override
                public void displayImage(Context context, final Object path, final ImageView imageView) {
                    com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(path.toString(), imageView);
                }
            });
            List<String> images = new ArrayList<>();
            for (Map<String, String> map : ResolveJsonData.getJSONData(json))
                images.add(map.get("image"));
            header.setImages(images);
            //banner设置方法全部调用完毕时最后调用
            header.start();
            //取得HotkeyWords圖片
            real_heigh = (int) ((dm.widthPixels - 40 * dm.density) / (float) 3.5);
            myRecyclerAdapter1 = new MyRecyclerAdapter(getActivity(), ResolveJsonData.getJSONData(json1), real_heigh, real_heigh * 3 / 4, 0);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(myRecyclerAdapter1);
            //取得Ptype圖片
            real_heigh = (int) ((dm.widthPixels - 10 * dm.density) / (float) 3.5);
            myRecyclerAdapter2 = new MyRecyclerAdapter(getActivity(), ResolveJsonData.getJSONData(json2), real_heigh, dm.widthPixels / 4, 1);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
            gridLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
            recyclerView2.setLayoutManager(gridLayoutManager);
            recyclerView2.setAdapter(myRecyclerAdapter2);
            myRecyclerAdapter2.setClickListener(new MyRecyclerAdapter.ClickListener() {
                @Override
                public void ItemClicked(View view, int postion, ArrayList<Map<String, String>> list) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), PtypeActivity.class);
                    intent.putExtra("position", postion);
                    startActivity(intent);
                }
            });
            //取得Brands圖片
            real_heigh = (int) ((dm.widthPixels - 25 * dm.density) / (float) 4);
            myRecyclerAdapter3 = new MyRecyclerAdapter(getActivity(), ResolveJsonData.getJSONData(json3), real_heigh, real_heigh / 4 * 3, 2);
            gridLayoutManager = new GridLayoutManager(getActivity(), 2);
            gridLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
            recyclerView3.setLayoutManager(gridLayoutManager);
            recyclerView3.setAdapter(myRecyclerAdapter3);
            //顯示畫面的動作
        }
    };
    private Runnable r3 = new Runnable() {

        public void run() {
            //myPagerAdapter.setFilter(json);
            List<String> images = new ArrayList<>();
            for (Map<String, String> map : ResolveJsonData.getJSONData(json))
                images.add(map.get("image"));
            header.update(images);
            myRecyclerAdapter1.setFilter(ResolveJsonData.getJSONData(json1));
            myRecyclerAdapter2.setFilter(ResolveJsonData.getJSONData(json2));
            myRecyclerAdapter3.setFilter(ResolveJsonData.getJSONData(json3));
            mSwipeLayout.setRefreshing(false);// 結束更新動畫
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //移除工人上的工作
        if (mThreadHandler != null) {
            mThreadHandler.removeCallbacks(r1);
        }

        //解聘工人 (關閉Thread)
        if (mThread != null) {
            mThread.quit();
        }
        recyclerView = null;
        recyclerView2 = null;
        recyclerView3 = null;
        header.releaseBanner();
        myRecyclerAdapter1 = null;
        myRecyclerAdapter2 = null;
        myRecyclerAdapter3 = null;
        handler.getLooper().quit();
    }


}
