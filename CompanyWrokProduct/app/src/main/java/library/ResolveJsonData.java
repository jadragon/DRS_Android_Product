package library;

import android.widget.Switch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class ResolveJsonData {

    /**
     * 解析首頁:
     * 熱門搜尋、分類、品牌、
     * 解析購物:
     * 每日新品、熱門商品、價格由低至高、價格由高至低
     */
    public static ArrayList<Map<String, String>> getJSONData(JSONObject json) {
        ArrayList<Map<String, String>> arrayList = new ArrayList<>();
        Map<String, String> map;
        JSONObject json_obj;
        JSONArray json_data;
        try {
            String success = json.getString("Success");
            if (success.equals("true")) {
                json_data = json.getJSONArray("Data");

                for (int i = 0; i < json_data.length(); i++) {
                    map = new HashMap<>();
                    json_obj = json_data.getJSONObject(i);
                    try {
                        map.put("title", json_obj.getString("title"));
                    } catch (Exception e) {
                        map.put("title", json_obj.getString("pname"));
                    }

                    try {
                        map.put("image", json_obj.getString("img"));
                    } catch (Exception e) {
                        map.put("image", json_obj.getString("bimg"));
                    }

                    try {
                        map.put("pno", json_obj.getString("pno"));
                        map.put("descs", json_obj.getString("descs"));
                        map.put("rprice", json_obj.getString("rprice"));
                        map.put("rsprice", json_obj.getString("rsprice"));
                        map.put("isnew", json_obj.getString("isnew"));
                        map.put("ishot", json_obj.getString("ishot"));
                        map.put("istime", json_obj.getString("istime"));
                        map.put("discount", json_obj.getString("discount"));
                        map.put("shipping", json_obj.getString("shipping"));
                        map.put("score", json_obj.getString("score"));
                    } catch (Exception e) {
                    }
                    arrayList.add(map);
                }
                return arrayList;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    /**
     * 解析首頁:
     * 分類中的細項
     */
    public static ArrayList<Map<String, String>> getPtypeDetail(JSONObject json, int index) {

        ArrayList<Map<String, String>> arrayList = new ArrayList<>();
        Map<String, String> map;
        try {
            String success = json.getString("Success");
            if (success.equals("true")) {
                JSONArray json_data = json.getJSONArray("Data");
                JSONObject json_obj = json_data.getJSONObject(index);
                JSONArray l2array = json_obj.getJSONArray("l2array");
                for (int j = 0; j < l2array.length(); j++) {
                    map = new HashMap<>();
                    try {
                        JSONObject l2array_obj = l2array.getJSONObject(j);
                        map.put("ptno", l2array_obj.getString("ptno"));
                        map.put("title", l2array_obj.getString("title"));
                        map.put("image", l2array_obj.getString("bimg"));
                        map.put("aimg", l2array_obj.getString("aimg"));
                        arrayList.add(map);
                    } catch (Exception e) {
                    }
                }

                return arrayList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    /**
     * 解析產品內頁:
     * 產品名稱、產品描述、產品原價、產品售價
     */
    public static Map<String, String> getPcContentInformation(JSONObject json) {
        Map<String, String> maps = new HashMap<>();
        try {
            if (json.getBoolean("Success")) {
                JSONObject json_obj = json.getJSONArray("Data").getJSONObject(0);
                maps.put("pname", json_obj.getString("pname"));
                maps.put("descs", json_obj.getString("descs"));
                maps.put("rprice", json_obj.getString("rprice"));
                maps.put("rsprice", json_obj.getString("rsprice"));
                return maps;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
        /*
        Iterator itt = json.keys();
        while (itt.hasNext()) {

            String key = itt.next().toString();
            Object value = json.get(key);
        }
        */

    }

    /**
     * 解析產品內頁:
     * 產品圖片
     */
    public static ArrayList<Map<String, String>> getPcContentImgArray(JSONObject json) {

        ArrayList<Map<String, String>> arrayList = new ArrayList<>();
        Map<String, String> map;
        try {
            String success = json.getString("Success");
            if (success.equals("true")) {
                JSONArray imgArray = json.getJSONArray("Data").getJSONObject(0).getJSONArray("imgArray");
                for (int j = 0; j < imgArray.length(); j++) {
                    map = new HashMap<>();
                    JSONObject imgArray_obj = imgArray.getJSONObject(j);
                    map.put("img", imgArray_obj.getString("img"));
                    arrayList.add(map);
                }
                return arrayList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    /**
     * 解析首頁:
     * 分類中的細項
     */
    public static Map<String, String> getWebView(JSONObject json) {
        Map<String, String> map = new HashMap<>();

        try {
            String success = json.getString("Success");
            if (success.equals("true")) {
                JSONObject json_obj = json.getJSONArray("Data").getJSONObject(0);
                map.put("content", json_obj.getString("content"));
                map.put("rpolicy", json_obj.getString("rpolicy"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

}
