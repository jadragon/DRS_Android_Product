package com.test.tw.wrokproduct;

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

import java.util.Map;

import library.AnalyzeJSON.AnalyzeMember;
import library.GetJsonData.MemberJsonData;
import library.SQLiteDatabaseHandler;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    EditText login_edit_account, login_edit_password;
    Button login_button;
    GlobalVariable gv;
    ImageView login_img_account, login_img_mobile, login_img_email, login_img_fb, login_img_google;
int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        gv = (GlobalVariable) getApplicationContext();
        initToolbar();
        initEditText();
        initButton();
        initImage();
    }

    private void initButton() {
        login_button = findViewById(R.id.login_button);
        login_button.setOnClickListener(this);
    }

    private void initImage() {
        login_img_account = findViewById(R.id.login_img_account);
        login_img_account.setOnClickListener(this);
        login_img_mobile = findViewById(R.id.login_img_mobile);
        login_img_mobile.setOnClickListener(this);
        login_img_email = findViewById(R.id.login_img_email);
        login_img_email.setOnClickListener(this);
        login_img_fb = findViewById(R.id.login_img_fb);
        login_img_fb.setOnClickListener(this);
        login_img_google = findViewById(R.id.login_img_google);
        login_img_google.setOnClickListener(this);
    }

    private void initEditText() {
        login_edit_account = findViewById(R.id.login_edit_account);
        login_edit_password = findViewById(R.id.login_edit_password);
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
                if(!login_edit_account.getText().toString().equals("")&&!login_edit_password.getText().toString().equals("") ){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final JSONObject jsonObject = new MemberJsonData().login(type, "+886", login_edit_account.getText().toString(), login_edit_password.getText().toString());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        boolean success = jsonObject.getBoolean("Success");
                                        if (success) {
                                            gv.setToken(jsonObject.getString("Token"));
                                            initMemberDB(jsonObject);
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
                }else {
                    Toast.makeText(getApplicationContext(), "請先輸入帳號密碼", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.login_img_account:
                login_edit_account.setHint("請輸入您的帳號");
                login_edit_account.setInputType(InputType.TYPE_CLASS_TEXT);
                type=0;
                break;
            case R.id.login_img_mobile:
                login_edit_account.setHint("請輸入您的電話");
                login_edit_account.setInputType(InputType.TYPE_CLASS_PHONE);
                type=1;
                break;
            case R.id.login_img_email:
                login_edit_account.setHint("請輸入您的信箱");
                login_edit_account.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                type=2;
                break;
            case R.id.login_img_fb:
                type=3;
                break;
            case R.id.login_img_google:
                type=4;
                break;
        }

    }

    private void initMemberDB(final JSONObject json) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(getApplicationContext());
                Map<String, String> datas = AnalyzeMember.getLogin(json);
                if (datas != null) {
                    db.resetLoginTables();
                    db.addMember(datas.get("Token"), datas.get("Name"), datas.get("Picture"));
                    db.close();
                }
            }
        }).start();

    }


}
