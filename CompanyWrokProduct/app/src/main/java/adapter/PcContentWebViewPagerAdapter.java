package adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;
import Fragment.*;
public class PcContentWebViewPagerAdapter extends FragmentPagerAdapter {

    private String[] mTabtitle;
    private String[] htmls;
    public PcContentWebViewPagerAdapter(FragmentManager fm, String[] mTabtitle, String[] htmls) {
        super(fm);
        this.mTabtitle=mTabtitle;
        this.htmls=htmls;
    }

    @Override
    public Fragment getItem(int position) {
        return  new Fragment_WebView(htmls[position]);
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