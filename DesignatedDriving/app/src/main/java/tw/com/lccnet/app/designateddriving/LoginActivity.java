package tw.com.lccnet.app.designateddriving;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import tw.com.lccnet.app.designateddriving.API.Analyze.AnalyzeCustomer;
import tw.com.lccnet.app.designateddriving.API.Analyze.AnalyzeUtil;
import tw.com.lccnet.app.designateddriving.API.CustomerApi;
import tw.com.lccnet.app.designateddriving.Thread.AsyncTaskUtils;
import tw.com.lccnet.app.designateddriving.Thread.IDataCallBack;
import tw.com.lccnet.app.designateddriving.Utils.MatchesUtils;
import tw.com.lccnet.app.designateddriving.db.SQLiteDatabaseHandler;

import static tw.com.lccnet.app.designateddriving.db.SQLiteDatabaseHandler.KEY_MODIFYDATE;
import static tw.com.lccnet.app.designateddriving.db.SQLiteDatabaseHandler.KEY_TOKEN;

public class LoginActivity extends ToolbarActivity implements View.OnClickListener {
    private Button login, register;
    private EditText phone, password;
    private TextView forget;
    private GlobalVariable gv;
    private SQLiteDatabaseHandler db;

    /*
    private SharedPreferences settings;
    public void saveData(String token) {
        settings = getSharedPreferences("user_data", 0);
        settings.edit()
                .putString("token", token)
                .commit();
        gv.setToken(token);
    }

    private boolean readData() {
        settings = getSharedPreferences("user_data", 0);
        String token = settings.getString("token", "");
        if (!token.equals("")) {
            gv.setToken(token);
            return true;
        }
        return false;
    }
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        gv = (GlobalVariable) getApplicationContext();
        db = new SQLiteDatabaseHandler(this);
        initAddress();
        ContentValues cv = db.getMemberDetail();
        if (cv != null) {
            gv.setToken(cv.getAsString(KEY_TOKEN));
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        initToolbar("登入", false);
        initView();
        initListenser();

    }

    private void initView() {
        login = findViewById(R.id.login_btn_login);
        register = findViewById(R.id.login_btn_register);
        forget = findViewById(R.id.login_btn_forget);
        phone = findViewById(R.id.login_edit_phone);
        password = findViewById(R.id.login_edit_pass);
    }

    private void initListenser() {
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        forget.setOnClickListener(this);
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
                    AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                        @Override
                        public JSONObject onTasking(Void... params) {
                            return CustomerApi.login(phone.getText().toString(), password.getText().toString());
                        }

                        @Override
                        public void onTaskAfter(JSONObject jsonObject) {
                            if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                startActivity(new Intent(LoginActivity.this, MapsActivity.class));
                                db.resetLoginTables();
                                ContentValues cv = AnalyzeCustomer.getLogin(jsonObject);
                                db.addMember(cv);
                                gv.setToken(cv.getAsString(KEY_TOKEN));
                                finish();
                            }
                            Log.e("login", jsonObject + "");
                            Toast.makeText(LoginActivity.this, AnalyzeUtil.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                        }
                    });

                }

                break;
            case R.id.login_btn_register:
                startActivity(new Intent(LoginActivity.this, Regist1Activity.class));
                break;
            case R.id.login_btn_forget:
                Intent intent = new Intent(this, ForgetActivity.class);
                intent.putExtra("phone", phone.getText().toString());
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }

    private void initAddress() {
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public JSONObject onTasking(Void... params) {
                return CustomerApi.getAddress("0");
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                    if (AnalyzeUtil.checkSuccess(jsonObject)) {
                        ArrayList<ContentValues> datas = AnalyzeUtil.getAddress(jsonObject);
                        if (datas.size() > 0 && db.getModifydate(datas.get(0).getAsString(KEY_MODIFYDATE)) == 0) {
                            db.resetAddressTables();
                            db.addAddressAll(datas);
                        }
                    }
                Log.e("Address", db.getAddressDetails() + "");
            }
        });
    }
}
