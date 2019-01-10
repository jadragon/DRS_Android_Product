package com.example.alex.eip_product;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import Utils.PreferenceUtil;
import db.OrderDatabase;

public class LoginActivity extends AppCompatActivity {
    private Button login;
    private EditText username, pw;
    private GlobalVariable gv;
    private ProgressDialog progressDialog;
    public static HandlerThread handlerThread;
    private Handler mHandler;
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
                progressDialog = ProgressDialog.show(LoginActivity.this, getResources().getString(R.string.loading_data), getResources().getString(R.string.wait), true);
                mHandler.post(runnable_refresh);

            }
        });
    }

    public void initHandler() {
        handlerThread = new HandlerThread("soap");
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper());
    }

    private Runnable runnable_refresh = new Runnable() {
        @Override
        public void run() {
            try {
                JSONObject json = new API_OrderInfo().getOrderInfo(username.getText().toString(), pw.getText().toString());
                if (AnalyzeUtil.checkSuccess(json)) {
                    Map<String, List<ContentValues>> map = Analyze_Order.getOrders(json);
                    db.addOrders(map.get("Orders"));
                    db.addOrderDetails(map.get("OrderDetails"));
                    db.addCheckFailedReasons(map.get("CheckFailedReasons"));
                    db.addOrderComments(map.get("OrderComments"));
                    db.addOrderItemComments(map.get("OrderItemComments"));
                    json = new API_OrderInfo().getItemDrawing(username.getText().toString(), pw.getText().toString());
                    if (AnalyzeUtil.checkSuccess(json)) {
                        db.addItemDrawings(Analyze_Order.getItemDrawings(json));
                        json = new API_OrderInfo().getDrawingFile(username.getText().toString(), pw.getText().toString());
                        if (AnalyzeUtil.checkSuccess(json)) {
                            db.addFiles(Analyze_Order.getFiles(json));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_success), Toast.LENGTH_SHORT).show();
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
                    } else {
                        Toast.makeText(LoginActivity.this, AnalyzeUtil.getMessage(json), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, AnalyzeUtil.getMessage(json), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.exception), Toast.LENGTH_SHORT).show();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.exception), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.exception), Toast.LENGTH_SHORT).show();
            } finally {
                progressDialog.dismiss();
            }
        }
    };

    @Override
    protected void onDestroy() {
        handlerThread.quit();
        mHandler.removeCallbacks(null);
        db.close();
        super.onDestroy();
    }

}
