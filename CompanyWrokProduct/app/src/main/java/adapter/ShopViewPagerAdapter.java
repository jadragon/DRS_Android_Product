package adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

public class ShopViewPagerAdapter  extends FragmentPagerAdapter {

    private String[] mTabtitle;
    private Fragment[] fragments;
    public ShopViewPagerAdapter(FragmentManager fm, String[] mTabtitle,Fragment[] fragments) {
        super(fm);
        this.mTabtitle=mTabtitle;
        this.fragments=fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return  fragments[position];
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return mTabtitle.length;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return this.mTabtitle[position];
    }
}