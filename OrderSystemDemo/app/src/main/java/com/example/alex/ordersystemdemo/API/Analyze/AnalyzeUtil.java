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

    public static String getToken(JSONObject json, int type) {
        try {
            json = json.getJSONObject("Data");
            switch (type) {
                case 0:
                    //student
                    return json.getString("m_id");
                case 1:
                    //store
                    return json.getString("s_id");
                case 2:
                    //deliver
                    return json.getString("d_id");

            }

            return null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "伺服器異常";
    }
}