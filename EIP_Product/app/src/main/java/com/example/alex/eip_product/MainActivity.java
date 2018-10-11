package com.example.alex.eip_product;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.alex.eip_product.fragment.Fragment_home;

import java.util.List;

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
        });
    }

    private void initFragment() {
        fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {//監聽迴車事件
            @Override
            public void onBackStackChanged() {
                if (fragmentManager.findFragmentById(R.id.fragment_content).getTag().equals("home")) {
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
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        Fragment fragmentfrom = fragmentManager.findFragmentByTag(tag);
        if (fragmentfrom == null) {//判斷要跳轉的fragment是否存在
            List<Fragment> fragmentList = fragmentManager.getFragments();
            //隱藏上一個fragment並加入新的fragment
            transaction.hide(fragmentList.get(fragmentList.size() - 1)).add(R.id.fragment_content, fragmentto, tag).addToBackStack(tag).commit();
        }
    }

}