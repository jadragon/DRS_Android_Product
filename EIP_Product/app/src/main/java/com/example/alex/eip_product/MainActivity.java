package com.example.alex.eip_product;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.alex.eip_product.fragment.Fragment_home;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private ImageView home, back;
    FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initButton();
        initFragment();
        // initRecylcerView();
    }

    private void initButton() {
        home = findViewById(R.id.home_btn);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//回首頁
                for (int i = 0; i < fragmentManager.getFragments().size() - 1; i++) {
                    fragmentManager.popBackStack();
                }
            }
        });

        back = findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {//回上一頁
            @Override
            public void onClick(View v) {
                fragmentManager.popBackStack();
            }
        });//回上一頁
    }

    private void initFragment() {
        fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {//監聽迴車事件
            @Override
            public void onBackStackChanged() {//監聽fragment返回事件
                if (fragmentManager.findFragmentById(R.id.fragment_content).getTag().equals("home")) {//判斷當前fragment的tag是否為home
                    home.setVisibility(View.INVISIBLE);
                    back.setVisibility(View.INVISIBLE);
                } else {
                    home.setVisibility(View.VISIBLE);
                    back.setVisibility(View.VISIBLE);
                }
            }
        });
        Fragment fragment_home = new Fragment_home();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragment_content, fragment_home, "home").commit();
    }

    /**
     * 切换Fragment
     */
    public void switchFrament(Fragment fragmentto, String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);//跳轉動畫
        Fragment fragmentfrom = fragmentManager.findFragmentByTag(tag);
        if (fragmentfrom == null) {//判斷要跳轉的fragment是否存在
            List<Fragment> fragmentList = fragmentManager.getFragments();
            //隱藏上一個fragment,加入新的fragment並新增於迴車棧中
            transaction.hide(fragmentList.get(fragmentList.size() - 1)).add(R.id.fragment_content, fragmentto, tag).addToBackStack(tag).commit();
        }
    }


    /**
     * 迴車鍵離開程式
     */
    private static Boolean isExit = false;
    private static Boolean hasTask = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK && fragmentManager.getFragments().size() <= 1) {
            Timer tExit = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                    hasTask = false;
                }
            };
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
            return false;
        }
        return true;
    }
}