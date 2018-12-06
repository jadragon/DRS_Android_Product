package tw.com.lccnet.app.designateddriving;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Map;

import tw.com.lccnet.app.designateddriving.Utils.PreferenceUtils;
import tw.com.lccnet.app.designateddriving.db.SQLiteDatabaseHandler;

import static tw.com.lccnet.app.designateddriving.db.SQLiteDatabaseHandler.KEY_ADDRESS;
import static tw.com.lccnet.app.designateddriving.db.SQLiteDatabaseHandler.KEY_AREA;
import static tw.com.lccnet.app.designateddriving.db.SQLiteDatabaseHandler.KEY_CITY;
import static tw.com.lccnet.app.designateddriving.db.SQLiteDatabaseHandler.KEY_ZIPCODE;

public class SelectCityActivity extends ToolbarActivity implements View.OnClickListener {
    private TextView add_delivery_txt_city, add_delivery_txt_area;
    private EditText add_delivery_edit_zipcode;
    private Button confirm;
    private String prezipcode = "";
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);
        position = getIntent().getIntExtra(KEY_ADDRESS, 0);
        initToolbar("常見地址", true);
        initView();
        initListener();
        initData();
    }

    private void initListener() {
        add_delivery_edit_zipcode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(getApplicationContext());
                    Map<String, String> map = db.getCityAndAreaByZipcode(add_delivery_edit_zipcode.getText().toString());
                    if (!map.isEmpty()) {
                        add_delivery_txt_city.setText(map.get(KEY_CITY));
                        add_delivery_txt_area.setText(map.get(KEY_AREA));
                        prezipcode = add_delivery_edit_zipcode.getText().toString();
                    } else {
                        add_delivery_edit_zipcode.setText(prezipcode);
                    }
                    db.close();
                }
            }
        });

        add_delivery_txt_city.setOnClickListener(this);
        add_delivery_txt_area.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }

    private void initView() {
        add_delivery_edit_zipcode = findViewById(R.id.add_delivery_edit_zipcode);
        add_delivery_txt_city = findViewById(R.id.add_delivery_txt_city);
        add_delivery_txt_area = findViewById(R.id.add_delivery_txt_area);
        confirm = findViewById(R.id.add_delivery_btn_confirm);
    }

    private void initData() {
        add_delivery_edit_zipcode.setText(PreferenceUtils.getString(KEY_ZIPCODE + position, ""));
        add_delivery_txt_city.setText(PreferenceUtils.getString(KEY_CITY + position, "請選擇縣市"));
        add_delivery_txt_area.setText(PreferenceUtils.getString(KEY_AREA + position, "請選擇鄉鎮市區"));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (requestCode == 0) {
                add_delivery_txt_area.setText("請選擇鄉鎮市區");
                add_delivery_edit_zipcode.setText(null);
                add_delivery_txt_city.setText(data.getStringExtra(KEY_CITY));
            } else if (requestCode == 1) {
                add_delivery_edit_zipcode.setText(null);
                add_delivery_txt_area.setText(data.getStringExtra(KEY_AREA));
                prezipcode = data.getStringExtra(KEY_ZIPCODE);
                add_delivery_edit_zipcode.setText(prezipcode);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_delivery_edit_zipcode:
                break;
            case R.id.add_delivery_txt_city:
                startActivityForResult(new Intent(SelectCityActivity.this, CityListActivity.class),
                        0);
                break;
            case R.id.add_delivery_txt_area:
                if (!add_delivery_txt_city.getText().toString().equals("請選擇縣市")) {
                    Intent intent = new Intent(SelectCityActivity.this, CityListActivity.class);
                    intent.putExtra(KEY_CITY, add_delivery_txt_city.getText().toString());
                    startActivityForResult(intent, 1);
                }
                break;
            case R.id.add_delivery_btn_confirm:
                break;
        }
    }
}
