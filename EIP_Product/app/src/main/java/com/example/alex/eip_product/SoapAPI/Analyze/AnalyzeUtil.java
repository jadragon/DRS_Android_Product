package com.example.alex.eip_product.SoapAPI.Analyze;

import org.json.JSONException;
import org.json.JSONObject;

public class AnalyzeUtil {

    public static boolean checkSuccess(JSONObject json) {
        try {
            return json.getBoolean("IsSuccess");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getMessage(JSONObject json) {
        try {
            return json.getString("Message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "伺服器異常";
    }
}