/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 */
package db;

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
    private static final String KEY_NAME = "name";
    private static final String KEY_EN = "en";
    private static final String KEY_DU_NO = "du_no";
    private static final String KEY_DNAME = "dname";
    private static final String KEY_S_NO = "s_no";
    private static final String KEY_STORE = "store";

    private static final String CREATE_ADDRESS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_ITEM + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT,"
            + KEY_EN + " TEXT,"
            + KEY_DU_NO + " TEXT,"
            + KEY_DNAME + " TEXT,"
            + KEY_S_NO + " TEXT,"
            + KEY_STORE + " TEXT" + ")";

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
    public void addItem(String name, String en, String du_no, String dname, String s_no, String store) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_EN, en);
        values.put(KEY_DU_NO, du_no);
        values.put(KEY_DNAME, dname);
        values.put(KEY_S_NO, s_no);
        values.put(KEY_STORE, store);
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
            data.put(KEY_NAME, cursor.getString(1));
            data.put(KEY_EN, cursor.getString(2));
            data.put(KEY_DU_NO, cursor.getString(3));
            data.put(KEY_DNAME, cursor.getString(4));
            data.put(KEY_S_NO, cursor.getString(5));
            data.put(KEY_STORE, cursor.getString(6));
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
    public void updateUserInfo(String name, String en, String du_no, String dname, String s_no, String store) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_EN, en);
        values.put(KEY_DU_NO, du_no);
        values.put(KEY_DNAME, dname);
        values.put(KEY_S_NO, s_no);
        values.put(KEY_STORE, store);
        // Inserting Row
        db.update(TABLE_ITEM, values, KEY_ID + "=" + 0, null);
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