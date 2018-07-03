package com.test.tw.wrokproduct;

import android.content.Intent;
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
import library.SQLiteDatabaseHandler;

public class AddStoreShipWayActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView toolbar_title;
    TextView addshipway_store;
    EditText addshipway_name, addshipway_phone;
    Button confirm;
    String mpcode = "886", shit = "TW";
    String  sno, plno, type, land, logistics;
    String mlno, name, mp;
    String sid, sname, city, area, zipcode, address;
    Intent intent;
    JSONObject json;
    int count_type;
    GlobalVariable gv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gv = (GlobalVariable) getApplicationContext();
        setContentView(R.layout.activity_add_store_shipway);
        intent = getIntent();
        count_type = intent.getIntExtra("count_type", 0);
        //shipwayInfo
        initShipwayInfo();
        initToolbar();
        addshipway_store = findViewById(R.id.addshipway_store);
        addshipway_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddStoreShipWayActivity.this, StoreWebViewActivity.class);
                intent.putExtra("logistics", logistics);
                startActivityForResult(intent, 0);
            }
        });
        addshipway_name = findViewById(R.id.addshipway_name);
        addshipway_phone = findViewById(R.id.addshipway_phone);
        confirm = findViewById(R.id.addshipway_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addshipway_name.getText().toString().equals("") || addshipway_phone.getText().toString().equals("") || addshipway_store.getText().toString().equals("請選擇門市")) {
                    Toast.makeText(AddStoreShipWayActivity.this, "資料填寫不完整", Toast.LENGTH_SHORT).show();
                } else {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            name = addshipway_name.getText().toString();
                            mp = addshipway_phone.getText().toString();
                            if (sno != null) {//運送方式
                                json = new ReCountJsonData().setMemberLogistics(count_type, gv.getToken(), sno, plno, type, land, logistics, name, mpcode, mp, sname, sid, shit, city, area, zipcode, address);
                            } else if (mlno != null) {//修改收貨方式
                                json = new LogisticsJsonData().updateLogistics( gv.getToken(), mlno, name, mpcode, mp, sname, sid, shit, city, area, zipcode, address);
                            } else {//新增收貨方式
                                json = new LogisticsJsonData().setLogistics( gv.getToken(), type, land, logistics, name, mpcode, mp, sname, sid, shit, city, area, zipcode, address);
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
            }
        });
        if (mlno != null)
            initTextView();
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
        zipcode = intent.getStringExtra("zipcode");
        address = intent.getStringExtra("address");
        //必備
        type = intent.getStringExtra("type");
        land = intent.getStringExtra("land");
        logistics = intent.getStringExtra("logistics");

    }

    private void initTextView() {
        addshipway_name.setText(name);
        addshipway_phone.setText(mp);
        addshipway_store.setText(sname);
    }

    private void initToolbar() {
        //Toolbar 建立
        toolbar = findViewById(R.id.include_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar_title = findViewById(R.id.include_toolbar_title);
        switch (logistics) {
            case "1":
                toolbar_title.setText("新增7-11門市");
                break;
            case "2":
                toolbar_title.setText("新增全家門市");
                break;
            case "3":
                toolbar_title.setText("新增萊爾富門市");
                break;
        }

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
            SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(getApplicationContext());
            Bundle bundle = data.getExtras();
            sid = bundle.getString("deliver");
            sname = bundle.getString("name");
            address = bundle.getString("address");
            addshipway_store.setText(sname);
            Map<String, String> address_data = db.getZipcodeByCityAndArea(address.substring(0, 3), address.substring(3, 5));
            city = address_data.get("city").replace("臺", "台");
            area = address_data.get("area");
            zipcode = address_data.get("zipcode");
            address = address.substring(city.length() + area.length());
            db.close();
        }

    }

}
