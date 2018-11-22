package com.example.alex.eip_product.SoapAPI.Analyze;

import android.content.ContentValues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static db.OrderDatabase.KEY_Area;
import static db.OrderDatabase.KEY_CheckMan;
import static db.OrderDatabase.KEY_Notes;
import static db.OrderDatabase.KEY_OrderComments;
import static db.OrderDatabase.KEY_OrderDetails;
import static db.OrderDatabase.KEY_OrderItemComments;
import static db.OrderDatabase.KEY_PONumber;
import static db.OrderDatabase.KEY_POVersion;
import static db.OrderDatabase.KEY_Phone;
import static db.OrderDatabase.KEY_PlanCheckDate;
import static db.OrderDatabase.KEY_SalesMan;
import static db.OrderDatabase.KEY_Shipping;
import static db.OrderDatabase.KEY_VendorCode;
import static db.OrderDatabase.KEY_VendorName;
import static db.OrderDatabase.KEY_LineNumber;
import static db.OrderDatabase.KEY_Item;
import static db.OrderDatabase.KEY_OrderQty;
import static db.OrderDatabase.KEY_Qty;
import static db.OrderDatabase.KEY_Uom;
import static db.OrderDatabase.KEY_Comment;
import static db.OrderDatabase.KEY_ItemNo;

public class Analyze_Order {

    /**
     * 3.1	訂單資訊
     */
    public static Map<String, List<ContentValues>> getOrders(JSONObject json) throws JSONException {
        Map<String, List<ContentValues>> map = new HashMap<>();
        List<ContentValues> list = new ArrayList<>();
        map.put("Orders", list);
        list = new ArrayList<>();
        map.put("OrderDetails", list);
        list = new ArrayList<>();
        map.put("OrderComments", list);
        list = new ArrayList<>();
        map.put("OrderItemComments", list);
        if (json.getBoolean("IsSuccess")) {
            ContentValues contentValues;
            JSONArray jsonArray = json.getJSONArray("Orders");
            JSONArray jsonInnerArray;
            JSONObject json_obj;
            for (int i = 0; i < jsonArray.length(); i++) {
                contentValues = new ContentValues();
                json_obj = jsonArray.getJSONObject(i);
                contentValues.put(KEY_PONumber, json_obj.getString("PONumber"));
                contentValues.put(KEY_POVersion, json_obj.getString("POVersion"));
                contentValues.put(KEY_PlanCheckDate, json_obj.getString("PlanCheckDate"));
                contentValues.put(KEY_VendorCode, json_obj.getString("VendorCode"));
                contentValues.put(KEY_VendorName, json_obj.getString("VendorName"));
                contentValues.put(KEY_Area, json_obj.getString("Area"));
                contentValues.put(KEY_Notes, json_obj.getString("Notes"));
                contentValues.put(KEY_Shipping, json_obj.getString("Shipping"));
                contentValues.put(KEY_SalesMan, json_obj.getString("SalesMan"));
                contentValues.put(KEY_Phone, json_obj.getString("Phone"));
                contentValues.put(KEY_CheckMan, json_obj.getString("CheckMan"));
                contentValues.put(KEY_OrderDetails, json_obj.getString("PONumber"));
                contentValues.put(KEY_OrderComments, json_obj.getString("PONumber"));
                contentValues.put(KEY_OrderItemComments, json_obj.getString("PONumber"));
                map.get("Orders").add(contentValues);
                //----------------------------------
                jsonInnerArray = json_obj.getJSONArray("OrderDetails");
                map.get("OrderDetails").addAll(getOrderDetails(jsonInnerArray));
                jsonInnerArray = json_obj.getJSONArray("OrderComments");
                map.get("OrderComments").addAll(getOrderComments(jsonInnerArray));
                jsonInnerArray = json_obj.getJSONArray("OrderItemComments");
                map.get("OrderItemComments").addAll(getOrderItemComments(jsonInnerArray));
            }
        }
        return map;
    }

    public static List<ContentValues> getOrderDetails(JSONArray jsonArray) throws JSONException {

        List<ContentValues> list = new ArrayList<>();
        ContentValues contentValues;
        JSONObject json_obj;
        for (int i = 0; i < jsonArray.length(); i++) {
            contentValues = new ContentValues();
            json_obj = jsonArray.getJSONObject(i);
            contentValues.put(KEY_PONumber, json_obj.getString("PONumber"));
            contentValues.put(KEY_POVersion, json_obj.getString("POVersion"));
            contentValues.put(KEY_LineNumber, json_obj.getString("LineNumber"));
            contentValues.put(KEY_Item, json_obj.getString("Item"));
            contentValues.put(KEY_OrderQty, json_obj.getString("OrderQty"));
            contentValues.put(KEY_Qty, json_obj.getString("Qty"));
            contentValues.put(KEY_Uom, json_obj.getString("Uom"));
            list.add(contentValues);
        }
        return list;
    }

    public static List<ContentValues> getOrderComments(JSONArray jsonArray) throws JSONException {
        List<ContentValues> list = new ArrayList<>();
        ContentValues contentValues;
        JSONObject json_obj;
        for (int i = 0; i < jsonArray.length(); i++) {
            contentValues = new ContentValues();
            json_obj = jsonArray.getJSONObject(i);
            contentValues.put(KEY_PONumber, json_obj.getString("PONumber"));
            contentValues.put(KEY_POVersion, json_obj.getString("POVersion"));
            contentValues.put(KEY_LineNumber, json_obj.getString("LineNumber"));
            contentValues.put(KEY_Comment, json_obj.getString("Comment"));
            list.add(contentValues);
        }
        return list;
    }

    public static List<ContentValues> getOrderItemComments(JSONArray jsonArray) throws JSONException {
        List<ContentValues> list = new ArrayList<>();
        ContentValues contentValues;
        JSONObject json_obj;
        for (int i = 0; i < jsonArray.length(); i++) {
            contentValues = new ContentValues();
            json_obj = jsonArray.getJSONObject(i);
            contentValues.put(KEY_PONumber, json_obj.getString("PONumber"));
            contentValues.put(KEY_POVersion, json_obj.getString("POVersion"));
            contentValues.put(KEY_LineNumber, json_obj.getString("LineNumber"));
            contentValues.put(KEY_ItemNo, json_obj.getString("ItemNo"));
            contentValues.put(KEY_Comment, json_obj.getString("Comment"));
            list.add(contentValues);
        }
        return list;
    }

}
