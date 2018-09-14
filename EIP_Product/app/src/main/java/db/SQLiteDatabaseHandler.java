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

import com.example.alex.eip_product.R;

import java.util.ArrayList;

public class SQLiteDatabaseHandler extends SQLiteOpenHelper {
    Context context;
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "android_common";


    // Insepect table name
    private static final String TABLE_INSEPECT = "getInsepect";
    // Insepect Table Columns names
    private static final String KEY_ID = "_id";
    private static final String KEY_STATUS = "status";
    private static final String KEY_IMAGE = "image";
    private static final String CREATE_INSEPECT_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_INSEPECT + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_STATUS + " INTEGER ,"
            + KEY_IMAGE + " BLOB" + ")";

    // Failed table name
    private static final String TABLE_FAILED = "getFailed";
    // Failed Table Columns names
    private static final String KEY_TYPE = "type";
    private static final String KEY_DESCRIPTION = "description";
    private static final String CREATE_FAILED_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_FAILED + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TYPE + " INTEGER ,"
            + KEY_DESCRIPTION + " TEXT" + ")";

    public SQLiteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_INSEPECT_TABLE);
        db.execSQL(CREATE_FAILED_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSEPECT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAILED);
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
    public void updatePhotoStatus(int id, int status) {
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

    public void initFailTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String[] type = context.getResources().getStringArray(R.array.type1);
        for (String description : type) {
            values.put(KEY_TYPE, 0);
            values.put(KEY_DESCRIPTION, description);
            db.insert(TABLE_FAILED, null, values);
        }
        type = context.getResources().getStringArray(R.array.type2);
        for (String description : type) {
            values.put(KEY_TYPE, 1);
            values.put(KEY_DESCRIPTION, description);
            db.insert(TABLE_FAILED, null, values);
        }
        type = context.getResources().getStringArray(R.array.type3);
        for (String description : type) {
            values.put(KEY_TYPE, 2);
            values.put(KEY_DESCRIPTION, description);
            db.insert(TABLE_FAILED, null, values);
        }
        type = context.getResources().getStringArray(R.array.type4);
        for (String description : type) {
            values.put(KEY_TYPE, 3);
            values.put(KEY_DESCRIPTION, description);
            db.insert(TABLE_FAILED, null, values);
        }
        db.close(); // Closing database connection
    }

    public void addFailTableItem(int type, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TYPE, type);
        values.put(KEY_DESCRIPTION, description);
        db.insert(TABLE_FAILED, null, values);
        db.close(); // Closing database connection
    }

    public ArrayList<String> getFailedDescription(int type) {
        ArrayList<String> arrayList = new ArrayList<>();
        String selectQuery = "SELECT  " + KEY_DESCRIPTION + " FROM " + TABLE_FAILED + " WHERE " + KEY_TYPE + " = " + type;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {
            arrayList.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        // return user
        return arrayList;
    }

}