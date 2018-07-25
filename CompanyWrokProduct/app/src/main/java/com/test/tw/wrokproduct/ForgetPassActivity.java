package com.test.tw.wrokproduct;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import library.Component.ToastMessageDialog;
import library.GetJsonData.MemberJsonData;
import library.JsonDataThread;
import library.LoadingView;

public class ForgetPassActivity extends AppCompatActivity {
    Toolbar toolbar;
    int type;
    EditText forget_edit_account;
    Button forget_button;
    ToastMessageDialog toastMessage;
    JSONObject json;

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
        new JsonDataThread() {
            @Override
            public JSONObject getJsonData() {
                return new MemberJsonData().forget(type, "886", forget_edit_account.getText().toString());
            }

            @Override
            public void runUiThread(JSONObject json) {
                try {
                    toastMessage.setMessageText(json.getString("Message"));
                    toastMessage.confirm();
                    LoadingView.hide();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();

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
