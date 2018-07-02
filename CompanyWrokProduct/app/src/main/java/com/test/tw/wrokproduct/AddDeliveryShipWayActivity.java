package com.test.tw.wrokproduct;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import library.GetJsonData.LogisticsJsonData;
import library.GetJsonData.ReCountJsonData;
import library.GetJsonData.ShopCartJsonData;
import library.SQLiteDatabaseHandler;

public class AddDeliveryShipWayActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView toolbar_title;
    TextView add_delivery_txt_city, add_delivery_txt_area;
    EditText add_delivery_edit_name, add_delivery_edit_phone, add_delivery_edit_zipcode, add_delivery_edit_address;
    Button confirm;
    String mpcode = "886", shit = "TW";
    String token, sno, plno, type, land, logistics;
    String mlno, name, mp;
    String sid, sname, city, area, prezipcode, address;
    JSONObject json;
    Intent intent;
    int count_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        token = ((GlobalVariable) getApplicationContext()).getToken();
        setContentView(R.layout.activity_add_delivery_shipway);
        intent = getIntent();
        count_type = intent.getIntExtra("count_type", 0);
        //shipwayInfo
        initShipwayInfo();

        initToolbar();
        add_delivery_edit_name = findViewById(R.id.add_delivery_edit_name);
        add_delivery_edit_phone = findViewById(R.id.add_delivery_edit_phone);
        add_delivery_edit_address = findViewById(R.id.add_delivery_edit_address);
        add_delivery_edit_zipcode = findViewById(R.id.add_delivery_edit_zipcode);
        add_delivery_edit_zipcode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(getApplicationContext());
                    Map<String, String> map = db.getCityAndAreaByZipcode(add_delivery_edit_zipcode.getText().toString());
                    if (!map.isEmpty()) {
                        area = map.get("area");
                        add_delivery_txt_area.setText(area);
                        add_delivery_txt_area.setTextColor(Color.BLACK);
                        city = map.get("city");
                        add_delivery_txt_city.setText(city);
                        add_delivery_txt_city.setTextColor(Color.BLACK);
                        prezipcode = add_delivery_edit_zipcode.getText().toString();
                    } else {
                        add_delivery_edit_zipcode.setText(prezipcode);
                    }
                    db.close();
                }
            }
        });
        add_delivery_txt_city = findViewById(R.id.add_delivery_txt_city);
        add_delivery_txt_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(AddDeliveryShipWayActivity.this, ListVIewActivity.class);
                intent.putExtra("land", land);
                startActivityForResult(intent, 0);
            }
        });
        add_delivery_txt_area = findViewById(R.id.add_delivery_txt_area);
        add_delivery_txt_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!add_delivery_txt_city.getText().toString().equals("請選擇縣市")) {
                    intent = new Intent(AddDeliveryShipWayActivity.this, ListVIewActivity.class);
                    intent.putExtra("city", add_delivery_txt_city.getText().toString());
                    startActivityForResult(intent, 1);
                }
            }
        });
        add_delivery_txt_area = findViewById(R.id.add_delivery_txt_area);
        confirm = findViewById(R.id.add_delivery_btn_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (add_delivery_edit_name.getText().toString().equals("") || add_delivery_edit_phone.getText().toString().equals("") || add_delivery_edit_zipcode.getText().toString().equals("")
                        || add_delivery_txt_city.getText().toString().equals("請選擇縣市") || add_delivery_txt_area.getText().toString().equals("請選擇鄉鎮市區")) {
                    Toast.makeText(AddDeliveryShipWayActivity.this, "資料填寫不完整", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    name = add_delivery_edit_name.getText().toString();
                                    mp = add_delivery_edit_phone.getText().toString();
                                    address = add_delivery_edit_address.getText().toString();
                                    if (sno != null) {//運送方式
                                        json = new ReCountJsonData().setMemberLogistics(count_type, token, sno, plno, type, land, logistics, name, mpcode, mp, sname, sid, shit, city, area, prezipcode, address);
                                    } else if (mlno != null) {//修改收貨方式
                                        json = new LogisticsJsonData().updateLogistics(token, mlno, name, mpcode, mp, sname, sid, shit, city, area, prezipcode, address);
                                    } else {//新增收貨方式
                                        json = new LogisticsJsonData().setLogistics(token, type, land, logistics, name, mpcode, mp, sname, sid, shit, city, area, prezipcode, address);
                                    }
                                    try {
                                        if (json.getBoolean("Success")) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(getApplicationContext(), "新增完成", Toast.LENGTH_SHORT).show();
                                                    setResult(1, null);
                                                    finish();
                                                }
                                            });
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }).start();
                        }
                    }).start();

                }
            }
        });
        if (mlno != null)
            initTextView();
    }

    private void initTextView() {
        add_delivery_edit_name.setText(name);
        add_delivery_edit_phone.setText(mp);
        add_delivery_txt_city.setText(city);
        add_delivery_txt_city.setTextColor(Color.BLACK);
        add_delivery_txt_area.setText(area);
        add_delivery_txt_area.setTextColor(Color.BLACK);
        add_delivery_edit_zipcode.setText(prezipcode);
        add_delivery_edit_address.setText(address);

    }

    private void initShipwayInfo() {
        //運送方式
        sno = intent.getStringExtra("sno");
        plno = intent.getStringExtra("plno");
        //修改收貨方式
        mlno = intent.getStringExtra("mlno");
        name = intent.getStringExtra("name");
        mp = intent.getStringExtra("mp");
        sname = intent.getStringExtra("sname");
        sid = intent.getStringExtra("sid");
        city = intent.getStringExtra("city");
        area = intent.getStringExtra("area");
        prezipcode = intent.getStringExtra("zipcode");
        address = intent.getStringExtra("address");
        //必備
        type = intent.getStringExtra("type");
        land = intent.getStringExtra("land");
        logistics = intent.getStringExtra("logistics");

    }

    private void initToolbar() {
        //Toolbar 建立
        toolbar = findViewById(R.id.include_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar_title = findViewById(R.id.include_toolbar_title);
        toolbar_title.setText("新增黑貓宅配地址");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (requestCode == 0) {
                add_delivery_txt_area.setText("請選擇鄉鎮市區");
                add_delivery_txt_area.setTextColor(getResources().getColor(R.color.gainsboro));
                add_delivery_edit_zipcode.setText(null);
                city = data.getStringExtra("city");
                add_delivery_txt_city.setText(city);
                add_delivery_txt_city.setTextColor(Color.BLACK);
            } else if (requestCode == 1) {
                add_delivery_edit_zipcode.setText(null);
                area = data.getStringExtra("area");
                add_delivery_txt_area.setText(area);
                add_delivery_txt_area.setTextColor(Color.BLACK);
                prezipcode = data.getStringExtra("zipcode");
                add_delivery_edit_zipcode.setText(prezipcode);

            }
        }
    }
}
