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
import android.widget.Toast;

import com.example.alex.eip_product.SoapAPI.Analyze.Analyze_Order;

import java.util.ArrayList;
import java.util.List;

public class OrderDatabase extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "OrdersDatabase";

    //Type
    public static int TYPE_NORMAL = 0x01;
    public static int TYPE_EDIT = 0x02;


    // table name
    private static final String TABLE_Orders = "Orders";
    private static final String TABLE_OrderDetails = "OrderDetails";
    private static final String TABLE_CheckFailedReasons = "CheckFailedReasons";
    private static final String TABLE_OrderComments = "OrderComments";
    private static final String TABLE_OrderItemComments = "OrderItemComments";

    private static final String TABLE_OrdersEdit = "OrdersEdit";
    private static final String TABLE_OrderDetailsEdit = "OrderDetailsEdit";
    private static final String TABLE_CheckFailedReasonsEdit = "CheckFailedReasonsEdit";
    private static final String TABLE_OrderCommentsEdit = "OrderCommentsEdit";
    private static final String TABLE_OrderItemCommentsEdit = "OrderItemCommentsEdit";
    //Columns Orders
    public static final String KEY_isOrderUpdate = "isOrderUpdate";
    public static final String KEY_isOrderEdit = "isOrderEdit";

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
    public static final String KEY_HasCompleted = "HasCompleted";
    public static final String KEY_Inspector = "Inspector";
    public static final String KEY_InspectorDate = "InspectorDate";
    public static final String KEY_VendorInspector = "VendorInspector";
    public static final String KEY_VendorInspectorDate = "VendorInspectorDate";
    public static final String KEY_FeedbackPerson = "FeedbackPerson";
    public static final String KEY_FeedbackRecommendations = "FeedbackRecommendations";
    public static final String KEY_FeedbackDate = "FeedbackDate";
    public static final String KEY_InspectionNumber = "InspectionNumber";

    public static final String KEY_OrderDetails = "OrderDetails";
    public static final String KEY_OrderComments = "OrderComments";
    public static final String KEY_OrderItemComments = "OrderItemComments";
    //Columns OrderDetails
    public static final String KEY_LineNumber = "LineNumber";
    public static final String KEY_Item = "Item";
    public static final String KEY_OrderQty = "OrderQty";
    public static final String KEY_Qty = "Qty";
    public static final String KEY_SampleNumber = "SampleNumber";
    public static final String KEY_Uom = "Uom";
    public static final String KEY_Size = "Size";
    public static final String KEY_Functions = "Functions";
    public static final String KEY_Surface = "Surface";
    public static final String KEY_Package = "Package";
    public static final String KEY_CheckPass = "CheckPass";
    public static final String KEY_Special = "Special";
    public static final String KEY_Rework = "Rework";
    public static final String KEY_Reject = "Reject";
    public static final String KEY_MainMarK = "MainMarK";
    public static final String KEY_SideMarK = "SideMarK";
    public static final String KEY_ReCheckDate = "ReCheckDate";
    public static final String KEY_Remarks = "Remarks";
    //Columns CheckFailedReasons
    public static final String KEY_CheckFailedReasons = "CheckFailedReasons";
    public static final String KEY_ReasonCode = "ReasonCode";
    public static final String KEY_ReasonDescr = "ReasonDescr";
    //Columns OrderComments
    public static final String KEY_Comment = "Comment";
    //Columns OrderItemComments
    public static final String KEY_ItemNo = "ItemNo";
    //Create Table
    private static final String CREATE_Orders_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_Orders + "("
            + KEY_ID + " INTEGER PRIMARY KEY" + ","
            + KEY_PONumber + " TEXT" + ","
            + KEY_POVersion + " TEXT" + ","
            + KEY_PlanCheckDate + " TEXT" + ","
            + KEY_VendorCode + " TEXT" + ","
            + KEY_VendorName + " TEXT" + ","
            + KEY_Area + " TEXT" + ","
            + KEY_Notes + " TEXT" + ","
            + KEY_Shipping + " TEXT" + ","
            + KEY_SalesMan + " TEXT" + ","
            + KEY_Phone + " TEXT" + ","
            + KEY_CheckMan + " TEXT" + ","
            + KEY_HasCompleted + " BOOLEAN" + ","
            + KEY_Inspector + " TEXT" + ","
            + KEY_InspectorDate + " TEXT" + ","
            + KEY_VendorInspector + " BLOB" + ","
            + KEY_VendorInspectorDate + " TEXT" + ","
            + KEY_FeedbackPerson + " TEXT" + ","
            + KEY_FeedbackRecommendations + " TEXT" + ","
            + KEY_FeedbackDate + " TEXT" + ","
            + KEY_InspectionNumber + " TEXT" + ","
            + KEY_OrderDetails + " TEXT" + ","
            + KEY_CheckFailedReasons + " TEXT" + ","
            + KEY_OrderComments + " TEXT" + ","
            + KEY_OrderItemComments + " TEXT" + ")";
    private static final String CREATE_CheckFailedReasons_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CheckFailedReasons + "("
            + KEY_ID + " INTEGER PRIMARY KEY" + ","
            + KEY_PONumber + " TEXT" + ","
            + KEY_POVersion + " TEXT" + ","
            + KEY_Item + " TEXT" + ","
            + KEY_ReasonCode + " TEXT" + ","
            + KEY_ReasonDescr + " TEXT" + ")";
    private static final String CREATE_OrderDetails_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_OrderDetails + "("
            + KEY_ID + " INTEGER PRIMARY KEY" + ","
            + KEY_PONumber + " TEXT" + ","
            + KEY_POVersion + " TEXT" + ","
            + KEY_LineNumber + " INTEGER" + ","
            + KEY_Item + " TEXT" + ","
            + KEY_OrderQty + " FLOAT" + ","
            + KEY_Qty + " FLOAT" + ","
            + KEY_SampleNumber + " INTEGER" + ","
            + KEY_Uom + " TEXT" + ","
            + KEY_Size + " INTEGER" + ","
            + KEY_Functions + " INTEGER" + ","
            + KEY_Surface + " INTEGER" + ","
            + KEY_Package + " INTEGER" + ","
            + KEY_CheckPass + " BOOLEAN" + ","
            + KEY_Special + " BOOLEAN" + ","
            + KEY_Rework + " BOOLEAN" + ","
            + KEY_Reject + " BOOLEAN" + ","
            + KEY_MainMarK + " BOOLEAN" + ","
            + KEY_SideMarK + " BOOLEAN" + ","
            + KEY_ReCheckDate + " TEXT" + ","
            + KEY_Remarks + " TEXT" + ")";
    private static final String CREATE_OrderComments_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_OrderComments + "("
            + KEY_ID + " INTEGER PRIMARY KEY" + ","
            + KEY_PONumber + " TEXT" + ","
            + KEY_POVersion + " TEXT" + ","
            + KEY_LineNumber + " INTEGER" + ","
            + KEY_Comment + " TEXT" + ")";
    private static final String CREATE_OrderItemComments_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_OrderItemComments + "("
            + KEY_ID + " INTEGER PRIMARY KEY" + ","
            + KEY_PONumber + " TEXT" + ","
            + KEY_POVersion + " TEXT" + ","
            + KEY_LineNumber + " INTEGER" + ","
            + KEY_ItemNo + " TEXT" + ","
            + KEY_Comment + " TEXT" + ")";

    //Create TableEdit
    private static final String CREATE_OrdersEdit_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_OrdersEdit + "("
            + KEY_ID + " INTEGER PRIMARY KEY" + ","
            + KEY_PONumber + " TEXT" + ","
            + KEY_POVersion + " TEXT" + ","
            + KEY_PlanCheckDate + " TEXT" + ","
            + KEY_VendorCode + " TEXT" + ","
            + KEY_VendorName + " TEXT" + ","
            + KEY_Area + " TEXT" + ","
            + KEY_Notes + " TEXT" + ","
            + KEY_Shipping + " TEXT" + ","
            + KEY_SalesMan + " TEXT" + ","
            + KEY_Phone + " TEXT" + ","
            + KEY_CheckMan + " TEXT" + ","
            + KEY_HasCompleted + " BOOLEAN" + ","
            + KEY_Inspector + " TEXT" + ","
            + KEY_InspectorDate + " TEXT" + ","
            + KEY_VendorInspector + " BLOB" + ","
            + KEY_VendorInspectorDate + " TEXT" + ","
            + KEY_FeedbackPerson + " TEXT" + ","
            + KEY_FeedbackRecommendations + " TEXT" + ","
            + KEY_FeedbackDate + " TEXT" + ","
            + KEY_InspectionNumber + " TEXT" + ","
            + KEY_OrderDetails + " TEXT" + ","
            + KEY_CheckFailedReasons + " TEXT" + ","
            + KEY_OrderComments + " TEXT" + ","
            + KEY_OrderItemComments + " TEXT" + ","
            + KEY_isOrderEdit + " BOOLEAN" + ","
            + KEY_isOrderUpdate + " BOOLEAN" + ")";
    private static final String CREATE_CheckFailedReasonsEdit_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CheckFailedReasonsEdit + "("
            + KEY_ID + " INTEGER PRIMARY KEY" + ","
            + KEY_PONumber + " TEXT" + ","
            + KEY_POVersion + " TEXT" + ","
            + KEY_Item + " TEXT" + ","
            + KEY_ReasonCode + " TEXT" + ","
            + KEY_ReasonDescr + " TEXT" + ","
            + KEY_isOrderEdit + " BOOLEAN" + ","
            + KEY_isOrderUpdate + " BOOLEAN" + ")";
    private static final String CREATE_OrderDetailsEdit_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_OrderDetailsEdit + "("
            + KEY_ID + " INTEGER PRIMARY KEY" + ","
            + KEY_PONumber + " TEXT" + ","
            + KEY_POVersion + " TEXT" + ","
            + KEY_LineNumber + " INTEGER" + ","
            + KEY_Item + " TEXT" + ","
            + KEY_OrderQty + " FLOAT" + ","
            + KEY_Qty + " FLOAT" + ","
            + KEY_SampleNumber + " INTEGER" + ","
            + KEY_Uom + " TEXT" + ","
            + KEY_Size + " INTEGER" + ","
            + KEY_Functions + " INTEGER" + ","
            + KEY_Surface + " INTEGER" + ","
            + KEY_Package + " INTEGER" + ","
            + KEY_CheckPass + " BOOLEAN" + ","
            + KEY_Special + " BOOLEAN" + ","
            + KEY_Rework + " BOOLEAN" + ","
            + KEY_Reject + " BOOLEAN" + ","
            + KEY_MainMarK + " BOOLEAN" + ","
            + KEY_SideMarK + " BOOLEAN" + ","
            + KEY_ReCheckDate + " TEXT" + ","
            + KEY_Remarks + " TEXT" + ","
            + KEY_isOrderEdit + " BOOLEAN" + ","
            + KEY_isOrderUpdate + " BOOLEAN" + ")";
    private static final String CREATE_OrderCommentsEdit_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_OrderCommentsEdit + "("
            + KEY_ID + " INTEGER PRIMARY KEY" + ","
            + KEY_PONumber + " TEXT" + ","
            + KEY_POVersion + " TEXT" + ","
            + KEY_LineNumber + " INTEGER" + ","
            + KEY_Comment + " TEXT" + ","
            + KEY_isOrderEdit + " BOOLEAN" + ","
            + KEY_isOrderUpdate + " BOOLEAN" + ")";
    private static final String CREATE_OrderItemCommentsEdit_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_OrderItemCommentsEdit + "("
            + KEY_ID + " INTEGER PRIMARY KEY" + ","
            + KEY_PONumber + " TEXT" + ","
            + KEY_POVersion + " TEXT" + ","
            + KEY_LineNumber + " INTEGER" + ","
            + KEY_ItemNo + " TEXT" + ","
            + KEY_Comment + " TEXT" + ","
            + KEY_isOrderEdit + " BOOLEAN" + ","
            + KEY_isOrderUpdate + " BOOLEAN" + ")";
    private Context ctx;

    public OrderDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.ctx = context;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_Orders_TABLE);
        db.execSQL(CREATE_OrderDetails_TABLE);
        db.execSQL(CREATE_CheckFailedReasons_TABLE);
        db.execSQL(CREATE_OrderComments_TABLE);
        db.execSQL(CREATE_OrderItemComments_TABLE);

        db.execSQL(CREATE_OrdersEdit_TABLE);
        db.execSQL(CREATE_OrderDetailsEdit_TABLE);
        db.execSQL(CREATE_CheckFailedReasonsEdit_TABLE);
        db.execSQL(CREATE_OrderCommentsEdit_TABLE);
        db.execSQL(CREATE_OrderItemCommentsEdit_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Orders);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OrderDetails);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CheckFailedReasons);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OrderComments);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OrderItemComments);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OrdersEdit);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OrderDetailsEdit);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CheckFailedReasonsEdit);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OrderCommentsEdit);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OrderItemCommentsEdit);
        // Create tables again
        onCreate(db);
    }

    /**
     * Add
     */
    public void addOrders(List<ContentValues> list) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction(); // 手动设置开始事务
        for (ContentValues v : list) {
            db.insert(TABLE_Orders, null, v);
            db.insert(TABLE_OrdersEdit, null, v);
        }
        db.setTransactionSuccessful(); // 设置事务处理成功，不设置会自动回滚不提交
        db.endTransaction(); // 处理完成
        db.close();
    }

    public void upDateOrders(List<ContentValues> list) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction(); // 手动设置开始事务
        for (ContentValues v : list) {
            db.insert(TABLE_Orders, null, v);
            int position = Analyze_Order.PONumberNames.indexOf(v.getAsString(KEY_PONumber));
            if (position != -1) {
                if (db.rawQuery("SELECT * FROM " + TABLE_Orders + " WHERE " +
                        KEY_PONumber + " = '" + v.getAsString(KEY_PONumber) + "'" + " AND " + KEY_HasCompleted + "= 1", null).getCount() > 0 ||
                        db.rawQuery("SELECT * FROM " + TABLE_OrdersEdit + " WHERE " +
                                KEY_PONumber + " = '" + v.getAsString(KEY_PONumber) + "'" + " AND " + KEY_isOrderEdit + "= 0", null).getCount() > 0) {
                    db.delete(TABLE_OrdersEdit, KEY_PONumber + " = '" + v.getAsString(KEY_PONumber) + "'", null);
                    db.insert(TABLE_OrdersEdit, null, v);
                }
            } else {
                db.insert(TABLE_OrdersEdit, null, v);
            }
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
            db.insert(TABLE_OrderDetailsEdit, null, v);
        }
        db.setTransactionSuccessful(); // 设置事务处理成功，不设置会自动回滚不提交
        db.endTransaction(); // 处理完成
        db.close();
    }

    public void upDateOrderDetails(List<ContentValues> list, List<String> PONumberNames) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction(); // 手动设置开始事务
        List<Boolean> resetList = new ArrayList<>();
        for (int i = 0; i < PONumberNames.size(); i++) {
            resetList.add(i, db.rawQuery("SELECT * FROM " + TABLE_Orders + " WHERE " +
                    KEY_PONumber + " = '" + PONumberNames.get(i) + "'" + " AND " + KEY_HasCompleted + "= 1", null).getCount() > 0 ||
                    db.rawQuery("SELECT * FROM " + TABLE_OrdersEdit + " WHERE " +
                            KEY_PONumber + " = '" + PONumberNames.get(i) + "'" + " AND " + KEY_isOrderEdit + "= 0", null).getCount() > 0);
        }
        for (ContentValues v : list) {
            db.insert(TABLE_OrderDetails, null, v);
            int position = PONumberNames.indexOf(v.getAsString(KEY_PONumber));
            if (position != -1) {
                if (resetList.get(position)) {
                    db.delete(TABLE_OrderDetailsEdit, KEY_PONumber + " = '" + v.getAsString(KEY_PONumber) + "'" + " AND " + KEY_LineNumber + " = '" + v.getAsString(KEY_LineNumber) + "'", null);
                    db.insert(TABLE_OrderDetailsEdit, null, v);
                }
            } else {
                db.insert(TABLE_OrderDetailsEdit, null, v);
            }
        }
        db.setTransactionSuccessful(); // 设置事务处理成功，不设置会自动回滚不提交
        db.endTransaction(); // 处理完成
        db.close();
    }

    public void addCheckFailedReasons(List<ContentValues> list) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction(); // 手动设置开始事务
        boolean resetable = false;
        if (list.size() > 0) {
            resetable = db.rawQuery("SELECT * FROM " + TABLE_Orders + " WHERE " + KEY_PONumber + " = '" + list.get(0).getAsString(KEY_PONumber) + "'" + " AND " + KEY_HasCompleted + " = 1 ", null).getCount() > 0;
        }
        if (resetable) {
            db.delete(TABLE_CheckFailedReasonsEdit, null, null);
            for (ContentValues v : list) {
                db.insert(TABLE_CheckFailedReasons, null, v);
                db.insert(TABLE_CheckFailedReasonsEdit, null, v);
            }
        } else {
            for (ContentValues v : list) {
                db.insert(TABLE_CheckFailedReasons, null, v);
            }
        }

        db.setTransactionSuccessful(); // 设置事务处理成功，不设置会自动回滚不提交
        db.endTransaction(); // 处理完成
        db.close();
    }

    public void addOrderComments(List<ContentValues> list) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction(); // 手动设置开始事务
        db.delete(TABLE_OrderCommentsEdit, null, null);
        for (ContentValues v : list) {
            db.insert(TABLE_OrderComments, null, v);
            db.insert(TABLE_OrderCommentsEdit, null, v);
        }
        db.setTransactionSuccessful(); // 设置事务处理成功，不设置会自动回滚不提交
        db.endTransaction(); // 处理完成
        db.close();
    }

    public void addOrderItemComments(List<ContentValues> list) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction(); // 手动设置开始事务
        db.delete(TABLE_OrderItemCommentsEdit, null, null);
        for (ContentValues v : list) {
            db.insert(TABLE_OrderItemComments, null, v);
            db.insert(TABLE_OrderItemCommentsEdit, null, v);
        }
        db.setTransactionSuccessful(); // 设置事务处理成功，不设置会自动回滚不提交
        db.endTransaction(); // 处理完成
        db.close();
    }

    /**
     * Search
     */
    public ArrayList<ContentValues> getOrdersByDate(String date) {
        ArrayList<ContentValues> datas = new ArrayList<>();
        ContentValues cv;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_OrdersEdit + " WHERE " + KEY_PlanCheckDate + " = '" + date + "' GROUP BY " + KEY_VendorCode + " ORDER BY " + KEY_PONumber + " ASC", null);
        //" AND " + KEY_Comment + " IS NOT NULL";
        while (cursor.moveToNext()) {
            cv = new ContentValues();
            cv.put(KEY_PONumber, cursor.getString(1));
            cv.put(KEY_POVersion, cursor.getString(2));
            cv.put(KEY_PlanCheckDate, cursor.getString(3));
            cv.put(KEY_VendorCode, cursor.getString(4));
            cv.put(KEY_VendorName, cursor.getString(5));
            cv.put(KEY_Area, cursor.getString(6));
            cv.put(KEY_Notes, cursor.getString(7));
            cv.put(KEY_Shipping, cursor.getString(8));
            cv.put(KEY_SalesMan, cursor.getString(9));
            cv.put(KEY_Phone, cursor.getString(10));
            cv.put(KEY_CheckMan, cursor.getString(11));

            cv.put(KEY_HasCompleted, cursor.getString(12));
            cv.put(KEY_Inspector, cursor.getString(13));
            cv.put(KEY_InspectorDate, cursor.getString(14));
            cv.put(KEY_VendorInspector, cursor.getBlob(15));
            cv.put(KEY_VendorInspectorDate, cursor.getString(16));
            cv.put(KEY_FeedbackPerson, cursor.getString(17));
            cv.put(KEY_FeedbackRecommendations, cursor.getString(18));
            cv.put(KEY_FeedbackDate, cursor.getString(19));
            cv.put(KEY_InspectionNumber, cursor.getString(20));

            cv.put(KEY_OrderDetails, cursor.getString(21));
            cv.put(KEY_CheckFailedReasons, cursor.getString(22));
            cv.put(KEY_OrderComments, cursor.getString(23));
            cv.put(KEY_OrderItemComments, cursor.getString(24));
            cv.put(KEY_isOrderEdit, cursor.getInt(25) > 0);
            cv.put(KEY_isOrderUpdate, cursor.getInt(26) > 0);
            datas.add(cv);
        }
        cursor.close();
        db.close();
        // return user
        return datas;
    }

    public ArrayList<ContentValues> getOrdersByDateAndVendorCode(String date, String VendorCode) {
        ArrayList<ContentValues> datas = new ArrayList<>();
        ContentValues cv;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_OrdersEdit + " WHERE " + KEY_PlanCheckDate + " = '" + date + "' AND " + KEY_VendorCode + " LIKE '%" + VendorCode + "%' GROUP BY " + KEY_VendorCode + " ORDER BY " + KEY_PONumber + " ASC", null);
        //" AND " + KEY_Comment + " IS NOT NULL";
        while (cursor.moveToNext()) {
            cv = new ContentValues();
            cv.put(KEY_PONumber, cursor.getString(1));
            cv.put(KEY_POVersion, cursor.getString(2));
            cv.put(KEY_PlanCheckDate, cursor.getString(3));
            cv.put(KEY_VendorCode, cursor.getString(4));
            cv.put(KEY_VendorName, cursor.getString(5));
            cv.put(KEY_Area, cursor.getString(6));
            cv.put(KEY_Notes, cursor.getString(7));
            cv.put(KEY_Shipping, cursor.getString(8));
            cv.put(KEY_SalesMan, cursor.getString(9));
            cv.put(KEY_Phone, cursor.getString(10));
            cv.put(KEY_CheckMan, cursor.getString(11));

            cv.put(KEY_HasCompleted, cursor.getString(12));
            cv.put(KEY_Inspector, cursor.getString(13));
            cv.put(KEY_InspectorDate, cursor.getString(14));
            cv.put(KEY_VendorInspector, cursor.getBlob(15));
            cv.put(KEY_VendorInspectorDate, cursor.getString(16));
            cv.put(KEY_FeedbackPerson, cursor.getString(17));
            cv.put(KEY_FeedbackRecommendations, cursor.getString(18));
            cv.put(KEY_FeedbackDate, cursor.getString(19));
            cv.put(KEY_InspectionNumber, cursor.getString(20));

            cv.put(KEY_OrderDetails, cursor.getString(21));
            cv.put(KEY_CheckFailedReasons, cursor.getString(22));
            cv.put(KEY_OrderComments, cursor.getString(23));
            cv.put(KEY_OrderItemComments, cursor.getString(24));
            cv.put(KEY_isOrderEdit, cursor.getInt(25) > 0);
            cv.put(KEY_isOrderUpdate, cursor.getInt(26) > 0);
            datas.add(cv);
        }
        cursor.close();
        db.close();
        // return user
        return datas;
    }

    /**
     * Search
     */
    public ArrayList<ContentValues> getOrdersByPONumber(String PONumber) {
        ArrayList<ContentValues> datas = new ArrayList<>();
        ContentValues cv;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_OrdersEdit + " WHERE " + KEY_PONumber + " = '" + PONumber + "'", null);
        //" AND " + KEY_Comment + " IS NOT NULL";
        while (cursor.moveToNext()) {
            cv = new ContentValues();
            cv.put(KEY_PONumber, cursor.getString(1));
            cv.put(KEY_POVersion, cursor.getString(2));
            cv.put(KEY_PlanCheckDate, cursor.getString(3));
            cv.put(KEY_VendorCode, cursor.getString(4));
            cv.put(KEY_VendorName, cursor.getString(5));
            cv.put(KEY_Area, cursor.getString(6));
            cv.put(KEY_Notes, cursor.getString(7));
            cv.put(KEY_Shipping, cursor.getString(8));
            cv.put(KEY_SalesMan, cursor.getString(9));
            cv.put(KEY_Phone, cursor.getString(10));
            cv.put(KEY_CheckMan, cursor.getString(11));

            cv.put(KEY_HasCompleted, cursor.getString(12));
            cv.put(KEY_Inspector, cursor.getString(13));
            cv.put(KEY_InspectorDate, cursor.getString(14));
            cv.put(KEY_VendorInspector, cursor.getBlob(15));
            cv.put(KEY_VendorInspectorDate, cursor.getString(16));
            cv.put(KEY_FeedbackPerson, cursor.getString(17));
            cv.put(KEY_FeedbackRecommendations, cursor.getString(18));
            cv.put(KEY_FeedbackDate, cursor.getString(19));
            cv.put(KEY_InspectionNumber, cursor.getString(20));

            cv.put(KEY_OrderDetails, cursor.getString(21));
            cv.put(KEY_CheckFailedReasons, cursor.getString(22));
            cv.put(KEY_OrderComments, cursor.getString(23));
            cv.put(KEY_OrderItemComments, cursor.getString(24));
            cv.put(KEY_isOrderEdit, cursor.getInt(25) > 0);
            cv.put(KEY_isOrderUpdate, cursor.getInt(26) > 0);
            datas.add(cv);
        }
        cursor.close();
        db.close();
        // return user
        return datas;
    }

    public ArrayList<ContentValues> getOrderDetailsByPONumber(String PONumber) {
        ArrayList<ContentValues> datas = new ArrayList<>();
        ContentValues cv;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_OrderDetailsEdit + " WHERE " + KEY_PONumber + " = '" + PONumber + "'", null);
        //" AND " + KEY_Comment + " IS NOT NULL";
        while (cursor.moveToNext()) {
            cv = new ContentValues();
            cv.put(KEY_PONumber, cursor.getString(1));
            cv.put(KEY_POVersion, cursor.getString(2));
            cv.put(KEY_LineNumber, cursor.getString(3));
            cv.put(KEY_Item, cursor.getString(4));
            cv.put(KEY_OrderQty, cursor.getString(5));
            cv.put(KEY_Qty, cursor.getString(6));
            cv.put(KEY_SampleNumber, cursor.getString(7));
            cv.put(KEY_Uom, cursor.getString(8));
            cv.put(KEY_Size, cursor.getString(9));
            cv.put(KEY_Functions, cursor.getString(10));
            cv.put(KEY_Surface, cursor.getString(11));
            cv.put(KEY_Package, cursor.getString(12));
            cv.put(KEY_CheckPass, cursor.getInt(13) > 0);
            cv.put(KEY_Special, cursor.getInt(14) > 0);
            cv.put(KEY_Rework, cursor.getInt(15) > 0);
            cv.put(KEY_Reject, cursor.getInt(16) > 0);
            cv.put(KEY_MainMarK, cursor.getInt(17) > 0);
            cv.put(KEY_SideMarK, cursor.getInt(18) > 0);
            cv.put(KEY_ReCheckDate, cursor.getString(19));
            cv.put(KEY_Remarks, cursor.getString(20));
            datas.add(cv);
        }
        cursor.close();
        db.close();
        // return user
        return datas;
    }

    public ArrayList<ContentValues> getCheckFailedReasonsByPONumber(String PONumber) {
        ArrayList<ContentValues> datas = new ArrayList<>();
        ContentValues cv;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CheckFailedReasons + " WHERE " + KEY_PONumber + " = '" + PONumber + "'", null);
        //" AND " + KEY_Comment + " IS NOT NULL";
        while (cursor.moveToNext()) {
            cv = new ContentValues();
            cv.put(KEY_PONumber, cursor.getString(1));
            cv.put(KEY_POVersion, cursor.getString(2));
            cv.put(KEY_Item, cursor.getString(3));
            cv.put(KEY_ReasonCode, cursor.getString(4));
            cv.put(KEY_ReasonDescr, cursor.getString(5));
            datas.add(cv);
        }
        cursor.close();
        db.close();
        // return user
        return datas;
    }

    public ArrayList<ContentValues> getOrderCommentsByPONumber(String PONumber) {
        ArrayList<ContentValues> datas = new ArrayList<>();
        ContentValues cv;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_OrderComments + " WHERE " + KEY_PONumber + " = '" + PONumber + "'", null);
        //" AND " + KEY_Comment + " IS NOT NULL";
        while (cursor.moveToNext()) {
            cv = new ContentValues();
            cv.put(KEY_PONumber, cursor.getString(1));
            cv.put(KEY_POVersion, cursor.getString(2));
            cv.put(KEY_LineNumber, cursor.getString(3));
            cv.put(KEY_Comment, cursor.getString(4));
            datas.add(cv);
        }
        cursor.close();
        db.close();
        // return user
        return datas;
    }

    public ArrayList<ContentValues> getOrderItemCommentsByPONumber(String PONumber) {
        ArrayList<ContentValues> datas = new ArrayList<>();
        ContentValues cv;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_OrderItemComments + " WHERE " + KEY_PONumber + " = '" + PONumber + "'", null);
        //" AND " + KEY_Comment + " IS NOT NULL";
        while (cursor.moveToNext()) {
            cv = new ContentValues();
            cv.put(KEY_PONumber, cursor.getString(1));
            cv.put(KEY_POVersion, cursor.getString(2));
            cv.put(KEY_LineNumber, cursor.getString(3));
            cv.put(KEY_ItemNo, cursor.getString(4));
            cv.put(KEY_Comment, cursor.getString(5));
            datas.add(cv);
        }
        cursor.close();
        db.close();
        // return user
        return datas;
    }

    /**
     * TODO:SAVE OrderListEdit
     */
    //=========================================================================
    public void saveOrdersEditBasic(String PONumber, ContentValues edit_cv) {
        ContentValues cv;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_OrdersEdit + " WHERE " + KEY_PONumber + " = '" + PONumber + "'", null);

        if (cursor.getCount() > 0) { //存在修改後的訂單
            //比對最新資料與修改後資料是否有異
            cursor = db.rawQuery("SELECT " + KEY_PONumber + "," + KEY_POVersion + "," + KEY_PlanCheckDate + "," + KEY_VendorCode + "," +
                    KEY_VendorName + "," + KEY_Area + "," + KEY_Notes + "," + KEY_Shipping + "," + KEY_SalesMan + "," + KEY_Phone + "," + KEY_CheckMan + "," +
                    KEY_HasCompleted + "," + KEY_FeedbackPerson + "," + KEY_FeedbackRecommendations + "," + KEY_FeedbackDate + "," + KEY_InspectionNumber + " FROM " + TABLE_Orders + " WHERE " + KEY_PONumber + " = '" + PONumber + "'" +
                    " EXCEPT " +
                    "SELECT " + KEY_PONumber + "," + KEY_POVersion + "," + KEY_PlanCheckDate + "," + KEY_VendorCode + "," +
                    KEY_VendorName + "," + KEY_Area + "," + KEY_Notes + "," + KEY_Shipping + "," + KEY_SalesMan + "," + KEY_Phone + "," + KEY_CheckMan + "," +
                    KEY_HasCompleted + "," + KEY_FeedbackPerson + "," + KEY_FeedbackRecommendations + "," + KEY_FeedbackDate + "," + KEY_InspectionNumber + " FROM " + TABLE_OrdersEdit + " WHERE " + KEY_PONumber + " = '" + PONumber + "'", null);
            if (cursor.getCount() > 0) {
                cursor = db.rawQuery("SELECT * FROM " + TABLE_Orders + " WHERE " + KEY_PONumber + " = '" + PONumber + "'", null);
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    cv = new ContentValues();
                    cv.put(KEY_PONumber, cursor.getString(1));
                    cv.put(KEY_POVersion, cursor.getString(2));
                    cv.put(KEY_PlanCheckDate, cursor.getString(3));
                    cv.put(KEY_VendorCode, cursor.getString(4));
                    cv.put(KEY_VendorName, cursor.getString(5));
                    cv.put(KEY_Area, cursor.getString(6));
                    cv.put(KEY_Notes, cursor.getString(7));
                    cv.put(KEY_Shipping, cursor.getString(8));
                    cv.put(KEY_SalesMan, cursor.getString(9));
                    cv.put(KEY_Phone, cursor.getString(10));
                    cv.put(KEY_CheckMan, cursor.getString(11));
                    cv.put(KEY_HasCompleted, cursor.getString(12));
                /*
                cv.put(KEY_Inspector, cursor.getString(13));
                cv.put(KEY_InspectorDate, cursor.getString(14));
                cv.put(KEY_VendorInspector, cursor.getString(15));
                cv.put(KEY_VendorInspectorDate, cursor.getString(16));
                */
                    cv.put(KEY_FeedbackPerson, cursor.getString(17));
                    cv.put(KEY_FeedbackRecommendations, cursor.getString(18));
                    cv.put(KEY_FeedbackDate, cursor.getString(19));
                    cv.put(KEY_InspectionNumber, cursor.getString(20));
                     /*
                    cv.put(KEY_OrderDetails, cursor.getString(21));
                    cv.put(KEY_CheckFailedReasons, cursor.getString(22));
                    cv.put(KEY_OrderComments, cursor.getString(23));
                    cv.put(KEY_OrderItemComments, cursor.getString(24));
                    */
                    //插入驗貨人員修改的資料
                    if (edit_cv != null)
                        cv.putAll(edit_cv);
                    //將最新資料做更新
                    db.update(TABLE_OrdersEdit, cv, KEY_PONumber + " = '" + PONumber + "'", null);
                    Toast.makeText(ctx, "資料有變動", Toast.LENGTH_SHORT).show();
                }
            } else {
                db.update(TABLE_OrdersEdit, edit_cv, KEY_PONumber + " = '" + PONumber + "'", null);
                Toast.makeText(ctx, "資料儲存成功", Toast.LENGTH_SHORT).show();
            }
        } else {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_Orders + " WHERE " + KEY_PONumber + " = '" + PONumber + "'", null);
            //" AND " + KEY_Comment + " IS NOT NULL";
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                cv = new ContentValues();
                cv.put(KEY_PONumber, cursor.getString(1));
                cv.put(KEY_POVersion, cursor.getString(2));
                cv.put(KEY_PlanCheckDate, cursor.getString(3));
                cv.put(KEY_VendorCode, cursor.getString(4));
                cv.put(KEY_VendorName, cursor.getString(5));
                cv.put(KEY_Area, cursor.getString(6));
                cv.put(KEY_Notes, cursor.getString(7));
                cv.put(KEY_Shipping, cursor.getString(8));
                cv.put(KEY_SalesMan, cursor.getString(9));
                cv.put(KEY_Phone, cursor.getString(10));
                cv.put(KEY_CheckMan, cursor.getString(11));
                cv.put(KEY_HasCompleted, cursor.getString(12));

                cv.put(KEY_Inspector, cursor.getString(13));
                cv.put(KEY_InspectorDate, cursor.getString(14));
                cv.put(KEY_VendorInspector, cursor.getString(15));
                cv.put(KEY_VendorInspectorDate, cursor.getString(16));

                cv.put(KEY_FeedbackPerson, cursor.getString(17));
                cv.put(KEY_FeedbackRecommendations, cursor.getString(18));
                cv.put(KEY_FeedbackDate, cursor.getString(19));
                cv.put(KEY_InspectionNumber, cursor.getString(20));

                cv.put(KEY_OrderDetails, cursor.getString(21));
                cv.put(KEY_CheckFailedReasons, cursor.getString(22));
                cv.put(KEY_OrderComments, cursor.getString(23));
                cv.put(KEY_OrderItemComments, cursor.getString(24));
                //插入驗貨人員修改的資料
                if (edit_cv != null)
                    cv.putAll(edit_cv);
                db.insert(TABLE_OrdersEdit, null, cv);
                Toast.makeText(ctx, "新增成功", Toast.LENGTH_SHORT).show();
            }
        }
        cursor.close();
        db.close();
        // return user
    }

    public void saveOrderDetailsEdit(String PONumber, ArrayList<ContentValues> edit_cv) {

        ContentValues cv;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_OrderDetailsEdit + " WHERE " + KEY_PONumber + " = '" + PONumber + "'", null);
        if (cursor.getCount() > 0) {
            cursor = db.rawQuery("SELECT " + KEY_PONumber + "," + KEY_POVersion + "," + KEY_LineNumber + "," + KEY_Item + "," +
                    KEY_OrderQty + "," + KEY_Qty + "," + KEY_SampleNumber + "," + KEY_Uom + " FROM " + TABLE_OrderDetails + " WHERE " + KEY_PONumber + " = '" + PONumber + "'" +
                    " EXCEPT " +
                    "SELECT " + KEY_PONumber + "," + KEY_POVersion + "," + KEY_LineNumber + "," + KEY_Item + "," +
                    KEY_OrderQty + "," + KEY_Qty + "," + KEY_SampleNumber + "," + KEY_Uom + " FROM " + TABLE_OrderDetailsEdit + " WHERE " + KEY_PONumber + " = '" + PONumber + "'", null);
            if (cursor.getCount() > 0) {
                cursor = db.rawQuery("SELECT * FROM " + TABLE_OrderDetails + " WHERE " + KEY_PONumber + " = '" + PONumber + "'", null);
                int i = 0;
                while (cursor.moveToNext()) {
                    cv = new ContentValues();
                    cv.put(KEY_PONumber, cursor.getString(1));
                    cv.put(KEY_POVersion, cursor.getString(2));
                    cv.put(KEY_LineNumber, cursor.getString(3));
                    cv.put(KEY_Item, cursor.getString(4));
                    cv.put(KEY_OrderQty, cursor.getString(5));
                    cv.put(KEY_Qty, cursor.getString(6));
                    cv.put(KEY_SampleNumber, cursor.getString(7));
                    cv.put(KEY_Uom, cursor.getString(8));
                /*
                cv.put(KEY_Size, cursor.getString(9));
                cv.put(KEY_Functions, cursor.getString(10));
                cv.put(KEY_Surface, cursor.getString(11));
                cv.put(KEY_Package, cursor.getString(12));
                cv.put(KEY_CheckPass, cursor.getString(13));
                cv.put(KEY_Special, cursor.getString(14));
                cv.put(KEY_Rework, cursor.getString(15));
                cv.put(KEY_Reject, cursor.getString(16));
                cv.put(KEY_MainMarK, cursor.getString(17));
                cv.put(KEY_SideMarK, cursor.getString(18));
                cv.put(KEY_ReCheckDate, cursor.getString(19));
                cv.put(KEY_Remarks, cursor.getString(20));
*/
                    //插入驗貨人員修改的資料
                    if (edit_cv != null && edit_cv.size() > 0)
                        cv.putAll(edit_cv.get(i));
                    //將最新資料做更新
                    db.update(TABLE_OrderDetailsEdit, cv, KEY_PONumber + " = '" + PONumber + "'" + " AND " + KEY_LineNumber + " = '" + cursor.getString(3) + "'", null);
                    i++;
                }
            } else {
                cursor = db.rawQuery("SELECT * FROM " + TABLE_OrderDetails + " WHERE " + KEY_PONumber + " = '" + PONumber + "'", null);
                int i = 0;
                while (cursor.moveToNext()) {
                    //插入驗貨人員修改的資料
                    if (edit_cv != null && edit_cv.size() > 0) {
                        db.update(TABLE_OrderDetailsEdit, edit_cv.get(i), KEY_PONumber + " = '" + PONumber + "'" + " AND " + KEY_LineNumber + " = '" + cursor.getString(3) + "'", null);
                    }
                    i++;
                }
                Toast.makeText(ctx, "資料儲存成功", Toast.LENGTH_SHORT).show();
            }
        } else {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_OrderDetails + " WHERE " + KEY_PONumber + " = '" + PONumber + "'", null);
            //" AND " + KEY_Comment + " IS NOT NULL";
            int i = 0;
            while (cursor.moveToNext()) {
                cv = new ContentValues();
                cv.put(KEY_PONumber, cursor.getString(1));
                cv.put(KEY_POVersion, cursor.getString(2));
                cv.put(KEY_LineNumber, cursor.getString(3));
                cv.put(KEY_Item, cursor.getString(4));
                cv.put(KEY_OrderQty, cursor.getString(5));
                cv.put(KEY_Qty, cursor.getString(6));
                cv.put(KEY_SampleNumber, cursor.getString(7));
                cv.put(KEY_Uom, cursor.getString(8));
                /*
                cv.put(KEY_Size, cursor.getString(9));
                cv.put(KEY_Functions, cursor.getString(10));
                cv.put(KEY_Surface, cursor.getString(11));
                cv.put(KEY_Package, cursor.getString(12));
                cv.put(KEY_CheckPass, cursor.getString(13));
                cv.put(KEY_Special, cursor.getString(14));
                cv.put(KEY_Rework, cursor.getString(15));
                cv.put(KEY_Reject, cursor.getString(16));
                cv.put(KEY_MainMarK, cursor.getString(17));
                cv.put(KEY_SideMarK, cursor.getString(18));
                cv.put(KEY_ReCheckDate, cursor.getString(19));
                cv.put(KEY_Remarks, cursor.getString(20));
                */
                //插入驗貨人員修改的資料
                if (edit_cv != null && edit_cv.size() > 0)
                    cv.putAll(edit_cv.get(i));
                db.insert(TABLE_OrderDetailsEdit, null, cv);
                i++;
            }
        }
        cursor.close();
        db.close();
    }

    public void saveCheckFailedReasonsEdit(String PONumber) {
        ContentValues cv;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CheckFailedReasonsEdit + " WHERE " + KEY_PONumber + " = '" + PONumber + "'", null);
        if (cursor.getCount() > 0) {

        } else {
            /*
            cursor = db.rawQuery("SELECT * FROM " + TABLE_CheckFailedReasons + " WHERE " + KEY_PONumber + " = '" + PONumber + "'", null);
            //" AND " + KEY_Comment + " IS NOT NULL";
            while (cursor.moveToNext()) {
                cv = new ContentValues();
                cv.put(KEY_PONumber, cursor.getString(1));
                cv.put(KEY_POVersion, cursor.getString(2));
                cv.put(KEY_Item, cursor.getString(3));
                cv.put(KEY_ReasonCode, cursor.getString(4));
                cv.put(KEY_ReasonDescr, cursor.getString(5));
                db.insert(TABLE_CheckFailedReasonsEdit, null, cv);
            }
            */
        }
        cursor.close();
        db.close();
    }

    public void saveOrderCommentsEdit(String PONumber) {
        ContentValues cv;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_OrderCommentsEdit + " WHERE " + KEY_PONumber + " = '" + PONumber + "'", null);
        if (cursor.getCount() > 0) {

        } else {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_OrderComments + " WHERE " + KEY_PONumber + " = '" + PONumber + "'", null);
            //" AND " + KEY_Comment + " IS NOT NULL";
            while (cursor.moveToNext()) {
                cv = new ContentValues();
                cv.put(KEY_PONumber, cursor.getString(1));
                cv.put(KEY_POVersion, cursor.getString(2));
                cv.put(KEY_LineNumber, cursor.getString(3));
                cv.put(KEY_Comment, cursor.getString(4));
                db.insert(TABLE_OrderCommentsEdit, null, cv);
            }
        }
        cursor.close();
        db.close();
    }

    public void saveOrderItemCommentsEdit(String PONumber) {
        ContentValues cv;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_OrderItemCommentsEdit + " WHERE " + KEY_PONumber + " = '" + PONumber + "'", null);
        if (cursor.getCount() > 0) {

        } else {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_OrderItemComments + " WHERE " + KEY_PONumber + " = '" + PONumber + "'", null);
            //" AND " + KEY_Comment + " IS NOT NULL";
            while (cursor.moveToNext()) {
                cv = new ContentValues();
                cv.put(KEY_PONumber, cursor.getString(1));
                cv.put(KEY_POVersion, cursor.getString(2));
                cv.put(KEY_LineNumber, cursor.getString(3));
                cv.put(KEY_ItemNo, cursor.getString(4));
                cv.put(KEY_Comment, cursor.getString(5));
                db.insert(TABLE_OrderItemCommentsEdit, null, cv);
            }
        }
        cursor.close();
        db.close();
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

    public int countCheckFailedReasons() {
        String selectQuery = "SELECT * FROM " + TABLE_CheckFailedReasons;
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

    public int countOrdersEdit() {
        String selectQuery = "SELECT * FROM " + TABLE_OrdersEdit;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        // return user
        return count;
    }

    public int countOrderDetailsEdit() {
        String selectQuery = "SELECT * FROM " + TABLE_OrderDetailsEdit;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        // return user
        return count;
    }

    public int countCheckFailedReasonsEdit() {
        String selectQuery = "SELECT * FROM " + TABLE_CheckFailedReasonsEdit;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        // return user
        return count;
    }

    public int countOrderCommentsEdit() {
        String selectQuery = "SELECT * FROM " + TABLE_OrderCommentsEdit;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        // return user
        return count;
    }

    public int countOrderItemCommentsEdit() {
        String selectQuery = "SELECT * FROM " + TABLE_OrderItemCommentsEdit;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        // return user
        return count;
    }

    /**
     * Update
     */
    public void updateUserInfo(String id, String area) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_Area, area);
        // Inserting Row
        db.update(TABLE_Orders, values, KEY_ID + "=" + id, null);
        db.close(); // Closing database connection
    }

    /**
     * Delete
     */
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