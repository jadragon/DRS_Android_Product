package com.test.tw.wrokproduct;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import library.GetJsonData.MemberJsonData;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    EditText login_account, login_password;
    Button login_button;
    GlobalVariable gv ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        gv = (GlobalVariable) getApplicationContext();
        initToolbar();
        initEditText();
        initButton();
    }

    private void initButton() {
        login_button = findViewById(R.id.login_button);
        login_button.setOnClickListener(this);
    }

    private void initEditText() {
        login_account = findViewById(R.id.login_account);
        login_password = findViewById(R.id.login_password);
    }

    private void initToolbar() {
        //Toolbar 建立
        toolbar = findViewById(R.id.include_toolbar);
        ((TextView) findViewById(R.id.include_toolbar_title)).setText("登入");
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_button:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final JSONObject jsonObject= new MemberJsonData().login(0, "886", login_account.getText().toString(), login_password.getText().toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    boolean success=jsonObject.getBoolean("Success");
                                    if(success) {
                                        gv.setToken(jsonObject.getString("Token"));
                                        finish();
                                    }
                                    Log.e("wwww",success+"");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }).start();


                break;
        }
    }
}
