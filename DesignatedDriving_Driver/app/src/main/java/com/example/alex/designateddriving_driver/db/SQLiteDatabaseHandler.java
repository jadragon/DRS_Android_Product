/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 */
package com.example.alex.designateddriving_driver.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SQLiteDatabaseHandler extends SQLiteOpenHelper {
    private Context context;
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "DesignatedDriving";

    // Login table name
    private static final String TABLE_MEMBER = "getMember";
    // Login Table Columns names
    public static final String KEY_ID = "_id";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_UNAME = "uname";
    public static final String KEY_PICTURE = "picture";
    public static final String KEY_SEX = "sex";
    public static final String KEY_BIRTHDAY = "birthday";
    public static final String KEY_MP = "mp";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_CONTACT = "contact";
    public static final String KEY_CMP = "cmp";
    private static final String CREATE_LOGIN_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_MEMBER + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TOKEN + " TEXT,"
            + KEY_PICTURE + " TEXT,"
            + KEY_UNAME + " TEXT,"
            + KEY_SEX + " TEXT,"
            + KEY_BIRTHDAY + " TEXT,"
            + KEY_EMAIL + " TEXT,"
            + KEY_CONTACT + " TEXT,"
            + KEY_CMP + " TEXT" + ")";

    // Address table name
    private static final String TABLE_ADDRESS = "getAddress";
    // Address Table Columns names
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_AD_ID = "_id";
    public static final String KEY_CITY = "city";
    public static final String KEY_AREA = "area";
    public static final String KEY_ZIPCODE = "zipcode";
    public static final String KEY_MODIFYDATE = "modifydate";
    public static final String CREATE_ADDRESS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_ADDRESS + "("
            + KEY_AD_ID + " INTEGER PRIMARY KEY,"
            + KEY_CITY + " TEXT,"
            + KEY_AREA + " TEXT,"
            + KEY_ZIPCODE + " TEXT,"
            + KEY_MODIFYDATE + " TEXT" + ")";

    //Bank table name
    public SQLiteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ADDRESS_TABLE);
        db.execSQL(CREATE_LOGIN_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDRESS);
        // Create tables again
        onCreate(db);
    }


    /**
     * Storing user details in database
     */
    public void addMember(ContentValues cv) {
        if (cv != null) {
            SQLiteDatabase db = this.getWritableDatabase();
            // Inserting Row
            db.insert(TABLE_MEMBER, null, cv);
            db.close(); // Closing database connection
        } else {
            Toast.makeText(context, "存取資料庫錯誤", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Getting user data from database
     */
    public ContentValues getMemberDetail() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MEMBER, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            ContentValues cv = new ContentValues();
            cv.put(KEY_TOKEN, cursor.getString(1));
            cv.put(KEY_PICTURE, cursor.getString(2));
            cv.put(KEY_UNAME, cursor.getString(3));
            cv.put(KEY_SEX, cursor.getString(4));
            cv.put(KEY_BIRTHDAY, cursor.getString(5));
            cv.put(KEY_EMAIL, cursor.getString(6));
            cv.put(KEY_CONTACT, cursor.getString(7));
            cv.put(KEY_CMP, cursor.getString(8));
            return cv;
        }
        cursor.close();
        db.close();
        // return user
        return null;
    }

    /**
     * Storing user details in database
     */
    public void updatePhotoImage(byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PICTURE, image); // background
        // Inserting Row
        db.update(TABLE_MEMBER, values, KEY_ID + "=" + 1, null);
        db.close(); // Closing database connection
    }

    public byte[] getPhotoImage() {
        String selectQuery = "SELECT  " + KEY_PICTURE + " FROM " + TABLE_MEMBER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            return cursor.getBlob(0);
        }
        cursor.close();
        db.close();
        // return user
        return null;
    }

    /**
     * Getting user login status
     * return true if rows are there in table
     */
    public int getLoginRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_MEMBER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
        // return row count
        return rowCount;
    }

    /**
     * Re crate database
     * Delete all tables and create them again
     */
    public void resetLoginTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_MEMBER, null, null);
        db.close();
    }

    /**
     * Storing user details in database
     */
    public void addAddressAll(ArrayList<ContentValues> datas) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < datas.size(); i++) {
            db.insert(TABLE_ADDRESS, null, datas.get(i));
        }
        // Inserting Row
        db.close(); // Closing database connection
    }

    /**
     * Getting user data from database
     */
    public ArrayList<Map<String, String>> getAddressDetails() {
        ArrayList<Map<String, String>> datas = new ArrayList<>();
        Map<String, String> data;
        String selectQuery = "SELECT  * FROM " + TABLE_ADDRESS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        //cursor.moveToFirst();
        while (cursor.moveToNext()) {
            data = new HashMap<>();
            data.put(KEY_CITY, cursor.getString(1));
            data.put(KEY_AREA, cursor.getString(2));
            data.put(KEY_ZIPCODE, cursor.getString(3));
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
    public ArrayList<String> getAllCity() {
        ArrayList<String> data = new ArrayList<>();
        String selectQuery = "SELECT DISTINCT " + KEY_CITY + " FROM " + TABLE_ADDRESS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
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
    public ArrayList<Map<String, String>> getInSideCityDetail() {
        ArrayList<Map<String, String>> datas = new ArrayList<>();
        Map<String, String> data;
        String selectQuery = "SELECT * FROM " + TABLE_ADDRESS + " WHERE " + KEY_CITY + " NOT LIKE '" + "%金%" + "'" + " AND " + KEY_CITY + " NOT LIKE '" + "%連%" + "'" + " AND " + KEY_CITY + " NOT LIKE '" + "%澎%" + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        while (cursor.moveToNext()) {
            data = new HashMap<>();
            data.put(KEY_CITY, cursor.getString(1));
            data.put(KEY_AREA, cursor.getString(2));
            data.put(KEY_ZIPCODE, cursor.getString(3));
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
    public ArrayList<String> getInSideCity() {
        ArrayList<String> data = new ArrayList<>();
        String selectQuery = "SELECT DISTINCT " + KEY_CITY + " FROM " + TABLE_ADDRESS + " WHERE " + KEY_CITY + " NOT LIKE '" + "%金%" + "'" + " AND " + KEY_CITY + " NOT LIKE '" + "%連%" + "'" + " AND " + KEY_CITY + " NOT LIKE '" + "%澎%" + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
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
    public ArrayList<String> getOutSideCity() {
        ArrayList<String> data = new ArrayList<>();
        String selectQuery = "SELECT DISTINCT " + KEY_CITY + " FROM " + TABLE_ADDRESS + " WHERE " + KEY_CITY + " LIKE '" + "%金%" + "'" + " OR " + KEY_CITY + " LIKE '" + "%連%" + "'" + " OR " + KEY_CITY + " LIKE '" + "%澎%" + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
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
    public ArrayList<Map<String, String>> getOutSideCityDetail() {
        ArrayList<Map<String, String>> datas = new ArrayList<>();
        Map<String, String> data;
        String selectQuery = "SELECT * FROM " + TABLE_ADDRESS + " WHERE " + KEY_CITY + " LIKE '" + "%金%" + "'" + " OR " + KEY_CITY + " LIKE '" + "%連%" + "'" + " OR " + KEY_CITY + " LIKE '" + "%澎%" + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        while (cursor.moveToNext()) {
            data = new HashMap<>();
            data.put(KEY_CITY, cursor.getString(1));
            data.put(KEY_AREA, cursor.getString(2));
            data.put(KEY_ZIPCODE, cursor.getString(3));
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
    public Map<String, String> getZipcodeByCityAndArea(String city, String area) {
        Map<String, String> data;
        city = city.substring(0, 1).equals("台") ? city.replace("台", "臺") : city;
        String selectQuery = "SELECT  * FROM " + TABLE_ADDRESS + " WHERE " + KEY_CITY + "='" + city + "'" + " AND " + KEY_AREA + " LIKE '%" + area + "%'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        data = new HashMap<>();
        if (cursor.getCount() > 0) {
            data.put(KEY_CITY, cursor.getString(1));
            data.put(KEY_AREA, cursor.getString(2));
            data.put(KEY_ZIPCODE, cursor.getString(3));
        }
        cursor.close();
        db.close();
        // return user
        return data;
    }

    /**
     * Getting user data from database
     */
    public ArrayList<String> getAreaByCity(String city) {
        ArrayList<String> data = new ArrayList<>();
        String selectQuery = "SELECT " + KEY_AREA + "  FROM " + TABLE_ADDRESS + " WHERE " + KEY_CITY + "='" + city + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
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
    public Map<String, String> getCityAndAreaByZipcode(String zipcode) {
        Map<String, String> data;
        String selectQuery = "SELECT  * FROM " + TABLE_ADDRESS + " WHERE " + KEY_ZIPCODE + "='" + zipcode + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        data = new HashMap<>();
        if (cursor.getCount() > 0) {
            data.put(KEY_CITY, cursor.getString(1));
            data.put(KEY_AREA, cursor.getString(2));
            data.put(KEY_ZIPCODE, cursor.getString(3));
        }
        cursor.close();
        db.close();
        // return user
        return data;
    }

    /**
     * Getting user login status
     * return true if rows are there in table
     */
    public int getAddressRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ADDRESS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
        // return row count
        return rowCount;
    }

    /**
     * Getting user login status
     * return true if rows are there in table
     */
    public int getModifydate(String modifydate) {
        String selectQuery = "SELECT  * FROM " + TABLE_ADDRESS + " WHERE " + KEY_MODIFYDATE + "='" + modifydate + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
        // return row count
        return rowCount;
    }

    /**
     * Re crate database
     * Delete all tables and create them again
     */
    public void resetAddressTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_ADDRESS, null, null);
        db.close();
    }


}