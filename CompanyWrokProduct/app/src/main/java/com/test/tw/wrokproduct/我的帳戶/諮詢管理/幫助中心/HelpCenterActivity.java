package com.test.tw.wrokproduct.我的帳戶.諮詢管理.幫助中心;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.test.tw.wrokproduct.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import adapter.recyclerview.HelpCenterRecyclerViewAdapter;
import library.AnalyzeJSON.AnalyzeHelpCenter;
import library.GetJsonData.HelpCenterJsonData;

public class HelpCenterActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    SearchView searchView;
    HelpCenterRecyclerViewAdapter adapter;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_center);
        initToolbar();
        recyclerView = findViewById(R.id.help_center_recycleview);

        new Thread(new Runnable() {
            @Override
            public void run() {
                final JSONObject json = new HelpCenterJsonData().getCategory();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setHasFixedSize(true);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
                        decoration.setDrawable(getResources().getDrawable(R.drawable.divider_10dp_invisble));
                        recyclerView.addItemDecoration(decoration);
                        recyclerView.setLayoutManager(layoutManager);
                        adapter = new HelpCenterRecyclerViewAdapter(getApplicationContext(), json);
                        recyclerView.setAdapter(adapter);
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

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final JSONObject json = new HelpCenterJsonData().searchCategory(s);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.setHeader(AnalyzeHelpCenter.getSearchCategory(json));
                                adapter.notifyDataSetChanged();
                                Log.e("WWWW", AnalyzeHelpCenter.getSearchCategory(json) + "");
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
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void initHeader(ArrayList<Map<String, String>> list) {
        view = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.table_view_header, recyclerView, false);
        //header 背景
        LinearLayout layout = view.findViewById(R.id.table_view_header_layout);
        layout.setBackgroundColor(getResources().getColor(R.color.gray));
        //header title
        TextView textView = view.findViewById(R.id.table_view_header_text);
        textView.setText("搜尋項目");
        //item layout
        layout = view.findViewById(R.id.table_view_item_layout);
        View item;
        for (Map<String, String> map : list) {
            item = LayoutInflater.from(getApplicationContext()).inflate(
                    R.layout.table_view_item, recyclerView, false);
            ((TextView) item.findViewById(R.id.table_view_item_text)).setText(map.get("title"));
            layout.addView(item);
        }
    }
}
