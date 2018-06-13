package library.AnalyzeJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AnalyzeHelpCenter {
    /**
     * 判斷是否成功
     */
    public static Boolean checkSuccess(JSONObject json) {
        try {
            return json.getBoolean("Success");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 8.3.1	讀取固定項目(第一層、第二層)Data:
     * title	String	第一層標題
     */
    public static ArrayList<String> getCategoryTitle(JSONObject json) {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            if (json.getBoolean("Success")) {
                JSONArray jsonArray = json.getJSONArray("Data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json_obj = jsonArray.getJSONObject(i);
                    arrayList.add(json_obj.getString("title"));
                }
                return arrayList;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 8.3.1	讀取固定項目(第一層、第二層)itemArray:
     * icno	 String	項目碼
     * title	 String	第二層標題
     */
    public static ArrayList<ArrayList<Map<String, String>>> getCategoryItemArray(JSONObject json) {
        ArrayList<ArrayList<Map<String, String>>> alllist;
        ArrayList<Map<String, String>> arrayList;
        Map<String, String> map;
        try {
            alllist = new ArrayList<>();
            if (json.getBoolean("Success")) {
                for (int i = 0; i < json.getJSONArray("Data").length(); i++) {
                    JSONArray jsonArray = json.getJSONArray("Data").getJSONObject(i).getJSONArray("itemArray");
                    arrayList = new ArrayList<>();
                    for (int j = 0; j < jsonArray.length(); j++) {
                        map = new HashMap<>();
                        JSONObject jsonObject = jsonArray.getJSONObject(j);
                        map.put("icno", jsonObject.getString("icno"));
                        map.put("title", jsonObject.getString("title"));
                        arrayList.add(map);
                    }
                    alllist.add(arrayList);
                }
                return alllist;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 8.3.2	讀取搜尋項目(第二層)–方法一:
     * icno	 String	項目碼
     * title	 String	第二層標題
     */
    public static ArrayList<Map<String, String>> getSearchCategory(JSONObject json) {
        ArrayList<Map<String, String>> arrayList = new ArrayList<>();
        Map<String, String> map;
        try {
            if (json.getBoolean("Success")) {
                JSONArray jsonArray = json.getJSONArray("Data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    map = new HashMap<>();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    map.put("icno", jsonObject.getString("icno"));
                    map.put("title", jsonObject.getString("title"));
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
