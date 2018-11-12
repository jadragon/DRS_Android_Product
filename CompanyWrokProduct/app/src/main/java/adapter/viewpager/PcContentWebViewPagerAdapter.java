package adapter.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.test.tw.wrokproduct.Fragment.Fragment_WebView;

public class PcContentWebViewPagerAdapter extends FragmentPagerAdapter {

    private String[] mTabtitle;
    private String[] htmls;
    private Fragment_WebView[] fragments;
    private int[] heigh;
    private ViewPager viewPager;

    public ViewPager getViewPager() {
        return viewPager;
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    public PcContentWebViewPagerAdapter(FragmentManager fm, String[] mTabtitle, String[] htmls) {
        super(fm);
        this.mTabtitle = mTabtitle;
        this.htmls = htmls;
        fragments = new Fragment_WebView[mTabtitle.length];
        heigh = new int[mTabtitle.length];
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (viewPager != null && heigh[position] != 0)
            viewPager.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heigh[position]));
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public Fragment getItem(final int position) {
        if (fragments[position] == null) {
            fragments[position] = new Fragment_WebView(htmls[position]);
            fragments[position].setOnHeighChangerListener(new Fragment_WebView.OnHeighChangerListener() {
                @Override
                public void valueChanged(int Height) {
                    heigh[position] = Height;
                    if(position==0)
                        viewPager.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heigh[position]));
                }
            });
        }

        return fragments[position];
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