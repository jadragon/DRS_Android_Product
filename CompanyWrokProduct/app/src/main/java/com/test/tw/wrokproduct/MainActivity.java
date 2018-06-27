package com.test.tw.wrokproduct;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import Fragment.*;
import library.AnalyzeJSON.AnalyzeMember;
import library.AnalyzeJSON.GetAddress;
import library.AppManager;
import library.BottomNavigationViewHelper;
import library.GetJsonData.GetInformationByPHP;
import library.GetJsonData.MemberJsonData;
import library.LoadingView;
import library.SQLiteDatabaseHandler;

public class MainActivity extends AppCompatActivity {
    private Fragment_home fragment_home;
    private Fragment_shop fragment_shop;
    private Fragment_community fragment_community;
    private Fragment_favorate fragment_favorate;
    private Fragment_notification fragment_notification;
    private Fragment[] fragments;
    private int lastShowFragment = 0;
    BottomNavigationView navigation;
    SQLiteDatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        AppManager.getAppManager().addActivity(this);
        LoadingView.setContext(getApplicationContext());
        LoadingView.getInstance();
        initDB();
        initFragments();
        initBtnNav();
        startActivity(new Intent(MainActivity.this, LoadingPage.class));

    }

    protected void initBtnNav() {//BottomLayout
        navigation = findViewById(R.id.tab_layout);
        BottomNavigationMenuView bottomNavigationMenuView =
                (BottomNavigationMenuView) navigation.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(3);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;

        View badge = LayoutInflater.from(this)
                .inflate(R.layout.notification_badge, bottomNavigationMenuView, false);

        itemView.addView(badge);
        new BottomNavigationViewHelper().disableShiftMode(navigation);//取消動畫
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {//監聽事件

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        switchFrament(lastShowFragment, 0);
                        lastShowFragment = 0;
                        return true;
                    case R.id.navigation_shop:
                        switchFrament(lastShowFragment, 1);
                        lastShowFragment = 1;
                        return true;
                    case R.id.navigation_my_favor:
                        switchFrament(lastShowFragment, 2);
                        lastShowFragment = 2;
                        return true;
                    case R.id.navigation_notification:
                        switchFrament(lastShowFragment, 3);
                        lastShowFragment = 3;
                        return true;
                    case R.id.navigation_member:
                        switchFrament(lastShowFragment, 4);
                        lastShowFragment = 4;
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
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    /**
     * 切换Fragment
     *
     * @param lastIndex 上个显示Fragment的索引
     * @param index     需要显示的Fragment的索引
     */
    public void switchFrament(int lastIndex, int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                //.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.hide(fragments[lastIndex]);
        if (!fragments[index].isAdded()) {
            transaction.add(R.id.content_layout, fragments[index]);
            transaction.show(fragments[index]).commitAllowingStateLoss();
        } else {
            transaction.show(fragments[index]).commitAllowingStateLoss();
        }
    }

    private void initFragments() {
        fragment_home = new Fragment_home();
        fragment_shop = new Fragment_shop();
        fragment_community = new Fragment_community();
        fragment_favorate = new Fragment_favorate();
        fragment_notification = new Fragment_notification();
        fragments = new Fragment[]{fragment_home, fragment_shop, fragment_favorate, fragment_notification, fragment_community};
        lastShowFragment = 0;
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content_layout, fragment_home)
                .show(fragment_home)
                .commit();
    }

    /**
     * 迴車鍵離開程式
     */
    private static Boolean isExit = false;
    private static Boolean hasTask = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Timer tExit = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                isExit = false;
                hasTask = false;
            }
        };
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isExit == false) {
                isExit = true;
                Toast.makeText(this, "再按一次後退鍵退出應用程式"
                        , Toast.LENGTH_SHORT).show();
                if (!hasTask) {
                    tExit.schedule(task, 2000);
                }
            } else {
                finish();
                System.exit(0);
            }
        }
        return false;
    }

    private void initDB() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                db = new SQLiteDatabaseHandler(getApplicationContext());
                JSONObject json = new GetInformationByPHP().getAddress("0");
                ArrayList<Map<String, String>> datas = GetAddress.getAddress(json);
                String lastestmodifydate = GetAddress.checkModifydate(json);
                int sqlmodifydate = db.getModifydate(lastestmodifydate);
                if (datas.size() > 0 && sqlmodifydate == 0) {
                    db.resetAddressTables();
                    db.addAddressAll(datas, lastestmodifydate);
                }
                GlobalVariable gv = (GlobalVariable) getApplicationContext();
                if (db.getLoginRowCount() > 0) {
                    gv.setToken(db.getMemberDetail().get("token"));
                }
                //
                if (db.getBankRowCount() < 1) {
                    ArrayList<Map<String, String>> banks = AnalyzeMember.getBankData(new MemberJsonData().getBankData());
                    db.addBankAll(banks);
                }
                db.close();
            }
        }).start();
    }
}