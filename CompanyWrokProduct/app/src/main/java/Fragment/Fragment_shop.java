package Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.R;
import com.test.tw.wrokproduct.SearchBarActivity;
import com.test.tw.wrokproduct.購物車.ShopCartActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import adapter.recyclerview.ShopRecyclerViewAdapter;
import adapter.viewpager.ShopViewPagerAdapter;
import library.GetJsonData.ProductJsonData;
import library.LoadingView;

public class Fragment_shop extends Fragment {
    private View v;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ArrayList<Fragment_shop_content> fragmentArrayList;
    private GlobalVariable gv;
    private String mvip;
    private ArrayList<String> tabtitle;
    private ShopViewPagerAdapter viewPagerAdapter;
    private Fragment_shop_content fragment_shop_content1, fragment_shop_content2, fragment_shop_content3, fragment_shop_content4, fragment_shop_content5;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_shop_layout, container, false);
        gv = (GlobalVariable) getContext().getApplicationContext();
        tabtitle = new ArrayList<>(Arrays.asList(getContext().getResources().getStringArray(R.array.shop_header_title)));
        viewPager = v.findViewById(R.id.shop_viewpager);
        tabLayout = v.findViewById(R.id.shop_header_tablayout);
        mvip = gv.getMvip();
        initSearchToolbar();
        tabLayout.setSelectedTabIndicatorHeight(6);
        viewPager.setOffscreenPageLimit(6);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        fragment_shop_content1 = new Fragment_shop_content(ShopRecyclerViewAdapter.SHOW_BANNER);
        fragment_shop_content1.setType(0);
        fragment_shop_content2 = new Fragment_shop_content(ShopRecyclerViewAdapter.SHOW_BANNER);
        fragment_shop_content2.setType(1);
        fragment_shop_content3 = new Fragment_shop_content(ShopRecyclerViewAdapter.SHOW_BANNER);
        fragment_shop_content3.setType(2);
        fragment_shop_content4 = new Fragment_shop_content(ShopRecyclerViewAdapter.SHOW_BANNER);
        fragment_shop_content4.setType(3);
        fragment_shop_content5 = new Fragment_shop_content(ShopRecyclerViewAdapter.SHOW_BANNER);
        fragment_shop_content5.setType(4);
        resetViewPager(mvip);
        setFilter();
        return v;
    }

    public void resetViewPager(String mvip) {

        if (mvip.equals("2")) {
            fragmentArrayList = new ArrayList<>();
            tabtitle = new ArrayList<>(Arrays.asList(getContext().getResources().getStringArray(R.array.shop_header_title)));
            fragmentArrayList.add(fragment_shop_content1);
            fragmentArrayList.add(fragment_shop_content2);
            fragmentArrayList.add(fragment_shop_content3);
            fragmentArrayList.add(fragment_shop_content4);
            fragmentArrayList.add(fragment_shop_content5);
            viewPagerAdapter = new ShopViewPagerAdapter(getChildFragmentManager(), tabtitle, fragmentArrayList);
            viewPager.setAdapter(viewPagerAdapter);
        } else {
            fragmentArrayList = new ArrayList<>();
            fragmentArrayList.add(fragment_shop_content1);
            fragmentArrayList.add(fragment_shop_content2);
            fragmentArrayList.add(fragment_shop_content3);
            fragmentArrayList.add(fragment_shop_content4);
            if (tabtitle.size() == 5)
                tabtitle.remove(4);
            viewPagerAdapter = new ShopViewPagerAdapter(getChildFragmentManager(), tabtitle, fragmentArrayList);
            viewPager.setAdapter(viewPagerAdapter);
        }
    }

    private void initSearchToolbar() {
        //Toolbar 建立
        Toolbar toolbar = v.findViewById(R.id.include_search_toolbar);
        ((AppCompatActivity) getContext()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getContext()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        setHasOptionsMenu(true);
        v.findViewById(R.id.include_search_toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SearchBarActivity.class);
                intent.addFlags(intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {   // 不在最前端显示 相当于调用了onPause();
            return;
        } else {  // 在最前端显示 相当于调用了onResume();
            if (!gv.getMvip().equals(mvip)) {
                mvip = gv.getMvip();
                resetViewPager(mvip);
                //网络数据刷新
                setFilter();
            }
        }
    }


    public void setFilter() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        LoadingView.setMessage("更新資料");
                        LoadingView.show(getView());
                    }
                });
                final JSONObject json1 = new ProductJsonData().getIplist(0, gv.getToken(), 1);
                final JSONObject json2 = new ProductJsonData().getIplist(1, gv.getToken(), 1);
                final JSONObject json3 = new ProductJsonData().getIplist(2, gv.getToken(), 1);
                final JSONObject json4 = new ProductJsonData().getIplist(3, gv.getToken(), 1);
                final JSONObject jsonheader1 = new ProductJsonData().getBanner(0);
                final JSONObject jsonheader2 = new ProductJsonData().getBanner(1);
                final JSONObject jsonheader3 = new ProductJsonData().getBanner(2);
                final JSONObject jsonheader4 = new ProductJsonData().getBanner(3);
                //========================
                final JSONObject json5;
                final JSONObject jsonheader5;
                if (mvip.equals("2")) {
                    json5 = new ProductJsonData().getIplist(4, gv.getToken(), 1);
                    jsonheader5 = new ProductJsonData().getBanner(4);
                } else {
                    json5 = null;
                    jsonheader5 = null;
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        fragment_shop_content1.setHeaderFilter(jsonheader1);
                        fragment_shop_content2.setHeaderFilter(jsonheader2);
                        fragment_shop_content3.setHeaderFilter(jsonheader3);
                        fragment_shop_content4.setHeaderFilter(jsonheader4);
                        fragment_shop_content1.setFilter(json1);
                        fragment_shop_content2.setFilter(json2);
                        fragment_shop_content3.setFilter(json3);
                        fragment_shop_content4.setFilter(json4);
                        fragment_shop_content5.setFilter(json5);
                        fragment_shop_content5.setHeaderFilter(jsonheader5);
                        //========================

                        LoadingView.hide();
                    }
                });
            }
        }).start();

    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.pc_content_menu, menu);
        menu.getItem(1).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //會員區
        if (item.getItemId() == R.id.pccontent_menu_shopcart) {
            if (gv.getToken() != null)
                startActivity(new Intent(getContext(), ShopCartActivity.class));
            else
                Toast.makeText(getContext(), "請先做登入動作", Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }
}
