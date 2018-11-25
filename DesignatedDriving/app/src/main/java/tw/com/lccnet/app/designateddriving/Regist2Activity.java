package tw.com.lccnet.app.designateddriving;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import tw.com.lccnet.app.designateddriving.API.Analyze.AnalyzeUtil;
import tw.com.lccnet.app.designateddriving.API.CustomerApi;
import tw.com.lccnet.app.designateddriving.Utils.AsyncTaskUtils;
import tw.com.lccnet.app.designateddriving.Utils.IDataCallBack;

public class Regist2Activity extends AppCompatActivity implements View.OnClickListener {
    private Button send;
    private EditText name, password, repassword;
    private RadioGroup sex;
    private TextView error;
    private String phone, vcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist2);
        phone = getIntent().getStringExtra("phone");
        vcode = getIntent().getStringExtra("vcode");
        initToolbar("註冊");
        initView();
        initListener();

    }

    private void initView() {
        send = findViewById(R.id.register2_btn_send);
        name = findViewById(R.id.register2_edit_name);
        password = findViewById(R.id.register2_edit_password);
        repassword = findViewById(R.id.register2_edit_repassword);
        sex = findViewById(R.id.register2_rg_sex);
        error = findViewById(R.id.female);
    }

    private void initListener() {
        send.setOnClickListener(this);
    }

    private void initToolbar(String title) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        ((TextView) findViewById(R.id.toolbar_title)).setText(title);
        toolbar.setNavigationIcon(R.drawable.back);
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
            case R.id.register2_btn_send:
                if (TextUtils.isEmpty(name.getText())) {
                    name.setError("請輸入姓名");
                } else if (TextUtils.isEmpty(password.getText())) {
                    password.setError("請輸入密碼");
                } else if (!password.getText().toString().equals(repassword.getText().toString())) {
                    repassword.setError("請確認密碼是否正確");
                } else if (sex.getCheckedRadioButtonId() == -1) {
                    error.setError("請選擇性別");
                } else {
                    AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                        @Override
                        public JSONObject onTasking(Void... params) {
                            int sex_num = 0;
                            switch (sex.getCheckedRadioButtonId()) {
                                case R.id.male:
                                    sex_num = 1;
                                    break;
                                case R.id.female:
                                    sex_num = 2;
                                    break;
                            }
                            return CustomerApi.register(phone, password.getText().toString(), vcode, name.getText().toString(), sex_num + "");
                        }

                        @Override
                        public void onTaskAfter(JSONObject jsonObject) {
                            if (jsonObject!=null&&AnalyzeUtil.checkSuccess(jsonObject)) {
                                finish();
                            }
                            Toast.makeText(Regist2Activity.this, AnalyzeUtil.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                break;
        }
    }

}
