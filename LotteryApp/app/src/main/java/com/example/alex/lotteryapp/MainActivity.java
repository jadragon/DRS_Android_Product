package com.example.alex.lotteryapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button main_lottery, main_setting;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_lottery = this.findViewById(R.id.main_lottery);
        main_setting = this.findViewById(R.id.main_setting);
      //  main_status = this.findViewById(R.id.main_status);

        main_lottery.setOnClickListener(this);
        main_setting.setOnClickListener(this);
     //   main_status.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_lottery:
                startActivity(new Intent(MainActivity.this, AnimationActivity.class));
                break;

            case R.id.main_setting:
                startActivity(new Intent(MainActivity.this, AllListActivity.class));
                break;
/*
            case R.id.main_status:
                break;
                */
            default:
                break;
        }

    }


}
