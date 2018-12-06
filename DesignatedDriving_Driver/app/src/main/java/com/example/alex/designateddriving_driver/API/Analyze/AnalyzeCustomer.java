package com.example.alex.designateddriving_driver.API.Analyze;

import android.content.ContentValues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.alex.designateddriving_driver.db.SQLiteDatabaseHandler.KEY_BIRTHDAY;
import static com.example.alex.designateddriving_driver.db.SQLiteDatabaseHandler.KEY_CMP;
import static com.example.alex.designateddriving_driver.db.SQLiteDatabaseHandler.KEY_CONTACT;
import static com.example.alex.designateddriving_driver.db.SQLiteDatabaseHandler.KEY_EMAIL;
import static com.example.alex.designateddriving_driver.db.SQLiteDatabaseHandler.KEY_MP;
import static com.example.alex.designateddriving_driver.db.SQLiteDatabaseHandler.KEY_PICTURE;
import static com.example.alex.designateddriving_driver.db.SQLiteDatabaseHandler.KEY_SEX;
import static com.example.alex.designateddriving_driver.db.SQLiteDatabaseHandler.KEY_TOKEN;
import static com.example.alex.designateddriving_driver.db.SQLiteDatabaseHandler.KEY_UNAME;


public class AnalyzeCustomer {

    /**
     * 1.1.3	登入
     */
    public static ContentValues getLogin(JSONObject json) {
        try {
            ContentValues cv = new ContentValues();
            if (json.getBoolean("Success")) {
                JSONObject jsonObject = json.getJSONObject("Data");
                cv.put(KEY_TOKEN, (jsonObject.getString(KEY_TOKEN)));
                cv.put(KEY_UNAME, (jsonObject.getString(KEY_UNAME)));
                cv.put(KEY_PICTURE, (jsonObject.getString(KEY_PICTURE)));
                cv.put(KEY_SEX, (jsonObject.getString(KEY_SEX)));
                cv.put(KEY_BIRTHDAY, (jsonObject.getString(KEY_BIRTHDAY)));
                cv.put(KEY_EMAIL, (jsonObject.getString(KEY_EMAIL)));
                cv.put(KEY_CONTACT, (jsonObject.getString(KEY_CONTACT)));
                cv.put(KEY_CMP, (jsonObject.getString(KEY_CMP)));
                return cv;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 1.2.1	讀取基本資料
     */
    public static ContentValues getBasicData(JSONObject json) {
        try {
            ContentValues cv = new ContentValues();
            if (json.getBoolean("Success")) {
                JSONObject jsonObject = json.getJSONObject("Data");
                cv.put(KEY_UNAME, (jsonObject.getString(KEY_UNAME)));
                cv.put(KEY_PICTURE, (jsonObject.getString(KEY_PICTURE)));
                cv.put(KEY_SEX, (jsonObject.getString(KEY_SEX)));
                cv.put(KEY_MP, (jsonObject.getString(KEY_MP)));
                cv.put(KEY_BIRTHDAY, (jsonObject.getString(KEY_BIRTHDAY)));
                cv.put(KEY_EMAIL, (jsonObject.getString(KEY_EMAIL)));
                cv.put(KEY_CONTACT, (jsonObject.getString(KEY_CONTACT)));
                cv.put(KEY_CMP, (jsonObject.getString(KEY_CMP)));
                return cv;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 1.4.1	我的優惠券
     */
    public static List<ContentValues> getMyCoupon(JSONObject json, int type) {

        List<ContentValues> arraylist = new ArrayList<>();
        try {
            if (json.getBoolean("Success")) {
                JSONArray jsonArray = json.getJSONArray("Data");
                ContentValues cv;
                JSONObject jsonObject;
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    cv = new ContentValues();
                    cv.put("name", (jsonObject.getString("name")));
                    cv.put("money", (jsonObject.getString("money")));
                    cv.put("edate", (jsonObject.getString("edate")));
                    if (type == 0) {
                        cv.put("coupon", (jsonObject.getString("coupon")));
                        cv.put("isuse", (jsonObject.getString("isuse")));
                        cv.put("utime", (jsonObject.getString("utime")));
                    } else if (type == 1) {
                        cv.put("mmno", (jsonObject.getString("mmno")));
                    }
                    arraylist.add(cv);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arraylist;
    }
}