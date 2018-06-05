package com.test.tw.wrokproduct.我的帳戶.個人管理.個人資料;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.test.tw.wrokproduct.R;

import library.SQLiteDatabaseHandler;

public class PersonalInfoActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button modify_coverbg;
    SQLiteDatabaseHandler db;
    View personal_info_bg;
    int coverbg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        db = new SQLiteDatabaseHandler(getApplicationContext());
        personal_info_bg = findViewById(R.id.personal_info_bg);
        try {
            coverbg = Integer.parseInt(db.getMemberDetail().get("background"));
        } catch (Exception e) {
            coverbg = 0;
        }

        initToolbar();
        modify_coverbg = findViewById(R.id.modify_coverbg);
        modify_coverbg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PersonalInfoActivity.this, SelectCoverActivity.class));
            }
        });
    }

    private void initToolbar() {
        //Toolbar 建立
        toolbar = findViewById(R.id.include_toolbar);
        ((TextView) findViewById(R.id.include_toolbar_title)).setText("個人資料");
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        try {
            coverbg = Integer.parseInt(db.getMemberDetail().get("background"));
        } catch (Exception e) {
            coverbg = 0;
        }
        personal_info_bg.setBackgroundResource(coverbg);
        Log.e("onRestart","onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
