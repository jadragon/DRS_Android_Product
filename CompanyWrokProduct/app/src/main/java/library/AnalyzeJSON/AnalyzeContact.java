package library.AnalyzeJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AnalyzeContact {

    /**
     * 8.2.1	讀取信件:
     * msno	String	信件碼
     * type	Number	1收件2寄件
     * person	String	人名
     * title	String	主題
     * isread	Number	讀取狀態 0未讀1已讀
     * content	String	內文
     * time	String	時間
     */
    public static ArrayList<Map<String, String>> getContact(JSONObject json) {
        ArrayList<Map<String, String>> arrayList = new ArrayList<>();
        Map<String, String> map;
        try {
            if (json.getBoolean("Success")) {
                JSONArray jsonArray = json.getJSONArray("Data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    map = new HashMap<>();
                    JSONObject json_obj = jsonArray.getJSONObject(i);
                    map.put("msno", json_obj.getString("msno"));
                    map.put("type", json_obj.getString("type"));
                    map.put("person", json_obj.getString("person"));
                    map.put("title", json_obj.getString("title"));
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
