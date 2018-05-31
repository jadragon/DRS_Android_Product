package com.test.tw.wrokproduct;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import library.GetJsonData.MemberJsonData;

public class RegisterDetailActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText registerdetail_edit_account, registerdetail_edit_password;
    Button registerdetail_button;
    GlobalVariable gv;
    int type = 1;
    String vcode, account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_detail);
        vcode = getIntent().getStringExtra("vcode");
        account = getIntent().getStringExtra("account");
        type=getIntent().getIntExtra("type",0);
        Log.e("type", type+"");
        initButton();
        initEditText();
        initToolbar();
    }

    private void initButton() {
        registerdetail_button = findViewById(R.id.registerdetail_button);
        registerdetail_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final JSONObject jsonObject = new MemberJsonData().register(type, account,registerdetail_edit_password.getText().toString(),"+886",account,vcode,"",0,"");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    boolean success = jsonObject.getBoolean("Success");
                                    if(success) {
                                        Toast.makeText(getApplicationContext(), "註冊成功", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                    Log.e("success", success + "" + jsonObject.getString("Message"));
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

    private void initEditText() {
        registerdetail_edit_account = findViewById(R.id.registerdetail_edit_account);
        registerdetail_edit_account.setText(account);
        registerdetail_edit_account.setFocusable(false);
        registerdetail_edit_password = findViewById(R.id.registerdetail_edit_password);
    }

    private void initToolbar() {
        //Toolbar 建立
        toolbar = findViewById(R.id.include_toolbar);
        ((TextView) findViewById(R.id.include_toolbar_title)).setText("註冊");
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
