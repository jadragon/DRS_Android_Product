package Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.test.tw.wrokproduct.CommunityActivity;
import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.LoginActivity;
import com.test.tw.wrokproduct.LogoutActivity;
import com.test.tw.wrokproduct.R;
import com.test.tw.wrokproduct.RegisterActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import adapter.listview.CommunityListViewAdapter;
import adapter.viewpager.CommunityPagerAdapter;
import de.hdodenhof.circleimageview.CircleImageView;
import library.GetJsonData.GetWebView;
import library.SQLiteDatabaseHandler;


public class Fragment_community extends Fragment {
    View v;
    TabLayout tabLayout;
    ViewPager viewPager;
    Toolbar toolbar;
    Button fragment_community_btn_login, fragment_community_btn_logout, fragment_community_btn_register;
    GlobalVariable gv;
    View login_success;
    CircleImageView login_photo;
    TextView login_name;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_community, container, false);
        initToolbar(v);
        gv = (GlobalVariable) getActivity().getApplicationContext();
        login_success = v.findViewById(R.id.login_success);


        viewPager = v.findViewById(R.id.fragment_community_viewpager);
        //List
        List<View> list = new ArrayList<>();
        View inflate=LayoutInflater.from(getContext()).inflate(R.layout.table_layout,null);
        initTableItem(inflate);
        list.add(inflate);
        ListView listView = new ListView(getActivity());
        listView.setAdapter(new CommunityListViewAdapter(getResources().obtainTypedArray(R.array.store_manage_image), getResources().getStringArray(R.array.store_manage_title)));
        list.add(listView);
        listView = new ListView(getActivity());
        listView.setAdapter(new CommunityListViewAdapter(getResources().obtainTypedArray(R.array.community_image), getResources().getStringArray(R.array.community_title)));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final String json = new GetWebView(getActivity()).getHtmlByPosition("I0JN9@_fTxybt/YuH1j1Ceg==", position);
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
        //List

        viewPager.setAdapter(new CommunityPagerAdapter(list));
        //TabLayout
        tabLayout = v.findViewById(R.id.fragment_community_tablayout);
        TabLayout.Tab tab = tabLayout.newTab();//获得每一个tab
        tab.setCustomView(R.layout.tabitem);//给每一个tab设置view
        ImageView imageView = tab.getCustomView().findViewById(R.id.viewitem_tabItem_img);
        imageView.setImageResource(R.drawable.my_account);
        ((TextView) tab.getCustomView().findViewById(R.id.viewitem_tabItem_txt)).setText("我的帳戶");
        tabLayout.addTab(tab);
        tab = tabLayout.newTab();//获得每一个tab
        tab.setCustomView(R.layout.tabitem);//给每一个tab设置view
        imageView = tab.getCustomView().findViewById(R.id.viewitem_tabItem_img);
        imageView.setImageResource(R.drawable.store_manage);
        ((TextView) tab.getCustomView().findViewById(R.id.viewitem_tabItem_txt)).setText("商家管理");
        tabLayout.addTab(tab);
        tab = tabLayout.newTab();//获得每一个tab
        tab.setCustomView(R.layout.tabitem);//给每一个tab设置view
        imageView = tab.getCustomView().findViewById(R.id.viewitem_tabItem_img);
        imageView.setImageResource(R.drawable.community);
        ((TextView) tab.getCustomView().findViewById(R.id.viewitem_tabItem_txt)).setText("社群");
        tabLayout.addTab(tab);
        //TabLayout

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        fragment_community_btn_register = v.findViewById(R.id.fragment_community_btn_register);
        fragment_community_btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), RegisterActivity.class));
            }
        });
        fragment_community_btn_login = v.findViewById(R.id.fragment_community_btn_login);
        fragment_community_btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
        fragment_community_btn_logout = v.findViewById(R.id.fragment_community_btn_logout);
        fragment_community_btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), LogoutActivity.class));

            }
        });
        initMember();
        return v;
    }


    private void initMember() {
        login_photo = v.findViewById(R.id.login_photo);
        login_name = v.findViewById(R.id.login_name);
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


    private void initToolbar(View view) {
        //Toolbar 建立
        toolbar = view.findViewById(R.id.include_toolbar);
        ((TextView) view.findViewById(R.id.include_toolbar_title)).setText("會員中心");
    }
    public void initTableItem(final View v) {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.a1:
                        if (v.findViewById(R.id.a1_0).getVisibility() == View.VISIBLE) {
                            v.findViewById(R.id.a1_0).setVisibility(View.GONE);
                        } else {
                            v.findViewById(R.id.a1_0).setVisibility(View.GONE);
                            v.findViewById(R.id.b1_0).setVisibility(View.GONE);
                            v.findViewById(R.id.c1_0).setVisibility(View.GONE);
                            v.findViewById(R.id.d1_0).setVisibility(View.GONE);

                            v.findViewById(R.id.a1_0).setVisibility(View.VISIBLE);
                        }
                        break;
                    case R.id.a1_1:
                        Toast.makeText(getActivity(), "a1_1", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.a1_2:
                        Toast.makeText(getActivity(), "a1_2", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.a1_3:
                        Toast.makeText(getActivity(), "a1_3", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.b1:
                        if (v.findViewById(R.id.b1_0).getVisibility() == View.VISIBLE) {
                            v.findViewById(R.id.b1_0).setVisibility(View.GONE);
                        } else {
                            v.findViewById(R.id.a1_0).setVisibility(View.GONE);
                            v.findViewById(R.id.b1_0).setVisibility(View.GONE);
                            v.findViewById(R.id.c1_0).setVisibility(View.GONE);
                            v.findViewById(R.id.d1_0).setVisibility(View.GONE);

                            v.findViewById(R.id.b1_0).setVisibility(View.VISIBLE);
                        }
                        break;
                    case R.id.b1_1:
                        Toast.makeText(getActivity(), "b1_1", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.b1_2:
                        Toast.makeText(getActivity(), "b1_2", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.b1_3:
                        Toast.makeText(getActivity(), "b1_3", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.b1_4:
                        Toast.makeText(getActivity(), "b1_4", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.b1_5:
                        Toast.makeText(getActivity(), "b1_5", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.b1_6:
                        Toast.makeText(getActivity(), "b1_6", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.b1_7:
                        Toast.makeText(getActivity(), "b1_7", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.b1_8:
                        Toast.makeText(getActivity(), "b1_8", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.b1_9:
                        Toast.makeText(getActivity(), "b1_9", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.c1:
                        if (v.findViewById(R.id.c1_0).getVisibility() == View.VISIBLE) {
                            v.findViewById(R.id.c1_0).setVisibility(View.GONE);
                        } else {
                            v.findViewById(R.id.a1_0).setVisibility(View.GONE);
                            v.findViewById(R.id.b1_0).setVisibility(View.GONE);
                            v.findViewById(R.id.c1_0).setVisibility(View.GONE);
                            v.findViewById(R.id.d1_0).setVisibility(View.GONE);

                            v.findViewById(R.id.c1_0).setVisibility(View.VISIBLE);
                        }
                        break;
                    case R.id.c1_1:
                        Toast.makeText(getActivity(), "c1_1", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.c1_2:
                        Toast.makeText(getActivity(), "c1_2", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.c1_3:
                        Toast.makeText(getActivity(), "c1_3", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.d1:
                        if (v.findViewById(R.id.d1_0).getVisibility() == View.VISIBLE) {
                            v.findViewById(R.id.d1_0).setVisibility(View.GONE);
                        } else {
                            v.findViewById(R.id.a1_0).setVisibility(View.GONE);
                            v.findViewById(R.id.b1_0).setVisibility(View.GONE);
                            v.findViewById(R.id.c1_0).setVisibility(View.GONE);
                            v.findViewById(R.id.d1_0).setVisibility(View.GONE);

                            v.findViewById(R.id.d1_0).setVisibility(View.VISIBLE);
                        }
                        break;
                    case R.id.d1_1:
                        Toast.makeText(getActivity(), "d1_1", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.d1_2:
                        Toast.makeText(getActivity(), "d1_2", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.d1_3:
                        Toast.makeText(getActivity(), "d1_3", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        v.findViewById(R.id.a1).setOnClickListener(clickListener);
        v.findViewById(R.id.a1_1).setOnClickListener(clickListener);
        v.findViewById(R.id.a1_2).setOnClickListener(clickListener);
        v.findViewById(R.id.a1_3).setOnClickListener(clickListener);
        v.findViewById(R.id.b1).setOnClickListener(clickListener);
        v.findViewById(R.id.b1_1).setOnClickListener(clickListener);
        v.findViewById(R.id.b1_2).setOnClickListener(clickListener);
        v.findViewById(R.id.b1_3).setOnClickListener(clickListener);
        v.findViewById(R.id.b1_4).setOnClickListener(clickListener);
        v.findViewById(R.id.b1_5).setOnClickListener(clickListener);
        v.findViewById(R.id.b1_6).setOnClickListener(clickListener);
        v.findViewById(R.id.b1_7).setOnClickListener(clickListener);
        v.findViewById(R.id.b1_8).setOnClickListener(clickListener);
        v.findViewById(R.id.b1_9).setOnClickListener(clickListener);
        v.findViewById(R.id.c1).setOnClickListener(clickListener);
        v.findViewById(R.id.c1_1).setOnClickListener(clickListener);
        v.findViewById(R.id.c1_2).setOnClickListener(clickListener);
        v.findViewById(R.id.c1_3).setOnClickListener(clickListener);
        v.findViewById(R.id.d1).setOnClickListener(clickListener);
        v.findViewById(R.id.d1_1).setOnClickListener(clickListener);
        v.findViewById(R.id.d1_2).setOnClickListener(clickListener);
        v.findViewById(R.id.d1_3).setOnClickListener(clickListener);
    }
    @Override
    public void onResume() {
        super.onResume();

        if (gv.getToken() != null) {
            login_success.setVisibility(View.VISIBLE);
            SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(getContext());
            Map<String, String> member = db.getMemberDetail();
            ImageLoader.getInstance().displayImage(member.get("photo"), login_photo);
            login_name.setText(member.get("name"));
            db.close();
            Log.e("member", "" + member);

        } else {
            login_success.setVisibility(View.INVISIBLE);
        }
    }
}
