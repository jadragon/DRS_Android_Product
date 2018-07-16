package com.test.tw.wrokproduct.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.test.tw.wrokproduct.GlobalVariable;
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
import library.GetJsonData.ProductJsonData;
import library.LoadingView;
import library.MyRecyclerViewTouchCallBack;

public class Fragment_shop_content extends Fragment {
    private RecyclerView recyclerView;
    private ShopRecyclerViewAdapter myRecyclerAdapter;
    private JSONObject json1, json2;
    private View v;
    private DisplayMetrics dm;
    private Banner header;
    private int type;
    private int nextpage = 2;
    private library.Component.MySwipeRefreshLayout mSwipeLayout;
    private EndLessOnScrollListener endLessOnScrollListener;
    private String ptno;
    private GlobalVariable gv;

    public void setPtno(String ptno) {
        this.ptno = ptno;
    }

    public void setType(int type) {
        this.type = type;
    }

    int banner;

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
        v = inflater.inflate(R.layout.include_refresh_recycler, container, false);
        gv = ((GlobalVariable) getContext().getApplicationContext());
        initViewPagerAndRecyclerView();
        initSwipeLayout();
        return v;
    }

    private void initSwipeLayout() {
        mSwipeLayout = v.findViewById(R.id.include_swipe_refresh);
        mSwipeLayout.setColorSchemeColors(Color.RED);
        //設定靈敏度
        mSwipeLayout.setTouchSlop(400);
        //設定刷新動作
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadingView.setMessage("更新資料");
                LoadingView.show(getView());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (banner == ShopRecyclerViewAdapter.SHOW_BANNER) {
                            json1 = new ProductJsonData().getBanner(type);
                            json2 = new ProductJsonData().getIplist(type, gv.getToken(), 1);
                        } else if (banner == ShopRecyclerViewAdapter.HIDE_BANNER) {
                            json2 = new ProductJsonData().getPlist(ptno, type, gv.getToken(), 1);
                        } else if (banner == ShopRecyclerViewAdapter.FAVORATE) {
                            json2 = new ProductJsonData().getFavorite(gv.getToken());
                        } else if (banner == ShopRecyclerViewAdapter.BROWSE) {
                            json2 = new ProductJsonData().getBrowse(gv.getToken());
                        }
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                if (banner == ShopRecyclerViewAdapter.SHOW_BANNER) {
                                    setHeaderFilter(json1);
                                }
                                myRecyclerAdapter.setFilter(json2);
                                mSwipeLayout.setRefreshing(false);
                                LoadingView.hide();
                                if (banner > ShopRecyclerViewAdapter.FAVORATE) {
                                    nextpage = 2;
                                    endLessOnScrollListener.reset();
                                }
                            }
                        });
                    }
                }).start();
            }

        });
    }

    private void initViewPagerAndRecyclerView() {
        recyclerView = v.findViewById(R.id.include_recyclerview);
        //recycleView
        dm = getResources().getDisplayMetrics();

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        myRecyclerAdapter = new ShopRecyclerViewAdapter(getContext(), json2, banner);
        if (banner == ShopRecyclerViewAdapter.SHOW_BANNER) {
            setHeaderView(myRecyclerAdapter);
        }
        recyclerView.setAdapter(myRecyclerAdapter);
        MyRecyclerViewTouchCallBack callBack = new MyRecyclerViewTouchCallBack(myRecyclerAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callBack);
        touchHelper.attachToRecyclerView(recyclerView);

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
                                    json2 = new ProductJsonData().getIplist(type, nextpage);
                                    nextpage++;
                                    getContext().runOnUiThread(new Runnable() {
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
        if (banner > ShopRecyclerViewAdapter.FAVORATE) {
            endLessOnScrollListener = new EndLessOnScrollListener(layoutManager) {
                @Override
                public void onLoadMore(int currentPage) {
                    //     LoadingView.show(v);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (banner == ShopRecyclerViewAdapter.SHOW_BANNER) {
                                json2 = new ProductJsonData().getIplist(type, gv.getToken(), nextpage);
                            } else if (banner == ShopRecyclerViewAdapter.HIDE_BANNER) {
                                json2 = new ProductJsonData().getPlist(ptno, type, gv.getToken(), nextpage);
                            }
                            nextpage++;
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    if (myRecyclerAdapter != null && !myRecyclerAdapter.setFilterMore(json2)) {
                                        nextpage--;
                                    }
                                    //    LoadingView.hide();
                                }
                            });
                        }
                    }).start();
                }
            };
            recyclerView.addOnScrollListener(endLessOnScrollListener);
        }

    }

    private void setHeaderView(ShopRecyclerViewAdapter adapter) {
        header = (Banner) LayoutInflater.from(getContext()).inflate(R.layout.testbanner, recyclerView, false);
        // Banner header=new Banner(getContext());
        header.setLayoutParams(new LinearLayout.LayoutParams(dm.widthPixels, dm.widthPixels * 19 / 54));
        header.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        header.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, final Object path, final ImageView imageView) {
                com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(path.toString(), imageView);
            }
        });
        List<String> images = new ArrayList<>();
        if (json1 != null) {
            for (Map<String, String> map : ResolveJsonData.getJSONData(json1))
                images.add(map.get("image"));
            header.setImages(images);
        }
        //banner设置方法全部调用完毕时最后调用
        //header.start();
        adapter.setmHeaderView(header);
    }


    public void resetRecyclerView(final JSONObject json) {
        json2 = json;
        nextpage = 2;
        if (endLessOnScrollListener != null)
            endLessOnScrollListener.reset();
        if (myRecyclerAdapter != null) {
            myRecyclerAdapter = new ShopRecyclerViewAdapter(getContext(), json, banner);
            recyclerView.setAdapter(myRecyclerAdapter);
        }
    }

    public void setFilter(JSONObject json) {
        json2 = json;
        nextpage = 2;
        if (myRecyclerAdapter != null) {
            myRecyclerAdapter.setFilter(json);
        }
    }

    public void setHeaderFilter(JSONObject json) {
        if (header != null) {
            List<String> images = new ArrayList<>();
            for (Map<String, String> map : ResolveJsonData.getJSONData(json))
                images.add(map.get("image"));
            header.update(images);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (header != null)
            header.releaseBanner();
        recyclerView = null;
        recycleBitamps();
        myRecyclerAdapter = null;
        json1 = null;
        json2 = null;
        v = null;
        dm = null;
        mSwipeLayout = null;
        endLessOnScrollListener = null;
    }

    public void recycleBitamps() {
        if (myRecyclerAdapter != null)
            myRecyclerAdapter.recycleBitmaps();
        recyclerView = null;
        myRecyclerAdapter = null;
        json1 = null;
        json2 = null;
        v = null;
        dm = null;
        if (header != null)
            header.releaseBanner();
        System.gc();
    }

}
