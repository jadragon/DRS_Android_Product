package com.example.alex.eip_product;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
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
import java.util.Locale;
import java.util.Map;

import Utils.PreferenceUtil;
import db.OrderDatabase;

public class LoginActivity extends AppCompatActivity {
    private Button login;
    private EditText username, pw;
    private GlobalVariable gv;
    private ProgressDialog progressDialog;
    private HandlerThread handlerThread;
    private Handler mHandler, UiHandler;
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
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        initView();
        initListenser();
        initHandler();
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
                progressDialog = ProgressDialog.show(LoginActivity.this, "讀取資料中", "請稍後", true);
                mHandler.sendEmptyMessageDelayed(0, 0);
            }
        });
    }

    public void initHandler() {
        UiHandler = new Handler(getMainLooper());
        handlerThread = new HandlerThread("soap");
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper(), mCallback);
    }

    Handler.Callback mCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            try {
                JSONObject json = new API_OrderInfo().getOrderInfo(username.getText().toString(), pw.getText().toString());
                if (AnalyzeUtil.checkSuccess(json)) {
                    db.resetTables();
                    Map<String, List<ContentValues>> map = Analyze_Order.getOrders(json);
                    db.addOrders(map.get("Orders"));
                    db.addOrderDetails(map.get("OrderDetails"));
                    db.addCheckFailedReasons(map.get("CheckFailedReasons"));
                    db.addOrderComments(map.get("OrderComments"));
                    db.addOrderItemComments(map.get("OrderItemComments"));
                    UiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "Orders:" + db.countOrders() + "\nOrderDetails:" + db.countOrderDetails() + "\nCheckFailedReasons:" + db.countCheckFailedReasons() + "\nOrderComments:" + db.countOrderComments() + "\nOrderItemComments:" + db.countOrderItemComments(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            gv.setUsername(username.getText().toString());
                            gv.setPw(pw.getText().toString());
                            PreferenceUtil.commitString("username", username.getText().toString());
                            PreferenceUtil.commitString("pw", pw.getText().toString());
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, AnalyzeUtil.getMessage(json), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(LoginActivity.this, "JSONException存取異常", Toast.LENGTH_SHORT).show();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
                Toast.makeText(LoginActivity.this, "XmlPullParserException存取異常", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(LoginActivity.this, "IOException存取異常", Toast.LENGTH_SHORT).show();
            } finally {
                progressDialog.dismiss();
            }
            return false;
        }
    };

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        db.close();
        super.onDestroy();
    }

}
