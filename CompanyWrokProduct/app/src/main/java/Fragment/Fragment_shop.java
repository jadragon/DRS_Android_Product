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
import com.test.tw.wrokproduct.ShopCartActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import adapter.recyclerview.ShopRecyclerViewAdapter;
import adapter.viewpager.ShopViewPagerAdapter;
import library.GetJsonData.GetInformationByPHP;
import library.LoadingView;

public class Fragment_shop extends Fragment {
    private View v;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ArrayList<Fragment_shop_content> fragmentArrayList;
    private GlobalVariable gv;
    private String mvip;
    ArrayList<String> tabtitle;
    ShopViewPagerAdapter viewPagerAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_shop_layout, container, false);
        gv = (GlobalVariable) getContext().getApplicationContext();
        tabtitle = new ArrayList<>(Arrays.asList(getContext().getResources().getStringArray(R.array.shop_header_title)));

        initSearchToolbar();
        tabLayout = v.findViewById(R.id.shop_header_tablayout);
        tabLayout.setSelectedTabIndicatorHeight(6);
        viewPager = v.findViewById(R.id.shop_viewpager);
        fragmentArrayList = new ArrayList<>();
        Fragment_shop_content fragment_shop_content = new Fragment_shop_content(ShopRecyclerViewAdapter.SHOW_BANNER);
        fragment_shop_content.setType(0);
        fragmentArrayList.add(fragment_shop_content);
        fragment_shop_content = new Fragment_shop_content(ShopRecyclerViewAdapter.SHOW_BANNER);
        fragment_shop_content.setType(1);
        fragmentArrayList.add(fragment_shop_content);
        fragment_shop_content = new Fragment_shop_content(ShopRecyclerViewAdapter.SHOW_BANNER);
        fragment_shop_content.setType(2);
        fragmentArrayList.add(fragment_shop_content);
        fragment_shop_content = new Fragment_shop_content(ShopRecyclerViewAdapter.SHOW_BANNER);
        fragment_shop_content.setType(3);
        fragmentArrayList.add(fragment_shop_content);
        fragment_shop_content = new Fragment_shop_content(ShopRecyclerViewAdapter.SHOW_BANNER);
        fragment_shop_content.setType(4);
        fragmentArrayList.add(fragment_shop_content);
        viewPagerAdapter=new ShopViewPagerAdapter(getFragmentManager(), tabtitle, fragmentArrayList);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(6);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        setFilter();
        mvip = gv.getMvip();
        return v;
    }

    public void resetViewPager(String mvip) {
        if (mvip.equals("2")) {
            tabtitle.remove(4);
            viewPagerAdapter=new ShopViewPagerAdapter(getFragmentManager(), tabtitle, fragmentArrayList);
            viewPager.setAdapter(viewPagerAdapter);
        } else {
            tabtitle = new ArrayList<>(Arrays.asList(getContext().getResources().getStringArray(R.array.shop_header_title)));
            viewPagerAdapter=new ShopViewPagerAdapter(getFragmentManager(), tabtitle, fragmentArrayList);
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
                LoadingView.show(getView());
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
                final JSONObject json1 = new GetInformationByPHP().getIplist(0, gv.getToken(), 1);
                final JSONObject json2 = new GetInformationByPHP().getIplist(1, gv.getToken(), 1);
                final JSONObject json3 = new GetInformationByPHP().getIplist(2, gv.getToken(), 1);
                final JSONObject json4 = new GetInformationByPHP().getIplist(3, gv.getToken(), 1);
                final JSONObject jsonheader1 = new GetInformationByPHP().getBanner(0);
                final JSONObject jsonheader2 = new GetInformationByPHP().getBanner(1);
                final JSONObject jsonheader3 = new GetInformationByPHP().getBanner(2);
                final JSONObject jsonheader4 = new GetInformationByPHP().getBanner(3);
                //========================
                final JSONObject json5 = new GetInformationByPHP().getIplist(4, gv.getToken(), 1);
                final JSONObject jsonheader5 = new GetInformationByPHP().getBanner(4);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        fragmentArrayList.get(0).setHeaderFilter(jsonheader1);
                        fragmentArrayList.get(1).setHeaderFilter(jsonheader2);
                        fragmentArrayList.get(2).setHeaderFilter(jsonheader3);
                        fragmentArrayList.get(3).setHeaderFilter(jsonheader4);

                        fragmentArrayList.get(0).setFilter(json1);
                        fragmentArrayList.get(1).setFilter(json2);
                        fragmentArrayList.get(2).setFilter(json3);
                        fragmentArrayList.get(3).setFilter(json4);
                        //========================
                        fragmentArrayList.get(4).setHeaderFilter(jsonheader5);
                        fragmentArrayList.get(4).setFilter(json5);

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
