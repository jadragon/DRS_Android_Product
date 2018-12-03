package tw.com.lccnet.app.designateddriving;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MyAccountActivity extends ToolbarActivity {
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        initToolbar("我的帳號", true);
        initlistView();
    }

    private void initlistView() {
        listView = findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[]{"個人資料","常用位置"}));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(MyAccountActivity.this, PersonalActivity.class));
                        break;
                    case 1:
                        break;
                }
            }
        });
    }
}
