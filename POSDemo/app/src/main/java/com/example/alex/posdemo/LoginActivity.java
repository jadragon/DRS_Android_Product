package com.example.alex.posdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import library.Component.ToastMessageDialog;
import library.JsonApi.RegistAndLoginApi;

public class LoginActivity extends AppCompatActivity {
    Button login_confirm;
    EditText login_account, login_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login_account = findViewById(R.id.login_account);
        login_password = findViewById(R.id.login_password);
        login_confirm = findViewById(R.id.login_confirm);
        login_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final JSONObject jsonObject = new RegistAndLoginApi().login_user(login_account.getText().toString(), login_password.getText().toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (jsonObject.getBoolean("Success")) {
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        new ToastMessageDialog(LoginActivity.this).confirm(jsonObject.getString("Message"));
                                    }
                                    Log.e("login", jsonObject + "");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }
                }).start();

            }
        });
    }
}
