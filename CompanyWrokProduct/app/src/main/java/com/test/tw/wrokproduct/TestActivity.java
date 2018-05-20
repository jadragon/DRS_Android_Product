package com.test.tw.wrokproduct;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.internal.bind.SqlDateTypeAdapter;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import library.AnalyzeJSON.GetAddress;
import library.GetJsonData.GetFileJsonByPHP;
import library.GetJsonData.GetInformationByPHP;
import library.SQLiteDatabaseHandler;

public class TestActivity extends AppCompatActivity {
    TextView test_textview;
    SQLiteDatabaseHandler db;
    ArrayList<Map<String, String>> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        test_textview = findViewById(R.id.test_textview);
       // writeFile();
        testDB();
    }

    private void testDB() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                //final String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                db = new SQLiteDatabaseHandler(getApplicationContext());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("GGGGGG", db.getOutSideCityDetail() + "");
                    }
                });
                /*
                final JSONObject json = new GetInformationByPHP().getAddress("0");
                datas=GetAddress.getAddress(json);
                final String lastestmodifydate=GetAddress.checkModifydate(json);
                final int sqlmodifydate=db.getModifydate(lastestmodifydate);
                if (datas.size()>0&&sqlmodifydate==0) {
                    db.resetTables();
                    db.addAddressAll(datas, lastestmodifydate);
                    db.close();
                }
                */
            }
        }).start();

    }

    private void writeFile() {//將json資料寫於SD卡中
        //要求存取全限
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return;
                }
            }
        }
        new Thread(new Runnable() {

            @Override
            public void run() {
                final String json = new GetFileJsonByPHP(getApplicationContext()).getAddress();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        test_textview.setText(json);
                        Log.e("TestJSONONONONON", json + "");
                    }
                });
            }
        }).start();

    }
}
