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

import Util.AsyncTaskUtils;
import Util.IDataCallBack;
import adapter.recyclerview.ChangeTextRecyclerViewAdapter;
import adapter.recyclerview.KeyWordRecyclerViewAdapter;
import library.GetJsonData.SearchJsonData;
import library.Component.AutoNewLineLayoutManager;

public class SearchBarActivity extends AppCompatActivity {
    private RecyclerView search_bar_recyclerview;
    private KeyWordRecyclerViewAdapter keyWordRecyclerViewAdapter;
    private AutoNewLineLayoutManager autoNewLineLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private ChangeTextRecyclerViewAdapter changeTextRecyclerViewAdapter;
    private DividerItemDecoration decoration;
    private GlobalVariable gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bar);
        gv = ((GlobalVariable) getApplicationContext());
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
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public JSONObject onTasking(Void... params) {
                return new SearchJsonData().search(gv.getToken(), "");
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                keyWordRecyclerViewAdapter.setFilter(jsonObject);
            }
        });

        search_bar_searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(SearchBarActivity.this, SearchResultActivity.class);
                intent.putExtra("keyword", query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                if (!newText.equals("")) {
                    AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                        @Override
                        public JSONObject onTasking(Void... params) {
                            return new SearchJsonData().search(gv.getToken(), newText);
                        }

                        @Override
                        public void onTaskAfter(JSONObject jsonObject) {
                            search_bar_recyclerview.setAdapter(changeTextRecyclerViewAdapter);
                            changeTextRecyclerViewAdapter.setFilter(jsonObject);
                            search_bar_recyclerview.setLayoutManager(linearLayoutManager);
                        }
                    });

                } else {
                    search_bar_recyclerview.setAdapter(keyWordRecyclerViewAdapter);
                    search_bar_recyclerview.setLayoutManager(autoNewLineLayoutManager);
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
