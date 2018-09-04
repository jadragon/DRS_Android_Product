package library.AnalyzeJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import library.AnalyzeJSON.APIpojo.All_Store_BrandPojo;
import library.AnalyzeJSON.APIpojo.StockDataPojo;

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
                array1.add("全部");
                array2.add("全部");
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < jsonArray.length(); i++) {
                    json_obj = jsonArray.getJSONObject(i);
                    builder.append("," + json_obj.getString("s_no"));
                    array1.add(json_obj.getString("s_no"));
                    array2.add(json_obj.getString("store"));
                }
                if (jsonArray.length() > 0) {
                    array1.set(0, builder.deleteCharAt(0).toString());
                }
                all_store_brandPojo.setS_no(array1);
                all_store_brandPojo.setStore(array2);
                jsonArray = json.getJSONObject("Data").getJSONArray("brand");
                array1 = new ArrayList<>();
                array2 = new ArrayList<>();
                array1.add("全部");
                array2.add("全部");
                builder = new StringBuilder();
                for (int i = 0; i < jsonArray.length(); i++) {
                    json_obj = jsonArray.getJSONObject(i);
                    builder.append("," + json_obj.getString("pb_no"));
                    array1.add(json_obj.getString("pb_no"));
                    array2.add(json_obj.getString("title"));
                }
                if (jsonArray.length() > 0) {
                    array1.set(0, builder.deleteCharAt(0).toString());
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
    public ArrayList<StockDataPojo> getStock_data(JSONObject json) {
        ArrayList<StockDataPojo> arrayList = new ArrayList<>();
        try {
            if (json.getBoolean("Success")) {
                StockDataPojo stockDataPojo = null;
                JSONArray jsonArray = json.getJSONObject("Data").getJSONArray("content");
                JSONObject json_obj;
                for (int i = 0; i < jsonArray.length(); i++) {
                    stockDataPojo = new StockDataPojo();
                    json_obj = jsonArray.getJSONObject(i);
                    stockDataPojo.setStore(json_obj.getString("store"));
                    stockDataPojo.setImg(json_obj.getString("img"));
                    stockDataPojo.setBrand_title(json_obj.getString("brand_title"));
                    stockDataPojo.setName(json_obj.getString("name"));
                    stockDataPojo.setPcode(json_obj.getString("pcode"));
                    stockDataPojo.setColor(json_obj.getString("color"));
                    stockDataPojo.setSize(json_obj.getString("size"));
                    stockDataPojo.setFprice(json_obj.getString("fprice"));
                    stockDataPojo.setPrice(json_obj.getString("price"));
                    stockDataPojo.setTotal(json_obj.getString("total"));
                    stockDataPojo.setStotal(json_obj.getString("stotal"));
                    stockDataPojo.setFlaw_total(json_obj.getString("flaw_total"));
                    arrayList.add(stockDataPojo);
                }
                return arrayList;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return arrayList;
    }


    /**
     * 6.2商品庫存(total)
     */
    public Map<String, Integer> getStock_dataTotal(JSONObject json) {
        try {
            if (json.getBoolean("Success")) {
                Map<String, Integer> map = new HashMap<>();
                JSONObject json_obj = json.getJSONObject("Data");
                map.put("total", json_obj.getInt("total"));
                map.put("stotal", json_obj.getInt("stotal"));
                map.put("flaw_total", json_obj.getInt("flaw_total"));
                return map;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }
}
