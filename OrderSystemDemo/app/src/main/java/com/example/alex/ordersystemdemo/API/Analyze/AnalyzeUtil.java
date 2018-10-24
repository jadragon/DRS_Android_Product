package com.example.alex.ordersystemdemo.API.Analyze;

import org.json.JSONException;
import org.json.JSONObject;

public class AnalyzeUtil {

    public static boolean checkSuccess(JSONObject json) {
        try {
            return json.getBoolean("Success");
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