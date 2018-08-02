package com.example.alex.xmpp;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;
import android.util.Log;

import com.example.alex.xmpp.dbhelper.SMSOpenHelper;
import com.example.alex.xmpp.provider.SMSProvider;

public class TestSMSProvider extends AndroidTestCase {
    public void testInsert() {
        ContentValues values = new ContentValues();
        values.put(SMSOpenHelper.ContactTabke.FROM_ACCOUNT, "1");
        values.put(SMSOpenHelper.ContactTabke.TO_ACCOUNT, "2");
        values.put(SMSOpenHelper.ContactTabke.BODY, "3");
        values.put(SMSOpenHelper.ContactTabke.TIME, "4");
        values.put(SMSOpenHelper.ContactTabke.TYPE, "5");
        values.put(SMSOpenHelper.ContactTabke.STATUS, "6");
        values.put(SMSOpenHelper.ContactTabke.SESSION_ACCOUNT, "7");
        getContext().getContentResolver().insert(SMSProvider.URI_SMS, values);
    }

    public void testDelete() {
        getContext().getContentResolver().delete(SMSProvider.URI_SMS, SMSOpenHelper.ContactTabke.FROM_ACCOUNT + "=?", new String[]{"11"});
    }

    public void testUpdate() {
        ContentValues values = new ContentValues();
        values.put(SMSOpenHelper.ContactTabke.FROM_ACCOUNT, "11");
        values.put(SMSOpenHelper.ContactTabke.TO_ACCOUNT, "22");
        values.put(SMSOpenHelper.ContactTabke.BODY, "33");
        values.put(SMSOpenHelper.ContactTabke.TIME, "44");
        values.put(SMSOpenHelper.ContactTabke.TYPE, "55");
        values.put(SMSOpenHelper.ContactTabke.STATUS, "66");
        values.put(SMSOpenHelper.ContactTabke.SESSION_ACCOUNT, "77");
        getContext().getContentResolver().update(SMSProvider.URI_SMS, values, SMSOpenHelper.ContactTabke.FROM_ACCOUNT + "=?", new String[]{"1"});
    }

    public void testQuery() {
        Cursor cursor = getContext().getContentResolver().query(SMSProvider.URI_SMS, null, null, null, null);
        int columncount = cursor.getColumnCount();//一共多少列
        while (cursor.moveToNext()) {
            for (int i = 0; i < columncount; i++) {
                Log.e("testquery", cursor.getString(i) + "     ");
            }
        }
    }

}
