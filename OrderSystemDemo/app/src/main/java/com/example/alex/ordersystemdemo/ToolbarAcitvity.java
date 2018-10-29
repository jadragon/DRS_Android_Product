package com.example.alex.ordersystemdemo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.ordersystemdemo.API.Analyze.AnalyzeUtil;
import com.example.alex.ordersystemdemo.API.List.OrderApi;
import com.example.alex.ordersystemdemo.library.AsyncTaskUtils;
import com.example.alex.ordersystemdemo.library.IDataCallBack;

import org.json.JSONException;
import org.json.JSONObject;

public class ToolbarAcitvity extends AppCompatActivity {
    private boolean menuVisible = false;
    private int navigationType = 0;
    private int menuType = 0;
    private GlobalVariable gv;
    private String sid = "";

    public void setNavigationType(int navigationType) {
        this.navigationType = navigationType;
    }

    public void setMenutype(int menuType) {
        this.menuType = menuType;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    protected void initToolbar(String toolbarTitle, boolean navigationVisible, boolean menuVisible) {
        this.menuVisible = menuVisible;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        ((TextView) findViewById(R.id.toolbar_title)).setText(toolbarTitle);
        if (navigationVisible) {
            if (navigationType == 1) {
                toolbar.setNavigationIcon(R.drawable.logout);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ToolbarAcitvity.this);
                        builder.setTitle("確定要登出?").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences settings;
                                settings = getSharedPreferences("user_data", 0);
                                settings.edit()
                                        .putString("id", "")
                                        .putInt("type", 0)
                                        .commit();
                                gv.setToken("");
                                gv.setType(0);
                                dialog.dismiss();
                                startActivity(new Intent(ToolbarAcitvity.this, LoginActivity.class));
                                finish();
                            }
                        }).show();

                    }
                });
            } else {
                toolbar.setNavigationIcon(R.drawable.ic_action_name);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menuVisible) {
            gv = (GlobalVariable) getApplicationContext();
            MenuItem item = null;
            switch (menuType) {
                case 0:
                    getMenuInflater().inflate(R.menu.searchmenu, menu);
                    item = menu.findItem(R.id.menu_send);
                    item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            startActivity(new Intent(ToolbarAcitvity.this, StudentAcitvity.class));
                            return true;
                        }
                    });
                    break;
                case 1:
                    item = menu.add(1, 100, 1, "接單狀態");
                    item.setIcon(getResources().getDrawable(R.drawable.menu_accept));
                    item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                    item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                                @Override
                                public JSONObject onTasking(Void... params) {
                                    return new OrderApi().search_storeStatus(gv.getToken());
                                }

                                @Override
                                public void onTaskAfter(JSONObject jsonObject) {
                                    if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                        Dialog dialog = new Dialog(ToolbarAcitvity.this);
                                        dialog.setContentView(R.layout.dialog_delivery);
                                        final TextView text_delivery = dialog.findViewById(R.id.text_delivery);
                                        text_delivery.setText("開啟接單");
                                        Switch toggle = dialog.findViewById(R.id.switch_delivery);
                                        try {
                                            if (jsonObject.getString("Data").equals("0")) {
                                                text_delivery.setText("關閉接單");
                                                toggle.setChecked(false);
                                            } else {
                                                text_delivery.setText("開啟接單");
                                                toggle.setChecked(true);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                            @Override
                                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                if (isChecked) {
                                                    AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                                                        @Override
                                                        public JSONObject onTasking(Void... params) {
                                                            return new OrderApi().store_status(gv.getToken(), "1");
                                                        }

                                                        @Override
                                                        public void onTaskAfter(JSONObject jsonObject) {
                                                            if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                                                text_delivery.setText("開啟接單");
                                                            }
                                                            Toast.makeText(ToolbarAcitvity.this, AnalyzeUtil.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                                } else {
                                                    AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                                                        @Override
                                                        public JSONObject onTasking(Void... params) {
                                                            return new OrderApi().store_status(gv.getToken(), "0");
                                                        }

                                                        @Override
                                                        public void onTaskAfter(JSONObject jsonObject) {
                                                            if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                                                text_delivery.setText("關閉接單");
                                                            }
                                                            Toast.makeText(ToolbarAcitvity.this, AnalyzeUtil.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                                }
                                            }
                                        });
                                        dialog.show();
                                    }
                                    Toast.makeText(ToolbarAcitvity.this, AnalyzeUtil.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                                }
                            });

                            return true;
                        }
                    });
                    break;
                case 2:
                    item = menu.add(1, 100, 1, "接單狀態");
                    item.setIcon(getResources().getDrawable(R.drawable.menu_accept));
                    item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                    item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                                @Override
                                public JSONObject onTasking(Void... params) {
                                    return new OrderApi().search_storeStatus(sid);
                                }

                                @Override
                                public void onTaskAfter(JSONObject jsonObject) {
                                    if (AnalyzeUtil.checkSuccess(jsonObject)) {

                                    }
                                    Toast.makeText(ToolbarAcitvity.this, AnalyzeUtil.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                                }
                            });
                            return true;
                        }
                    });
                    break;
            }

        }
        /*
          menuItem = menu.findItem(R.id.menu_search);
        //通过MenuItem得到SearchView
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setQueryHint("輸入需要搜尋的店家");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String s) {
                ((InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(ToolbarAcitvity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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
                return false;
            }
        });
        */
        return super.onCreateOptionsMenu(menu);

    }

}
