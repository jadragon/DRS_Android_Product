package tw.com.lccnet.app.designateddriving;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import tw.com.lccnet.app.designateddriving.API.Analyze.AnalyzeCustomer;
import tw.com.lccnet.app.designateddriving.API.Analyze.AnalyzeUtil;
import tw.com.lccnet.app.designateddriving.API.CustomerApi;
import tw.com.lccnet.app.designateddriving.Utils.AsyncTaskUtils;
import tw.com.lccnet.app.designateddriving.Utils.IDataCallBack;
import tw.com.lccnet.app.designateddriving.Utils.SQLiteDatabaseHandler;

import static tw.com.lccnet.app.designateddriving.Utils.SQLiteDatabaseHandler.KEY_TOKEN;
import static tw.com.lccnet.app.designateddriving.Utils.SQLiteDatabaseHandler.KEY_UNAME;

public class ContactMeActivity extends ToolbarActivity implements View.OnClickListener {
    private TextView uname;
    private EditText title, content;
    private Button call, send;
    private int MY_PERMISSIONS_REQUEST_CALL_PHONE = 100;
    private GlobalVariable gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_me);
        initToolbar("聯絡我們", true);
        gv = (GlobalVariable) getApplicationContext();
        initView();
        initListener();
        initData();
    }

    private void initData() {
        SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(this);
        uname.setText(db.getMemberDetail().getAsString(KEY_UNAME));
        db.close();
    }

    private void initListener() {
        call.setOnClickListener(this);
        send.setOnClickListener(this);
    }

    private void initView() {
        uname = findViewById(R.id.uname);
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        uname = findViewById(R.id.uname);
        call = findViewById(R.id.call);
        send = findViewById(R.id.send);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.call:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("確定撥打電話?").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (ContextCompat.checkSelfPermission(ContactMeActivity.this,
                                Manifest.permission.CALL_PHONE)
                                != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions((Activity) ContactMeActivity.this,
                                    new String[]{Manifest.permission.CALL_PHONE},
                                    MY_PERMISSIONS_REQUEST_CALL_PHONE);

                            // MY_PERMISSIONS_REQUEST_CALL_PHONE is an
                            // app-defined int constant. The callback method gets the
                            // result of the request.
                        } else {
                            //You already have permission
                            Intent call = new Intent("android.intent.action.CALL", Uri.parse("tel:" + "03-3558128"));
                            ContactMeActivity.this.startActivity(call);
                            dialog.dismiss();
                        }

                    }
                }).show();
                break;
            case R.id.send:
                if (TextUtils.isEmpty(title.getText())) {
                    title.setError("請輸入主旨");
                    return;
                } else if (TextUtils.isEmpty(content.getText())) {
                    content.setError("請輸入內容");
                    return;
                }
                AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                    @Override
                    public JSONObject onTasking(Void... params) {
                        return CustomerApi.contact(gv.getToken(), title.getText().toString(), content.getText().toString());
                    }

                    @Override
                    public void onTaskAfter(JSONObject jsonObject) {
                        if (jsonObject != null) {
                            Toast.makeText(ContactMeActivity.this, AnalyzeUtil.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE) {

            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    //判断是否勾选禁止后不再询问
                    boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(ContactMeActivity.this, permissions[i]);
                    if (showRequestPermission) {
                        Toast.makeText(this, "權限尚未申請", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        Toast.makeText(this, "因權限問題，部分功能無法使用", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
            Intent call = new Intent("android.intent.action.CALL", Uri.parse("tel:" + "03-3558128"));
            ContactMeActivity.this.startActivity(call);
        }

    }
}
