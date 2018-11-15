package com.example.alex.lotteryapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alex.lotteryapp.library.AsyncTaskUtils;
import com.example.alex.lotteryapp.library.IDataCallBack;
import com.example.alex.lotteryapp.library.SQLiteDatabaseHandler;


public class NewItemActivity extends ToolbarActivity {
    SQLiteDatabaseHandler db;
    EditText gift, number;
    EditText type;
    Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
        initToolbar(true, "新增項目");
        db = new SQLiteDatabaseHandler(this);
        initView();
        initListener();
    }

    private void initListener() {
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(TextUtils.isEmpty(gift.getText()))) {

                    AsyncTaskUtils.doAsync(new IDataCallBack<Void>() {
                        @Override
                        public Void onTasking(Void... params) {
                            db.addItems(type.getText().toString(), gift.getText().toString(), Integer.parseInt(number.getText().toString()));
                            return null;
                        }

                        @Override
                        public void onTaskAfter(Void aVoid) {
                            Toast.makeText(NewItemActivity.this, "寫入成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            }
        });
    }

    private void initView() {
        gift = findViewById(R.id.newitem_ed_gift);
        type = findViewById(R.id.newitem_sp_type);
        number = findViewById(R.id.newitem_ed_number);
        confirm = findViewById(R.id.newitem_btn_confirm);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
