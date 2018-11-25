package tw.com.lccnet.app.designateddriving;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import tw.com.lccnet.app.designateddriving.Utils.AsyncTaskUtils;
import tw.com.lccnet.app.designateddriving.Utils.IDataCallBack;

public class ForgetActivity extends AppCompatActivity implements View.OnClickListener {
    private Button getpassword;
    private EditText phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        initToolbar("忘記密碼");
        initView();
        initListener();
        initData();
    }

    private void initData() {
        String pre_phone = getIntent().getStringExtra("phone");
        if (!pre_phone.equals("")) {
            phone.setText(pre_phone);
        }
    }

    private void initView() {
        phone = findViewById(R.id.forget_edit_phone);
        getpassword = findViewById(R.id.forget_btn_getpassword);
    }

    private void initListener() {
        getpassword.setOnClickListener(this);
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

    private void copyPassword(String password) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(password);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("text label", password);
            clipboard.setPrimaryClip(clip);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forget_btn_getpassword:
                if (TextUtils.isEmpty(phone.getText()) || !phone.getText().toString().matches("09[0-9]{8}")) {
                    phone.setError("請輸入手機號碼");
                } else {
                    AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                        @Override
                        public JSONObject onTasking(Void... params) {
                            return CustomerApi.forget(phone.getText().toString());
                        }

                        @Override
                        public void onTaskAfter(JSONObject jsonObject) {
                            if (jsonObject != null && AnalyzeUtil.checkSuccess(jsonObject)) {
                                Toast.makeText(ForgetActivity.this, "密碼已寄出，請檢查是否收到簡訊", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ForgetActivity.this, AnalyzeUtil.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                break;
        }
    }

}
