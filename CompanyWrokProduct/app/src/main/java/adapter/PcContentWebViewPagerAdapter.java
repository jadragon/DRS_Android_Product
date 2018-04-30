package adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import Fragment.*;
public class PcContentWebViewPagerAdapter extends FragmentPagerAdapter {

    private String[] mTabtitle;
    private String[] htmls;
    private Fragment[] fragments;
    public PcContentWebViewPagerAdapter(FragmentManager fm, String[] mTabtitle, String[] htmls) {
        super(fm);

        this.mTabtitle=mTabtitle;
        this.htmls=htmls;
        fragments=new Fragment[mTabtitle.length];
    }



    @Override
    public Fragment getItem(int position) {
        if(fragments[position]==null)
        fragments[position]=new Fragment_WebView(htmls[position]);
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