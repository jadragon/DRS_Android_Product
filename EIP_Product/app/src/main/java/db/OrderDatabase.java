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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDatabase extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "OrdersDatabase";

    // table name
    private static final String TABLE_Orders = "Orders";
    private static final String TABLE_OrderDetails = "OrderDetails";
    private static final String TABLE_OrderComments = "OrderComments";
    private static final String TABLE_OrderItemComments = "OrderItemComments";
    //Columns Orders
    public static final String KEY_ID = "_id";
    public static final String KEY_PONumber = "PONumber";
    public static final String KEY_POVersion = "POVersion";
    public static final String KEY_PlanCheckDate = "PlanCheckDate";
    public static final String KEY_VendorCode = "VendorCode";
    public static final String KEY_VendorName = "VendorName";
    public static final String KEY_Area = "Area";
    public static final String KEY_Notes = "Notes";
    public static final String KEY_Shipping = "Shipping";
    public static final String KEY_SalesMan = "SalesMan";
    public static final String KEY_Phone = "Phone";
    public static final String KEY_CheckMan = "CheckMan";
    public static final String KEY_OrderDetails = "OrderDetails";
    public static final String KEY_OrderComments = "OrderComments";
    public static final String KEY_OrderItemComments = "OrderItemComments";
    //Columns OrderDetails
    public static final String KEY_LineNumber = "LineNumber";
    public static final String KEY_Item = "Item";
    public static final String KEY_OrderQty = "OrderQty";
    public static final String KEY_Qty = "Qty";
    public static final String KEY_Uom = "Uom";
    //Columns OrderComments
    public static final String KEY_Comment = "Comment";
    //Columns OrderItemComments
    public static final String KEY_ItemNo = "ItemNo";

    private static final String CREATE_Orders_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_Orders + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_PONumber + " TEXT,"
            + KEY_POVersion + " TEXT,"
            + KEY_PlanCheckDate + " TEXT,"
            + KEY_VendorCode + " TEXT,"
            + KEY_VendorName + " TEXT,"
            + KEY_Area + " TEXT,"
            + KEY_Notes + " TEXT,"
            + KEY_Shipping + " TEXT,"
            + KEY_SalesMan + " TEXT,"
            + KEY_Phone + " TEXT,"
            + KEY_CheckMan + " TEXT,"
            + KEY_OrderDetails + " TEXT,"
            + KEY_OrderComments + " TEXT,"
            + KEY_OrderItemComments + " TEXT" + ")";
    private static final String CREATE_OrderDetails_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_OrderDetails + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_PONumber + " TEXT,"
            + KEY_POVersion + " TEXT,"
            + KEY_LineNumber + " INTEGER,"
            + KEY_Item + " TEXT,"
            + KEY_OrderQty + " FLOAT,"
            + KEY_Qty + " FLOAT,"
            + KEY_Uom + " TEXT" + ")";
    private static final String CREATE_OrderComments_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_OrderComments + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_PONumber + " TEXT,"
            + KEY_POVersion + " TEXT,"
            + KEY_LineNumber + " INTEGER,"
            + KEY_Comment + " TEXT" + ")";
    private static final String CREATE_OrderItemComments_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_OrderItemComments + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_PONumber + " TEXT,"
            + KEY_POVersion + " TEXT,"
            + KEY_LineNumber + " INTEGER,"
            + KEY_ItemNo + " TEXT,"
            + KEY_Comment + " TEXT" + ")";

    public OrderDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_Orders_TABLE);
        db.execSQL(CREATE_OrderDetails_TABLE);
        db.execSQL(CREATE_OrderComments_TABLE);
        db.execSQL(CREATE_OrderItemComments_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Orders);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OrderDetails);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OrderComments);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OrderItemComments);
        // Create tables again
        onCreate(db);
    }

    /*
        public void addOrders(String PONumber, String POVersion, String PlanCheckDate, String VendorCode,
                              String VendorName, String Area, String Notes, String Shipping,
                              String SalesMan, String Phone, String CheckMan, String OrderDetails,
                              String OrderComments, String OrderItemComments) {
            ContentValues values = new ContentValues();
            values.put(KEY_PONumber, PONumber);
            values.put(KEY_POVersion, POVersion);
            values.put(KEY_PlanCheckDate, PlanCheckDate);
            values.put(KEY_VendorName, VendorCode);
            values.put(KEY_VendorCode, VendorName);
            values.put(KEY_Area, Area);
            values.put(KEY_Notes, Notes);
            values.put(KEY_Shipping, Shipping);
            values.put(KEY_SalesMan, SalesMan);
            values.put(KEY_Phone, Phone);
            values.put(KEY_CheckMan, CheckMan);
            values.put(KEY_OrderDetails, OrderDetails);
            values.put(KEY_OrderComments, OrderComments);
            values.put(KEY_OrderItemComments, OrderItemComments);
            this.getWritableDatabase().insert(TABLE_Orders, null, values);
        }
    */
    public void addOrders(List<ContentValues> list) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction(); // 手动设置开始事务
        for (ContentValues v : list) {
            db.insert(TABLE_Orders, null, v);
        }
        db.setTransactionSuccessful(); // 设置事务处理成功，不设置会自动回滚不提交
        db.endTransaction(); // 处理完成
        db.close();
    }

    public void addOrderDetails(List<ContentValues> list) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction(); // 手动设置开始事务
        for (ContentValues v : list) {
            db.insert(TABLE_OrderDetails, null, v);
        }
        db.setTransactionSuccessful(); // 设置事务处理成功，不设置会自动回滚不提交
        db.endTransaction(); // 处理完成
        db.close();
    }

    public void addOrderComments(List<ContentValues> list) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction(); // 手动设置开始事务
        for (ContentValues v : list) {
            db.insert(TABLE_OrderComments, null, v);
        }
        db.setTransactionSuccessful(); // 设置事务处理成功，不设置会自动回滚不提交
        db.endTransaction(); // 处理完成
        db.close();
    }

    public void addOrderItemComments(List<ContentValues> list) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction(); // 手动设置开始事务
        for (ContentValues v : list) {
            db.insert(TABLE_OrderItemComments, null, v);
        }
        db.setTransactionSuccessful(); // 设置事务处理成功，不设置会自动回滚不提交
        db.endTransaction(); // 处理完成
        db.close();
    }

    public ArrayList<String> getTypes() {
        ArrayList<String> datas = new ArrayList<>();
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT  DISTINCT " + KEY_PONumber + " FROM " + TABLE_Orders, null);
        while (cursor.moveToNext()) {
            datas.add(cursor.getString(0));
        }
        cursor.close();
        // return user
        return datas;
    }

    public int countOrders() {
        String selectQuery = "SELECT * FROM " + TABLE_Orders;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        // return user
        return count;
    }

    public int countOrderDetails() {
        String selectQuery = "SELECT * FROM " + TABLE_OrderDetails;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        // return user
        return count;
    }

    public int countOrderComments() {
        String selectQuery = "SELECT * FROM " + TABLE_OrderComments;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        // return user
        return count;
    }

    public int countOrderItemComments() {
        String selectQuery = "SELECT * FROM " + TABLE_OrderItemComments;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        // return user
        return count;
    }

    public ArrayList<String> getWinnerNames(String ponumber) {
        ArrayList<String> datas = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_Orders + " WHERE " + KEY_PONumber + " = '" + ponumber + "' AND " + KEY_Comment + " IS NOT NULL";
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

    public void updateUserInfo(String id, String area) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_Area, area);
        // Inserting Row
        db.update(TABLE_Orders, values, KEY_ID + "=" + id, null);
        db.close(); // Closing database connection
    }

    public void resetTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_Orders, null, null);
        db.delete(TABLE_OrderDetails, null, null);
        db.delete(TABLE_OrderComments, null, null);
        db.delete(TABLE_OrderItemComments, null, null);
        db.close();
    }

}