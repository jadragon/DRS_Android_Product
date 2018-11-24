package tw.com.lccnet.app.designateddriving;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import tw.com.lccnet.app.designateddriving.API.Analyze.AnalyzeUtil;
import tw.com.lccnet.app.designateddriving.API.CustomerApi;
import tw.com.lccnet.app.designateddriving.Http.JSONParser;
import tw.com.lccnet.app.designateddriving.Utils.AsyncTaskUtils;
import tw.com.lccnet.app.designateddriving.Utils.IDataCallBack;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button login, register;
    private EditText phone, password;
    private TextView forget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initToolbar("登入");
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
    }

    private void initToolbar(String title) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        ((TextView) findViewById(R.id.toolbar_title)).setText(title);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.login_btn_login:
                if (TextUtils.isEmpty(phone.getText())) {
                    phone.setError("請輸入正確的手機號碼");
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
                                finish();
                            }
                            Toast.makeText(LoginActivity.this, AnalyzeUtil.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                        }
                    });

                }

                break;
            case R.id.login_btn_register:
                startActivity(new Intent(LoginActivity.this, Regist1Activity.class));
                break;
            case R.id.login_btn_forget:
                break;
        }
    }
}
