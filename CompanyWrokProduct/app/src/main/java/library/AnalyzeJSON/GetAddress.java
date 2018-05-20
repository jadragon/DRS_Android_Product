package library.AnalyzeJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GetAddress {
    /**
     * 判斷是否成功
     */
    public static String checkModifydate(JSONObject json) {
        try {
            return json.getString("modifydate");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
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
    public static ArrayList<Map<String, String>> getAddress(JSONObject json) {
        ArrayList<Map<String, String>> arrayList = new ArrayList<>();
        Map<String, String> map;
        try {
            if (json.getBoolean("Success")) {
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
                                map = new HashMap<>();
                                map.put("city", key);
                                map.put("area", jsonObject.getString("area"));
                                map.put("zipcode", jsonObject.getString("zipcode"));
                                arrayList.add(map);
                            }catch (Exception e){
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
