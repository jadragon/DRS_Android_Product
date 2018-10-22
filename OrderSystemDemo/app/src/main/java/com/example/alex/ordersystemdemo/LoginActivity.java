package com.example.alex.ordersystemdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    View fb, gplus, login;
    private final static int STUDENT = 0;
    private final static int STORE = 1;
    private final static int DELIVERY = 2;
    private int currentType = 0;
    private SwitchCompat login_switch;
    private TextView login_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = findViewById(R.id.login);
        login.setOnClickListener(this);
        fb = findViewById(R.id.login_fb);
        fb.setOnClickListener(this);
        gplus = findViewById(R.id.login_gplus);
        gplus.setOnClickListener(this);


        login_switch = findViewById(R.id.login_switch);
        login_title = findViewById(R.id.login_title);
        login_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    login_title.setText("外送員登入");
                } else {
                    login_title.setText("商家登入");
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.login:
                startActivity(new Intent(LoginActivity.this, StoreListActivity.class));
                break;
            case R.id.login_fb:
                startActivity(new Intent(LoginActivity.this, StoreActivity.class));
                break;
            case R.id.login_gplus:
                startActivity(new Intent(LoginActivity.this, DeliveryActivity.class));
                break;
        }
        finish();

    }
}
