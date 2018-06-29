package com.test.tw.wrokproduct;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import library.GetJsonData.MemberJsonData;
import library.Component.ToastMessageDialog;

public class ForgetPassActivity extends AppCompatActivity {
    Toolbar toolbar;
    int type;
    EditText forget_edit_account, forget_edit_password;
    Button forget_btn_gvcode, forget_button;
    String vcode;
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
        forget_btn_gvcode = findViewById(R.id.forget_btn_gvcode);
        forget_btn_gvcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!forget_edit_account.getText().toString().equals("")) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            json = new MemberJsonData().gvcode(0, "+886", forget_edit_account.getText().toString());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        boolean success = json.getBoolean("Success");
                                        if (success) {
                                            vcode = json.getString("Data");
                                            forget_edit_account.setFocusable(false);
                                        }
                                        toastMessage.setMessageText(json.getString("Message"));
                                        toastMessage.confirm();
                                        Log.e("success", success + "" + json.getString("Message"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }).start();
                } else {
                    toastMessage.setMessageText("請先輸入帳號");
                    toastMessage.confirm();
                }
            }
        });
        forget_button = findViewById(R.id.forget_button);
        forget_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vcode != null) {
                    if (vcode.toString().equals(forget_edit_password.getText().toString())) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                json = new MemberJsonData().forget(1, "+886", forget_edit_account.getText().toString());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            toastMessage.setMessageText(json.getString("Message"));
                                            toastMessage.confirm();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        }).start();
                    } else {
                        toastMessage.setMessageText("請確認您輸入的驗證碼是否正確");
                        toastMessage.confirm();
                    }
                } else {
                    toastMessage.setMessageText("請先取得驗證碼後再進行下一步");
                    toastMessage.confirm();
                }
            }
        });
    }

    private void initEditText() {
        forget_edit_account = findViewById(R.id.forget_edit_account);
        forget_edit_password = findViewById(R.id.forget_edit_password);
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
