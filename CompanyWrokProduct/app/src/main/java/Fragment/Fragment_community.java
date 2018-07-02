package Fragment;

import android.content.Intent;
import android.graphics.BitmapFactory;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.test.tw.wrokproduct.CommunityActivity;
import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.LoginActivity;
import com.test.tw.wrokproduct.LogoutActivity;
import com.test.tw.wrokproduct.R;
import com.test.tw.wrokproduct.RegisterActivity;
import com.test.tw.wrokproduct.ShopCartActivity;
import com.test.tw.wrokproduct.帳務管理.波克點值and庫瓦點值and雙閃幣.PointActivity;
import com.test.tw.wrokproduct.我的帳戶.個人管理.修改密碼.ModifyPasswordActivity;
import com.test.tw.wrokproduct.我的帳戶.個人管理.個人資料.PersonalInfoActivity;
import com.test.tw.wrokproduct.我的帳戶.訂單管理.收貨地址.ShipAddressActivity;
import com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.OrderInfoActivity;
import com.test.tw.wrokproduct.我的帳戶.諮詢管理.幫助中心.HelpCenterActivity;
import com.test.tw.wrokproduct.我的帳戶.諮詢管理.聯絡劦譽.ContactActivity;

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
    int coverbg;
    Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_community, container, false);
        initToolbar();
        gv = (GlobalVariable) getContext().getApplicationContext();
        login_success = v.findViewById(R.id.login_success);
        viewPager = v.findViewById(R.id.fragment_community_viewpager);

        //List
        List<View> list = new ArrayList<>();
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.table_layout, null);
        initTableItem(inflate);
        list.add(inflate);
        ListView listView = new ListView(getContext());
        listView.setAdapter(new CommunityListViewAdapter(getResources().obtainTypedArray(R.array.store_manage_image), getResources().getStringArray(R.array.store_manage_title)));
        listView.setDivider(null);
        list.add(listView);
        listView = new ListView(getContext());
        listView.setAdapter(new CommunityListViewAdapter(getResources().obtainTypedArray(R.array.community_image), getResources().getStringArray(R.array.community_title)));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final String json = new GetWebView(getContext()).getHtmlByPosition("I0JN9@_fTxybt/YuH1j1Ceg==", position);
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getContext(), CommunityActivity.class);
                                intent.putExtra("html", json);
                                intent.putExtra("title", getResources().getStringArray(R.array.community_title)[position]);
                                startActivity(intent);
                            }
                        });
                    }
                }).start();
            }
        });
        listView.setDivider(null);
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
        ((TextView) tab.getCustomView().findViewById(R.id.viewitem_tabItem_txt)).setText("社群網路");
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
                startActivity(new Intent(getContext(), RegisterActivity.class));
            }
        });
        fragment_community_btn_login = v.findViewById(R.id.fragment_community_btn_login);
        fragment_community_btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });
        fragment_community_btn_logout = v.findViewById(R.id.fragment_community_btn_logout);
        fragment_community_btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), LogoutActivity.class));

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


    private void initToolbar() {
        //Toolbar 建立
        toolbar = v.findViewById(R.id.include_toolbar);
        ((AppCompatActivity) getContext()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getContext()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        setHasOptionsMenu(true);
        ((TextView) v.findViewById(R.id.include_toolbar_title)).setText("會員中心");
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

    public void initTableItem(final View v) {

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gv.getToken() != null) {
                    switch (view.getId()) {
                        case R.id.a1:
                            if (v.findViewById(R.id.a1_0).getVisibility() == View.VISIBLE) {
                                v.findViewById(R.id.a1_0).setVisibility(View.GONE);
                                v.findViewById(R.id.a1_arrow).setRotation(90);
                            } else {
                                closeItem();
                                v.findViewById(R.id.a1_arrow).setRotation(270);
                                v.findViewById(R.id.a1_0).setVisibility(View.VISIBLE);
                            }
                            break;
                        case R.id.a1_1:
                            startActivity(new Intent(getContext(), OrderInfoActivity.class));
                            break;
                        case R.id.a1_2:
                            startActivity(new Intent(getContext(), ShipAddressActivity.class));
                            break;
                        case R.id.a1_3:
                            Toast.makeText(getContext(), "a1_3", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.b1:
                            if (v.findViewById(R.id.b1_0).getVisibility() == View.VISIBLE) {
                                v.findViewById(R.id.b1_0).setVisibility(View.GONE);
                                v.findViewById(R.id.b1_arrow).setRotation(90);
                            } else {
                                closeItem();
                                v.findViewById(R.id.b1_arrow).setRotation(270);
                                v.findViewById(R.id.b1_0).setVisibility(View.VISIBLE);
                            }
                            break;
                        case R.id.b1_1:
                            Toast.makeText(getContext(), "b1_1", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.b1_2:
                            intent = new Intent(getContext(), PointActivity.class);
                            intent.putExtra("point_type", 1);
                            startActivity(intent);
                            break;
                        case R.id.b1_3:
                            intent = new Intent(getContext(), PointActivity.class);
                            intent.putExtra("point_type", 2);
                            startActivity(intent);
                            break;
                        case R.id.b1_4:
                            intent = new Intent(getContext(), PointActivity.class);
                            intent.putExtra("point_type", 3);
                            startActivity(intent);
                            break;
                        case R.id.b1_5:
                            intent = new Intent(getContext(), PointActivity.class);
                            intent.putExtra("point_type", 4);
                            startActivity(intent);
                            break;
                        case R.id.b1_6:
                            Toast.makeText(getContext(), "b1_6", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.b1_7:
                            Toast.makeText(getContext(), "b1_7", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.b1_8:
                            Toast.makeText(getContext(), "b1_8", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.b1_9:
                            Toast.makeText(getContext(), "b1_9", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.c1:
                            if (v.findViewById(R.id.c1_0).getVisibility() == View.VISIBLE) {
                                v.findViewById(R.id.c1_0).setVisibility(View.GONE);
                                v.findViewById(R.id.c1_arrow).setRotation(90);
                            } else {
                                closeItem();
                                v.findViewById(R.id.c1_arrow).setRotation(270);
                                v.findViewById(R.id.c1_0).setVisibility(View.VISIBLE);
                            }
                            break;
                        case R.id.c1_1:
                            startActivity(new Intent(getContext(), PersonalInfoActivity.class));
                            break;
                        case R.id.c1_2:
                            startActivity(new Intent(getContext(), ModifyPasswordActivity.class));
                            break;
                        case R.id.c1_3:
                            startActivity(new Intent(getContext(), LogoutActivity.class));
                            break;
                        case R.id.d1:
                            if (v.findViewById(R.id.d1_0).getVisibility() == View.VISIBLE) {
                                v.findViewById(R.id.d1_0).setVisibility(View.GONE);
                                v.findViewById(R.id.d1_arrow).setRotation(90);
                            } else {
                                closeItem();
                                v.findViewById(R.id.d1_arrow).setRotation(270);
                                v.findViewById(R.id.d1_0).setVisibility(View.VISIBLE);
                            }
                            break;
                        case R.id.d1_1:
                            Toast.makeText(getContext(), "d1_1", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.d1_2:
                            startActivity(new Intent(getContext(), ContactActivity.class));
                            break;
                        case R.id.d1_3:
                            startActivity(new Intent(getContext(), HelpCenterActivity.class));
                            break;
                    }
                } else {
                    startActivity(new Intent(getContext(), LoginActivity.class));
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

    private void closeItem() {
        v.findViewById(R.id.a1_0).setVisibility(View.GONE);
        v.findViewById(R.id.b1_0).setVisibility(View.GONE);
        v.findViewById(R.id.c1_0).setVisibility(View.GONE);
        v.findViewById(R.id.d1_0).setVisibility(View.GONE);
        v.findViewById(R.id.a1_arrow).setRotation(90);
        v.findViewById(R.id.b1_arrow).setRotation(90);
        v.findViewById(R.id.c1_arrow).setRotation(90);
        v.findViewById(R.id.d1_arrow).setRotation(90);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (gv.getToken() != null) {
            login_success.setVisibility(View.VISIBLE);
            SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(getContext());
            Map<String, String> member = db.getMemberDetail();
            byte[] bis = db.getPhotoImage();
            login_photo.setImageBitmap(BitmapFactory.decodeByteArray(bis, 0, bis.length));
            // ImageLoader.getInstance().displayImage(member.get("photo"), login_photo);
            login_name.setText(member.get("name"));
            try {
                coverbg = Integer.parseInt(db.getMemberDetail().get("background"));
            } catch (Exception e) {
                coverbg = R.drawable.member_bg2;
            }
            login_success.setBackgroundResource(coverbg);
            db.close();
        } else {
            login_success.setVisibility(View.INVISIBLE);
        }
    }
}
