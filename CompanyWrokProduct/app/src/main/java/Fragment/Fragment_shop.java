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

import adapter.ShopViewPagerAdapter;
import library.GetInformationByPHP;

public class Fragment_shop extends Fragment {
    View v;
    TabLayout tabLayout;
    ViewPager viewPager;
    Fragment_shop_content fragment_shop_content1,fragment_shop_content2,fragment_shop_content3,fragment_shop_content4;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_shop_layout, container, false);
        tabLayout = v.findViewById(R.id.shop_header_tablayout);
        tabLayout.setSelectedTabIndicatorHeight(6);
        viewPager = v.findViewById(R.id.shop_viewpager);
        fragment_shop_content1=new Fragment_shop_content();
        new Thread(new Runnable() {
            @Override
            public void run() {
                fragment_shop_content1=new Fragment_shop_content(new GetInformationByPHP().getBanner(0),new GetInformationByPHP().getIplist(0,1));
               // fragment_shop_content1.setJson( new GetInformationByPHP().getBanner(0),new GetInformationByPHP().getIplist(0,1));
                fragment_shop_content2=new Fragment_shop_content(new GetInformationByPHP().getBanner(1),new GetInformationByPHP().getIplist(1,1));
                //fragment_shop_content2.setJson( new GetInformationByPHP().getBanner(1),new GetInformationByPHP().getIplist(1,1));
                fragment_shop_content3=new Fragment_shop_content(new GetInformationByPHP().getBanner(2),new GetInformationByPHP().getIplist(2,1));
              //  fragment_shop_content3.setJson( new GetInformationByPHP().getBanner(2),new GetInformationByPHP().getIplist(2,1));
                fragment_shop_content4=new Fragment_shop_content(new GetInformationByPHP().getBanner(3),new GetInformationByPHP().getIplist(3,1));
               // fragment_shop_content4.setJson( new GetInformationByPHP().getBanner(3),new GetInformationByPHP().getIplist(3,1));
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewPager.setAdapter(new ShopViewPagerAdapter(getFragmentManager(),getActivity().getResources().getStringArray(R.array.shop_header_title),new Fragment[]{fragment_shop_content1,fragment_shop_content2,fragment_shop_content3,fragment_shop_content4}));
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
        Log.e("onResume","onResume");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Log.e("onAttach","onAttach");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("onStart","onStart");
    }
    public void setFilter() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                fragment_shop_content1.setFilter(new GetInformationByPHP().getIplist(0, 1));
                fragment_shop_content2.setFilter(new GetInformationByPHP().getIplist(1, 1));
                fragment_shop_content3.setFilter(new GetInformationByPHP().getIplist(2, 1));
                fragment_shop_content4.setFilter(new GetInformationByPHP().getIplist(3, 1));
            }
        }).start();
    }
}
