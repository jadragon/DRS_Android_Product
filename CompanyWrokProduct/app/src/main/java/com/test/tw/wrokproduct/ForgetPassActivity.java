package com.test.tw.wrokproduct;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import Util.AsyncTaskUtils;
import Util.IDataCallBack;
import library.AnalyzeJSON.AnalyzeUtil;
import library.Component.ToastMessageDialog;
import library.GetJsonData.MemberJsonData;
import library.LoadingView;

public class ForgetPassActivity extends AppCompatActivity {
    private  Toolbar toolbar;
    private   EditText forget_edit_account;
    private  Button forget_button;
    private  ToastMessageDialog toastMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);
        toastMessage = new ToastMessageDialog(this);
        initToolbar();
        initEditText();
        initButton();
    }

    private void initButton() {

        forget_button = findViewById(R.id.forget_button);
        forget_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (forget_edit_account.getText().toString().matches("09[0-9]{8}")) {
                    sendAPI(1);
                } else if (forget_edit_account.getText().toString().matches("[\\w-.]+@[\\w-]+(.[\\w_-]+)+")) {
                    sendAPI(2);
                } else {
                    toastMessage.setMessageText("請輸入正確的Email或手機");
                    toastMessage.confirm();
                }

            }

        });
    }

    private void sendAPI(final int type) {
        LoadingView.show(getCurrentFocus());

        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public JSONObject onTasking(Void... params) {
                return new MemberJsonData().forget(type, "886", forget_edit_account.getText().toString());
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                toastMessage.setMessageText(AnalyzeUtil.getMessage(jsonObject));
                toastMessage.confirm();
                LoadingView.hide();
            }
        });

    }

    private void initEditText() {
        forget_edit_account = findViewById(R.id.forget_edit_account);
    }

    private void initToolbar() {
        //Toolbar 建立
        toolbar = findViewById(R.id.include_toolbar);
        ((TextView) findViewById(R.id.include_toolbar_title)).setText("忘記密碼");
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
