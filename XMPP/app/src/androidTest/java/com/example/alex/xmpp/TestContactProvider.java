package com.example.alex.xmpp;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;
import android.util.Log;

import com.example.alex.xmpp.dbhelper.ContactOpenHelper;
import com.example.alex.xmpp.provider.ContactProvider;

public class TestContactProvider extends AndroidTestCase {
    public void testInsert() {
        ContentValues values = new ContentValues();
        values.put(ContactOpenHelper.ContactTabke.ACCOUNT, "bilibili@yahoo.com.tw");
        values.put(ContactOpenHelper.ContactTabke.NICKNAME, "小三");
        values.put(ContactOpenHelper.ContactTabke.AVATAR, "0");
        values.put(ContactOpenHelper.ContactTabke.PINYIN, "shaosan");
        getContext().getContentResolver().insert(ContactProvider.URI_CONTACT, values);
    }

    public void testDelete() {
        getContext().getContentResolver().delete(ContactProvider.URI_CONTACT, ContactOpenHelper.ContactTabke.ACCOUNT + "=?", new String[]{"bilibili@yahoo.com.tw"});
    }

    public void testUpdate() {
        ContentValues values = new ContentValues();
        values.put(ContactOpenHelper.ContactTabke.ACCOUNT, "bilibili@yahoo.com.tw");
        values.put(ContactOpenHelper.ContactTabke.NICKNAME, "我是小三");
        values.put(ContactOpenHelper.ContactTabke.AVATAR, "0");
        values.put(ContactOpenHelper.ContactTabke.PINYIN, "woshishaosan");
        getContext().getContentResolver().update(ContactProvider.URI_CONTACT, values, ContactOpenHelper.ContactTabke.ACCOUNT + "=?", new String[]{"bilibili@yahoo.com.tw"});
    }

    public void testQuery() {
        Cursor cursor = getContext().getContentResolver().query(ContactProvider.URI_CONTACT, null, null, null, null);
        int columncount = cursor.getColumnCount();//一共多少列
        while (cursor.moveToNext()) {
            for (int i = 0; i < columncount; i++) {
                Log.e("testquery", cursor.getString(i) + "     ");
            }
        }
    }

}
