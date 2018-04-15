package Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ScrollView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.test.tw.wrokproduct.R;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import adapter.MyPagerAdapter;
import adapter.MyRecyclerAdapter;
import library.GetInformationByPHP;

/**
 * Created by user on 2017/5/30.
 */

public class Fragment_home extends Fragment {
    DisplayMetrics dm;
    List<ImageView> list;
    RelativeLayout relativeLayout;
    ViewPager viewPager;
    Runnable runnable;
    ScrollView mainScrollView;
    int real_heigh;
    MyPagerAdapter myPagerAdapter;
    SwipeRefreshLayout mSwipeLayout;
    RecyclerView recyclerView, recyclerView2, recyclerView3;
    JSONObject json, json1, json2, json3;
    MyRecyclerAdapter myRecyclerAdapter1, myRecyclerAdapter2, myRecyclerAdapter3;
    ImageLoaderConfiguration config;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home_layout, container, false);
        getID(v);
        init();
        initSwipeLayout();
        initViewPagerAndRecyclerView();
        config = new ImageLoaderConfiguration
                .Builder(getActivity())
                .memoryCacheExtraOptions(480, 800) //保存每個緩存圖片的最大寬高
                .threadPriority(Thread.NORM_PRIORITY - 2) //線池中的緩存數
                .denyCacheImageMultipleSizesInMemory() //禁止緩存多張圖片
                //               .memoryCache(new FIFOLimitedMemoryCache(2 * 1024 * 1024))//缓存策略
//                .memoryCacheSize(50 * 1024 * 1024) //設置內存緩存的大小
                //              .diskCacheFileNameGenerator(new Md5FileNameGenerator()) //缓存文件名的保存方式
//                .diskCacheSize(200 * 1024 * 1024) //緩存大小
                //             .tasksProcessingOrder(QueueProcessingType.LIFO) //工作序列
                .diskCacheFileCount(200) //緩存的文件數量
                .build();
        if (!ImageLoader.getInstance().isInited()) {//偵測如果imagloader已經init，就不再init
            ImageLoader.getInstance().init(config);
        }
        //  ImageLoader.getInstance().displayImage(url, imageView, ImageUsing);

//    UsingFreqLimitedMemoryCache（如果緩存的圖片總量超過限定值，先刪除使用頻率最小的bitmap）
//    LRULimitedMemoryCache（這個也是使用的lru算法，和LruMemoryCache不同的是，他緩存的是bitmap的弱引用）
//    FIFOLimitedMemoryCache（先進先出的緩存策略，當超過設定值，先刪除最先加入緩存的bitmap）
//    LargestLimitedMemoryCache(當超過緩存限定值，先刪除最大的bitmap對象)
//    LimitedAgeMemoryCache（當bitmap加入緩存中的時間超過我們設定的值，將其刪除）

//    Universal-Image-Loader的硬盤緩存策略
//    詳細的硬盤緩存策略可以移步：http://blog.csdn.net/xiaanming/article/details/27525741，下方是總結的結果：

//    FileCountLimitedDiscCache（可以設定緩存圖片的個數，當超過設定值，刪除掉最先加入到硬盤的文件）
//    LimitedAgeDiscCache（設定文件存活的最長時間，當超過這個值，就刪除該文件）
//    TotalSizeLimitedDiscCache（設定緩存bitmap的最大值，當超過這個值，刪除最先加入到硬盤的文件）
//    UnlimitedDiscCache（這個緩存類沒有任何的限制）

//    DisplayImageOptions ImageUsing = new DisplayImageOptions.Builder()
//            .showImageOnLoading(R.drawable.loading)//圖片還沒下載好時跑的臨時圖片
//            .showImageForEmptyUri(R.drawable.loading)
//            .showImageOnFail(R.drawable.loading).cacheInMemory(true)//緩存
//            .cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565)
//            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//            .build();

//.showImageOnLoading(R.drawable.loading)//圖片還沒下載好時跑的臨時圖片
//.showImageOnFail(R.drawable.loading)//圖片讀取失敗時跑的臨時圖片

        return v;
    }

    private void initSwipeLayout() {
        mSwipeLayout.setColorSchemeColors(Color.RED);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        json1 = new GetInformationByPHP().getHotkeywords();
                        json2 = new GetInformationByPHP().getPtype();
                        json3 = new GetInformationByPHP().getBrands();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                myRecyclerAdapter1.setFilter(json1);
                                myRecyclerAdapter2.setFilter(json2);
                                myRecyclerAdapter3.setFilter(json3);
                                mSwipeLayout.setRefreshing(false);// 結束更新動畫
                            }
                        });
                    }
                }).start();

            }

        });
    }

    private void initViewPagerAndRecyclerView() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                /**
                 * //取得Slider圖片
                 * */

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        json = new GetInformationByPHP().getSlider();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //高度等比縮放[   圖片高度/(圖片寬度/手機寬度)    ]
                                // float real_heigh = bitmaps1.get(0).getImage().getHeight() / (bitmaps1.get(0).getImage().getWidth() / (float) dm.widthPixels);
                                relativeLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, real_heigh));
                                myPagerAdapter = new MyPagerAdapter(getActivity(), json);
                                viewPager.setAdapter(myPagerAdapter);
                            }
                        });
                    }
                });

                /**
                 * //取得HotkeyWords圖片
                 * */
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        json1 = new GetInformationByPHP().getHotkeywords();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                //recycleView
                                real_heigh = (int) ((dm.widthPixels - 40 * dm.density) / (float) 3.5);
                                myRecyclerAdapter1 = new MyRecyclerAdapter(getActivity(), json1, real_heigh, real_heigh * 3 / 4, 0);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                                layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setAdapter(myRecyclerAdapter1);
                            }
                        });
                    }
                });

                /**
                 * //取得Ptype圖片
                 * */
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        json2 = new GetInformationByPHP().getPtype();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                real_heigh = (int) ((dm.widthPixels - 10 * dm.density) / (float) 3.5);
                                myRecyclerAdapter2 = new MyRecyclerAdapter(getActivity(), json2, real_heigh, dm.widthPixels / 4, 1);
                                GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
                                layoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
                                recyclerView2.setLayoutManager(layoutManager);
                                recyclerView2.setAdapter(myRecyclerAdapter2);
                            }
                        });
                    }

                });
                /**
                 * //取得Brands圖片
                 * */
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        json3 = new GetInformationByPHP().getBrands();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                real_heigh = (int) ((dm.widthPixels - 25 * dm.density) / (float) 4);
                                myRecyclerAdapter3 = new MyRecyclerAdapter(getActivity(), json3, real_heigh, real_heigh / 4 * 3, 2);
                                GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
                                layoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
                                recyclerView3.setLayoutManager(layoutManager);
                                recyclerView3.setAdapter(myRecyclerAdapter3);

                            }
                        });
                    }

                });

                Looper.loop();
            }
        }).start();

    }

    private void init() {
        list = new ArrayList<>();
        dm = getResources().getDisplayMetrics();
        real_heigh = (int) ((dm.widthPixels - 20 * dm.density) / (float) 3.5 / 4 * 3);
        recyclerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, real_heigh));
        recyclerView.setHasFixedSize(true);
        real_heigh = (dm.widthPixels) / 2;
        recyclerView2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, real_heigh));
        recyclerView2.setHasFixedSize(true);
        real_heigh = (int) ((dm.widthPixels - 40 * dm.density) / (float) 4 / 4 * 3 + 10 * dm.density) * 2;
        recyclerView3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, real_heigh));
        recyclerView3.setHasFixedSize(true);
        runnable = new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        };

    }

    private void getID(View v) {
        mSwipeLayout = v.findViewById(R.id.swipe_container);
        mainScrollView = v.findViewById(R.id.mainScrollView);
        recyclerView = v.findViewById(R.id.reView);
        recyclerView2 = v.findViewById(R.id.reView2);
        recyclerView3 = v.findViewById(R.id.reView3);
        viewPager = v.findViewById(R.id.adView);
        relativeLayout = v.findViewById(R.id.RelateView);
    }

}
