package Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.R;

import java.util.ArrayList;

import adapter.viewpager.ShopViewPagerAdapter;
import library.GetJsonData.GetInformationByPHP;

public class Fragment_shop extends Fragment {
    View v;
    TabLayout tabLayout;
    ViewPager viewPager;
    ArrayList<Fragment_shop_content> fragmentArrayList;
    Fragment_shop_content fragment_shop_content;
    String token;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_shop_layout, container, false);
        GlobalVariable gv = (GlobalVariable) getContext().getApplicationContext();
        token = gv.getToken();
        tabLayout = v.findViewById(R.id.shop_header_tablayout);
        tabLayout.setSelectedTabIndicatorHeight(6);
        viewPager = v.findViewById(R.id.shop_viewpager);
        new Thread(new Runnable() {
            @Override
            public void run() {
                fragmentArrayList = new ArrayList<>();
                fragment_shop_content = new Fragment_shop_content(Fragment_shop_content.SHOW_BANNER);
                fragment_shop_content.setJson(new GetInformationByPHP().getBanner(0), new GetInformationByPHP().getIplist(0, token, 1));
                fragment_shop_content.setType(0);
                fragmentArrayList.add(fragment_shop_content);
                fragment_shop_content = new Fragment_shop_content(Fragment_shop_content.SHOW_BANNER);
                fragment_shop_content.setJson(new GetInformationByPHP().getBanner(1), new GetInformationByPHP().getIplist(1, token, 1));
                fragment_shop_content.setType(1);
                fragmentArrayList.add(fragment_shop_content);
                fragment_shop_content = new Fragment_shop_content(Fragment_shop_content.SHOW_BANNER);
                fragment_shop_content.setJson(new GetInformationByPHP().getBanner(2), new GetInformationByPHP().getIplist(2, token, 1));
                fragment_shop_content.setType(2);
                fragmentArrayList.add(fragment_shop_content);
                fragment_shop_content = new Fragment_shop_content(Fragment_shop_content.SHOW_BANNER);
                fragment_shop_content.setJson(new GetInformationByPHP().getBanner(3), new GetInformationByPHP().getIplist(3, token, 1));
                fragment_shop_content.setType(3);
                fragmentArrayList.add(fragment_shop_content);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        viewPager.setAdapter(new ShopViewPagerAdapter(getFragmentManager(), getContext().getResources().getStringArray(R.array.shop_header_title), fragmentArrayList));
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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {   // 不在最前端显示 相当于调用了onPause();
            return;
        } else {  // 在最前端显示 相当于调用了onResume();

            //  setFilter();
            //网络数据刷新
        }
    }
}
