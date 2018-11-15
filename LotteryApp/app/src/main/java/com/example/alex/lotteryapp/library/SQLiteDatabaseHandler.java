/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 */
package com.example.alex.lotteryapp.library;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SQLiteDatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "userinfo";

    // Address table name
    private static final String TABLE_ITEM = "item_list";
    // Address Table Columns names
    public static final String KEY_ID = "_id";
    public static final String KEY_TYPE = "type";
    public static final String KEY_GIFT = "gift";
    public static final String KEY_WINNER = "winner";
    private Context context;
    private static final String CREATE_ADDRESS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_ITEM + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TYPE + " INTEGER,"
            + KEY_GIFT + " TEXT,"
            + KEY_WINNER + " TEXT" + ")";

    public SQLiteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ADDRESS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     */
    public void addItems(String type, String gift, int number) {
        ContentValues values = new ContentValues();
        values.put(KEY_TYPE, type);
        values.put(KEY_GIFT, gift);

        for (int i = 0; i < number; i++) {
            this.getWritableDatabase().insert(TABLE_ITEM, null, values);
        }
        //  values.put(KEY_WINNER, winner);
        // Inserting Row
    }

    /**
     * Getting user data from database
     */

    public ArrayList<String> getTypes() {
        ArrayList<String> datas = new ArrayList<>();
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT  DISTINCT " + KEY_TYPE + " FROM " + TABLE_ITEM, null);
        while (cursor.moveToNext()) {
            datas.add(cursor.getString(0));
        }
        cursor.close();
        // return user
        return datas;
    }

    /**
     * Getting user data from database
     */

    public ArrayList<Map<String, String>> getItems() {
        ArrayList<Map<String, String>> datas = new ArrayList<>();
        Map<String, String> data;
        Cursor cursor;
        ArrayList<String> array = getTypes();
        for (int i = 0; i < array.size(); i++) {
            cursor = this.getReadableDatabase().rawQuery("SELECT  * FROM " + TABLE_ITEM + " WHERE " + KEY_TYPE + " = '" + array.get(i) + "'", null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                data = new HashMap<>();
                data.put(KEY_TYPE, cursor.getString(1));
                data.put(KEY_GIFT, cursor.getString(2));
                data.put("number", cursor.getCount() + "");
                datas.add(data);
                cursor.close();
            }

        }
        // return user
        return datas;
    }

    /**
     * Getting user data from database
     */

    public ArrayList<String> getItems(String type) {
        ArrayList<String> datas = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_ITEM + " WHERE " + KEY_TYPE + " = '" + type + "' AND " + KEY_WINNER + " =''";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            datas.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        // return user
        return datas;
    }


    public Cursor getUserInfo() {
        String selectQuery = "SELECT  * FROM " + TABLE_ITEM + " ORDER BY " + KEY_TYPE + " ASC";
        Cursor cursor = this.getWritableDatabase().rawQuery(selectQuery, null);
        cursor.moveToFirst();
        /*
        Map<String, String> data = new HashMap<>();
        if (cursor.moveToNext()) {
            data.put(KEY_TYPE, cursor.getString(1));
            data.put(KEY_GIFT, cursor.getString(2));
            data.put(KEY_WINNER, cursor.getString(3));
            return data;
        }

        cursor.close();
         */
        // return user
        return cursor;
    }
/*
    public Cursor getItems(int type) {
        String selectQuery = "SELECT  * FROM " + TABLE_ITEM + " WHERE " + KEY_TYPE + " = " + type;
        Cursor cursor = this.getReadableDatabase().rawQuery(selectQuery, null);
        // return user
        return cursor;
    }
*/

    /**
     * Storing user details in database
     */
    public void updateUserInfo(String id, String winner) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_WINNER, winner);
        // Inserting Row
        db.update(TABLE_ITEM, values, KEY_ID + "=" + id, null);
        db.close(); // Closing database connection
    }

    /**
     * Storing user details in database
     */
    public void deleteItem(String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Inserting Row
        db.delete(TABLE_ITEM, KEY_TYPE + " = '" + type + "'", null);
        db.close(); // Closing database connection
    }

    /**
     * Re crate database
     * Delete all tables and create them again
     */
    public void resetTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_ITEM, null, null);
        db.close();
    }


}