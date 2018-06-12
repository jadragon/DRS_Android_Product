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

    // Address table name
    private static final String TABLE_ADDRESS = "getAddress";
    // Address Table Columns names
    private static final String KEY_AD_ID = "id";
    private static final String KEY_CITY = "city";
    private static final String KEY_AREA = "area";
    private static final String KEY_ZIPCODE = "zipcode";
    private static final String KEY_MODIFYDATE = "modifydate";
    private static final String CREATE_ADDRESS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_ADDRESS + "("
            + KEY_AD_ID + " INTEGER PRIMARY KEY,"
            + KEY_CITY + " TEXT,"
            + KEY_AREA + " TEXT,"
            + KEY_ZIPCODE + " TEXT,"
            + KEY_MODIFYDATE + " TEXT" + ")";

    // Login table name
    private static final String TABLE_MEMBER = "getMember";
    // Login Table Columns names
    private static final String KEY_LG_ID = "id";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_ACCOUNT = "account";
    private static final String KEY_NAME = "name";
    private static final String KEY_PHOTO = "photo";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_BACKGROUND = "background";
    private static final String CREATE_LOGIN_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_MEMBER + "("
            + KEY_LG_ID + " INTEGER PRIMARY KEY,"
            + KEY_TOKEN + " TEXT,"
            + KEY_ACCOUNT + " TEXT,"
            + KEY_NAME + " TEXT,"
            + KEY_PHOTO + " TEXT,"
            + KEY_IMAGE + " BLOB,"
            + KEY_BACKGROUND + " TEXT" + ")";
    //Bank table name
    private static final String TABLE_BANK = "getBank";
    // Bank Table Columns names
    private static final String KEY_BK_ID = "id";
    private static final String KEY_BCODE = "bcode";
    private static final String KEY_BNAME = "bname";
    private static final String CREATE_BANK_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_BANK + "("
            + KEY_BK_ID + " INTEGER PRIMARY KEY,"
            + KEY_BCODE + " TEXT,"
            + KEY_BNAME + " TEXT" + ")";

    public SQLiteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_LOGIN_TABLE);
        db.execSQL(CREATE_ADDRESS_TABLE);
        db.execSQL(CREATE_BANK_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDRESS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BANK);
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
        ArrayList<String> data = new ArrayList<>();
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
        String selectQuery = "SELECT * FROM " + TABLE_ADDRESS + " where " + KEY_CITY + " not like '" + "%金%" + "'" + " and " + KEY_CITY + " not like '" + "%連%" + "'" + " and " + KEY_CITY + " not like '" + "%澎%" + "'";
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
        ArrayList<String> data = new ArrayList<>();
        String selectQuery = "SELECT distinct city FROM " + TABLE_ADDRESS + " where " + KEY_CITY + " not like '" + "%金%" + "'" + " and " + KEY_CITY + " not like '" + "%連%" + "'" + " and " + KEY_CITY + " not like '" + "%澎%" + "'";
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
        String selectQuery = "SELECT distinct city FROM " + TABLE_ADDRESS + " where " + KEY_CITY + " like '" + "%金%" + "'" + " or " + KEY_CITY + " like '" + "%連%" + "'" + " or " + KEY_CITY + " like '" + "%澎%" + "'";
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
        String selectQuery = "SELECT * FROM " + TABLE_ADDRESS + " where " + KEY_CITY + " like '" + "%金%" + "'" + " or " + KEY_CITY + " like '" + "%連%" + "'" + " or " + KEY_CITY + " like '" + "%澎%" + "'";
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
        city = city.substring(0, 1).equals("台") ? city.replace("台", "臺") : city;
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
    public ArrayList<String> getAreaByCity(String city) {
        ArrayList<String> data = new ArrayList<>();
        String selectQuery = "SELECT " + KEY_AREA + "  FROM " + TABLE_ADDRESS + " where " + KEY_CITY + "='" + city + "'";
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
        String selectQuery = "SELECT  * FROM " + TABLE_ADDRESS + " where " + KEY_MODIFYDATE + "='" + modifydate + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        modifydate = null;
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

    /**
     * Storing user details in database
     */
    public void addMember(String token, String account, String name, String photo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TOKEN, token); // token
        values.put(KEY_ACCOUNT, account); // account
        values.put(KEY_NAME, name); // name
        values.put(KEY_PHOTO, photo); // photo
        // Inserting Row
        db.insert(TABLE_MEMBER, null, values);
        db.close(); // Closing database connection
    }

    /**
     * Storing user details in database
     */
    public void updateBackground(String background) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_BACKGROUND, background); // background
        // Inserting Row
        db.update(TABLE_MEMBER, values, KEY_LG_ID + "=" + 1, null);
        db.close(); // Closing database connection
    }

    /**
     * Getting user data from database
     */
    public Map<String, String> getMemberDetail() {
        Map<String, String> data;
        String selectQuery = "SELECT  * FROM " + TABLE_MEMBER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        data = new HashMap<>();
        if (cursor.getCount() > 0) {
            data.put("token", cursor.getString(1));
            data.put("account", cursor.getString(2));
            data.put("name", cursor.getString(3));
            data.put("photo", cursor.getString(4));
            data.put("background", cursor.getString(6));
        }
        cursor.close();
        db.close();
        // return user
        return data;
    }

    /**
     * Storing user details in database
     */
    public void updatePhotoImage(byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_IMAGE, image); // background
        // Inserting Row
        db.update(TABLE_MEMBER, values, KEY_LG_ID + "=" + 1, null);
        db.close(); // Closing database connection
    }

    public byte[] getPhotoImage() {
        String selectQuery = "SELECT  "+KEY_IMAGE+" FROM " + TABLE_MEMBER;
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
    public void addBankAll(ArrayList<Map<String, String>> datas) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        for (int i = 0; i < datas.size(); i++) {
            values.put(KEY_BCODE, datas.get(i).get("bcode")); // bcode
            values.put(KEY_BNAME, datas.get(i).get("bname")); // bname
            db.insert(TABLE_BANK, null, values);
        }
        // Inserting Row
        db.close(); // Closing database connection
    }

    /**
     * Getting user data from database
     */
    public ArrayList<Map<String, String>> getBankDetail() {
        ArrayList<Map<String, String>> datas = new ArrayList<>();
        Map<String, String> map;
        String selectQuery = "SELECT  * FROM " + TABLE_BANK;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        // Move to first row
        while (cursor.moveToNext()) {
            map = new HashMap<>();
            map.put("bcode", cursor.getString(1));
            map.put("bname", cursor.getString(2));
            datas.add(map);
        }
        cursor.close();
        db.close();
        // return user
        return datas;
    }


    /**
     * Getting user login status
     * return true if rows are there in table
     */
    public int getBankRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_BANK;
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
    public void resetBankTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_BANK, null, null);
        db.close();
    }
}