package Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.test.tw.wrokproduct.CommunityActivity;
import com.test.tw.wrokproduct.R;

import java.util.ArrayList;
import java.util.List;

import adapter.listview.CommunityListViewAdapter;
import adapter.viewpager.CommunityPagerAdapter;
import library.GetWebView;

public class Fragment_community extends Fragment {
    View v;
    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_community, container, false);
        tabLayout = v.findViewById(R.id.fragment_community_tablayout);
        TabLayout.Tab tab = tabLayout.newTab();//获得每一个tab
        tab.setCustomView(R.layout.tabitem);//给每一个tab设置view
        ImageView imageView = tab.getCustomView().findViewById(R.id.viewitem_tabItem_img);
        imageView.setImageResource(R.drawable.community);
        imageView.setSelected(true);
        ((TextView) tab.getCustomView().findViewById(R.id.viewitem_tabItem_txt)).setText("社群");
        tabLayout.addTab(tab);
        /*
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getCustomView().findViewById(R.id.viewitem_tabItem_img).setSelected(true);

            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getCustomView().findViewById(R.id.viewitem_tabItem_img).setSelected(false);
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

*/
        viewPager = v.findViewById(R.id.fragment_community_viewpager);
        List<View> list = new ArrayList<>();
        ListView listView = new ListView(getActivity());
        listView.setAdapter(new CommunityListViewAdapter(getResources().obtainTypedArray(R.array.community_image), getResources().getStringArray(R.array.community_title)));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final String json = new GetWebView().getHtmlByPosition("I0JN9@_fTxybt/YuH1j1Ceg==",position);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getActivity().getApplicationContext(), CommunityActivity.class);
                                intent.putExtra("html", json);
                                startActivity(intent);

                            }
                        });
                    }
                }).start();
            }
        });
        list.add(listView);
        viewPager.setAdapter(new CommunityPagerAdapter(list));

        return v;
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {   // 不在最前端显示 相当于调用了onPause();
            return;
        } else {  // 在最前端显示 相当于调用了onResume();

            //  setFilter();
            //网络数据刷新
        }
    }


}
