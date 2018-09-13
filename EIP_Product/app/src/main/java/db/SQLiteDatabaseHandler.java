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

import java.util.ArrayList;

public class SQLiteDatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "android_common";


    // Login table name
    private static final String TABLE_INSEPECT = "getInsepect";
    // Login Table Columns names
    private static final String KEY_ID = "_id";
    private static final String KEY_STATUS = "status";
    private static final String KEY_IMAGE = "image";
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_INSEPECT + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_STATUS + " INTEGER ,"
            + KEY_IMAGE + " BLOB" + ")";

    public SQLiteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSEPECT);
        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     */
    public void addImage(byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_IMAGE, image);
        values.put(KEY_STATUS, 0);
        // Inserting Row
        db.insert(TABLE_INSEPECT, null, values);
        db.close(); // Closing database connection
    }

    public ArrayList<ImagePojo> getPhotoImage() {
        ArrayList<ImagePojo> arrayList = new ArrayList<>();
        String selectQuery = "SELECT  " + KEY_ID + "," + KEY_IMAGE + " FROM " + TABLE_INSEPECT + " WHERE " + KEY_STATUS + " = 0";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {
            arrayList.add(new ImagePojo(cursor.getInt(0), cursor.getBlob(1)));
        }
        cursor.close();
        db.close();
        // return user
        return arrayList;
    }

    /**
     * Storing user details in database
     */
    public void updatePhotoStatus(int id,int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_STATUS, status);
        // Inserting Row
        db.update(TABLE_INSEPECT, values, KEY_ID + "=" + id, null);
        db.close(); // Closing database connection
    }

    /**
     * Re crate database
     * Delete all tables and create them again
     */
    public void resetInsepectTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_INSEPECT, null, null);
        db.close();
    }

}