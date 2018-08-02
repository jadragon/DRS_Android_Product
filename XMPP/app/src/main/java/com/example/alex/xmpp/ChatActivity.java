package com.example.alex.xmpp;


import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.alex.xmpp.dbhelper.SMSOpenHelper;
import com.example.alex.xmpp.provider.SMSProvider;
import com.example.alex.xmpp.service.IMService;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

public class ChatActivity extends AppCompatActivity {
    String account;
    String nickname;
    TextView title;
    ListView listview;
    EditText edit_body;
    Button btn_send;
    CursorAdapter adapter;
    ChatManager chatManager;
    Chat chat;
    Handler handler;
MyMessageListener myMessageListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        handler = new Handler();
        init();
        initView();
        initData();
        chatManager = IMService.getConnection().getChatManager();
        chat = chatManager.createChat(account, null);

        ChatManagerListener chatManagerListener = new ChatManagerListener() {
            @Override
            public void chatCreated(Chat chat, boolean b) {
                chat.addMessageListener(myMessageListener);
            }
        };
        chatManager.addChatListener(chatManagerListener);
        initListener();
    }

    private void init() {
        registerContentObserver();
        myMessageListener=new MyMessageListener();
        account = getIntent().getStringExtra("account");
        nickname = getIntent().getStringExtra("nickname");
        title = findViewById(R.id.title);
        listview = findViewById(R.id.listview);
        edit_body = findViewById(R.id.edit_body);
        btn_send = findViewById(R.id.btn_send);
    }


    private void initData() {
        setOrUpdateAdapter();
    }

    private void initView() {
        //title
        title.setText("與" + nickname + "聊天中");


    }

    private void initListener() {
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //發送消息
                            Message msg = new Message();
                            msg.setFrom(IMService.CURRENT_ACCOUNT);
                            msg.setTo(account);
                            msg.setBody(edit_body.getText().toString());
                            msg.setType(Message.Type.chat);
                            chat.sendMessage(msg);
                            //將訊息存入sql
                            saveMessage(account, msg);
                            //清空輸入框
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    edit_body.setText("");
                                }
                            });
                        } catch (XMPPException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });
    }

    private void setOrUpdateAdapter() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //adapter不存在
                if (adapter != null) {
                    adapter.getCursor().requery();
                    return;
                }
                final Cursor cursor = getContentResolver().query(SMSProvider.URI_SMS, null, null, null, null);
                //沒有數據時
                if (cursor.getCount() <= 0) {
                    return;
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new CursorAdapter(ChatActivity.this, cursor) {
                            @Override
                            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                                return new TextView(context);
                            }

                            @Override
                            public void bindView(View view, Context context, Cursor cursor) {
                                TextView tv = (TextView) view;
                                String account = cursor.getString(cursor.getColumnIndex(SMSOpenHelper.ContactTabke.BODY));
                                tv.setText(account);
                            }
                        };
                        listview.setAdapter(adapter);
                    }
                });
            }
        });

    }

    /***
     * 保存消息
     */
    private void saveMessage(String seesion_account, Message msg) {
        ContentValues values = new ContentValues();
        values.put(SMSOpenHelper.ContactTabke.FROM_ACCOUNT, msg.getFrom());
        values.put(SMSOpenHelper.ContactTabke.TO_ACCOUNT, msg.getTo());
        values.put(SMSOpenHelper.ContactTabke.BODY, msg.getBody());
        values.put(SMSOpenHelper.ContactTabke.STATUS, "offline");
        values.put(SMSOpenHelper.ContactTabke.TYPE, msg.getType().name());
        values.put(SMSOpenHelper.ContactTabke.TIME, System.currentTimeMillis());
        values.put(SMSOpenHelper.ContactTabke.SESSION_ACCOUNT, seesion_account);
        //插入訊息
        getContentResolver().insert(SMSProvider.URI_SMS, values);


    }

    class MyMessageListener implements MessageListener {

        @Override
        public void processMessage(final Chat chat, final Message message) {
            if (message.getType() == Message.Type.chat || message.getType() == Message.Type.normal) {
                if (message.getBody() != null) {
                    String participant = chat.getParticipant();
                    saveMessage(participant, message);

                }
            }
        }
    }

    private MyContentObserver myContentObserver=new MyContentObserver(handler);
    /**
     * 註冊監聽
     */
    public void registerContentObserver() {
        getContentResolver().registerContentObserver(SMSProvider.URI_SMS, true, myContentObserver);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterContentObserver();
    }
}
