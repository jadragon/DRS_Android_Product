package tw.com.lccnet.app.designateddriving;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import tw.com.lccnet.app.designateddriving.db.SQLiteDatabaseHandler;

public class CityListActivity extends ToolbarActivity implements AdapterView.OnItemClickListener {
    private ListView listView;
    private String[] array;
    private SQLiteDatabaseHandler db;
    private String city, area, zipcode;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);
        intent = getIntent();
        city = intent.getStringExtra("city");
        db = new SQLiteDatabaseHandler(this);
        if (city != null) {
            initToolbar("鄉鎮市", true);
            array = db.getAreaByCity(city).toArray(new String[0]);
        } else {
            initToolbar("縣市", true);
            array = db.getAllCity().toArray(new String[0]);
        }
        initListView();
    }

    private void initListView() {
        listView = findViewById(R.id.listview);
        ListAdapter adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                array);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (city != null) {
            area = array[position];
            zipcode = db.getZipcodeByCityAndArea(city, area).get("zipcode");
            intent.putExtra("area", area);
            intent.putExtra("zipcode", zipcode);
            setResult(1, intent);
        } else {
            intent.putExtra("city", array[position]);
            setResult(0, intent);
        }
        this.finish();
    }
}
