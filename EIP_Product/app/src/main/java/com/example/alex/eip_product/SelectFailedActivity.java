package com.example.alex.eip_product;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.alex.eip_product.adapter.KeyWordRecyclerViewAdapter;

import Component.AutoNewLineLayoutManager;
import Utils.CommonUtil;

public class SelectFailedActivity extends AppCompatActivity {
    private  KeyWordRecyclerViewAdapter keyWordRecyclerViewAdapter;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_failed);
        intent = getIntent();
        RecyclerView recyclerView = findViewById(R.id.select_failed_recyclerview);
        boolean type1 = intent.getBooleanExtra("type1", false);
        boolean type2 = intent.getBooleanExtra("type2", false);
        boolean type3 = intent.getBooleanExtra("type3", false);
        boolean type4 = intent.getBooleanExtra("type4", false);

        keyWordRecyclerViewAdapter = new KeyWordRecyclerViewAdapter(this, type1, type2, type3, type4, true, true);
        AutoNewLineLayoutManager autoNewLineLayoutManager = new AutoNewLineLayoutManager(this);
        autoNewLineLayoutManager.setDivider(20);
        autoNewLineLayoutManager.setAloneViewType(KeyWordRecyclerViewAdapter.TYPE_HEADER);
        recyclerView.setLayoutManager(autoNewLineLayoutManager);
        recyclerView.setAdapter(keyWordRecyclerViewAdapter);

        initButton();
    }

    private void initButton() {
        Button select_failed_confirm = findViewById(R.id.select_failed_confirm);
        select_failed_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(keyWordRecyclerViewAdapter.getSelectFailNums().size()>0) {
                    intent.putExtra("ReasonCode", keyWordRecyclerViewAdapter.getSelectFailNums());
                    intent.putExtra("ReasonDescr", keyWordRecyclerViewAdapter.getSelectFailDescription());
                    setResult(110, intent);
                    finish();
                }else {
                    CommonUtil.toastErrorMessage(SelectFailedActivity.this, getResources().getString(R.string.warning), getResources().getString(R.string.unfill_recheckdate3));
                }
            }
        });
        Button select_failed_cancel = findViewById(R.id.select_failed_cancel);
        select_failed_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
