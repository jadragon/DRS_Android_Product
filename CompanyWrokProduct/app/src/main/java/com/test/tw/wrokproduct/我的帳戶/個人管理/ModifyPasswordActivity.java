package com.test.tw.wrokproduct.我的帳戶.個人管理;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.R;

import org.json.JSONException;
import org.json.JSONObject;

import library.AnalyzeJSON.AnalyzeMember;
import library.GetJsonData.MemberJsonData;
import library.SQLiteDatabaseHandler;
import library.component.ToastMessageDialog;

public class ModifyPasswordActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText modify_account, modify_oldpass, modify_newpass, modify_renewpass;
    Button modify_confirm;
    ToastMessageDialog toastMessageDialog;
String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        token = ((GlobalVariable) getApplicationContext()).getToken();
        initToolbar();
        toastMessageDialog = new ToastMessageDialog(this);
        getViewById();
        SQLiteDatabaseHandler db=new SQLiteDatabaseHandler(this);
        modify_account.setText( db.getMemberDetail().get("account"));
        modify_account.setEnabled(false);
        db.close();

        modify_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!modify_oldpass.getText().toString().equals("") && !modify_newpass.getText().toString().equals("") && !modify_renewpass.getText().toString().equals("")) {
                    if (!modify_oldpass.getText().toString().equals(modify_newpass.getText().toString())) {
                        if (modify_newpass.getText().toString().equals(modify_renewpass.getText().toString())) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    final JSONObject json=new MemberJsonData().updatePersonPawd(token,modify_oldpass.getText().toString(),modify_newpass.getText().toString());
                                    if(AnalyzeMember.checkSuccess(json)){
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    toastMessageDialog.setMessageText(json.getString("Message"));
                                                    toastMessageDialog.show();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        });
                                    }
                                }
                            }).start();
                        } else {
                            toastMessageDialog.setMessageText("再次確認填寫有誤");
                            toastMessageDialog.confirm();
                        }
                    } else {
                        toastMessageDialog.setMessageText("新密碼與舊密碼重複");
                        toastMessageDialog.confirm();
                    }
                } else {
                    toastMessageDialog.setMessageText("資料請填寫完整");
                    toastMessageDialog.confirm();
                }
            }
        });
    }

    private void getViewById() {
        modify_account = findViewById(R.id.modify_account);
        modify_oldpass = findViewById(R.id.modify_oldpass);
        modify_newpass = findViewById(R.id.modify_newpass);
        modify_renewpass = findViewById(R.id.modify_renewpass);
        modify_confirm = findViewById(R.id.modify_confirm);
    }

    private void initToolbar() {
        //Toolbar 建立
        toolbar = findViewById(R.id.include_toolbar);
        ((TextView) findViewById(R.id.include_toolbar_title)).setText("修改密碼");
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
