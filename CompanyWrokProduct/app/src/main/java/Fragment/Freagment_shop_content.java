package Fragment;

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

import com.test.tw.wrokproduct.R;

import org.json.JSONObject;

import adapter.ShopRecyclerViewAdapter;
import library.GetInformationByPHP;

public class Freagment_shop_content extends Fragment {
    RecyclerView recyclerView;
    ShopRecyclerViewAdapter myRecyclerAdapter1;
    ViewPager viewPager;
    JSONObject json1, json2;
    View v;
    Handler handler;

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
                        /**
                                           * //取得Slider圖片
                                          * */
                        json1 = new GetInformationByPHP().getSlider();
                        /**
                                          * //取得HotkeyWords圖片
                                          * */
                        json2 = new GetInformationByPHP().getIplist(0, 1);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //recycleView
                                DisplayMetrics dm = getResources().getDisplayMetrics();
                                int real_heigh = (int) ((dm.widthPixels - 10 * dm.density) / (float)2);
                                myRecyclerAdapter1 = new ShopRecyclerViewAdapter(getContext(), json1,json2, real_heigh, (int)(real_heigh+(110 * dm.density)), 1);
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
                                setHeaderView(recyclerView);
                            }
                        });
                    }
                });
                Looper.loop();
            }
        }).start();


    }

    private void setHeaderView(RecyclerView view) {
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.viewitem_homeheader, view, false);
        myRecyclerAdapter1.setmHeaderView(header);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.getLooper().quit();
    }
}
