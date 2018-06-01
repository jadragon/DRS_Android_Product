package com.test.tw.wrokproduct;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import library.GetJsonData.MemberJsonData;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    EditText register_edit_account, register_edit_password;
    Button register_button, register_btn_gvcode;
    GlobalVariable gv;
    ImageView register_img_mobile, register_img_email, register_img_fb, register_img_google;
    int type = 1;
    String vcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initButton();
        initImage();
        initEditText();
        initToolbar();
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

    private void initButton() {
        register_button = findViewById(R.id.register_button);
        register_button.setOnClickListener(this);
        register_btn_gvcode = findViewById(R.id.register_btn_gvcode);
        register_btn_gvcode.setOnClickListener(this);

    }

    private void initEditText() {
        register_edit_account = findViewById(R.id.register_edit_account);
        register_edit_password = findViewById(R.id.register_edit_password);
    }

    private void initImage() {
        register_img_mobile = findViewById(R.id.register_img_mobile);
        register_img_mobile.setOnClickListener(this);
        register_img_email = findViewById(R.id.register_img_email);
        register_img_email.setOnClickListener(this);
        register_img_fb = findViewById(R.id.register_img_fb);
        register_img_fb.setOnClickListener(this);
        register_img_google = findViewById(R.id.register_img_google);
        register_img_google.setOnClickListener(this);
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.register_button:
                if (vcode != null) {
                    if(vcode.toString().equals(register_edit_password.getText().toString())) {
                        Intent intent = new Intent(RegisterActivity.this, RegisterDetailActivity.class);
                        intent.putExtra("type", type);
                        intent.putExtra("vcode", vcode);
                        intent.putExtra("account", register_edit_account.getText().toString());
                        startActivity(intent);
                    }else {
                        Toast.makeText(getApplicationContext(), "請確認您輸入的驗證碼是否正確", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "請先取得驗證碼後再進行下一步", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.register_btn_gvcode:
                if(!register_edit_account.getText().toString().equals("")) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final JSONObject jsonObject = new MemberJsonData().gvcode(type, "+886", register_edit_account.getText().toString());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        boolean success = jsonObject.getBoolean("Success");
                                        if (success) {
                                            vcode = jsonObject.getString("Data");
                                            register_edit_account.setFocusable(false);
                                        }
                                        Toast.makeText(getApplicationContext(),  jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
                                        Log.e("success", success + "" + jsonObject.getString("Message"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }).start();
                }else {
                    Toast.makeText(getApplicationContext(), "請先輸入手機號碼或信箱", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.register_img_mobile:
                register_edit_account.setHint("請輸入手機號碼");
                register_edit_account.setInputType(InputType.TYPE_CLASS_PHONE);
                register_edit_account.setFocusableInTouchMode(true);
                register_edit_account.setFocusable(true);
                register_edit_account.requestFocus();
                register_edit_account.setText(null);
                register_edit_password.setText(null);
                vcode=null;
                type = 1;
                break;
            case R.id.register_img_email:
                register_edit_account.setHint("請輸入信箱帳號");
                register_edit_account.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                register_edit_account.setFocusableInTouchMode(true);
                register_edit_account.setFocusable(true);
                register_edit_account.requestFocus();
                register_edit_account.setText(null);
                register_edit_password.setText(null);
                vcode=null;
                type = 2;
                break;
            case R.id.register_img_fb:
                type = 3;
                break;
            case R.id.register_img_google:
                type = 4;
                break;
        }
    }
}
