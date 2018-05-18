package com.test.tw.wrokproduct;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import library.GetJsonData.GetFileJsonByPHP;

public class TestActivity extends AppCompatActivity {
    TextView test_textview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        test_textview = findViewById(R.id.test_textview);
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
        testJson();
    }

    private void testJson() {

        new Thread(new Runnable() {

            @Override
            public void run() {
          final String json=   new GetFileJsonByPHP(getApplicationContext()).getAddress();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("TestJSONONONONON", json + "");
                    }
                });
            }
        }).start();

    }
}
