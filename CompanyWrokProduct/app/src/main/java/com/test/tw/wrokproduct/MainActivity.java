package com.test.tw.wrokproduct;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Fragment.Fragment_home;
import adapter.MyPagerAdapter;
import adapter.TestAdapter;
import library.BottomNavigationViewHelper;
import library.GetInformationByPHP;
import pojo.HotkeywordsPojo;

public class MainActivity extends AppCompatActivity {
    ScrollView mainScrollView;
    BottomNavigationView navigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        initBtnNav();
        startActivity(new Intent(MainActivity.this, LoadingPage.class));

    }

    protected void initBtnNav() {//BottomLayout
        navigation = (BottomNavigationView) findViewById(R.id.tab_layout);
        new BottomNavigationViewHelper().disableShiftMode(navigation);//取消動畫

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {//監聽事件

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Fragment_home fh = new Fragment_home();
                        ft.replace(R.id.content_layout, fh);
                        ft.commit();
                        return true;
                    case R.id.navigation_shop:

                        return true;
                    case R.id.navigation_my_favor:

                        return true;
                    case R.id.navigation_member:

                        return true;

                }
                return false;
            }

        });
        navigation.setSelectedItemId(R.id.navigation_home);

    }

}