package library;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResolveJsonData {


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

    public static ArrayList<Map<String, String>> getPtypeDetail(JSONObject json,int index) {

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
}
