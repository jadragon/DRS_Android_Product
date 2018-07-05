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

import java.util.ArrayList;

import adapter.recyclerview.ShopRecyclerViewAdapter;
import adapter.viewpager.ShopViewPagerAdapter;
import library.GetJsonData.GetInformationByPHP;

public class Fragment_shop extends Fragment {
    View v;
    TabLayout tabLayout;
    ViewPager viewPager;
    ArrayList<Fragment_shop_content> fragmentArrayList;
    Fragment_shop_content fragment_shop_content;
    GlobalVariable gv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_shop_layout, container, false);
        gv = (GlobalVariable) getContext().getApplicationContext();
        initSearchToolbar();
        tabLayout = v.findViewById(R.id.shop_header_tablayout);
        tabLayout.setSelectedTabIndicatorHeight(6);
        viewPager = v.findViewById(R.id.shop_viewpager);
        new Thread(new Runnable() {
            @Override
            public void run() {
                fragmentArrayList = new ArrayList<>();
                fragment_shop_content = new Fragment_shop_content(ShopRecyclerViewAdapter.SHOW_BANNER);
                fragment_shop_content.setJson(new GetInformationByPHP().getBanner(0), new GetInformationByPHP().getIplist(0, gv.getToken(), 1));
                fragment_shop_content.setType(0);
                fragmentArrayList.add(fragment_shop_content);
                fragment_shop_content = new Fragment_shop_content(ShopRecyclerViewAdapter.SHOW_BANNER);
                fragment_shop_content.setJson(new GetInformationByPHP().getBanner(1), new GetInformationByPHP().getIplist(1, gv.getToken(), 1));
                fragment_shop_content.setType(1);
                fragmentArrayList.add(fragment_shop_content);
                fragment_shop_content = new Fragment_shop_content(ShopRecyclerViewAdapter.SHOW_BANNER);
                fragment_shop_content.setJson(new GetInformationByPHP().getBanner(2), new GetInformationByPHP().getIplist(2, gv.getToken(), 1));
                fragment_shop_content.setType(2);
                fragmentArrayList.add(fragment_shop_content);
                fragment_shop_content = new Fragment_shop_content(ShopRecyclerViewAdapter.SHOW_BANNER);
                fragment_shop_content.setJson(new GetInformationByPHP().getBanner(3), new GetInformationByPHP().getIplist(3, gv.getToken(), 1));
                fragment_shop_content.setType(3);
                fragmentArrayList.add(fragment_shop_content);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        viewPager.setAdapter(new ShopViewPagerAdapter(getFragmentManager(), getContext().getResources().getStringArray(R.array.shop_header_title), fragmentArrayList));
                        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                        tabLayout.setupWithViewPager(viewPager, true);
                    }
                });
            }
        }).start();

        return v;
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
        /*
        if (hidden) {   // 不在最前端显示 相当于调用了onPause();
            return;
        } else {  // 在最前端显示 相当于调用了onResume();

            //  setFilter();
            //网络数据刷新
        }
        */
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
