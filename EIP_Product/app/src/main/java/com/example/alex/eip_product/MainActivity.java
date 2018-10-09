package com.example.alex.eip_product;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.alex.eip_product.fragment.Fragment_calendar;
import com.example.alex.eip_product.fragment.Fragment_company;

public class MainActivity extends AppCompatActivity {

    private Button confirm;
    private ImageView home, back;

    private String pre_fragment_name = "";
    private String current_date = "";

    public String getCurrent_date() {
        return current_date;
    }

    public void setCurrent_date(String current_date) {
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
                Fragment_company fragment_company = new Fragment_company();
                Bundle bundle = new Bundle();
                bundle.putString("date", current_date);
                fragment_company.setArguments(bundle);
                switchFrament(fragment_company, "company");
            }
        });

        home = findViewById(R.id.home_btn);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pre_fragment_name = "company";
                switchFrament(new Fragment_calendar(), "calendar");
            }
        });

        back = findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFrament(new Fragment_calendar(), pre_fragment_name);
            }
        });
    }

    private void initFragment() {
        pre_fragment_name = "calendar";
        switchFrament(new Fragment_calendar(), pre_fragment_name);
    }

    /**
     * 切换Fragment
     */
    public void switchFrament(Fragment fragment, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment fragmentfrom = getSupportFragmentManager().findFragmentByTag(tag);

        if (fragmentfrom != null) {    // 先判断是否被add过
            transaction.show(fragmentfrom).commit();
        } else {
            transaction.replace(R.id.fragment_content, fragment, tag).commitAllowingStateLoss();
        }
    }





 /*
    private void initRecylcerView() {

        RecyclerView main_recylcerview;
        CompanyListAdapter mainMenuAdapter;
        main_recylcerview = findViewById(R.id.main_recylcerview);
        mainMenuAdapter=new CompanyListAdapter(this);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,3);
        int imagewidth=250;
        gridLayoutManager.setSpanCount(dm.widthPixels/imagewidth);
        main_recylcerview.setLayoutManager(gridLayoutManager);
        main_recylcerview.setAdapter(mainMenuAdapter);
    }
*/
}