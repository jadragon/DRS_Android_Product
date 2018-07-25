package com.test.tw.wrokproduct;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import library.Component.ToastMessageDialog;
import library.GetJsonData.MemberJsonData;

public class RegisterDetailActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText registerdetail_edit_account, registerdetail_edit_password, registerdetail_edit_repassword;
    Button registerdetail_button;
    int type = 1;
    String vcode, account;
    String id, email, name, photo;
    int gender;
    JSONObject jsonObject;
    ToastMessageDialog toastMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_detail);
        toastMessage = new ToastMessageDialog(this);
        type = getIntent().getIntExtra("type", 0);
        if (type == 1 || type == 2) {
            vcode = getIntent().getStringExtra("vcode");
            account = getIntent().getStringExtra("account");
        } else if (type == 3) {
            id = getIntent().getStringExtra("id");
            email = getIntent().getStringExtra("email");
            name = getIntent().getStringExtra("name");
            gender = getIntent().getIntExtra("gender", 0);
            photo = getIntent().getStringExtra("photo");
            account = email.substring(0, email.indexOf("@"));
        } else if (type == 4) {
            id = getIntent().getStringExtra("id");
            email = getIntent().getStringExtra("email");
            name = getIntent().getStringExtra("name");
            gender = getIntent().getIntExtra("gender", 0);
            photo = getIntent().getStringExtra("photo");
            account = email.substring(0, email.indexOf("@"));
        }
        initButton();
        initEditText();
        initToolbar();
    }

    private void initButton() {
        registerdetail_button = findViewById(R.id.registerdetail_button);
        registerdetail_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!registerdetail_edit_account.getText().toString().equals("")) {
                    if ((registerdetail_edit_password.getText().length() > 5 && registerdetail_edit_password.getText().length() < 16)) {
                        if (registerdetail_edit_password.getText().toString().equals(registerdetail_edit_repassword.getText().toString())) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    switch (type) {
                                        case 1:
                                            jsonObject = new MemberJsonData().register(type, registerdetail_edit_account.getText().toString(), registerdetail_edit_password.getText().toString(), "886", account, vcode, "", 0, "");
                                            break;
                                        case 2:
                                            jsonObject = new MemberJsonData().register(type, registerdetail_edit_account.getText().toString(), registerdetail_edit_password.getText().toString(), "886", account, vcode, "", 0, "");
                                            break;
                                        case 3:
                                            jsonObject = new MemberJsonData().register(type, registerdetail_edit_account.getText().toString(), registerdetail_edit_password.getText().toString(), "886", id, vcode, name, gender, photo);
                                            break;
                                        case 4:
                                            jsonObject = new MemberJsonData().register(type, registerdetail_edit_account.getText().toString(), registerdetail_edit_password.getText().toString(), "886", id, vcode, name, gender, photo);
                                            break;
                                    }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                boolean success = jsonObject.getBoolean("Success");
                                                if (success) {
                                                    Toast.makeText(getApplicationContext(), "註冊成功", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(RegisterDetailActivity.this, LoginActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(intent);
                                                } else {
                                                    toastMessage.setMessageText(jsonObject.getString("Message"));
                                                    toastMessage.confirm();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                                }
                            }).start();
                        } else {
                            toastMessage.setMessageText("請確認密碼輸入是否正確");
                            toastMessage.confirm();
                        }


                    } else {
                        toastMessage.setMessageText("密碼長度為6-15字");
                        toastMessage.confirm();
                    }
                } else {
                    toastMessage.setMessageText("帳號不可為空");
                    toastMessage.confirm();
                }
            }

        });

    }

    private void initEditText() {
        registerdetail_edit_account = findViewById(R.id.registerdetail_edit_account);
        registerdetail_edit_account.setText(account);
        if (type > 2) {
            registerdetail_edit_account.setFocusable(true);
        } else {
            registerdetail_edit_account.setFocusable(false);
        }
        registerdetail_edit_password = findViewById(R.id.registerdetail_edit_password);
        registerdetail_edit_password.requestFocus();
        registerdetail_edit_repassword = findViewById(R.id.registerdetail_edit_repassword);
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
