package adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import Fragment.Fragment_home;
import Fragment.Fragment_shop;

public class ShopViewPagerAdapter  extends FragmentPagerAdapter {


    public ShopViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Fragment_home();
            case 1:
                return new Fragment_shop();
            default:
                return null;
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public int getCount() {
        return 2;
    }
}