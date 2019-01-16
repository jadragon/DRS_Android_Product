package com.example.alex.eip_product.SoapAPI.Analyze;

import android.content.ContentValues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Utils.StringUtils;

import static db.OrderDatabase.KEY_Area;
import static db.OrderDatabase.KEY_CheckFailedReasons;
import static db.OrderDatabase.KEY_CheckMan;
import static db.OrderDatabase.KEY_CheckPass;
import static db.OrderDatabase.KEY_Comment;
import static db.OrderDatabase.KEY_Extension;
import static db.OrderDatabase.KEY_FeedbackDate;
import static db.OrderDatabase.KEY_FeedbackPerson;
import static db.OrderDatabase.KEY_FeedbackRecommendations;
import static db.OrderDatabase.KEY_FileName;
import static db.OrderDatabase.KEY_FilePath;
import static db.OrderDatabase.KEY_Functions;
import static db.OrderDatabase.KEY_HasCompleted;
import static db.OrderDatabase.KEY_InspectionNumber;
import static db.OrderDatabase.KEY_Inspector;
import static db.OrderDatabase.KEY_InspectorDate;
import static db.OrderDatabase.KEY_Item;
import static db.OrderDatabase.KEY_ItemNo;
import static db.OrderDatabase.KEY_LineNumber;
import static db.OrderDatabase.KEY_MainMarK;
import static db.OrderDatabase.KEY_Notes;
import static db.OrderDatabase.KEY_OrderComments;
import static db.OrderDatabase.KEY_OrderDetails;
import static db.OrderDatabase.KEY_OrderItemComments;
import static db.OrderDatabase.KEY_OrderQty;
import static db.OrderDatabase.KEY_PONumber;
import static db.OrderDatabase.KEY_POVersion;
import static db.OrderDatabase.KEY_Package;
import static db.OrderDatabase.KEY_Phone;
import static db.OrderDatabase.KEY_PlanCheckDate;
import static db.OrderDatabase.KEY_Qty;
import static db.OrderDatabase.KEY_ReCheckDate;
import static db.OrderDatabase.KEY_ReasonCode;
import static db.OrderDatabase.KEY_ReasonDescr;
import static db.OrderDatabase.KEY_Reject;
import static db.OrderDatabase.KEY_Remarks;
import static db.OrderDatabase.KEY_Rework;
import static db.OrderDatabase.KEY_SalesMan;
import static db.OrderDatabase.KEY_SampleNumber;
import static db.OrderDatabase.KEY_Shipping;
import static db.OrderDatabase.KEY_SideMarK;
import static db.OrderDatabase.KEY_Size;
import static db.OrderDatabase.KEY_Special;
import static db.OrderDatabase.KEY_Surface;
import static db.OrderDatabase.KEY_Uom;
import static db.OrderDatabase.KEY_VendorCode;
import static db.OrderDatabase.KEY_VendorInspector;
import static db.OrderDatabase.KEY_VendorInspectorDate;
import static db.OrderDatabase.KEY_VendorName;

public class Analyze_Order {

    /**
     * 3.1	訂單資訊
     */
    public static Map<String, List<ContentValues>> getOrders(JSONObject json) throws JSONException {
        Map<String, List<ContentValues>> map = new HashMap<>();
        List<ContentValues> list = new ArrayList<>();
        map.put("Orders", list);
        list = new ArrayList<>();
        map.put(KEY_OrderDetails, list);
        list = new ArrayList<>();
        map.put(KEY_CheckFailedReasons, list);
        list = new ArrayList<>();
        map.put(KEY_OrderComments, list);
        list = new ArrayList<>();
        map.put(KEY_OrderItemComments, list);
        if (json.getBoolean("IsSuccess")) {
            ContentValues contentValues;
            JSONArray jsonArray = json.getJSONArray("Orders");
            JSONArray jsonInnerArray;
            JSONObject json_obj;
            for (int i = 0; i < jsonArray.length(); i++) {
                contentValues = new ContentValues();
                json_obj = jsonArray.getJSONObject(i);
                contentValues.put(KEY_PONumber, json_obj.getString(KEY_PONumber));
                contentValues.put(KEY_POVersion, json_obj.getString(KEY_POVersion));
                contentValues.put(KEY_PlanCheckDate, json_obj.getString(KEY_PlanCheckDate));
                contentValues.put(KEY_VendorCode, json_obj.getString(KEY_VendorCode));
                contentValues.put(KEY_VendorName, json_obj.getString(KEY_VendorName));
                contentValues.put(KEY_Area, json_obj.getString(KEY_Area));
                contentValues.put(KEY_Notes, json_obj.getString(KEY_Notes));
                contentValues.put(KEY_Shipping, json_obj.getString(KEY_Shipping));
                contentValues.put(KEY_SalesMan, json_obj.getString(KEY_SalesMan));
                contentValues.put(KEY_Phone, json_obj.getString(KEY_Phone));
                contentValues.put(KEY_CheckMan, json_obj.getString(KEY_CheckMan));
                contentValues.put(KEY_HasCompleted, json_obj.getString(KEY_HasCompleted));
                contentValues.put(KEY_Inspector, json_obj.getString(KEY_Inspector));
                contentValues.put(KEY_InspectorDate, json_obj.getString(KEY_InspectorDate));
                contentValues.put(KEY_VendorInspector, StringUtils.decodeBase64(json_obj.getString(KEY_VendorInspector)));
                contentValues.put(KEY_VendorInspectorDate, json_obj.getString(KEY_VendorInspectorDate));
                contentValues.put(KEY_FeedbackPerson, json_obj.getString(KEY_FeedbackPerson));
                contentValues.put(KEY_FeedbackRecommendations, json_obj.getString(KEY_FeedbackRecommendations));
                contentValues.put(KEY_FeedbackDate, json_obj.getString(KEY_FeedbackDate));
                contentValues.put(KEY_InspectionNumber, json_obj.getString(KEY_InspectionNumber));
                contentValues.put(KEY_OrderDetails, json_obj.getString(KEY_PONumber));
                contentValues.put(KEY_CheckFailedReasons, json_obj.getString(KEY_PONumber));
                contentValues.put(KEY_OrderComments, json_obj.getString(KEY_PONumber));
                contentValues.put(KEY_OrderItemComments, json_obj.getString(KEY_PONumber));
                map.get("Orders").add(contentValues);
                //----------------------------------
                jsonInnerArray = json_obj.getJSONArray(KEY_OrderDetails);
                map.get(KEY_OrderDetails).addAll(getOrderDetails(jsonInnerArray));
                jsonInnerArray = json_obj.getJSONArray(KEY_CheckFailedReasons);
                map.get(KEY_CheckFailedReasons).addAll(getCheckFailedReasons(jsonInnerArray));
                jsonInnerArray = json_obj.getJSONArray(KEY_OrderComments);
                map.get(KEY_OrderComments).addAll(getOrderComments(jsonInnerArray));
                jsonInnerArray = json_obj.getJSONArray(KEY_OrderItemComments);
                map.get(KEY_OrderItemComments).addAll(getOrderItemComments(jsonInnerArray));
            }

        }
        return map;
    }

    /**
     * 3.1	訂單資訊(PONumber)
     */
    public static Map<String, List<ContentValues>> getOrders(JSONObject json, String PONumber) throws JSONException {
        Map<String, List<ContentValues>> map = new HashMap<>();
        List<ContentValues> list = new ArrayList<>();
        map.put("Orders", list);
        list = new ArrayList<>();
        map.put(KEY_OrderDetails, list);
        list = new ArrayList<>();
        map.put(KEY_CheckFailedReasons, list);
        list = new ArrayList<>();
        map.put(KEY_OrderComments, list);
        list = new ArrayList<>();
        map.put(KEY_OrderItemComments, list);
        if (json.getBoolean("IsSuccess")) {
            ContentValues contentValues;
            JSONArray jsonArray = json.getJSONArray("Orders");
            JSONArray jsonInnerArray;
            JSONObject json_obj;
            for (int i = 0; i < jsonArray.length(); i++) {
                contentValues = new ContentValues();
                json_obj = jsonArray.getJSONObject(i);
                if (json_obj.getString(KEY_PONumber).equals(PONumber)) {
                    contentValues.put(KEY_PONumber, json_obj.getString(KEY_PONumber));
                    contentValues.put(KEY_POVersion, json_obj.getString(KEY_POVersion));
                    contentValues.put(KEY_PlanCheckDate, json_obj.getString(KEY_PlanCheckDate));
                    contentValues.put(KEY_VendorCode, json_obj.getString(KEY_VendorCode));
                    contentValues.put(KEY_VendorName, json_obj.getString(KEY_VendorName));
                    contentValues.put(KEY_Area, json_obj.getString(KEY_Area));
                    contentValues.put(KEY_Notes, json_obj.getString(KEY_Notes));
                    contentValues.put(KEY_Shipping, json_obj.getString(KEY_Shipping));
                    contentValues.put(KEY_SalesMan, json_obj.getString(KEY_SalesMan));
                    contentValues.put(KEY_Phone, json_obj.getString(KEY_Phone));
                    contentValues.put(KEY_CheckMan, json_obj.getString(KEY_CheckMan));
                    contentValues.put(KEY_HasCompleted, json_obj.getString(KEY_HasCompleted));
                    contentValues.put(KEY_Inspector, json_obj.getString(KEY_Inspector));
                    contentValues.put(KEY_InspectorDate, json_obj.getString(KEY_InspectorDate));
                    contentValues.put(KEY_VendorInspector, StringUtils.decodeBase64(json_obj.getString(KEY_VendorInspector)));
                    contentValues.put(KEY_VendorInspectorDate, json_obj.getString(KEY_VendorInspectorDate));
                    contentValues.put(KEY_FeedbackPerson, json_obj.getString(KEY_FeedbackPerson));
                    contentValues.put(KEY_FeedbackRecommendations, json_obj.getString(KEY_FeedbackRecommendations));
                    contentValues.put(KEY_FeedbackDate, json_obj.getString(KEY_FeedbackDate));
                    contentValues.put(KEY_InspectionNumber, json_obj.getString(KEY_InspectionNumber));
                    contentValues.put(KEY_OrderDetails, json_obj.getString(KEY_PONumber));
                    contentValues.put(KEY_CheckFailedReasons, json_obj.getString(KEY_PONumber));
                    contentValues.put(KEY_OrderComments, json_obj.getString(KEY_PONumber));
                    contentValues.put(KEY_OrderItemComments, json_obj.getString(KEY_PONumber));
                    map.get("Orders").add(contentValues);
                    //----------------------------------
                    jsonInnerArray = json_obj.getJSONArray(KEY_OrderDetails);
                    map.get(KEY_OrderDetails).addAll(getOrderDetails(jsonInnerArray));
                    jsonInnerArray = json_obj.getJSONArray(KEY_CheckFailedReasons);
                    map.get(KEY_CheckFailedReasons).addAll(getCheckFailedReasons(jsonInnerArray));
                    jsonInnerArray = json_obj.getJSONArray(KEY_OrderComments);
                    map.get(KEY_OrderComments).addAll(getOrderComments(jsonInnerArray));
                    jsonInnerArray = json_obj.getJSONArray(KEY_OrderItemComments);
                    map.get(KEY_OrderItemComments).addAll(getOrderItemComments(jsonInnerArray));
                    break;
                }
            }

        }
        return map;
    }

    private static List<ContentValues> getOrderDetails(JSONArray jsonArray) throws JSONException {

        List<ContentValues> list = new ArrayList<>();
        ContentValues contentValues;
        JSONObject json_obj;
        for (int i = 0; i < jsonArray.length(); i++) {
            contentValues = new ContentValues();
            json_obj = jsonArray.getJSONObject(i);
            contentValues.put(KEY_PONumber, json_obj.getString(KEY_PONumber));
            contentValues.put(KEY_POVersion, json_obj.getString(KEY_POVersion));
            contentValues.put(KEY_LineNumber, json_obj.getString(KEY_LineNumber));
            contentValues.put(KEY_Item, json_obj.getString(KEY_Item));
            contentValues.put(KEY_OrderQty, json_obj.getString(KEY_OrderQty));
            contentValues.put(KEY_Qty, json_obj.getString(KEY_Qty));
            contentValues.put(KEY_SampleNumber, json_obj.getString(KEY_SampleNumber));
            contentValues.put(KEY_Uom, json_obj.getString(KEY_Uom));
            contentValues.put(KEY_Size, json_obj.getString(KEY_Size));
            contentValues.put(KEY_Functions, json_obj.getString(KEY_Functions));
            contentValues.put(KEY_Surface, json_obj.getString(KEY_Surface));
            contentValues.put(KEY_Package, json_obj.getString(KEY_Package));
            contentValues.put(KEY_CheckPass, json_obj.getBoolean(KEY_CheckPass));
            contentValues.put(KEY_Special, json_obj.getBoolean(KEY_Special));
            contentValues.put(KEY_Rework, json_obj.getBoolean(KEY_Rework));
            contentValues.put(KEY_Reject, json_obj.getBoolean(KEY_Reject));
            contentValues.put(KEY_MainMarK, json_obj.getBoolean(KEY_MainMarK));
            contentValues.put(KEY_SideMarK, json_obj.getBoolean(KEY_SideMarK));
            contentValues.put(KEY_ReCheckDate, json_obj.getString(KEY_ReCheckDate));
            contentValues.put(KEY_Remarks, json_obj.getString(KEY_Remarks));

            list.add(contentValues);
        }
        return list;
    }

    private static List<ContentValues> getCheckFailedReasons(JSONArray jsonArray) throws JSONException {

        List<ContentValues> list = new ArrayList<>();
        ContentValues contentValues;
        JSONObject json_obj;
        for (int i = 0; i < jsonArray.length(); i++) {
            contentValues = new ContentValues();
            json_obj = jsonArray.getJSONObject(i);
            contentValues.put(KEY_PONumber, json_obj.getString(KEY_PONumber));
            contentValues.put(KEY_POVersion, json_obj.getString(KEY_POVersion));
            contentValues.put(KEY_Item, json_obj.getString(KEY_Item));
            contentValues.put(KEY_ReasonCode, json_obj.getString(KEY_ReasonCode));
            contentValues.put(KEY_ReasonDescr, json_obj.getString(KEY_ReasonDescr));
            list.add(contentValues);
        }
        return list;
    }

    private static List<ContentValues> getOrderComments(JSONArray jsonArray) throws JSONException {
        List<ContentValues> list = new ArrayList<>();
        ContentValues contentValues;
        JSONObject json_obj;
        for (int i = 0; i < jsonArray.length(); i++) {
            contentValues = new ContentValues();
            json_obj = jsonArray.getJSONObject(i);
            contentValues.put(KEY_PONumber, json_obj.getString(KEY_PONumber));
            contentValues.put(KEY_POVersion, json_obj.getString(KEY_POVersion));
            contentValues.put(KEY_LineNumber, json_obj.getString(KEY_LineNumber));
            contentValues.put(KEY_Comment, json_obj.getString(KEY_Comment));
            list.add(contentValues);
        }
        return list;
    }

    private static List<ContentValues> getOrderItemComments(JSONArray jsonArray) throws JSONException {
        List<ContentValues> list = new ArrayList<>();
        ContentValues contentValues;
        JSONObject json_obj;
        for (int i = 0; i < jsonArray.length(); i++) {
            contentValues = new ContentValues();
            json_obj = jsonArray.getJSONObject(i);
            contentValues.put(KEY_PONumber, json_obj.getString(KEY_PONumber));
            contentValues.put(KEY_POVersion, json_obj.getString(KEY_POVersion));
            contentValues.put(KEY_LineNumber, json_obj.getString(KEY_LineNumber));
            contentValues.put(KEY_ItemNo, json_obj.getString(KEY_ItemNo));
            contentValues.put(KEY_Comment, json_obj.getString(KEY_Comment));
            list.add(contentValues);
        }
        return list;
    }

    public static List<ContentValues> getItemDrawings(JSONObject json) throws JSONException {
        List<ContentValues> list = new ArrayList<>();
        if (json.getBoolean("IsSuccess")) {
            ContentValues contentValues;
            JSONArray jsonArray = json.getJSONArray("ItemDrawings");
            JSONObject json_obj;
            for (int i = 0; i < jsonArray.length(); i++) {
                contentValues = new ContentValues();
                json_obj = jsonArray.getJSONObject(i);
                contentValues.put(KEY_Item, json_obj.getString(KEY_Item));
                contentValues.put(KEY_FileName, json_obj.getString(KEY_FileName));
                contentValues.put(KEY_Extension, json_obj.getString(KEY_Extension));
                list.add(contentValues);
            }
        }
        return list;
    }

    public static List<ContentValues> getFiles(JSONObject json) throws JSONException {
        List<ContentValues> list = new ArrayList<>();
        if (json.getBoolean("IsSuccess")) {
            ContentValues contentValues;
            JSONArray jsonArray = json.getJSONArray("Files");
            JSONObject json_obj;
            String file_path;
            for (int i = 0; i < jsonArray.length(); i++) {
                contentValues = new ContentValues();
                json_obj = jsonArray.getJSONObject(i);
                contentValues.put(KEY_FileName, json_obj.getString(KEY_FileName));
                contentValues.put(KEY_Extension, json_obj.getString(KEY_Extension));
                file_path = json_obj.getString(KEY_FilePath);
                contentValues.put(KEY_FilePath, file_path != null ? file_path.replace(" ", "%20") : "");
                list.add(contentValues);
            }
        }
        return list;
    }

}
