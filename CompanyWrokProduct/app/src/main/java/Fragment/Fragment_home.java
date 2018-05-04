package Fragment;

import android.content.Context;
import android.content.Intent;
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

import com.test.tw.wrokproduct.PtypeActivity;
import com.test.tw.wrokproduct.R;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import adapter.viewpager.MyPagerAdapter;
import adapter.recyclerview.MyRecyclerAdapter;
import library.GetInformationByPHP;
import library.component.MySwipeRefreshLayout;
import library.ResolveJsonData;

/**
 * Created by user on 2017/5/30.
 */

public class Fragment_home extends Fragment {
    DisplayMetrics dm;
    List<ImageView> list;
    RelativeLayout relativeLayout;
    ViewPager viewPager;
    int real_heigh;
    MyPagerAdapter myPagerAdapter;
    MySwipeRefreshLayout mSwipeLayout;
    RecyclerView recyclerView, recyclerView2, recyclerView3;
    JSONObject json, json1, json2, json3;
    MyRecyclerAdapter myRecyclerAdapter1, myRecyclerAdapter2, myRecyclerAdapter3;
    Handler handler;
    View v;
    Banner  header;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_home_layout, container, false);
        //取得ID
        getID(v);
        //起始方法
        init();
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
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        json = new GetInformationByPHP().getSlider();
                        json1 = new GetInformationByPHP().getHotkeywords();
                        json2 = new GetInformationByPHP().getPtype();
                        json3 = new GetInformationByPHP().getBrands();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
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
                handler =new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        json = new GetInformationByPHP().getSlider();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //高度等比縮放[   圖片高度/(圖片寬度/手機寬度)    ]
                                // float real_heigh = bitmaps1.get(0).getImage().getHeight() / (bitmaps1.get(0).getImage().getWidth() / (float) dm.widthPixels);
                                header =v.findViewById(R.id.testbanner);
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
                            }
                        });
                    }
                });

                /**
                 * //取得HotkeyWords圖片
                 * */
                handler =new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        json1 = new GetInformationByPHP().getHotkeywords();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //recycleView
                                real_heigh = (int) ((dm.widthPixels - 40 * dm.density) / (float) 3.5);
                                myRecyclerAdapter1 = new MyRecyclerAdapter(getActivity(), ResolveJsonData.getJSONData(json1), real_heigh, real_heigh * 3 / 4, 0);
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
                handler =new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        json2 = new GetInformationByPHP().getPtype();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                real_heigh = (int) ((dm.widthPixels - 10 * dm.density) / (float) 3.5);
                                myRecyclerAdapter2 = new MyRecyclerAdapter(getActivity(), ResolveJsonData.getJSONData(json2), real_heigh, dm.widthPixels / 4, 1);
                                GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
                                layoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
                                recyclerView2.setLayoutManager(layoutManager);
                                recyclerView2.setAdapter(myRecyclerAdapter2);
                                myRecyclerAdapter2.setClickListener(new MyRecyclerAdapter.ClickListener() {
                                    @Override
                                    public void ItemClicked(View view, int postion, ArrayList<Map<String, String>> list) {
                                        Intent intent=new Intent(getActivity().getApplicationContext(), PtypeActivity.class);
                                        intent.putExtra("position",postion);
                                        startActivity(intent);
                                    }
                                });
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
                                myRecyclerAdapter3 = new MyRecyclerAdapter(getActivity(), ResolveJsonData.getJSONData(json3), real_heigh, real_heigh / 4 * 3, 2);
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.getLooper().quit();
    }


}
