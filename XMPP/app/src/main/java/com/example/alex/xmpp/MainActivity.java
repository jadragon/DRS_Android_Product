package com.example.alex.xmpp;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.alex.xmpp.dbhelper.ContactOpenHelper;
import com.example.alex.xmpp.provider.ContactProvider;

public class MainActivity extends AppCompatActivity {
    ListView list_view;

    CursorAdapter adapter;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list_view = findViewById(R.id.list_view);
        init();
        initData();
        initListener();
    }

    private void init() {
        registerContentObserver();
    }

    @Override
    protected void onDestroy() {
        unRegisterContentObserver();
        super.onDestroy();
    }


    private void initData() {
        //同步存入sql
        setOrUpdateAdapter();
    }

    private void initListener() {
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = adapter.getCursor();
                c.moveToPosition(position);
                //取得jid
                String account=c.getString(c.getColumnIndex(ContactOpenHelper.ContactTabke.ACCOUNT));
                String nickname=c.getString(c.getColumnIndex(ContactOpenHelper.ContactTabke.NICKNAME));
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                intent.putExtra("account",account);
                intent.putExtra("nickname",nickname);
                startActivity(intent);
            }
        });
    }

    private void setOrUpdateAdapter() {
        //adapter不存在
        if (adapter != null) {
            adapter.getCursor().requery();
            return;
        }
        final Cursor cursor = getContentResolver().query(ContactProvider.URI_CONTACT, null, null, null, null);
        //沒有數據時
        if (cursor.getCount() <= 0) {
            return;
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                adapter = new CursorAdapter(MainActivity.this, cursor) {
                    @Override
                    public View newView(Context context, Cursor cursor, ViewGroup parent) {
                        return new TextView(context);
                    }

                    @Override
                    public void bindView(View view, Context context, Cursor cursor) {
                        TextView tv = (TextView) view;
                        String account = cursor.getString(cursor.getColumnIndex(ContactOpenHelper.ContactTabke.ACCOUNT));
                        tv.setText(account);
                    }
                };
                list_view.setAdapter(adapter);
            }
        });
    }


    //監聽數據庫改變
    MyContentObserver myContentObserver = new MyContentObserver(handler);

    /**
     * 註冊監聽
     */
    public void registerContentObserver() {
        getContentResolver().registerContentObserver(ContactProvider.URI_CONTACT, true, myContentObserver);
    }

    /**
     * 反註冊監聽
     */
    public void unRegisterContentObserver() {
        getContentResolver().unregisterContentObserver(myContentObserver);
    }

    class MyContentObserver extends ContentObserver {
        public MyContentObserver(Handler handler) {
            super(handler);
        }

        /**
         * 如果數據庫改變
         */
        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            //更新adapter
            setOrUpdateAdapter();
        }
    }
}
