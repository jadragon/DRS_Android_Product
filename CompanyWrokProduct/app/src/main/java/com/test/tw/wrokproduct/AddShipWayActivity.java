package com.test.tw.wrokproduct;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Map;

import library.AnalyzeJSON.AnalyzeShopCart;
import library.GetJsonData.ShopCartJsonData;
import library.SQLiteDatabaseHandler;

public class AddShipWayActivity extends AppCompatActivity {
    TextView addshipway_store, addshipway_test;
    EditText addshipway_name, addshipway_phone;
    Button confirm;
    String mpcode="+886",shit="TW";
    String token, sno, plno, type, land, logistics;
    String sid,sname,city, area, zipcode, address;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalVariable gv = (GlobalVariable) getApplicationContext();
        token = gv.getToken();
        setContentView(R.layout.activity_add_shipway);
        intent = getIntent();
        sno = intent.getStringExtra("sno");
        plno = intent.getStringExtra("plno");
        type = intent.getStringExtra("type");
        land = intent.getStringExtra("land");
        logistics = intent.getStringExtra("logistics");
        Log.e("ADD", "\nsno:" + sno + "\nplno:" + plno + "\ntype:" + type + "\nland:" + land + "\nlogistics:" + logistics);
        addshipway_store = findViewById(R.id.addshipway_store);
        addshipway_test = findViewById(R.id.addshipway_test);
        addshipway_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddShipWayActivity.this, StoreWebViewActivity.class);
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
                    Toast.makeText(AddShipWayActivity.this, "資料填寫不完整", Toast.LENGTH_SHORT).show();
                }else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final JSONObject json= new ShopCartJsonData().setMemberLogistics(token,sno,plno,type,land,logistics,addshipway_name.getText().toString(),
                                    mpcode,addshipway_phone.getText().toString(),sname,sid,shit,city,area,zipcode,address);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e("Success", AnalyzeShopCart.checkSuccess(json)+"");
                                }
                            });
                        }
                    }).start();

                }
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
            city = address_data.get("city");
            area = address_data.get("area");
            zipcode = address_data.get("zipcode");
            address = address.substring(city.length() + area.length());
            addshipway_test.setText(city + "\n" + zipcode + " " + area + "\n" + address);
            db.close();
        }

    }
}
