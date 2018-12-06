package tw.com.lccnet.app.designateddriving.API.Analyze;

import android.content.ContentValues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import static tw.com.lccnet.app.designateddriving.db.SQLiteDatabaseHandler.KEY_AREA;
import static tw.com.lccnet.app.designateddriving.db.SQLiteDatabaseHandler.KEY_CITY;
import static tw.com.lccnet.app.designateddriving.db.SQLiteDatabaseHandler.KEY_MODIFYDATE;
import static tw.com.lccnet.app.designateddriving.db.SQLiteDatabaseHandler.KEY_ZIPCODE;

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

    /**
     * 解析購物車:
     * 商家碼
     * 商家名稱
     * 商家圖
     * 商家小計
     * 優惠標題
     * 運費資訊
     * 商品項目
     */
    public static ArrayList<ContentValues> getAddress(JSONObject json) {
        ArrayList<ContentValues> arrayList = new ArrayList<>();
        ContentValues cv;
        try {
            if (json.getBoolean("Success")) {
                String modifydate = json.getString(KEY_MODIFYDATE);
                JSONObject json_obj = json.getJSONObject("Data");
                JSONArray jsonArray;
                Iterator<?> keys = json_obj.keys();
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    if (json_obj.get(key) instanceof JSONArray) {
                        jsonArray = (JSONArray) json_obj.get(key);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            try {
                                cv = new ContentValues();
                                cv.put(KEY_CITY, key);
                                cv.put(KEY_AREA, jsonObject.getString(KEY_AREA));
                                cv.put(KEY_ZIPCODE, jsonObject.getString(KEY_ZIPCODE));
                                cv.put(KEY_MODIFYDATE, modifydate);
                                arrayList.add(cv);
                            } catch (Exception e) {
                                break;
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }
}