package com.test.tw.wrokproduct.我的帳戶.諮詢管理.聯絡劦譽;

import android.content.Intent;
import android.graphics.BitmapFactory;
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

import Util.AsyncTaskUtils;
import Util.IDataCallBack;
import adapter.recyclerview.ReplyRecyclerAdapter;
import library.GetJsonData.ContactJsonData;

public class ReplyActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ReplyRecyclerAdapter adapter;
    private String msno;
    private GlobalVariable gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        gv = ((GlobalVariable) getApplicationContext());
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
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public JSONObject onTasking(Void... params) {
                return new ContactJsonData().getContactCont(gv.getToken(), msno);
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                adapter = new ReplyRecyclerAdapter(ReplyActivity.this, jsonObject);
                adapter.setMsno(msno);
                recyclerView.setAdapter(adapter);
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                if (data != null) {
                    byte[] bis = data.getByteArrayExtra("picture");
                    adapter.setPhotos(BitmapFactory.decodeByteArray(bis, 0, bis.length), 0);
                    //  write_mail_photo1.setImageBitmap(BitmapFactory.decodeByteArray(bis, 0, bis.length));
                }
                break;
            case 200:
                if (data != null) {
                    byte[] bis = data.getByteArrayExtra("picture");
                    adapter.setPhotos(BitmapFactory.decodeByteArray(bis, 0, bis.length), 1);
                    //    write_mail_photo2.setImageBitmap(BitmapFactory.decodeByteArray(bis, 0, bis.length));
                }
                break;
            case 300:
                if (data != null) {
                    byte[] bis = data.getByteArrayExtra("picture");
                    adapter.setPhotos(BitmapFactory.decodeByteArray(bis, 0, bis.length), 2);
                    //   write_mail_photo3.setImageBitmap(BitmapFactory.decodeByteArray(bis, 0, bis.length));
                }
                break;
            case 400:
                if (data != null) {
                    byte[] bis = data.getByteArrayExtra("picture");
                    adapter.setPhotos(BitmapFactory.decodeByteArray(bis, 0, bis.length), 3);
                    //write_mail_photo4.setImageBitmap(BitmapFactory.decodeByteArray(bis, 0, bis.length));
                }
                break;
            case 500:
                if (data != null) {
                    byte[] bis = data.getByteArrayExtra("picture");
                    adapter.setPhotos(BitmapFactory.decodeByteArray(bis, 0, bis.length), 4);
                    // write_mail_photo5.setImageBitmap(BitmapFactory.decodeByteArray(bis, 0, bis.length));
                }
                break;
            case 600:
                if (data != null) {
                    byte[] bis = data.getByteArrayExtra("picture");
                    adapter.setPhotos(BitmapFactory.decodeByteArray(bis, 0, bis.length), 5);
                    //write_mail_photo6.setImageBitmap(BitmapFactory.decodeByteArray(bis, 0, bis.length));
                }
                break;
        }

    }
}
