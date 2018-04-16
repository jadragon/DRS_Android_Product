package com.test.tw.wrokproduct;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import Fragment.Fragment_home;
import Fragment.Fragment_shop;
import library.BottomNavigationViewHelper;

public class MainActivity extends AppCompatActivity {
    private Fragment_home fragment_home;
    private Fragment_shop fragment_shop;
    private Fragment[] fragments;
    private int lastShowFragment = 0;
    BottomNavigationView  navigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        initFragments();
        initBtnNav();
        startActivity(new Intent(MainActivity.this, LoadingPage.class));

    }

    protected void initBtnNav() {//BottomLayout
       navigation = findViewById(R.id.tab_layout);
        new BottomNavigationViewHelper().disableShiftMode(navigation);//取消動畫

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {//監聽事件

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        switchFrament(lastShowFragment,0);
                        lastShowFragment=0;
                        return true;
                    case R.id.navigation_shop:
                        switchFrament(lastShowFragment,1);
                        lastShowFragment=1;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("onDestroy","onDestroy");
    }
    /**
     * 切换Fragment
     *
     * @param lastIndex 上个显示Fragment的索引
     * @param index     需要显示的Fragment的索引
     */
    public void switchFrament(int lastIndex, int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments[lastIndex]);
        if (!fragments[index].isAdded()) {
            transaction.add(R.id.content_layout, fragments[index]);
        }
        transaction.show(fragments[index]).commitAllowingStateLoss();
    }

    private void initFragments() {
        fragment_home = new Fragment_home();
        fragment_shop = new Fragment_shop();
        fragments = new Fragment[]{fragment_home, fragment_shop};
        lastShowFragment = 0;
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content_layout, fragment_home)
                .show(fragment_home)
                .commit();
    }
}