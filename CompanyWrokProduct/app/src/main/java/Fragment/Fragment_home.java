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
import android.widget.ScrollView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.test.tw.wrokproduct.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.MyPagerAdapter;
import adapter.TestAdapter;
import library.GetInformationByPHP;

/**
 * Created by user on 2017/5/30.
 */

public class Fragment_home extends Fragment {
    DisplayMetrics dm;
    List<ImageView> list;
    View include;
    TextView dot1, dot2, dot3;
    ViewPager viewPager;
    Runnable runnable;
    ScrollView mainScrollView;
    int real_heigh;
    MyPagerAdapter myPagerAdapter;
    SwipeRefreshLayout mSwipeLayout;
    RecyclerView recyclerView, recyclerView2, recyclerView3;
    JSONObject json, json1, json2, json3;
    TestAdapter testAdapter1, testAdapter2, testAdapter3;
    ImageLoader imageLoader;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home_layout, container, false);
        getID(v);
        init();
        initSwipeLayout();
        getAllImage();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));

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
                                testAdapter1.setFilter(json1);
                                testAdapter2.setFilter(json2);
                                testAdapter3.setFilter(json3);
                                mSwipeLayout.setRefreshing(false);// 結束更新動畫
                            }
                        });
                    }
                }).start();

            }

        });
    }

    private void getAllImage() {

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
                                include.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, real_heigh));
                                myPagerAdapter = new MyPagerAdapter(getActivity(), imageLoader, json, viewPager, dot1, dot2, dot3);
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
                                testAdapter1 = new TestAdapter(getActivity(), json1, imageLoader, real_heigh, real_heigh * 3 / 4, 0);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                                layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setAdapter(testAdapter1);
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
                                testAdapter2 = new TestAdapter(getActivity(), json2, imageLoader, real_heigh, dm.widthPixels / 4, 1);
                                GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
                                layoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
                                recyclerView2.setLayoutManager(layoutManager);
                                recyclerView2.setAdapter(testAdapter2);
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
                                testAdapter3 = new TestAdapter(getActivity(), json3, imageLoader, real_heigh, real_heigh / 4 * 3, 2);
                                GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
                                layoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
                                recyclerView3.setLayoutManager(layoutManager);
                                recyclerView3.setAdapter(testAdapter3);

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
        include = v.findViewById(R.id.RelateView);
        dot1 = v.findViewById(R.id.dot1);
        dot2 = v.findViewById(R.id.dot2);
        dot3 = v.findViewById(R.id.dot3);
    }

}
