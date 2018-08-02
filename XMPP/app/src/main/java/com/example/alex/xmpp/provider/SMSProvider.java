package com.example.alex.xmpp.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.alex.xmpp.dbhelper.SMSOpenHelper;

public class SMSProvider extends ContentProvider {
    //主機地址常量--?當前類的完整路徑
    private static final String AUTHORITIES = SMSProvider.class.getCanonicalName();//得到一個類的完整路徑4
    public static final int SMS = 1;
    //地址匹配對象
    static UriMatcher mUriMatcher;

    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        //添加一個匹配規則
        mUriMatcher.addURI(AUTHORITIES, "/sms", SMS);
    }

    //動應聯繫人表的一個uri常量
    public static Uri URI_SMS = Uri.parse("content://" + AUTHORITIES + "/sms");

    private SMSOpenHelper mHelper;

    @Override
    public boolean onCreate() {
        mHelper = new SMSOpenHelper(getContext());
        if (mHelper != null) {
            return true;
        }
        return false;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int code = mUriMatcher.match(uri);
        switch (code) {
            case SMS:
                SQLiteDatabase db = mHelper.getWritableDatabase();
                //新插入的ID
                long id = db.insert(SMSOpenHelper.TABLE_SMS, "", values);
                if (id != -1) {
                    Log.e("Insert", "------------------------InsertSuccess----------------------------");
                    //拼接最新的uri
                    uri = ContentUris.withAppendedId(uri, id);
                    //通知ContentObserver數據改變了
                    getContext().getContentResolver().notifyChange(URI_SMS, null);//為空所有都可收到
                }
                break;
        }
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int code = mUriMatcher.match(uri);
        int deleteCount = 0;
        switch (code) {
            case SMS:
                SQLiteDatabase db = mHelper.getWritableDatabase();
                deleteCount = db.delete(SMSOpenHelper.TABLE_SMS, selection, selectionArgs);
                if (deleteCount > 0) {
                    Log.e("Delete", "------------------------DeleteSuccess----------------------------");
                    //通知ContentObserver數據改變了
                    getContext().getContentResolver().notifyChange(URI_SMS, null);//為空所有都可收到
                }
                break;
        }
        return deleteCount;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int code = mUriMatcher.match(uri);
        int updateCount = 0;
        switch (code) {
            case SMS:
                SQLiteDatabase db = mHelper.getWritableDatabase();
                updateCount = db.update(SMSOpenHelper.TABLE_SMS, values, selection, selectionArgs);
                if (updateCount > 0) {
                    Log.e("Update", "------------------------UpdateSuccess----------------------------");
                    //通知ContentObserver數據改變了
                    getContext().getContentResolver().notifyChange(URI_SMS, null);//為空所有都可收到
                }
                break;
        }
        return updateCount;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        int code = mUriMatcher.match(uri);
        Cursor cursor = null;
        switch (code) {
            case SMS:
                SQLiteDatabase db = mHelper.getWritableDatabase();
                cursor = db.query(SMSOpenHelper.TABLE_SMS, projection, selection, selectionArgs, null, null, sortOrder);
                Log.e("Query", "------------------------QuerySuccess----------------------------");
                break;
        }
        return cursor;
    }
}
