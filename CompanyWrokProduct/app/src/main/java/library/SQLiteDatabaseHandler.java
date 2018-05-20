/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 */
package library;

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
    private static final String DATABASE_NAME = "android_common";

    // Login table name
    private static final String TABLE_ADDRESS = "getAddress";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_CITY = "city";
    private static final String KEY_AREA = "area";
    private static final String KEY_ZIPCODE = "zipcode";
    private static final String KEY_MODIFYDATE = "modifydate";

    public SQLiteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_ADDRESS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_CITY + " TEXT,"
                + KEY_AREA + " TEXT,"
                + KEY_ZIPCODE + " TEXT,"
                + KEY_MODIFYDATE + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDRESS);
        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     */
    public void addAddress(String city, String area, String zipcode, String modifydate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CITY, city); // city
        values.put(KEY_AREA, area); // area
        values.put(KEY_ZIPCODE, zipcode); // zipcode
        values.put(KEY_MODIFYDATE, modifydate); // modifydate
        // Inserting Row
        db.insert(TABLE_ADDRESS, null, values);
        db.close(); // Closing database connection
    }

    /**
     * Storing user details in database
     */
    public void addAddressAll(ArrayList<Map<String, String>> datas, String modifydate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        for (int i = 0; i < datas.size(); i++) {
            values.put(KEY_CITY, datas.get(i).get("city")); // Email
            values.put(KEY_AREA, datas.get(i).get("area")); // Pass
            values.put(KEY_ZIPCODE, datas.get(i).get("zipcode")); // Phone
            values.put(KEY_MODIFYDATE, modifydate); // Phone
            db.insert(TABLE_ADDRESS, null, values);
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
            data.put("city", cursor.getString(1));
            data.put("area", cursor.getString(2));
            data.put("zipcode", cursor.getString(3));
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
        ArrayList<String> data=new ArrayList<>();
        String selectQuery = "SELECT distinct city FROM " + TABLE_ADDRESS;
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
        String selectQuery = "SELECT * FROM " + TABLE_ADDRESS + " where " + KEY_CITY + " not like '" + "%金%" + "'"+" and "+ KEY_CITY + " not like '" + "%連%" + "'"+" and "+ KEY_CITY + " not like '" + "%澎%" + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        while (cursor.moveToNext()) {
            data = new HashMap<>();
            data.put("city", cursor.getString(1));
            data.put("area", cursor.getString(2));
            data.put("zipcode", cursor.getString(3));
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
        ArrayList<String> data=new ArrayList<>();
        String selectQuery = "SELECT distinct city FROM " + TABLE_ADDRESS + " where " + KEY_CITY + " not like '" + "%金%" + "'"+" and "+ KEY_CITY + " not like '" + "%連%" + "'"+" and "+ KEY_CITY + " not like '" + "%澎%" + "'";
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
        ArrayList<String> data=new ArrayList<>();
        String selectQuery = "SELECT distinct city FROM " + TABLE_ADDRESS + " where " + KEY_CITY + " like '" + "%金%" + "'"+" or "+ KEY_CITY + " like '" + "%連%" + "'"+" or "+ KEY_CITY + " like '" + "%澎%" + "'";
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
        String selectQuery = "SELECT * FROM " + TABLE_ADDRESS + " where " + KEY_CITY + " like '" + "%金%" + "'"+" or "+ KEY_CITY + " like '" + "%連%" + "'"+" or "+ KEY_CITY + " like '" + "%澎%" + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        while (cursor.moveToNext()) {
            data = new HashMap<>();
            data.put("city", cursor.getString(1));
            data.put("area", cursor.getString(2));
            data.put("zipcode", cursor.getString(3));
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
        String selectQuery = "SELECT  * FROM " + TABLE_ADDRESS + " where " + KEY_CITY + "='" + city + "'" + " and " + KEY_AREA + " like '%" + area + "%'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        data = new HashMap<>();
        if (cursor.getCount() > 0) {
            data.put("city", cursor.getString(1));
            data.put("area", cursor.getString(2));
            data.put("zipcode", cursor.getString(3));
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
        String selectQuery = "SELECT  * FROM " + TABLE_ADDRESS + " where " + KEY_ZIPCODE + "='" + zipcode + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        data = new HashMap<>();
        if (cursor.getCount() > 0) {
            data.put("city", cursor.getString(1));
            data.put("area", cursor.getString(2));
            data.put("zipcode", cursor.getString(3));
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
    public int getRowCount() {
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
        String selectQuery = "SELECT  * FROM " + TABLE_ADDRESS + " where " + KEY_MODIFYDATE + "='" + modifydate + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        modifydate=null;
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
    public void resetTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_ADDRESS, null, null);
        db.close();
    }

}