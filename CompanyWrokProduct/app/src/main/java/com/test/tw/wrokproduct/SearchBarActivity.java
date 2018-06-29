package com.test.tw.wrokproduct;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;

import org.json.JSONObject;

import adapter.recyclerview.ChangeTextRecyclerViewAdapter;
import adapter.recyclerview.KeyWordRecyclerViewAdapter;
import library.GetJsonData.SearchJsonData;
import library.Component.AutoNewLineLayoutManager;

public class SearchBarActivity extends AppCompatActivity {
    RecyclerView search_bar_recyclerview;
    KeyWordRecyclerViewAdapter keyWordRecyclerViewAdapter;
    JSONObject json;
    AutoNewLineLayoutManager autoNewLineLayoutManager;
    LinearLayoutManager linearLayoutManager;
    ChangeTextRecyclerViewAdapter changeTextRecyclerViewAdapter;
    DividerItemDecoration decoration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bar);
        search_bar_recyclerview = findViewById(R.id.search_bar_recyclerview);
        decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.decoration_line));
        changeTextRecyclerViewAdapter = new ChangeTextRecyclerViewAdapter(this, null);
        keyWordRecyclerViewAdapter = new KeyWordRecyclerViewAdapter(this, null);
        search_bar_recyclerview.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        autoNewLineLayoutManager = new AutoNewLineLayoutManager(this);
        autoNewLineLayoutManager.setDivider(10);
        autoNewLineLayoutManager.setAloneViewType(KeyWordRecyclerViewAdapter.TYPE_HEADER);
        search_bar_recyclerview.setLayoutManager(autoNewLineLayoutManager);
        search_bar_recyclerview.setAdapter(keyWordRecyclerViewAdapter);
        initSearchbar();
    }

    private void initSearchbar() {
        SearchView search_bar_searchview = findViewById(R.id.search_bar_searchview);
        search_bar_searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent=new Intent(SearchBarActivity.this,SearchResultActivity.class);
                intent.putExtra("keyword",query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                if (!newText.equals("")) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            json = new SearchJsonData().search(newText);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    search_bar_recyclerview.setAdapter(changeTextRecyclerViewAdapter);
                                    changeTextRecyclerViewAdapter.setFilter(json);
                                    search_bar_recyclerview.setLayoutManager(linearLayoutManager);
                                    search_bar_recyclerview.addItemDecoration(decoration);
                                }
                            });

                        }
                    }).start();

                } else {
                    search_bar_recyclerview.setAdapter(keyWordRecyclerViewAdapter);
                    search_bar_recyclerview.setLayoutManager(autoNewLineLayoutManager);
                    search_bar_recyclerview.removeItemDecoration(decoration);
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    public void cancel(View view) {
        finish();
        overridePendingTransition(0, 0);
    }

}
