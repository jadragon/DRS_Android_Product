package tw.com.lccnet.app.designateddriving;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import tw.com.lccnet.app.designateddriving.API.Analyze.AnalyzeUtil;
import tw.com.lccnet.app.designateddriving.API.CustomerApi;
import tw.com.lccnet.app.designateddriving.Utils.AsyncTaskUtils;
import tw.com.lccnet.app.designateddriving.Utils.IDataCallBack;
import tw.com.lccnet.app.designateddriving.Utils.MatchesUtils;

public class Regist1Activity extends ToolbarActivity implements View.OnClickListener {

    private EditText phone, vcode;
    private Button next, gvcode;
    private View agree;
    private CheckBox cb_agree;
    private String gvcode_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist1);
        initToolbar("註冊", true);
        initView();
        initListener();
    }

    private void initView() {
        phone = findViewById(R.id.register_edit_phone);
        gvcode = findViewById(R.id.register_btn_gvcode);
        vcode = findViewById(R.id.register_edit_vcode);
        agree = findViewById(R.id.register_btn_agree);
        cb_agree = findViewById(R.id.register_cb_agree);
        next = findViewById(R.id.register_btn_next);
    }

    private void initListener() {
        next.setOnClickListener(this);
        gvcode.setOnClickListener(this);
        agree.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_btn_next:
                if (TextUtils.isEmpty(phone.getText()) || !MatchesUtils.matchPhone(phone.getText().toString())) {
                    phone.setError("請輸入電話號碼");
                } else if (TextUtils.isEmpty(vcode.getText())) {
                    vcode.setError("請輸入驗證碼");
                } else if (!cb_agree.isChecked()) {
                    cb_agree.setError("請先同意條款");
                } else if (!vcode.getText().toString().equals(gvcode_str)) {
                    vcode.setError("驗證碼有誤");
                } else {
                    Intent intent = new Intent(Regist1Activity.this, Regist2Activity.class);
                    intent.putExtra("phone", phone.getText().toString());
                    intent.putExtra("vcode", vcode.getText().toString());
                    startActivity(intent);
                    finish();
                }

                break;
            case R.id.register_btn_gvcode:
                if (TextUtils.isEmpty(phone.getText())) {
                    phone.setError("請輸入手機號碼");
                } else {
                    AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                        @Override
                        public JSONObject onTasking(Void... params) {
                            return CustomerApi.gvcode(phone.getText().toString());
                        }

                        @Override
                        public void onTaskAfter(JSONObject jsonObject) {
                            if (jsonObject != null && AnalyzeUtil.checkSuccess(jsonObject)) {
                                try {
                                    phone.setEnabled(false);
                                    gvcode_str = jsonObject.getString("Data");
                                    Toast.makeText(Regist1Activity.this, "驗證碼已寄出", Toast.LENGTH_SHORT).show();
                                    Log.e("gvcode", gvcode_str);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
                break;
            case R.id.register_btn_agree:
                Intent intent = new Intent(this, SimpleWebviewActivity.class);
                intent.putExtra("title", "服務條款");
                startActivity(intent);
                break;
        }
    }

}
