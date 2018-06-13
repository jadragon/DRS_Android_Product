package com.test.tw.wrokproduct.我的帳戶.諮詢管理.幫助中心;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.test.tw.wrokproduct.R;

import org.json.JSONObject;

import adapter.listview.TableViewAdapter;
import library.AnalyzeJSON.AnalyzeHelpCenter;
import library.GetJsonData.HelpCenterJsonData;

public class HelpCenterActivity extends AppCompatActivity {
    Toolbar toolbar;
    ListView listView;
    SearchView searchView;
    TableViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_center);
        initToolbar();
        listView = findViewById(R.id.help_center_listview);

        new Thread(new Runnable() {
            @Override
            public void run() {
                final JSONObject json = new HelpCenterJsonData().getCategory();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new TableViewAdapter(getApplicationContext(), json);
                        listView.setAdapter(adapter);
                    }
                });
            }
        }).start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchmenu, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        //通过MenuItem得到SearchView
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("輸入常見問題");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String s) {
                adapter.setHeader(initHeader());
                adapter.notifyDataSetChanged();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                       final JSONObject json= new HelpCenterJsonData().searchCategory(s);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("WWWW", AnalyzeHelpCenter.getSearchCategory(json)+"");
                            }
                        });
                    }
                }).start();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                adapter.removeHeader();
                adapter.notifyDataSetChanged();
                Toast.makeText(HelpCenterActivity.this, "close", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void initToolbar() {
        //Toolbar 建立
        toolbar = findViewById(R.id.include_toolbar);
        ((TextView) findViewById(R.id.include_toolbar_title)).setText("幫助中心");
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public View initHeader() {
        View view = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.table_view_header, listView, false);
        //header 背景
        LinearLayout layout = view.findViewById(R.id.table_view_header_layout);
        layout.setBackgroundColor(getResources().getColor(R.color.gray));
        //header title
        TextView textView = view.findViewById(R.id.table_view_header_text);
        textView.setText("搜尋項目");
        //item layout
        layout = view.findViewById(R.id.table_view_item_layout);

        View item = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.table_view_item, listView, false);
        layout.addView(item);
        item = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.table_view_item, listView, false);
        layout.addView(item);
        item = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.table_view_item, listView, false);
        layout.addView(item);
        return view;
    }
}
