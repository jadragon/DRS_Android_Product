package com.example.alex.designateddriving_driver;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListViewActivity extends ToolbarActivity {
    private ListView listView;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        type = getIntent().getStringExtra("type");
        switch (type) {
            case "account":
                initToolbar("我的帳號", true);
                initlistView1();
                break;
            case "about":
                initToolbar("關於我們", true);
                initlistView2();
                break;
        }

    }

    private void initlistView1() {
        listView = findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<>(this, R.layout.listview_item, new String[]{"個人資料", "常用位置", "修改密碼"}));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:

                        break;
                    case 1:

                        break;
                    case 2:
                        break;
                }
            }
        });
    }

    private void initlistView2() {
        listView = findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<>(this, R.layout.listview_item, new String[]{"關於我們", "服務條款", "服務宗旨", "隱私權政策", "常見問題", "聯繫我們", "全民代駕"}));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent intent = new Intent(ListViewActivity.this, SimpleWebviewActivity.class);
                        intent.putExtra("title", "關於我們");
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(ListViewActivity.this, SimpleWebviewActivity.class);
                        intent.putExtra("title", "服務條款");
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(ListViewActivity.this, SimpleWebviewActivity.class);
                        intent.putExtra("title", "服務宗旨");
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(ListViewActivity.this, SimpleWebviewActivity.class);
                        intent.putExtra("title", "隱私權政策");
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(ListViewActivity.this, SimpleWebviewActivity.class);
                        intent.putExtra("title", "常見問題");
                        startActivity(intent);
                        break;
                    case 5:
                        intent = new Intent(ListViewActivity.this, ContactMeActivity.class);
                        startActivity(intent);
                        break;
                    case 6:
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://alpha.ntgo.com.tw/index.php"));
                        startActivity(intent);
                        break;
                }
            }
        });
    }
}
