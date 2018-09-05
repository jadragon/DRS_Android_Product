package library.AnalyzeJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import library.AnalyzeJSON.APIpojo.ProductfilePojo;

public class Analyze_ProductfileInfo {


    /**
     * 6.4商品建檔資料
     */
    public ArrayList<ProductfilePojo> getProduct_filing(JSONObject json) {
        ArrayList<ProductfilePojo> arrayList = new ArrayList<>();
        try {
            if (json.getBoolean("Success")) {
                ProductfilePojo productfilePojo = null;
                JSONArray jsonArray = json.getJSONArray("Data");
                JSONObject json_obj;
                for (int i = 0; i < jsonArray.length(); i++) {
                    productfilePojo = new ProductfilePojo();
                    json_obj = jsonArray.getJSONObject(i);
                    productfilePojo.setP_no(json_obj.getString("p_no"));
                    productfilePojo.setPi_no(json_obj.getString("pi_no"));
                    productfilePojo.setImg(json_obj.getString("img"));
                    productfilePojo.setName(json_obj.getString("name"));
                    productfilePojo.setPcode(json_obj.getString("pcode"));
                    productfilePojo.setColor(json_obj.getString("color"));
                    productfilePojo.setSize(json_obj.getString("size"));
                    productfilePojo.setTitle(json_obj.getString("title"));
                    productfilePojo.setBrand_title(json_obj.getString("brand_title"));
                    productfilePojo.setCost(json_obj.getInt("cost"));
                    productfilePojo.setFprice(json_obj.getInt("fprice"));
                    productfilePojo.setPrice(json_obj.getInt("price"));
                    productfilePojo.setStotal(json_obj.getInt("stotal"));
                    productfilePojo.setU_time(json_obj.getString("u_time"));
                    productfilePojo.setExpired_date(json_obj.getString("expired_date"));
                    productfilePojo.setExist(json_obj.getBoolean("exist"));
                    arrayList.add(productfilePojo);
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

}
