package com.example.alex.eip_product;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alex.eip_product.SoapAPI.API_OrderInfo;
import com.example.alex.eip_product.SoapAPI.Analyze.AnalyzeUtil;
import com.example.alex.eip_product.SoapAPI.Analyze.Analyze_Order;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import Utils.CommonUtil;
import Utils.PreferenceUtil;
import db.OrderDatabase;

public class LoginActivity extends AppCompatActivity {
    private Button login;
    private EditText username, pw;
    private GlobalVariable gv;
    private ProgressDialog progressDialog;
    private OrderDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        gv = (GlobalVariable) getApplicationContext();
        db = new OrderDatabase(this);
        if (!PreferenceUtil.getString("username", "").equals("")) {
            gv.setUsername(PreferenceUtil.getString("username", ""));
            gv.setPw(PreferenceUtil.getString("pw", ""));
            gv.setPermission(PreferenceUtil.getString("permission", ""));
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        initView();
        initListenser();
    }

    private void initView() {
        username = findViewById(R.id.username);
        pw = findViewById(R.id.pw);
        login = findViewById(R.id.login);
    }

    private void initListenser() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = ProgressDialog.show(LoginActivity.this, getResources().getString(R.string.loading_data), getResources().getString(R.string.wait), true);
                MainActivity.getHandler().post(runnable_refresh);
            }
        });
    }



    private Runnable runnable_refresh = new Runnable() {
        @Override
        public void run() {
            try {
                JSONObject json = API_OrderInfo.getOrderInfo(username.getText().toString(), pw.getText().toString());
                if (AnalyzeUtil.checkSuccess(json)) {
                    //perssion
                    final String permission = AnalyzeUtil.getUserPermission(json);
                    Map<String, List<ContentValues>> map = Analyze_Order.getOrders(json);
                    db.addOrders(map.get("Orders"));
                    db.addOrderDetails(map.get("OrderDetails"));
                    db.addCheckFailedReasons(map.get("CheckFailedReasons"));
                    db.addOrderComments(map.get("OrderComments"));
                    db.addOrderItemComments(map.get("OrderItemComments"));
                    json = API_OrderInfo.getItemDrawing(username.getText().toString(), pw.getText().toString());
                    if (AnalyzeUtil.checkSuccess(json)) {
                        db.addItemDrawings(Analyze_Order.getItemDrawings(json));
                        json = API_OrderInfo.getDrawingFile(username.getText().toString(), pw.getText().toString());
                        if (AnalyzeUtil.checkSuccess(json)) {
                            db.addFiles(Analyze_Order.getFiles(json));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                                    gv.setUsername(username.getText().toString());
                                    gv.setPw(pw.getText().toString());
                                    gv.setPermission(permission);
                                    PreferenceUtil.commitString("username", username.getText().toString());
                                    PreferenceUtil.commitString("pw", pw.getText().toString());
                                    PreferenceUtil.commitString("permission", permission);
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                }
                            });
                        } else {
                            CommonUtil.toastErrorMessage(LoginActivity.this, getResources().getString(R.string.warning), AnalyzeUtil.getMessage(json));
                        }
                    } else {
                        CommonUtil.toastErrorMessage(LoginActivity.this, getResources().getString(R.string.warning), AnalyzeUtil.getMessage(json));
                    }
                } else {
                    CommonUtil.toastErrorMessage(LoginActivity.this, getResources().getString(R.string.warning), AnalyzeUtil.getMessage(json));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                CommonUtil.toastErrorMessage(LoginActivity.this, getResources().getString(R.string.warning), getResources().getString(R.string.download_fail_info));
            } catch (XmlPullParserException e) {
                e.printStackTrace();
                CommonUtil.toastErrorMessage(LoginActivity.this, getResources().getString(R.string.warning), getResources().getString(R.string.download_fail_info));
            } catch (IOException e) {
                e.printStackTrace();
                CommonUtil.toastErrorMessage(LoginActivity.this, getResources().getString(R.string.warning), getResources().getString(R.string.download_fail_info));
            } finally {
                progressDialog.dismiss();
            }
        }
    };

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }

    /**
     * 迴車鍵離開程式
     */
    private static Boolean isExit = false;
    private static Boolean hasTask = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Timer tExit = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                    hasTask = false;
                }
            };
            if (!isExit) {
                isExit = true;
                Toast.makeText(this, getResources().getString(R.string.close_app)
                        , Toast.LENGTH_SHORT).show();
                if (!hasTask) {
                    tExit.schedule(task, 2000);
                }
            } else {
                finish();
                System.exit(0);
            }
            return false;
        }
        return true;
    }
}
