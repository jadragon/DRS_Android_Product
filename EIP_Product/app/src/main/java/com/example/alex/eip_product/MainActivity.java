package com.example.alex.eip_product;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.alex.eip_product.fragment.Fragment_calendar;
import com.example.alex.eip_product.fragment.Fragment_company;
import com.example.alex.eip_product.fragment.Fragment_home;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button confirm;
    private ImageView home, back;
    private Date current_date;
    private Map<String, Fragment> fragments;
    private Fragment current_fragment;

    public String getCurrent_date() {
        Calendar c = Calendar.getInstance();
        c.setTime(current_date);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        return year + "/" + (month + 1) + "/" + dayOfMonth + "(" + getDayOfWeek(dayOfWeek) + ")";
    }

    public void setCurrent_date(Date current_date) {
        this.current_date = current_date;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initButton();
        initFragment();
        // initRecylcerView();
    }

    private void initButton() {
        confirm = findViewById(R.id.confirm_date);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("date", getCurrent_date());
                fragments.get("company").setArguments(bundle);
                switchFrament(fragments.get("company"), "company");
                confirm.setVisibility(View.INVISIBLE);
            }
        });

        home = findViewById(R.id.home_btn);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFrament(fragments.get("home"), "home");
            }
        });

        back = findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initFragment() {
        initFragmentList();
        switchFrament(fragments.get("home"), "home");
    }

    /**
     * 切换Fragment
     */
    public void switchFrament(Fragment fragmentto, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment fragmentfrom = current_fragment;
        if (fragmentfrom != null) {
            if (fragmentto.isAdded()) {    // 先判斷是否被adde過
                transaction.hide(fragmentfrom).show(fragmentto).commit();
            } else {
                transaction.hide(fragmentfrom).add(R.id.fragment_content, fragmentto, tag).commit();
            }
        } else {
            transaction.add(R.id.fragment_content, fragmentto, tag).commit();
        }

        current_fragment = fragmentto;

        if (tag.equals("calendar")) {//回到月曆時顯示確認按鈕
            confirm.setVisibility(View.VISIBLE);
        }
    }

    private void initFragmentList() {
        fragments = new HashMap<>();
        fragments.put("home", new Fragment_home());
        fragments.put("calendar", new Fragment_calendar());
        fragments.put("company", new Fragment_company());
    }


    public String getDayOfWeek(int dayofweek) {
        String day = "";
        switch (dayofweek) {
            case Calendar.SUNDAY:
                day = "日";
                break;
            case Calendar.MONDAY:
                day = "一";
                break;
            case Calendar.TUESDAY:
                day = "二";
                break;
            case Calendar.WEDNESDAY:
                day = "三";
                break;
            case Calendar.THURSDAY:
                day = "四";
                break;
            case Calendar.FRIDAY:
                day = "五";
                break;
            case Calendar.SATURDAY:
                day = "六";
                break;
        }
        return day;
    }
}