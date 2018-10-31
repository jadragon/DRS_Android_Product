package com.example.alex.ordersystemdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alex.ordersystemdemo.API.Analyze.AnalyzeUtil;
import com.example.alex.ordersystemdemo.API.List.DelivertApi;
import com.example.alex.ordersystemdemo.API.List.StoreApi;
import com.example.alex.ordersystemdemo.API.List.StudentApi;
import com.example.alex.ordersystemdemo.QuickLoginUtil.FacebookQL;
import com.example.alex.ordersystemdemo.QuickLoginUtil.GoogleQL;
import com.example.alex.ordersystemdemo.library.AsyncTaskUtils;
import com.example.alex.ordersystemdemo.library.IDataCallBack;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    View fb, gplus, delivery_login, store_login;
    GoogleQL googleQL;
    FacebookQL facebookQL;
    private final static int STUDENT = 0;
    private final static int STORE = 1;
    private final static int DELIVERY = 2;
    private EditText account, password;
    private GlobalVariable gv;
    private SharedPreferences settings;

    public void saveData(String id, int type) {
        settings = getSharedPreferences("user_data", 0);
        settings.edit()
                .putString("id", id)
                .putInt("type", type)
                .commit();
        gv.setToken(id);
        gv.setType(type);
    }

    private boolean readData() {
        settings = getSharedPreferences("user_data", 0);
        String id = settings.getString("id", "");
        int type = settings.getInt("type", 0);
        if (!id.equals("")) {
            gv.setToken(id);
            gv.setType(type);
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        gv = (GlobalVariable) getApplicationContext();
        if (readData()) {
            switch (gv.getType()) {
                case STUDENT:
                    startActivity(new Intent(LoginActivity.this, StoreListActivity.class));
                    finish();
                    break;
                case STORE:
                    startActivity(new Intent(LoginActivity.this, StoreActivity.class));
                    finish();
                    break;
                case DELIVERY:
                    startActivity(new Intent(LoginActivity.this, DeliveryActivity.class));
                    finish();
                    break;
            }
        }
        googleQL = new GoogleQL(this);
        facebookQL = new FacebookQL(this);
        initTextView();
        initClickView();
        //  initSwitch();

    }

    private void initTextView() {
        account = findViewById(R.id.account);
        password = findViewById(R.id.password);
    }

    /*
        private void initSwitch() {
            login_switch = findViewById(R.id.login_switch);
            login_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        currentType = DELIVERY;
                        login_title.setText("外送員登入");
                    } else {
                        currentType = STORE;
                        login_title.setText("商家登入");
                    }
                }
            });
        }
    */
    private void initClickView() {
        store_login = findViewById(R.id.store_login);
        store_login.setOnClickListener(this);
        delivery_login = findViewById(R.id.delivery_login);
        delivery_login.setOnClickListener(this);
        fb = findViewById(R.id.login_fb);
        fb.setOnClickListener(this);
        gplus = findViewById(R.id.login_gplus);
        gplus.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.store_login:
                AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                    @Override
                    public JSONObject onTasking(Void... params) {
                        return new StoreApi().store_login(account.getText().toString(), password.getText().toString());
                    }

                    @Override
                    public void onTaskAfter(JSONObject jsonObject) {
                        if (jsonObject != null) {
                            if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                saveData(AnalyzeUtil.getToken(jsonObject, STORE), STORE);
                                startActivity(new Intent(LoginActivity.this, StoreActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, AnalyzeUtil.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(LoginActivity.this, "連線異常", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.delivery_login:

                AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                    @Override
                    public JSONObject onTasking(Void... params) {
                        return new DelivertApi().deliver_login(account.getText().toString(), password.getText().toString());
                    }

                    @Override
                    public void onTaskAfter(JSONObject jsonObject) {
                        if (jsonObject != null) {
                            if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                saveData(AnalyzeUtil.getToken(jsonObject, DELIVERY), DELIVERY);
                                startActivity(new Intent(LoginActivity.this, DeliveryActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, AnalyzeUtil.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(LoginActivity.this, "連線異常", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.login_fb:
                facebookQL.loginFB();
                break;
            case R.id.login_gplus:
                googleQL.signIn();
                break;
        }

    }

    /*
        @Override
        protected void onStart() {
            super.onStart();
            googleQL.onStart();
        }
    */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebookQL.onActivityResult(requestCode, resultCode, data);
        final GoogleSignInAccount signInAccount = googleQL.onActivityResult(requestCode, resultCode, data);
        if (signInAccount != null) {
            AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                @Override
                public JSONObject onTasking(Void... params) {
                    return new StudentApi().student_login(signInAccount.getId(), signInAccount.getDisplayName(), signInAccount.getEmail(), "0");
                }

                @Override
                public void onTaskAfter(JSONObject jsonObject) {
                    if (jsonObject != null) {
                        if (AnalyzeUtil.checkSuccess(jsonObject)) {
                            saveData(AnalyzeUtil.getToken(jsonObject, STUDENT), STUDENT);
                            startActivity(new Intent(LoginActivity.this, StoreListActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, AnalyzeUtil.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(LoginActivity.this, "連線異常", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
