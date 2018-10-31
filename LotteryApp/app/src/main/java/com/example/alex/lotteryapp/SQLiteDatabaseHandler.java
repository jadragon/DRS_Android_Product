/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 */
package com.example.alex.lotteryapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
    private static final String KEY_ID = "_id";
    private static final String KEY_TYPE = "type";
    private static final String KEY_GIFT = "gift";
    private static final String KEY_WINNER = "winner";

    private static final String CREATE_ADDRESS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_ITEM + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TYPE + " TEXT,"
            + KEY_GIFT + " TEXT,"
            + KEY_WINNER + " TEXT" + ")";

    public SQLiteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
    public void addItem(String type, String gift, String winner) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TYPE, type);
        values.put(KEY_GIFT, gift);
        values.put(KEY_WINNER, winner);
        // Inserting Row
        db.insert(TABLE_ITEM, null, values);
        db.close(); // Closing database connection
    }

    /**
     * Getting user data from database
     */
    /*
    public ArrayList<Map<String, String>> getItems() {
        ArrayList<Map<String, String>> datas = new ArrayList<>();
        Map<String, String> data;
        String selectQuery = "SELECT  * FROM " + TABLE_ITEM;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        //cursor.moveToFirst();
        while (cursor.moveToNext()) {
            data = new HashMap<>();
            data.put(KEY_NAME, cursor.getString(1));
            data.put(KEY_PRICE, cursor.getString(2));
            data.put(KEY_DISTANCE, cursor.getString(3));
            data.put(KEY_HOBBY, cursor.getString(4));
            data.put(KEY_TYPE, cursor.getString(5));
            data.put(KEY_NOTE, cursor.getString(6));
            datas.add(data);
        }
        cursor.close();
        db.close();
        // return user
        return datas;
    }
    */
    public Map<String, String> getUserInfo() {
        String selectQuery = "SELECT  * FROM " + TABLE_ITEM;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        Map<String, String> data = new HashMap<>();
        if (cursor.moveToNext()) {
            data.put(KEY_TYPE, cursor.getString(1));
            data.put(KEY_GIFT, cursor.getString(2));
            data.put(KEY_WINNER, cursor.getString(3));
            return data;
        }
        cursor.close();
        db.close();
        // return user
        return null;
    }


    /**
     * Storing user details in database
     */
    public void updateUserInfo(int id,String type, String gift, String winner) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TYPE, type);
        values.put(KEY_GIFT, gift);
        values.put(KEY_WINNER, winner);
        // Inserting Row
        db.update(TABLE_ITEM, values, KEY_ID + "=" + id, null);
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