package com.test.tw.wrokproduct;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ScrollView;
import Fragment.Fragment_home;
import library.BottomNavigationViewHelper;

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
        navigation = findViewById(R.id.tab_layout);
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