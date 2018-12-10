package com.example.alex.designateddriving_driver;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.alex.designateddriving_driver.Utils.MatchesUtils;
import com.example.alex.designateddriving_driver.db.SQLiteDatabaseHandler;


public class LoginActivity extends ToolbarActivity implements View.OnClickListener {
    private Button login;
    private EditText phone, password;
    private GlobalVariable gv;
    private SQLiteDatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        gv = (GlobalVariable) getApplicationContext();
        db = new SQLiteDatabaseHandler(this);
        /*
        ContentValues cv = db.getMemberDetail();
        if (cv != null) {
            gv.setToken(cv.getAsString(KEY_TOKEN));
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        */
        initToolbar("登入", false);
        initView();
        initListenser();

    }

    private void initView() {
        login = findViewById(R.id.login_btn_login);
        phone = findViewById(R.id.login_edit_phone);
        password = findViewById(R.id.login_edit_pass);
    }

    private void initListenser() {
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn_login:
                if (TextUtils.isEmpty(phone.getText()) || !MatchesUtils.matchPhone(phone.getText().toString())) {
                    phone.setError("請輸入手機號碼");
                } else if (TextUtils.isEmpty(password.getText())) {
                    password.setError("請輸入密碼");
                } else {
                    /*
                    AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                        @Override
                        public JSONObject onTasking(Void... params) {
                            return CustomerApi.login(phone.getText().toString(), password.getText().toString());
                        }

                        @Override
                        public void onTaskAfter(JSONObject jsonObject) {
                            if (jsonObject != null && AnalyzeUtil.checkSuccess(jsonObject)) {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                db.resetLoginTables();
                                ContentValues cv = AnalyzeCustomer.getLogin(jsonObject);
                                db.addMember(cv);
                                gv.setToken(cv.getAsString(KEY_TOKEN));

                            }
                            Log.e("login", jsonObject + "");
                            Toast.makeText(LoginActivity.this, AnalyzeUtil.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                        }
                    });
*/
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }

                break;
        }
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }


}
