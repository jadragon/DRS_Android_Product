package com.example.alex.lotteryapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.alex.lotteryapp.library.SQLiteDatabaseHandler;

public class AllListActivity_COPY extends ToolbarActivity {
    Button btn_new, btn_ok;
    ListView listView;
    SQLiteDatabaseHandler db;
    CursorAdapter adapter;
    //edit item
    EditText ed_gift, ed_winner;
    Spinner sp_type;
    //show item
    TextView type, gift, winner;
    Cursor cursor;
    String[] array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_list);
        array = getResources().getStringArray(R.array.type_list);
        initToolbar(true, "項目列表");
        db = new SQLiteDatabaseHandler(this);
        cursor = db.getUserInfo();
        initView();
        initListener();
    }

    private void initView() {
        //btn
        btn_new = findViewById(R.id.alllist_btn_export);
        btn_ok = findViewById(R.id.alllist_btn_reset);
        //list
        listView = findViewById(R.id.alllist_listview);
        adapter = new CursorAdapter(this, cursor) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
                View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_list, null);
                return view;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                type = view.findViewById(R.id.item_list_type);
                gift = view.findViewById(R.id.item_list_gift);
              //  winner = view.findViewById(R.id.item_list_winner);
                initItemData(cursor, 0);
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AllListActivity_COPY.this);
                View layout = LayoutInflater.from(getBaseContext()).inflate(R.layout.include_new_item, null);
                initItemView(layout);
                final Cursor cursor = db.getUserInfo();
                cursor.moveToPosition(position);
                final int c_id = cursor.getInt(0);
                initItemData(cursor, 1);
                builder.setView(layout);
                builder.setNegativeButton("修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateData(c_id);
                        AllListActivity_COPY.this.cursor = db.getUserInfo();
                        //refresh adatper
                        adapter.changeCursor(AllListActivity_COPY.this.cursor);
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("刪除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       // db.deleteItem(c_id);
                        AllListActivity_COPY.this.cursor = db.getUserInfo();
                        //refresh adatper
                        adapter.changeCursor(AllListActivity_COPY.this.cursor);
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });


    }

    private void initListener() {
        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AllListActivity_COPY.this, NewItemActivity.class));
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void initItemView(View view) {
        //edit item
        ed_gift = view.findViewById(R.id.newitem_ed_gift);
        sp_type = view.findViewById(R.id.newitem_sp_type);
        sp_type.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, array));

    }

    private void initItemData(Cursor cursor, int opt) {
        String c_type = cursor.getString(1);
        String c_gift = cursor.getString(2);
        String c_winner = cursor.getString(3);
        if (opt == 0) {
            type.setText(array[Integer.parseInt(c_type)]);
            gift.setText(c_gift);
            winner.setText(c_winner);
        } else {
            sp_type.setSelection(Integer.parseInt(c_type));
            ed_gift.setText(c_gift);
            ed_winner.setText(c_winner);
        }

    }

    private void updateData(int c_id) {
        String c_type = sp_type.getSelectedItemPosition() + "";
        String c_gift = ed_gift.getText().toString();
        String c_winner = ed_winner.getText().toString();
     //   db.updateUserInfo(c_id, c_winner);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        cursor = db.getUserInfo();
        adapter.changeCursor(cursor);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }
}
