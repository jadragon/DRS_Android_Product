package adapter.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import com.test.tw.wrokproduct.Fragment.Fragment_shop_content;

public class ShopViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<String> mTabtitle;
    private ArrayList<Fragment_shop_content> fragmentArrayList;


    public ShopViewPagerAdapter(FragmentManager fm, ArrayList<String> mTabtitle, ArrayList<Fragment_shop_content> fragmentArrayList) {
        super(fm);
        this.mTabtitle = mTabtitle;
        this.fragmentArrayList = fragmentArrayList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public int getCount() {
        return mTabtitle.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return this.mTabtitle.get(position);
    }
}