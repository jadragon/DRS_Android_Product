package tw.com.lccnet.app.designateddriving;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

import tw.com.lccnet.app.designateddriving.Fragment.Fragment_getCoupon;
import tw.com.lccnet.app.designateddriving.Fragment.Fragment_myCoupon;

public class CouponActivity extends ToolbarActivity {
    private ArrayList<Fragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        initToolbar("我的優惠券", true);
        initRecylcerViewAndTabLayout();
    }

    private void initRecylcerViewAndTabLayout() {
        ViewPager viewPager = findViewById(R.id.include_viewpager);
        fragmentList = new ArrayList<>();
        fragmentList.add(new Fragment_myCoupon());
        fragmentList.add(new Fragment_getCoupon());
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            String[] mTabtitle = new String[]{"查看優惠券", "取得優惠券"};

            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTabtitle[position];
            }
        };
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setOffscreenPageLimit(7);
    }


    public void setFilter() {
        ((Fragment_myCoupon) fragmentList.get(0)).setFilter();
        ((Fragment_getCoupon) fragmentList.get(1)).setFilter();
    }
}
