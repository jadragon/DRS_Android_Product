package com.example.alex.xmpp.service;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.alex.xmpp.XmppConnection;
import com.example.alex.xmpp.dbhelper.ContactOpenHelper;
import com.example.alex.xmpp.provider.ContactProvider;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;

import java.util.Collection;

public class IMService extends Service {
    public XMPPConnection connection;
    Roster roster;
    MyRosterListener myRosterListener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.e("IMService", "------onCreate------");
        connection = XmppConnection.getConnection();
        //同步聯繫人
        new Thread(new Runnable() {
            @Override
            public void run() {
                //得到聯繫人
                Roster roster = connection.getRoster();
                Collection<RosterEntry> entryts = roster.getEntries();
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
        XmppConnection.closeConnection();
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
