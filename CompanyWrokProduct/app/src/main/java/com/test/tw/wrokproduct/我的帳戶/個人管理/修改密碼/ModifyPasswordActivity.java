package com.test.tw.wrokproduct.我的帳戶.個人管理.修改密碼;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.R;

import org.json.JSONObject;

import Util.AsyncTaskUtils;
import Util.IDataCallBack;
import library.AnalyzeJSON.AnalyzeUtil;
import library.Component.ToastMessageDialog;
import library.GetJsonData.MemberJsonData;
import library.SQLiteDatabaseHandler;

public class ModifyPasswordActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText modify_account, modify_oldpass, modify_newpass, modify_renewpass;
    private  Button modify_confirm;
    private   ToastMessageDialog toastMessageDialog;
    private  GlobalVariable gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        gv = ((GlobalVariable) getApplicationContext());
        initToolbar();
        toastMessageDialog = new ToastMessageDialog(this);
        getViewById();
        SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(this);
        modify_account.setText(db.getMemberDetail().get("account"));
        modify_account.setEnabled(false);
        db.close();

        modify_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!modify_oldpass.getText().toString().equals("") && !modify_newpass.getText().toString().equals("") && !modify_renewpass.getText().toString().equals("")) {
                    if (!modify_oldpass.getText().toString().equals(modify_newpass.getText().toString())) {
                        if (modify_newpass.getText().toString().equals(modify_renewpass.getText().toString())) {
                            AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                                @Override
                                public JSONObject onTasking(Void... params) {
                                    return new MemberJsonData().updatePersonPawd(gv.getToken(), modify_oldpass.getText().toString(), modify_newpass.getText().toString());
                                }

                                @Override
                                public void onTaskAfter(JSONObject jsonObject) {
                                    toastMessageDialog.setMessageText(AnalyzeUtil.getMessage(jsonObject));
                                    toastMessageDialog.show();
                                }
                            });
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
