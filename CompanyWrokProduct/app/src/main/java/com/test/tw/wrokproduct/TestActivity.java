package com.test.tw.wrokproduct;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

import library.GetJsonData.GetFileJsonByPHP;
import library.SQLiteDatabaseHandler;

public class TestActivity extends AppCompatActivity {
    TextView test_textview;
    SQLiteDatabaseHandler db;
    ArrayList<Map<String, String>> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_layout);
        //   test_textview = findViewById(R.id.test_textview);
        // writeFile();
        // testDB();
        initTableItem();
    }

    public void initTableItem() {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.a1:
                        if (findViewById(R.id.a1_0).getVisibility() == View.VISIBLE) {
                            findViewById(R.id.a1_0).setVisibility(View.GONE);
                        } else {
                            findViewById(R.id.a1_0).setVisibility(View.GONE);
                            findViewById(R.id.b1_0).setVisibility(View.GONE);
                            findViewById(R.id.c1_0).setVisibility(View.GONE);
                            findViewById(R.id.d1_0).setVisibility(View.GONE);

                            findViewById(R.id.a1_0).setVisibility(View.VISIBLE);
                        }
                        break;
                    case R.id.a1_1:
                        Toast.makeText(getApplicationContext(), "a1_1", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.a1_2:
                        Toast.makeText(getApplicationContext(), "a1_2", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.a1_3:
                        Toast.makeText(getApplicationContext(), "a1_3", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.b1:
                        if (findViewById(R.id.b1_0).getVisibility() == View.VISIBLE) {
                            findViewById(R.id.b1_0).setVisibility(View.GONE);
                        } else {
                            findViewById(R.id.a1_0).setVisibility(View.GONE);
                            findViewById(R.id.b1_0).setVisibility(View.GONE);
                            findViewById(R.id.c1_0).setVisibility(View.GONE);
                            findViewById(R.id.d1_0).setVisibility(View.GONE);

                            findViewById(R.id.b1_0).setVisibility(View.VISIBLE);
                        }
                        break;
                    case R.id.b1_1:
                        Toast.makeText(getApplicationContext(), "b1_1", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.b1_2:
                        Toast.makeText(getApplicationContext(), "b1_2", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.b1_3:
                        Toast.makeText(getApplicationContext(), "b1_3", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.b1_4:
                        Toast.makeText(getApplicationContext(), "b1_4", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.b1_5:
                        Toast.makeText(getApplicationContext(), "b1_5", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.b1_6:
                        Toast.makeText(getApplicationContext(), "b1_6", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.b1_7:
                        Toast.makeText(getApplicationContext(), "b1_7", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.b1_8:
                        Toast.makeText(getApplicationContext(), "b1_8", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.b1_9:
                        Toast.makeText(getApplicationContext(), "b1_9", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.c1:
                        if (findViewById(R.id.c1_0).getVisibility() == View.VISIBLE) {
                            findViewById(R.id.c1_0).setVisibility(View.GONE);
                        } else {
                            findViewById(R.id.a1_0).setVisibility(View.GONE);
                            findViewById(R.id.b1_0).setVisibility(View.GONE);
                            findViewById(R.id.c1_0).setVisibility(View.GONE);
                            findViewById(R.id.d1_0).setVisibility(View.GONE);

                            findViewById(R.id.c1_0).setVisibility(View.VISIBLE);
                        }
                        break;
                    case R.id.c1_1:
                        Toast.makeText(getApplicationContext(), "c1_1", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.c1_2:
                        Toast.makeText(getApplicationContext(), "c1_2", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.c1_3:
                        Toast.makeText(getApplicationContext(), "c1_3", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.d1:
                        if (findViewById(R.id.d1_0).getVisibility() == View.VISIBLE) {
                            findViewById(R.id.d1_0).setVisibility(View.GONE);
                        } else {
                            findViewById(R.id.a1_0).setVisibility(View.GONE);
                            findViewById(R.id.b1_0).setVisibility(View.GONE);
                            findViewById(R.id.c1_0).setVisibility(View.GONE);
                            findViewById(R.id.d1_0).setVisibility(View.GONE);

                            findViewById(R.id.d1_0).setVisibility(View.VISIBLE);
                        }
                        break;
                    case R.id.d1_1:
                        Toast.makeText(getApplicationContext(), "d1_1", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.d1_2:
                        Toast.makeText(getApplicationContext(), "d1_2", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.d1_3:
                        Toast.makeText(getApplicationContext(), "d1_3", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        findViewById(R.id.a1).setOnClickListener(clickListener);
        findViewById(R.id.a1_1).setOnClickListener(clickListener);
        findViewById(R.id.a1_2).setOnClickListener(clickListener);
        findViewById(R.id.a1_3).setOnClickListener(clickListener);
        findViewById(R.id.b1).setOnClickListener(clickListener);
        findViewById(R.id.b1_1).setOnClickListener(clickListener);
        findViewById(R.id.b1_2).setOnClickListener(clickListener);
        findViewById(R.id.b1_3).setOnClickListener(clickListener);
        findViewById(R.id.b1_4).setOnClickListener(clickListener);
        findViewById(R.id.b1_5).setOnClickListener(clickListener);
        findViewById(R.id.b1_6).setOnClickListener(clickListener);
        findViewById(R.id.b1_7).setOnClickListener(clickListener);
        findViewById(R.id.b1_8).setOnClickListener(clickListener);
        findViewById(R.id.b1_9).setOnClickListener(clickListener);
        findViewById(R.id.c1).setOnClickListener(clickListener);
        findViewById(R.id.c1_1).setOnClickListener(clickListener);
        findViewById(R.id.c1_2).setOnClickListener(clickListener);
        findViewById(R.id.c1_3).setOnClickListener(clickListener);
        findViewById(R.id.d1).setOnClickListener(clickListener);
        findViewById(R.id.d1_1).setOnClickListener(clickListener);
        findViewById(R.id.d1_2).setOnClickListener(clickListener);
        findViewById(R.id.d1_3).setOnClickListener(clickListener);
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
