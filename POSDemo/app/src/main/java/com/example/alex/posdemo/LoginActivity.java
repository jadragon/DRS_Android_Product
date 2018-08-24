package com.example.alex.posdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.alex.posdemo.GlobalVariable.UserInfo;

import org.json.JSONObject;

import java.util.Map;

import Utils.AsyncTaskUtils;
import Utils.IDataCallBack;
import db.SQLiteDatabaseHandler;
import library.AnalyzeJSON.APIpojo.LoginInfoPojo;
import library.AnalyzeJSON.AnalyzeUtil;
import library.AnalyzeJSON.Analyze_UserInfo;
import library.Component.ToastMessageDialog;
import library.JsonApi.RegistAndLoginApi;

public class LoginActivity extends AppCompatActivity {
    Button login_confirm;
    EditText login_account, login_password;
    private UserInfo userInfo;
    SQLiteDatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = new SQLiteDatabaseHandler(this);
        userInfo = (UserInfo) getApplicationContext();
        if (db.getUserInfo() != null) {
            Map<String, String> map = db.getUserInfo();
            userInfo.setName(map.get("name"));
            userInfo.setEn(map.get("en"));
            userInfo.setDu_no(map.get("du_no"));
            userInfo.setDname(map.get("dname"));
            userInfo.setS_no(map.get("s_no"));
            userInfo.setStore(map.get("store"));
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        login_account = findViewById(R.id.login_account);
        login_password = findViewById(R.id.login_password);
        login_confirm = findViewById(R.id.login_confirm);
        login_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                    @Override
                    public void onTaskBefore() {

                    }

                    @Override
                    public JSONObject onTasking(Void... params) {
                        return new RegistAndLoginApi().login_user(login_account.getText().toString(), login_password.getText().toString());
                    }

                    @Override
                    public void onTaskAfter(JSONObject jsonObject) {
                        if (AnalyzeUtil.checkSuccess(jsonObject)) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            LoginInfoPojo loginInfoPojo = new Analyze_UserInfo().getLoginInfo(jsonObject);
                            if (loginInfoPojo != null) {
                                db.addItem(loginInfoPojo.getName(), loginInfoPojo.getEn(), loginInfoPojo.getDu_no(), loginInfoPojo.getDname(), loginInfoPojo.getS_no(), loginInfoPojo.getStore());
                                userInfo.setName(loginInfoPojo.getName());
                                userInfo.setEn(loginInfoPojo.getEn());
                                userInfo.setDu_no(loginInfoPojo.getDu_no());
                                userInfo.setDname(loginInfoPojo.getDname());
                                userInfo.setS_no(loginInfoPojo.getS_no());
                                userInfo.setStore(loginInfoPojo.getStore());
                                startActivity(intent);
                                finish();
                            } else {
                                new ToastMessageDialog(LoginActivity.this,ToastMessageDialog.TYPE_ERROR).confirm("資料異常");
                            }
                        } else {
                            new ToastMessageDialog(LoginActivity.this,ToastMessageDialog.TYPE_ERROR).confirm(AnalyzeUtil.getMessage(jsonObject));
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
