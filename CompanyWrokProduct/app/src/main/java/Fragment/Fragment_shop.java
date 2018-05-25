package Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.tw.wrokproduct.R;

import org.json.JSONObject;

import adapter.viewpager.ShopViewPagerAdapter;
import library.GetJsonData.GetInformationByPHP;

public class Fragment_shop extends Fragment {
    View v;
    TabLayout tabLayout;
    ViewPager viewPager;
    JSONObject json1, json2, json3, json4;
    Fragment_shop_content fragment_shop_content1, fragment_shop_content2, fragment_shop_content3, fragment_shop_content4;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_shop_layout, container, false);
        tabLayout = v.findViewById(R.id.shop_header_tablayout);
        tabLayout.setSelectedTabIndicatorHeight(6);
        viewPager = v.findViewById(R.id.shop_viewpager);
        new Thread(new Runnable() {
            @Override
            public void run() {
                fragment_shop_content1 = new Fragment_shop_content();
                fragment_shop_content1.setJson(new GetInformationByPHP().getBanner(0), new GetInformationByPHP().getIplist(0, 1));
                fragment_shop_content1.setType(1);
                fragment_shop_content2 = new Fragment_shop_content();
                fragment_shop_content2.setJson(new GetInformationByPHP().getBanner(1), new GetInformationByPHP().getIplist(1, 1));
                fragment_shop_content1.setType(2);
                fragment_shop_content3 = new Fragment_shop_content();
                fragment_shop_content3.setJson(new GetInformationByPHP().getBanner(2), new GetInformationByPHP().getIplist(2, 1));
                fragment_shop_content1.setType(3);
                fragment_shop_content4 = new Fragment_shop_content();
                fragment_shop_content4.setJson(new GetInformationByPHP().getBanner(3), new GetInformationByPHP().getIplist(3, 1));
                fragment_shop_content1.setType(4);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewPager.setAdapter(new ShopViewPagerAdapter(getFragmentManager(), getActivity().getResources().getStringArray(R.array.shop_header_title), new Fragment_shop_content[]{fragment_shop_content1, fragment_shop_content2, fragment_shop_content3, fragment_shop_content4}));
                        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                        tabLayout.setupWithViewPager(viewPager, true);
                    }
                });
            }
        }).start();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("onResume", "onResume");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Log.e("onAttach", "onAttach");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("onStart", "onStart");
    }

    public void setFilter() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                json1 = new GetInformationByPHP().getIplist(0, 1);
                json2 = new GetInformationByPHP().getIplist(1, 1);
                json3 = new GetInformationByPHP().getIplist(2, 1);
                json4 = new GetInformationByPHP().getIplist(3, 1);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fragment_shop_content1.setFilter(json1);
                        fragment_shop_content2.setFilter(json2);
                        fragment_shop_content3.setFilter(json3);
                        fragment_shop_content4.setFilter(json4);
                    }
                });

            }
        }).start();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {   // 不在最前端显示 相当于调用了onPause();
            return;
        } else {  // 在最前端显示 相当于调用了onResume();

            setFilter();
            //网络数据刷新
        }
    }
}
