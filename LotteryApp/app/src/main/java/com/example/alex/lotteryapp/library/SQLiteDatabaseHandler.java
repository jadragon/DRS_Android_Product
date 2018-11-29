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
    private static final String CREATE_ADDRESS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_ITEM + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TYPE + " INTEGER,"
            + KEY_GIFT + " TEXT,"
            + KEY_WINNER + " TEXT" + ")";

    public SQLiteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ADDRESS_TABLE);
        initAllAward(db);
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

    public ArrayList<ArrayList<String>> getExcelData() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ArrayList<String>> datas = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT  DISTINCT " + KEY_TYPE + " FROM " + TABLE_ITEM, null);
        while (cursor.moveToNext()) {
            types.add(cursor.getString(0));
        }
        ArrayList<String> data;
        ArrayList<String> winners;
        for (String type : types) {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_ITEM + " WHERE " + KEY_TYPE + " = '" + type + "'", null);
            data = new ArrayList<>();
            cursor.moveToFirst();
            data.add(cursor.getString(1));
            data.add(cursor.getString(2));
            winners = new ArrayList<>();
            winners.add(cursor.getString(3));
            while (cursor.moveToNext()) {
                winners.add(cursor.getString(3));
            }
            data.add(winners + "");
            datas.add(data);
        }

        cursor.close();
        db.close();
        // return user
        return datas;
    }

    /**
     * Getting user data from database
     */

    public ArrayList<Map<String, String>> getItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Map<String, String>> datas = new ArrayList<>();
        Map<String, String> data;
        Cursor cursor = null;
        ArrayList<String> array = getTypes();
        for (int i = 0; i < array.size(); i++) {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_ITEM + " WHERE " + KEY_TYPE + " = '" + array.get(i) + "'", null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                data = new HashMap<>();
                data.put(KEY_TYPE, cursor.getString(1));
                data.put(KEY_GIFT, cursor.getString(2));
                int total = cursor.getCount();
                cursor = db.rawQuery("SELECT " + KEY_ID + " FROM " + TABLE_ITEM + " WHERE " + KEY_TYPE + " = '" + cursor.getString(1) + "' AND " + KEY_WINNER + " IS NOT NULL", null);
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    data.put("number", cursor.getCount() + "/" + total + "");
                } else {
                    data.put("number", 0 + "/" + total + "");
                }
                datas.add(data);
            }
        }
        cursor.close();
        db.close();
        // return user
        return datas;
    }

    public String getGift(String type) {
        String selectQuery = "SELECT " + KEY_GIFT + " FROM " + TABLE_ITEM + " WHERE " + KEY_TYPE + " = '" + type + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        String gift = null;
        if (cursor.getCount() > 0) {
            gift = cursor.getString(0);
        }
        cursor.close();
        db.close();
        // return user
        return gift;
    }

    /**
     * Getting user data from database
     */

    public ArrayList<String> getItems(String type) {
        ArrayList<String> datas = new ArrayList<>();
        String selectQuery = "SELECT " + KEY_ID + " FROM " + TABLE_ITEM + " WHERE " + KEY_TYPE + " = '" + type + "' AND " + KEY_WINNER + " IS NULL";
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

    /**
     * Getting user data from database
     */

    public ArrayList<ArrayList<String>> getAllWinnerNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ArrayList<String>> datas = new ArrayList<>();

        String selectQuery = "SELECT  DISTINCT " + KEY_TYPE + " FROM " + TABLE_ITEM;
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<String> types = new ArrayList<>();
        while (cursor.moveToNext()) {
            types.add(cursor.getString(0));
        }
        ArrayList<String> data;
        for (String type : types) {
            data = new ArrayList<>();
            selectQuery = "SELECT " + KEY_WINNER + " FROM " + TABLE_ITEM + " WHERE " + KEY_TYPE + " = '" + type + "' AND " + KEY_WINNER + " IS NOT NULL";
            cursor = db.rawQuery(selectQuery, null);
            while (cursor.moveToNext()) {
                data.add(cursor.getString(0));
            }
            datas.add(data);
        }
        cursor.close();
        db.close();
        // return user
        return datas;
    }

    /**
     * Getting user data from database
     */

    public ArrayList<String> getCurrentWinners() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> data = new ArrayList<>();

        String selectQuery = "SELECT " + KEY_WINNER + " FROM " + TABLE_ITEM + " WHERE " + KEY_WINNER + " IS NOT NULL";
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            data.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        // return user
        return data;
    }

    /**
     * Getting user data from database
     */

    public ArrayList<String> getWinnerNames(String type) {
        ArrayList<String> datas = new ArrayList<>();
        String selectQuery = "SELECT " + KEY_WINNER + " FROM " + TABLE_ITEM + " WHERE " + KEY_TYPE + " = '" + type + "' AND " + KEY_WINNER + " IS NOT NULL";
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

    /**
     * Getting user data from database
     */

    public int getLeftWinners(String type) {
        String selectQuery = "SELECT " + KEY_WINNER + " FROM " + TABLE_ITEM + " WHERE " + KEY_TYPE + " = '" + type + "' AND " + KEY_WINNER + " IS NULL";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        // return user
        return count;
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


    public void initAllAward(SQLiteDatabase db) {
        //頭獎
        ContentValues values = new ContentValues();
        values.put(KEY_TYPE, "頭獎");
        values.put(KEY_GIFT, "30萬");
        for (int i = 0; i < 1; i++) {
            db.insert(TABLE_ITEM, null, values);
        }
        //二獎
        values = new ContentValues();
        values.put(KEY_TYPE, "二獎");
        values.put(KEY_GIFT, "6萬");
        for (int i = 0; i < 4; i++) {
            db.insert(TABLE_ITEM, null, values);
        }

        //三獎
        values = new ContentValues();
        values.put(KEY_TYPE, "三獎");
        values.put(KEY_GIFT, "5萬");
        for (int i = 0; i < 6; i++) {
            db.insert(TABLE_ITEM, null, values);
        }
        //四獎
        values = new ContentValues();
        values.put(KEY_TYPE, "四獎");
        values.put(KEY_GIFT, "3萬");
        for (int i = 0; i < 12; i++) {
            db.insert(TABLE_ITEM, null, values);
        }
        //五獎
        values = new ContentValues();
        values.put(KEY_TYPE, "五獎");
        values.put(KEY_GIFT, "2萬");
        for (int i = 0; i < 20; i++) {
            db.insert(TABLE_ITEM, null, values);
        }
        //六獎
        values = new ContentValues();
        values.put(KEY_TYPE, "六獎");
        values.put(KEY_GIFT, "1萬2");
        for (int i = 0; i < 25; i++) {
            db.insert(TABLE_ITEM, null, values);
        }
    }

    public void resetAllAward() {
        SQLiteDatabase db = this.getReadableDatabase();
        resetTables(db);
        //頭獎
        ContentValues values = new ContentValues();
        values.put(KEY_TYPE, "頭獎");
        values.put(KEY_GIFT, "30萬");
        for (int i = 0; i < 1; i++) {
            db.insert(TABLE_ITEM, null, values);
        }
        //二獎
        values = new ContentValues();
        values.put(KEY_TYPE, "二獎");
        values.put(KEY_GIFT, "10萬");
        for (int i = 0; i < 5; i++) {
            db.insert(TABLE_ITEM, null, values);
        }

        //三獎
        values = new ContentValues();
        values.put(KEY_TYPE, "三獎");
        values.put(KEY_GIFT, "5萬");
        for (int i = 0; i < 10; i++) {
            db.insert(TABLE_ITEM, null, values);
        }
        //四獎
        values = new ContentValues();
        values.put(KEY_TYPE, "四獎");
        values.put(KEY_GIFT, "3萬");
        for (int i = 0; i < 15; i++) {
            db.insert(TABLE_ITEM, null, values);
        }
        //五獎
        values = new ContentValues();
        values.put(KEY_TYPE, "五獎");
        values.put(KEY_GIFT, "2萬");
        for (int i = 0; i < 30; i++) {
            db.insert(TABLE_ITEM, null, values);
        }
        //六獎
        values = new ContentValues();
        values.put(KEY_TYPE, "六獎");
        values.put(KEY_GIFT, "1萬2");
        for (int i = 0; i < 25; i++) {
            db.insert(TABLE_ITEM, null, values);
        }
        db.close();
    }

    /**
     * Re crate database
     * Delete all tables and create them again
     */
    public void resetTables(SQLiteDatabase db) {
        // Delete All Rows
        db.delete(TABLE_ITEM, null, null);
    }


}