package com.example.alex.ordersystemdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    View fb, gplus, login;
    private final static int STUDENT = 0;
    private final static int STORE = 1;
    private final static int DELIVERY = 2;
    private int currentType = 0;

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

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.login:
                startActivity(new Intent(LoginActivity.this, StudentAcitvity.class));
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
