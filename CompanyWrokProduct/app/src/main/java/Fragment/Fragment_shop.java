package Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.tw.wrokproduct.R;

import adapter.ShopViewPagerAdapter;

public class Fragment_shop extends Fragment {
    View v;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_shop_layout, container, false);
        tabLayout = v.findViewById(R.id.shop_header_tablayout);
        viewPager = v.findViewById(R.id.shop_viewpager);
        viewPager.setAdapter(new ShopViewPagerAdapter(getFragmentManager(),getActivity().getResources().getStringArray(R.array.shop_header_title),new Fragment[]{new Freagment_shop_content(),new Fragment_shop(),new Fragment_shop(),new Fragment_shop()}));
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager, true);

        return v;
    }


}
