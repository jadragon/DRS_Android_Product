package com.company.alex.ordersystemdemo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.company.alex.ordersystemdemo.API.Analyze.AnalyzeUtil;
import com.company.alex.ordersystemdemo.API.List.OrderApi;
import com.company.alex.ordersystemdemo.library.AsyncTaskUtils;
import com.company.alex.ordersystemdemo.library.IDataCallBack;

import org.json.JSONException;
import org.json.JSONObject;

public class ToolbarAcitvity extends AppCompatActivity {
    private boolean menuVisible = false;
    private int navigationType = 0;
    private int menuType = 0;
    private GlobalVariable gv;
    private boolean toggleOn = false;
    private boolean boardOn = false;

    public void setNavigationType(int navigationType) {
        this.navigationType = navigationType;
    }

    public void setMenutype(int menuType) {
        this.menuType = menuType;
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
            switch (menuType) {
                case 0:
                    addMenu_board(menu, 1, 100, 1);
                    addMenu_orderlist(menu, 1, 200, 2);
                    break;
                case 1:
                    addMenu_board(menu, 1, 100, 1);
                    addMenu_accept(menu, 1, 200, 2);
                    break;
                case 2:
                    addMenu_board(menu, 1, 100, 1);
                    break;
            }

        }
        return super.onCreateOptionsMenu(menu);
    }

    private void addMenu_board(Menu menu, int groupId, int itemId, int order) {
        MenuItem item = menu.add(groupId, itemId, order, "公佈欄");
        item.setIcon(getResources().getDrawable(R.drawable.menu_board));
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (!boardOn) {
                    boardOn = true;
                    AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                        @Override
                        public JSONObject onTasking(Void... params) {
                            return new OrderApi().board();
                        }

                        @Override
                        public void onTaskAfter(JSONObject jsonObject) {
                            if (jsonObject != null) {
                                if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                    try {
                                        Dialog dialog = new Dialog(ToolbarAcitvity.this);
                                        View view = LayoutInflater.from(ToolbarAcitvity.this).inflate(R.layout.dialog_board, null);
                                        TextView textView = view.findViewWithTag("text");
                                        textView.setText(jsonObject.getString("Data"));
                                        dialog.setContentView(view);
                                        dialog.show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            } else {
                                Toast.makeText(ToolbarAcitvity.this, "連線異常", Toast.LENGTH_SHORT).show();
                            }
                            boardOn = false;
                        }

                    });
                }
                return true;

            }
        });
    }

    private void addMenu_orderlist(Menu menu, int groupId, int itemId, int order) {
        MenuItem item = menu.add(groupId, itemId, order, "訂單資訊");
        item.setIcon(getResources().getDrawable(R.drawable.menu_orderlist));
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(ToolbarAcitvity.this, StudentAcitvity.class));
                return true;
            }
        });
    }

    private void addMenu_accept(Menu menu, int groupId, int itemId, int order) {
        MenuItem item = menu.add(groupId, itemId, order, "接單狀態");
        item.setIcon(getResources().getDrawable(R.drawable.menu_accept));
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (!toggleOn) {
                    AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                        @Override
                        public JSONObject onTasking(Void... params) {
                            return new OrderApi().search_storeStatus(gv.getToken());
                        }

                        @Override
                        public void onTaskAfter(JSONObject jsonObject) {
                            if (jsonObject != null) {
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
                                                        if (jsonObject != null) {
                                                            if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                                                text_delivery.setText("開啟接單");
                                                            }
                                                            Toast.makeText(ToolbarAcitvity.this, AnalyzeUtil.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(ToolbarAcitvity.this, "連線異常", Toast.LENGTH_SHORT).show();
                                                        }
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
                                                        if (jsonObject != null) {
                                                            if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                                                text_delivery.setText("關閉接單");
                                                            }
                                                            Toast.makeText(ToolbarAcitvity.this, AnalyzeUtil.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                                                        }else {
                                                            Toast.makeText(ToolbarAcitvity.this, "連線異常", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                });
                                            }
                                        }
                                    });
                                    dialog.show();
                                    toggleOn = false;
                                }
                                Toast.makeText(ToolbarAcitvity.this, AnalyzeUtil.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ToolbarAcitvity.this, "連線異常", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                return true;

            }

        });
    }
}
