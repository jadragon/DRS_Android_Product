package library.AnalyzeJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import library.AnalyzeJSON.APIpojo.All_Store_BrandPojo;

public class Analyze_StockInfo {

    /**
     * 6.1全部門市與品牌
     */
    public All_Store_BrandPojo getAll_store_brand(JSONObject json) {
        try {
            if (json.getBoolean("Success")) {
                All_Store_BrandPojo all_store_brandPojo = new All_Store_BrandPojo();
                JSONArray jsonArray = json.getJSONObject("Data").getJSONArray("store");
                JSONObject json_obj;
                ArrayList<String> array1 = new ArrayList<>();
                ArrayList<String> array2 = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    json_obj = jsonArray.getJSONObject(i);
                    array1.add(json_obj.getString("s_no"));
                    array2.add(json_obj.getString("store"));
                }
                all_store_brandPojo.setS_no(array1);
                all_store_brandPojo.setStore(array2);
                jsonArray = json.getJSONObject("Data").getJSONArray("brand");
                array1 = new ArrayList<>();
                array2 = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    json_obj = jsonArray.getJSONObject(i);
                    array1.add(json_obj.getString("pb_no"));
                    array2.add(json_obj.getString("title"));
                }
                all_store_brandPojo.setPb_no(array1);
                all_store_brandPojo.setTitle(array2);
                return all_store_brandPojo;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 6.2商品庫存
     */
    public All_Store_BrandPojo getStock_data(JSONObject json) {
        try {
            if (json.getBoolean("Success")) {
                All_Store_BrandPojo all_store_brandPojo = new All_Store_BrandPojo();
                JSONArray jsonArray = json.getJSONObject("Data").getJSONArray("store");
                JSONObject json_obj;
                ArrayList<String> array1 = new ArrayList<>();
                ArrayList<String> array2 = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    json_obj = jsonArray.getJSONObject(i);
                    array1.add(json_obj.getString("s_no"));
                    array2.add(json_obj.getString("store"));
                }
                return all_store_brandPojo;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

}
