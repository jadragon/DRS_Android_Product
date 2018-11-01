package com.example.alex.lotteryapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.alex.lotteryapp.library.AsyncTaskUtils;
import com.example.alex.lotteryapp.library.IDataCallBack;
import com.example.alex.lotteryapp.library.SQLiteDatabaseHandler;


public class NewItemActivity extends ToolbarActivity {
    SQLiteDatabaseHandler db;
    EditText gift;
    Spinner type;
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

                    AsyncTaskUtils.doAsync(new IDataCallBack<Long>() {
                        @Override
                        public Long onTasking(Void... params) {
                            return db.addItem( type.getSelectedItemPosition(), gift.getText().toString());
                        }

                        @Override
                        public void onTaskAfter(Long count) {
                            if (count > -1) {
                                Toast.makeText(NewItemActivity.this, "寫入成功", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }

    private void initView() {
        gift = findViewById(R.id.newitem_ed_gift);
        type = findViewById(R.id.newitem_sp_type);
        type.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.type_list)));
        confirm = findViewById(R.id.newitem_btn_confirm);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
