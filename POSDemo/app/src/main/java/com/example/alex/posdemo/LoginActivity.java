package com.example.alex.posdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.alex.posdemo.GlobalVariable.UserInfo;

import org.json.JSONObject;

import Utils.AsyncTaskUtils;
import Utils.IDataCallBack;
import library.AnalyzeJSON.APIpojo.LoginInfoPojo;
import library.AnalyzeJSON.AnalyzeUtil;
import library.AnalyzeJSON.Analyze_UserInfo;
import library.Component.ToastMessageDialog;
import library.JsonApi.RegistAndLoginApi;

public class LoginActivity extends AppCompatActivity {
    Button login_confirm;
    EditText login_account, login_password;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userInfo = (UserInfo) getApplicationContext();
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
                                userInfo.setDu_no(loginInfoPojo.getDu_no());
                                userInfo.setS_no(loginInfoPojo.getS_no());
                                startActivity(intent);
                                finish();
                            } else {
                                new ToastMessageDialog(LoginActivity.this).confirm("資料異常");
                            }
                        } else {
                            new ToastMessageDialog(LoginActivity.this).confirm(AnalyzeUtil.getMessage(jsonObject));
                        }
                    }
                });
            }
        });
    }
}
