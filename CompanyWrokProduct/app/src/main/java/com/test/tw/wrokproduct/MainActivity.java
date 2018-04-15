package com.test.tw.wrokproduct;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ScrollView;

import Fragment.Fragment_home;
import library.BottomNavigationViewHelper;

public class MainActivity extends AppCompatActivity {
    ScrollView mainScrollView;
    BottomNavigationView navigation;
    FragmentManager fm = getSupportFragmentManager();
    FragmentTransaction ft = fm.beginTransaction();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        ft.add(R.id.content_layout, new Fragment_home(), "fh");
        ft.commit();
        initBtnNav();
        startActivity(new Intent(MainActivity.this, LoadingPage.class));

    }

    protected void initBtnNav() {//BottomLayout
        navigation = findViewById(R.id.tab_layout);
        new BottomNavigationViewHelper().disableShiftMode(navigation);//取消動畫

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {//監聽事件

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction ft = fm.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        ft.show(fm.findFragmentByTag("fh"));
                        ft.commit();
                        return true;
                    case R.id.navigation_shop:
                        ft.hide(fm.findFragmentByTag("fh"));
                        ft.commit();
                        return true;
                    case R.id.navigation_my_favor:

                        return true;
                    case R.id.navigation_member:

                        return true;

                }
                return false;
            }

        });
        //  navigation.setSelectedItemId(R.id.navigation_home);

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("onStop","onStop");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("onStart","onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("onPause","onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("onRestart","onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("onResume","onResume");
    }
}