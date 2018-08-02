package com.example.alex.xmpp.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class SMSOpenHelper extends SQLiteOpenHelper {

    public static final String TABLE_SMS = "table_sms";

    public SMSOpenHelper(Context context) {
        super(context, "sms.db", null, 1);
    }


    public class ContactTabke implements BaseColumns {//預設_id _count
        public static final String FROM_ACCOUNT = "from_account";
        public static final String TO_ACCOUNT = "to_account";
        public static final String BODY = "body";
        public static final String STATUS = "status";
        public static final String TYPE = "type";
        public static final String TIME = "time";
        public static final String SESSION_ACCOUNT = "session_account";

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_SMS + "(" + ContactTabke._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ContactTabke.FROM_ACCOUNT + " TEXT, "
                + ContactTabke.TO_ACCOUNT + " TEXT, "
                + ContactTabke.BODY + " TEXT, "
                + ContactTabke.STATUS + " TEXT, "
                + ContactTabke.TYPE + " TEXT, "
                + ContactTabke.TIME + " TEXT, "
                + ContactTabke.SESSION_ACCOUNT + " TEXT);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
