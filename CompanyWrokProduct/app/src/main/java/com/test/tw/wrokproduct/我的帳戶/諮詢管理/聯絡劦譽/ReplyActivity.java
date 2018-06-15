package com.test.tw.wrokproduct.我的帳戶.諮詢管理.聯絡劦譽;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.R;

import org.json.JSONObject;

import adapter.recyclerview.ReplyRecyclerAdapter;
import library.GetJsonData.ContactJsonData;

public class ReplyActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    ReplyRecyclerAdapter adapter;
    String token, msno;
    JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        token = ((GlobalVariable) getApplicationContext()).getToken();
        msno = getIntent().getStringExtra("msno");
        initToolbar();
        initRecycleView();
    }

    private void initRecycleView() {
        recyclerView = findViewById(R.id.reply_recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        new Thread(new Runnable() {
            @Override
            public void run() {
                json = new ContactJsonData().getContactCont(token, msno);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new ReplyRecyclerAdapter(ReplyActivity.this, json);
                        adapter.setMsno(msno);
                        recyclerView.setAdapter(adapter);
                    }
                });
            }
        }).start();


    }

    private void initToolbar() {
        //Toolbar 建立
        toolbar = findViewById(R.id.include_toolbar);
        ((TextView) findViewById(R.id.include_toolbar_title)).setText("回覆");
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

}
