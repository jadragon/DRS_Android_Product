package Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.tw.wrokproduct.R;

import adapter.HeaderRecylcerAdapter;
import adapter.ShopViewPagerAdapter;

public class Fragment_shop extends Fragment {
    View v;
    RecyclerView recyclerView;
    HeaderRecylcerAdapter headerRecylcerAdapter;
    ViewPager viewPager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_shop_layout, container, false);
        viewPager=v.findViewById(R.id.shop_viewpager);
        viewPager.setAdapter(new ShopViewPagerAdapter(getFragmentManager()));
        recyclerView=v.findViewById(R.id.shop_header_recycleview);
        recyclerView.setHasFixedSize(true);
        headerRecylcerAdapter=new HeaderRecylcerAdapter(getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(headerRecylcerAdapter);
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("onDestroyView","onDestroyView");
    }
}
