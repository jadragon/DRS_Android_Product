package com.test.tw.wrokproduct;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import library.SQLiteDatabaseHandler;

public class LogoutActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView logout_account;
    Button logout_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        initToolbar();
        SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(getApplicationContext());
        String account=db.getMemberDetail().get("account");
        Log.e("account",account);
        db.close();
        logout_account=findViewById(R.id.logout_account);
        logout_account.setText("會員帳號:"+account);
        logout_button=findViewById(R.id.logout_button);
        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(getApplicationContext());
                db.resetLoginTables();
                db.close();
                GlobalVariable gv = (GlobalVariable) getApplicationContext();
                gv.setToken(null);
                Toast.makeText(getApplicationContext(), "登出", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
    private void initToolbar() {
        //Toolbar 建立
        toolbar = findViewById(R.id.include_toolbar);
        ((TextView) findViewById(R.id.include_toolbar_title)).setText("帳號登出");
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
