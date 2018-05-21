package com.test.tw.wrokproduct;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Map;

import library.GetJsonData.ShopCartJsonData;
import library.SQLiteDatabaseHandler;

public class AddStoreShipWayActivity extends AppCompatActivity implements TextWatcher {
    Toolbar toolbar;
    TextView toolbar_title;
    TextView addshipway_store;
    EditText addshipway_name, addshipway_phone;
    Button confirm;
    String mpcode = "+886", shit = "TW";
    String token, sno, plno, type, land, logistics;
    String sid, sname, city, area, zipcode, address;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalVariable gv = (GlobalVariable) getApplicationContext();
        token = gv.getToken();
        setContentView(R.layout.activity_add_store_shipway);
        intent = getIntent();
        sno = intent.getStringExtra("sno");
        plno = intent.getStringExtra("plno");
        type = intent.getStringExtra("type");
        land = intent.getStringExtra("land");
        logistics = intent.getStringExtra("logistics");

        initToolbar();
        Log.e("ADD", "\nsno:" + sno + "\nplno:" + plno + "\ntype:" + type + "\nland:" + land + "\nlogistics:" + logistics);
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
        addshipway_name.addTextChangedListener(this);
        addshipway_phone.addTextChangedListener(this);
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
                            final JSONObject json = new ShopCartJsonData().setMemberLogistics(token, sno, plno, type, land, logistics, addshipway_name.getText().toString(),
                                    mpcode, addshipway_phone.getText().toString(), sname, sid, shit, city, area, zipcode, address);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "新增完成", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                        }
                    }).start();

                }
            }
        });
    }

    private void initToolbar() {
        //Toolbar 建立
        toolbar = findViewById(R.id.addshipway_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar_title = findViewById(R.id.addshipway_title);
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

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        /*
        // TODO Auto-generated method stub
        int lines = editText.getLineCount();
        // 限制最大输入行数
        if (lines > MAXLINES) {
            String str = s.toString();
            int cursorStart = editText.getSelectionStart();
            int cursorEnd = editText.getSelectionEnd();
            if (cursorStart == cursorEnd && cursorStart < str.length() && cursorStart >= 1) {
                str = str.substring(0, cursorStart-1) + str.substring(cursorStart);
            } else {
                str = str.substring(0, s.length()-1);
            }
            // setText会触发afterTextChanged的递归
            editText.setText(str);
            // setSelection用的索引不能使用str.length()否则会越界
            editText.setSelection(editText.getText().length());

        }
 */
    }
}
