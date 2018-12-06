package tw.com.lccnet.app.designateddriving;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import tw.com.lccnet.app.designateddriving.Utils.PreferenceUtils;
import tw.com.lccnet.app.designateddriving.db.SQLiteDatabaseHandler;

import static tw.com.lccnet.app.designateddriving.db.SQLiteDatabaseHandler.KEY_AREA;
import static tw.com.lccnet.app.designateddriving.db.SQLiteDatabaseHandler.KEY_CITY;
import static tw.com.lccnet.app.designateddriving.db.SQLiteDatabaseHandler.KEY_ZIPCODE;
import static tw.com.lccnet.app.designateddriving.db.SQLiteDatabaseHandler.KEY_ADDRESS;

public class CommonLocationActivity extends ToolbarActivity implements View.OnClickListener {
    private SQLiteDatabaseHandler db;
    private View item1, item2, item3;
    private TextView address1, address2, address3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_location);
        db = new SQLiteDatabaseHandler(this);
        initToolbar("常用地點", true);
        initView();
        initListener();
        initData();
    }

    private void initListener() {
        item1.setOnClickListener(this);
        item2.setOnClickListener(this);
        item3.setOnClickListener(this);
    }

    private void initData() {
        String address = PreferenceUtils.getString(KEY_ZIPCODE + 1, "") +
                PreferenceUtils.getString(KEY_CITY + 1, "") +
                PreferenceUtils.getString(KEY_AREA + 1, "") +
                PreferenceUtils.getString(KEY_ADDRESS + 1, "");
        address1.setText(address);
        address = PreferenceUtils.getString(KEY_ZIPCODE + 2, "") +
                PreferenceUtils.getString(KEY_CITY + 2, "") +
                PreferenceUtils.getString(KEY_AREA + 2, "") +
                PreferenceUtils.getString(KEY_ADDRESS + 2, "");
        address2.setText(address);
        address = PreferenceUtils.getString(KEY_ZIPCODE + 3, "") +
                PreferenceUtils.getString(KEY_CITY + 3, "") +
                PreferenceUtils.getString(KEY_AREA + 3, "") +
                PreferenceUtils.getString(KEY_ADDRESS + 3, "");
        address3.setText(address);
    }

    private void initView() {
        item1 = findViewById(R.id.item1);
        item2 = findViewById(R.id.item2);
        item3 = findViewById(R.id.item3);
        address1 = findViewById(R.id.address1);
        address2 = findViewById(R.id.address2);
        address3 = findViewById(R.id.address3);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(CommonLocationActivity.this, SelectCityActivity.class);
        switch (v.getId()) {
            case R.id.item1:
                intent.putExtra("position", 1);
                startActivity(intent);
                break;
            case R.id.item2:
                intent.putExtra("position", 2);
                startActivity(intent);
                break;
            case R.id.item3:
                intent.putExtra("position", 3);
                startActivity(intent);
                break;
        }

    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}
