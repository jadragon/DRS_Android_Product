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
                    map.put("title", json_obj.getString("title"));
                    try {
                        map.put("image", json_obj.getString("img"));
                    } catch (Exception e) {
                        map.put("image", json_obj.getString("bimg"));
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

}
