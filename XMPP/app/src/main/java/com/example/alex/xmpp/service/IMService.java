package com.example.alex.xmpp.service;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.alex.xmpp.dbhelper.ContactOpenHelper;
import com.example.alex.xmpp.provider.ContactProvider;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

import java.util.Collection;


public class IMService extends Service {
    Roster roster;
    MyRosterListener myRosterListener;
    private static XMPPConnection connection = null;
    public final static int SERVER_PORT = 5222;//服务端口 可以在openfire上设置
    public final static String SERVER_HOST = "220.134.248.18";//你openfire服务器所在的ip
    public final static String SERVER_NAME = "220.134.248.18";
    public static String CURRENT_ACCOUNT = "";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private static void openConnection() {
        try {
            //配置连接
            ConnectionConfiguration conConfig = new ConnectionConfiguration(
                    SERVER_HOST, SERVER_PORT);
            conConfig.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);// 传明文
            conConfig.setDebuggerEnabled(true);
            //设置断网重连 默认为true
            conConfig.setReconnectionAllowed(true);
            //设置登录状态 true-为在线
            conConfig.setSendPresence(true);
            //设置不需要SAS验证
            conConfig.setSASLAuthenticationEnabled(true);
            //开启连接
            connection = new XMPPConnection(conConfig);
            //配置各种Provider，如果不配置，则会无法解析数据
            //   configureConnection(ProviderManager.getInstance());

            connection.connect();
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建连接
     */
    public static XMPPConnection getConnection() {
        if (connection == null) {
            openConnection();
        }

        return connection;
    }

    @Override
    public void onCreate() {
        Log.e("IMService", "------onCreate------");
        //同步聯繫人
        new Thread(new Runnable() {
            @Override
            public void run() {
                //得到聯繫人
                roster = connection.getRoster();
                Collection<RosterEntry> entryts = roster.getEntries();
                //新增監聽
                myRosterListener = new MyRosterListener();
                roster.addRosterListener(myRosterListener);
                for (RosterEntry entry : entryts) {
                    saveOrUpdate(entry);
                }
            }
        }).start();

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("IMService", "------onStartCommand------");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.e("IMService", "------onDestroy------");
        if (roster != null && myRosterListener != null) {
            roster.removeRosterListener(myRosterListener);
        }
        super.onDestroy();
    }

    private void saveOrUpdate(RosterEntry entry) {
        ContentValues values = new ContentValues();
        String account = entry.getUser();
        values.put(ContactOpenHelper.ContactTabke.ACCOUNT, account);
        String nickname = entry.getName();
        values.put(ContactOpenHelper.ContactTabke.NICKNAME, nickname);
        values.put(ContactOpenHelper.ContactTabke.AVATAR, "0");
        values.put(ContactOpenHelper.ContactTabke.PINYIN, entry.getUser());

        //先update後Insert
        int updateCount = getContentResolver().update(ContactProvider.URI_CONTACT, values, ContactOpenHelper.ContactTabke.ACCOUNT + "=?", new String[]{account});
        if (updateCount <= 0) {
            getContentResolver().insert(ContactProvider.URI_CONTACT, values);
        }
    }


    private class MyRosterListener implements RosterListener {
        @Override
        public void entriesAdded(Collection<String> collection) {//添加聯絡人
            Log.e("entriesAdded", "------------------------entriesAdded----------------------------");
            for (String address : collection) {
                RosterEntry entry = roster.getEntry(address);
                //更新或插入
                saveOrUpdate(entry);
            }
        }

        @Override
        public void entriesUpdated(Collection<String> collection) {//更新聯絡人
            Log.e("entriesUpdated", "------------------------entriesUpdated----------------------------");
            for (String address : collection) {
                RosterEntry entry = roster.getEntry(address);
                //更新或插入
                saveOrUpdate(entry);
            }
        }

        @Override
        public void entriesDeleted(Collection<String> collection) {//刪除聯絡人
            Log.e("entriesDeleted", "------------------------entriesDeleted----------------------------");
            for (String address : collection) {
                //刪除
                getContentResolver().delete(ContactProvider.URI_CONTACT, ContactOpenHelper.ContactTabke.ACCOUNT + "=?", new String[]{address});
            }
        }

        @Override
        public void presenceChanged(Presence presence) {//狀態改變
            Log.e("presenceChanged", "------------------------presenceChanged----------------------------");
        }
    }

}
