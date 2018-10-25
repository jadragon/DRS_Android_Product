package com.example.alex.ordersystemdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.ordersystemdemo.API.Analyze.AnalyzeUtil;
import com.example.alex.ordersystemdemo.API.List.DelivertApi;
import com.example.alex.ordersystemdemo.API.List.StoreApi;
import com.example.alex.ordersystemdemo.API.List.StudentApi;
import com.example.alex.ordersystemdemo.QuickLoginUtil.GoogleQL;
import com.example.alex.ordersystemdemo.library.AsyncTaskUtils;
import com.example.alex.ordersystemdemo.library.IDataCallBack;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    View fb, gplus, login;
    GoogleQL googleQL;
    private final static int STORE = 0;
    private final static int DELIVERY = 1;
    private final static int STUDENT = 2;
    private int currentType = 0;
    private SwitchCompat login_switch;
    private TextView login_title;
    private String[] members = {"商家登入", "外送員登入"};
    private EditText account, password;
    private GlobalVariable gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        gv = (GlobalVariable) getApplicationContext();
        googleQL = new GoogleQL(this);
        initTextView();
        initClickView();
        initSwitch();


    }

    private void initTextView() {
        login_title = findViewById(R.id.login_title);
        account = findViewById(R.id.account);
        password = findViewById(R.id.password);
    }

    private void initSwitch() {
        login_switch = findViewById(R.id.login_switch);
        login_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    currentType = DELIVERY;
                    login_title.setText(members[currentType]);
                } else {
                    currentType = STORE;
                    login_title.setText(members[currentType]);
                }
            }
        });
    }

    private void initClickView() {
        login = findViewById(R.id.login);
        login.setOnClickListener(this);
        fb = findViewById(R.id.login_fb);
        fb.setOnClickListener(this);
        gplus = findViewById(R.id.login_gplus);
        gplus.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.login:
                if (currentType == STORE) {
                    AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                        @Override
                        public JSONObject onTasking(Void... params) {
                            return new StoreApi().store_login(account.getText().toString(), password.getText().toString());
                        }

                        @Override
                        public void onTaskAfter(JSONObject jsonObject) {
                            if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                gv.setToken(AnalyzeUtil.getToken(jsonObject, STORE));
                                startActivity(new Intent(LoginActivity.this, StoreActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, AnalyzeUtil.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else if (currentType == DELIVERY) {
                    AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                        @Override
                        public JSONObject onTasking(Void... params) {
                            return new DelivertApi().deliver_login(account.getText().toString(), password.getText().toString());
                        }

                        @Override
                        public void onTaskAfter(JSONObject jsonObject) {
                            if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                gv.setToken(AnalyzeUtil.getToken(jsonObject, DELIVERY));
                                startActivity(new Intent(LoginActivity.this, DeliveryActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, AnalyzeUtil.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                            }
                            Log.e("STORE", jsonObject + "");
                        }
                    });
                }
                break;
            case R.id.login_fb:
                break;
            case R.id.login_gplus:
                googleQL.signIn();
                break;
        }


    }


    @Override
    protected void onStart() {
        super.onStart();
        googleQL.onStart();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final GoogleSignInAccount signInAccount = googleQL.onActivityResult(requestCode, resultCode, data);
        if (signInAccount != null) {
            AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                @Override
                public JSONObject onTasking(Void... params) {
                    return new StudentApi().student_login(signInAccount.getId(), signInAccount.getDisplayName(), signInAccount.getEmail(), "0");
                }

                @Override
                public void onTaskAfter(JSONObject jsonObject) {
                    if (AnalyzeUtil.checkSuccess(jsonObject)) {
                        gv.setToken(AnalyzeUtil.getToken(jsonObject, STUDENT));
                        startActivity(new Intent(LoginActivity.this, StoreListActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, AnalyzeUtil.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                    }
                    Log.e("STORE", jsonObject + "");
                }
            });
            Log.e("Google+", signInAccount.getId() + "\n" + signInAccount.getDisplayName() + "\n" + signInAccount.getEmail());
        }
    }
}
