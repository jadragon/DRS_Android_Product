package com.company.alex.ordersystemdemo;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.company.alex.ordersystemdemo.API.Analyze.AnalyzeUtil;
import com.company.alex.ordersystemdemo.API.List.OrderApi;
import com.company.alex.ordersystemdemo.API.List.RestaurantApi;
import com.company.alex.ordersystemdemo.RecyclerViewAdapter.MenuListAdapter;
import com.company.alex.ordersystemdemo.library.AsyncTaskUtils;
import com.company.alex.ordersystemdemo.library.IDataCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class StoreDetailActivity extends ToolbarAcitvity {
    private RecyclerView recyclerView;
    private MenuListAdapter menuListAdapter;
    private String s_id, name;
    private GlobalVariable gv;
    private SharedPreferences settings;
    private Button send;
    private ProgressDialog progressDialog;

    public void saveData(String name, String phone, String address) {
        settings = getSharedPreferences("user_data", 0);
        settings.edit()
                .putString("name", name)
                .putString("phone", phone)
                .putString("address", address)
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);
        gv = (GlobalVariable) getApplicationContext();
        s_id = getIntent().getStringExtra("s_id");
        name = getIntent().getStringExtra("name");
        initToolbar(name, true, true);
        initRecyclerView();
        initButton();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchmenu, menu);
        MenuItem item = menu.findItem(R.id.menu_search);
        //通过MenuItem得到SearchView
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setQueryHint("輸入要搜尋的餐點");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String s) {
                int position = menuListAdapter.searchItem(s);
                if (position != -1) {
                    ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(position, -recyclerView.getPaddingTop());
                } else {
                    Toast.makeText(StoreDetailActivity.this, "查無餐點", Toast.LENGTH_SHORT).show();
                }
                ((InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(StoreDetailActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.equals("")) {
                    menuListAdapter.searchItem(s);
                }
                return false;
            }
        });

        item = menu.findItem(R.id.menu_accept);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                checkOrder();
                return true;
            }
        });
        return true;
    }

    private void initButton() {
        send = findViewById(R.id.store_detail_send);
        checkOrder();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Map<String, String> map = menuListAdapter.getStudentInfo();
                if (!(map.get("name").equals("") || map.get("phone").equals("") || map.get("address").equals(""))) {
                    if (map.get("phone").matches("09[0-9]{8}")) {
                        if (menuListAdapter.getTotalMoney() >= 50) {
                            AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                                @Override
                                public JSONObject onTasking(Void... params) {
                                    return new OrderApi().checkout(gv.getToken(), s_id, map.get("name"), map.get("phone"), map.get("note"), map.get("address"), menuListAdapter.getContent(), menuListAdapter.getTotalMoney() + "");
                                }

                                @Override
                                public void onTaskAfter(JSONObject jsonObject) {
                                    if (jsonObject != null) {
                                        if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                            saveData(map.get("name"), map.get("phone"), map.get("address"));
                                            AlertDialog dialog = new AlertDialog.Builder(StoreDetailActivity.this).setTitle("貼心提醒").setMessage("稍後將通知您餐點送達時間").setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    finish();
                                                }
                                            }).create();
                                            dialog.setCanceledOnTouchOutside(false);
                                            dialog.show();
                                        } else {
                                            Toast.makeText(StoreDetailActivity.this, AnalyzeUtil.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(StoreDetailActivity.this, "連線異常", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(StoreDetailActivity.this, "最低消費為50元", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(StoreDetailActivity.this, "請輸入正確的手機號碼", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(StoreDetailActivity.this, "請確認資料是否填寫完整", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void initRecyclerView() {
        findViewById(R.id.include_swipe_refresh).setEnabled(false);
        recyclerView = findViewById(R.id.include_recyclerview);
        menuListAdapter = new MenuListAdapter(this, null);
        recyclerView.setAdapter(menuListAdapter);
        progressDialog = ProgressDialog.show(StoreDetailActivity.this,
                "讀取中", "請稍後...", true);
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public JSONObject onTasking(Void... params) {
                return new RestaurantApi().menu(s_id);
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                progressDialog.dismiss();
                if (jsonObject != null) {
                    if (AnalyzeUtil.checkSuccess(jsonObject)) {
                        menuListAdapter.setFilter(jsonObject);
                    }
                } else {
                    Toast.makeText(StoreDetailActivity.this, "連線異常", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkOrder() {
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public JSONObject onTasking(Void... params) {
                return new OrderApi().search_storeStatus(s_id);
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                if (jsonObject != null) {
                    if (AnalyzeUtil.checkSuccess(jsonObject)) {
                        try {
                            if (jsonObject.getString("Data").equals("1")) {
                                send.setEnabled(true);
                                send.setBackgroundResource(R.color.green);
                            } else {
                                send.setEnabled(false);
                                send.setBackgroundResource(R.color.gray2);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(StoreDetailActivity.this, AnalyzeUtil.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(StoreDetailActivity.this, "連線異常", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
