package Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
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

import adapter.recyclerview.ShopRecyclerViewAdapter;
import library.AnalyzeJSON.ResolveJsonData;
import library.EndLessOnScrollListener;
import library.GetJsonData.GetInformationByPHP;
import library.LoadingView;

public class Fragment_shop_content extends Fragment {
    RecyclerView recyclerView;
    ShopRecyclerViewAdapter myRecyclerAdapter;
    ViewPager viewPager;
    JSONObject json1, json2;
    View v;
    DisplayMetrics dm;
    Banner header;
    int type;
    int nextpage = 2;
    library.Component.MySwipeRefreshLayout mSwipeLayout;
    EndLessOnScrollListener endLessOnScrollListener;
    String ptno;

    public void setPtno(String ptno) {
        this.ptno = ptno;
    }

    public void setType(int type) {
        this.type = type;
    }

    int banner;
    public final static int HIDE_BANNER = 0;
    public final static int SHOW_BANNER = 1;

    public Fragment_shop_content() {
    }


    @SuppressLint("ValidFragment")
    public Fragment_shop_content(int banner) {
        this.banner = banner;
    }

    public void setJson(JSONObject json1, JSONObject json2) {
        this.json1 = json1;
        this.json2 = json2;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_shop_content_layout, container, false);
        initViewPagerAndRecyclerView();
        initSwipeLayout();

        return v;
    }

    private void initSwipeLayout() {
        mSwipeLayout = v.findViewById(R.id.swipe_refresh);
        mSwipeLayout.setColorSchemeColors(Color.RED);
        //設定靈敏度
        mSwipeLayout.setTouchSlop(400);
        //設定刷新動作
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadingView.show(v);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (banner == SHOW_BANNER) {
                            json1 = new GetInformationByPHP().getBanner(type);
                            json2 = new GetInformationByPHP().getIplist(type, 1);
                        } else if (banner == HIDE_BANNER) {
                            json2 = new GetInformationByPHP().getPlist(ptno, type, 1);
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (banner == SHOW_BANNER) {
                                    List<String> images = new ArrayList<>();
                                    for (Map<String, String> map : ResolveJsonData.getJSONData(json1))
                                        images.add(map.get("image"));
                                    header.update(images);
                                }
                                myRecyclerAdapter.setFilter(json2);
                                mSwipeLayout.setRefreshing(false);
                                LoadingView.hide();
                                nextpage = 2;
                                endLessOnScrollListener.reset();
                            }
                        });
                    }
                }).start();
            }

        });
    }

    private void initViewPagerAndRecyclerView() {
        recyclerView = v.findViewById(R.id.shop_conttent_re);
        viewPager = v.findViewById(R.id.adView);
        //recycleView
        dm = getResources().getDisplayMetrics();
        int real_heigh = (int) ((dm.widthPixels - 10 * dm.density) / (float) 2);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        myRecyclerAdapter = new ShopRecyclerViewAdapter(getActivity().getApplicationContext(), json2, real_heigh, (int) (real_heigh + (110 * dm.density)), banner, type);
        if(banner==SHOW_BANNER)
            setHeaderView(myRecyclerAdapter);
        recyclerView.setAdapter(myRecyclerAdapter);
        // OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        /*重複一直刷
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == SCROLL_STATE_IDLE) {
                    if (recyclerView.computeVerticalScrollOffset() > 0) {// 有滚动距离，说明可以加载更多，解决了 items 不能充满 RecyclerView   的问题及滑动方向问题
                        boolean isBottom = false;
                        isBottom = recyclerView.computeVerticalScrollExtent()
                                + recyclerView.computeVerticalScrollOffset()
                                == recyclerView.computeVerticalScrollRange();
                        // 也可以使用 方法2
                        // isBottom = !recyclerView.canScrollVertically(1) ;
                        if (isBottom) {
                            LoadingView.show(v);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    json2 = new GetInformationByPHP().getIplist(type, nextpage);
                                    nextpage++;
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (!myRecyclerAdapter.setFilterMore(json2)) {
                                                nextpage--;
                                            }
                                            LoadingView.hide();
                                        }
                                    });
                                }
                            }).start();

                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        */
//只刷一次
        endLessOnScrollListener = new EndLessOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                LoadingView.show(v);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (banner == SHOW_BANNER) {
                            json2 = new GetInformationByPHP().getIplist(type, nextpage);
                        } else if (banner == HIDE_BANNER) {
                            json2 = new GetInformationByPHP().getPlist(ptno, type, nextpage);
                        }
                        nextpage++;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!myRecyclerAdapter.setFilterMore(json2)) {
                                    nextpage--;
                                }
                                LoadingView.hide();
                            }
                        });
                    }
                }).start();
            }
        };
        recyclerView.addOnScrollListener(endLessOnScrollListener);

        setFooterView(myRecyclerAdapter);
        /*
        myRecyclerAdapter.setClickListener(new ShopRecyclerViewAdapter.ClickListener() {
            @Override
            public void ItemClicked(View view, int postion, ArrayList<Map<String, String>> list) {
                Intent intent = new Intent(getActivity().getApplicationContext(), PcContentActivity.class);
                if (banner == HIDE_BANNER) {
                    intent.putExtra("pno", list.get(postion).get("pno"));
                } else {
                    intent.putExtra("pno", list.get(postion - 1).get("pno"));
                }
                startActivity(intent);
            }
        });
        */
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

    }

    private void setHeaderView(ShopRecyclerViewAdapter adapter) {
        header = (Banner) LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.testbanner, recyclerView, false);
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
        for (Map<String, String> map : ResolveJsonData.getJSONData(json1))
            images.add(map.get("image"));
        header.setImages(images);
        //banner设置方法全部调用完毕时最后调用
        header.start();
        adapter.setmHeaderView(header);
    }

    private void setFooterView(ShopRecyclerViewAdapter adapter) {
        View footer = new LinearLayout(getActivity().getApplicationContext());
        footer.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50));
        adapter.setmFooterViewView(footer);
    }

    public void resetRecyclerView(JSONObject json) {
        if (myRecyclerAdapter != null) {
            int real_heigh = (int) ((dm.widthPixels - 10 * dm.density) / (float) 2);
            myRecyclerAdapter = new ShopRecyclerViewAdapter(getActivity().getApplicationContext(), json, real_heigh, (int) (real_heigh + (110 * dm.density)), banner);
            recyclerView.setAdapter(myRecyclerAdapter);
        }
    }

    public void setFilter(JSONObject json) {
        this.json2 = json;
        if (myRecyclerAdapter != null) {
            myRecyclerAdapter.setFilter(json);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recycleBitamps();
    }

    public void recycleBitamps() {
        if (myRecyclerAdapter != null)
            myRecyclerAdapter.recycleBitmaps();
        recyclerView = null;
        myRecyclerAdapter = null;
        viewPager = null;
        json1 = null;
        json2 = null;
        v = null;
        dm = null;
        if (header != null)
            header.releaseBanner();
        System.gc();
    }

}
