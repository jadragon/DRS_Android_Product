package com.example.alex.xmpp.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class ContactOpenHelper extends SQLiteOpenHelper {

    public static final String TABLE_CONTACT = "table_contact";

    public ContactOpenHelper(Context context) {
        super(context, "contact.db", null, 1);
    }


    public class ContactTabke implements BaseColumns {//預設_id _count
        public static final String ACCOUNT = "account";//帳號
        public static final String NICKNAME = "nickname";//暱稱
        public static final String AVATAR = "avatar";//頭像
        public static final String PINYIN = "pinyin";//拼音
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_CONTACT + "(" + ContactTabke._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ContactTabke.ACCOUNT + " TEXT, "
                + ContactTabke.NICKNAME + " TEXT, "
                + ContactTabke.AVATAR + " TEXT, "
                + ContactTabke.PINYIN + " TEXT);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
