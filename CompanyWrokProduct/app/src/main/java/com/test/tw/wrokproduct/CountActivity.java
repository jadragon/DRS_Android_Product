package com.test.tw.wrokproduct;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import Util.AsyncTaskUtils;
import Util.IDataCallBack;
import adapter.recyclerview.ReCountRecyclerViewAdapter;
import library.Component.ToastMessageDialog;
import library.GetJsonData.ReCountJsonData;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class CountActivity extends AppCompatActivity {
    private  RecyclerView recyclerView;
    private  ReCountRecyclerViewAdapter countRecyclerViewAdapter;
    private  Toolbar toolbar;
    private  TextView toolbar_title;
    private  GlobalVariable gv;
    private  Button count_gotobuy;
    private int count_type;
    private ToastMessageDialog toastMessageDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);
        gv = ((GlobalVariable) getApplicationContext());
        count_type = getIntent().getIntExtra("count_type", 0);

        initToolbar();
        initToastMessage();
        initRecycleView();

    }

    private void initToastMessage() {
        toastMessageDialog = new ToastMessageDialog(this);
        toastMessageDialog.setTitleText("確定要開始結帳?");
        toastMessageDialog.setCheckListener(false, new ToastMessageDialog.ClickListener() {
            @Override
            public void ItemClicked(Dialog dialog, View view, String note) {
                if (countRecyclerViewAdapter.checkShipwayAndPayWay()) {

                    AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                        @Override
                        public JSONObject onTasking(Void... params) {
                            return new ReCountJsonData().setVat(count_type, gv.getToken(), countRecyclerViewAdapter.getInvoice(), countRecyclerViewAdapter.getCtitle(), countRecyclerViewAdapter.getVat());
                        }

                        @Override
                        public void onTaskAfter(JSONObject jsonObject) {
                            Intent intent = new Intent(CountActivity.this, GoldFlowActivity.class);
                            intent.putExtra("count_type", count_type);
                            startActivity(intent);
                            finish();
                        }
                    });

                } else {
                    new ToastMessageDialog(CountActivity.this, "請填寫運送方式及付款方式").confirm();
                }
                dialog.dismiss();
            }
        });
    }

    private void initRecycleView() {
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public JSONObject onTasking(Void... params) {
                return new ReCountJsonData().getCheckout(count_type, gv.getToken());
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                recyclerView = findViewById(R.id.count_review);
                recyclerView.setHasFixedSize(true);
                countRecyclerViewAdapter = new ReCountRecyclerViewAdapter(CountActivity.this, jsonObject, count_type);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(countRecyclerViewAdapter);
                DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL);
                decoration.setDrawable(getResources().getDrawable(R.drawable.decoration_line));
                recyclerView.addItemDecoration(decoration);
                //IOS like
                OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
                //第三方 彈跳效果
                //  ElasticityHelper.setUpOverScroll(recyclerView, ORIENTATION.VERTICAL);
                initButton();
            }
        });


    }

    private void initButton() {
        count_gotobuy = findViewById(R.id.count_gotobuy);
        count_gotobuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toastMessageDialog.showCheck();
            }
        });
    }

    private void initToolbar() {
        //Toolbar 建立
        toolbar = findViewById(R.id.include_toolbar);
        toolbar_title = findViewById(R.id.include_toolbar_title);
        toolbar_title.setText("結帳");
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public JSONObject onTasking(Void... params) {
                return new ReCountJsonData().getCheckout(count_type, gv.getToken());
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                countRecyclerViewAdapter.setFilter(jsonObject);
            }
        });
    }

}
