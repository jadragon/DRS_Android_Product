package library.AnalyzeJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AnalyzeLogistics {

    /**
     * 3.2.1	讀取收貨地址:
     * mlno	String	收貨地址碼
     * land	Number	送貨島嶼0無1本島2離島3海外
     * logistics	Number	送貨方式 0.無 1.7-11 2.全家 3.萊爾富 4.黑貓宅配 5.郵局 6.賣家宅配 7.海外配送
     * logisticsVal	String	送貨名稱 “7-11”
     * name	String	姓名
     * mpcode	String	手機國碼
     * mp	String	手機號
     * sname	String	門市名稱 “懿德門市”
     * sid	String	門市店號
     * shit	String	國名縮寫2碼TW
     * city	String	縣市
     * area	String	區
     * zipcode	String	郵遞區號
     * address	String	地址
     * isused	Number	物流預設使用狀態 0未使用1使用中
     */
    public static ArrayList<Map<String, String>> getLogistics(JSONObject json) {
        ArrayList<Map<String, String>> arrayList = new ArrayList<>();
        Map<String, String> map;
        try {
            if (json.getBoolean("Success")) {
                JSONArray jsonArray = json.getJSONArray("Data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    map = new HashMap<>();
                    JSONObject json_obj = jsonArray.getJSONObject(i);
                    map.put("mlno", json_obj.getString("mlno"));
                    map.put("land", json_obj.getString("land"));
                    map.put("logistics", json_obj.getString("logistics"));
                    map.put("logisticsVal", json_obj.getString("logisticsVal"));
                    map.put("name", json_obj.getString("name"));
                    map.put("mpcode", json_obj.getString("mpcode"));
                    map.put("mp", json_obj.getString("mp"));
                    map.put("sname", json_obj.getString("sname"));
                    map.put("sid", json_obj.getString("sid"));
                    map.put("shit", json_obj.getString("shit"));
                    map.put("city", json_obj.getString("city"));
                    map.put("area", json_obj.getString("area"));
                    map.put("zipcode", json_obj.getString("zipcode"));
                    map.put("address", json_obj.getString("address"));
                    map.put("isused", json_obj.getString("isused"));
                    arrayList.add(map);
                }
                return arrayList;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 8.2.1	讀取信件cont:
     * type	Number	0劦譽 1會員
     * person	String	人名
     * title	String	主題
     * isread	Number	讀取狀態 0未讀1已讀
     * content	String	內文
     * time	String	時間
     */
    public static ArrayList<Map<String, String>> getContactCont(JSONObject json) {
        ArrayList<Map<String, String>> arrayList = new ArrayList<>();
        Map<String, String> map;
        try {
            if (json.getBoolean("Success")) {
                JSONArray jsonArray = json.getJSONObject("Data").getJSONArray("cont");
                for (int i = 0; i < jsonArray.length(); i++) {
                    map = new HashMap<>();
                    JSONObject json_obj = jsonArray.getJSONObject(i);
                    map.put("type", json_obj.getString("type"));
                    map.put("person", json_obj.getString("person"));
                    map.put("isread", json_obj.getString("isread"));
                    map.put("content", json_obj.getString("content"));
                    map.put("time", json_obj.getString("time"));
                    arrayList.add(map);
                }
                return arrayList;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
