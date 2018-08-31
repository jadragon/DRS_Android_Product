package library.AnalyzeJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import library.AnalyzeJSON.APIpojo.BrandListPojo;

public class Analyze_BrandInfo {

    /**
     * 5.1 品牌
     */
    public ArrayList<BrandListPojo> getBrand_Data(JSONObject json) {
        ArrayList<BrandListPojo> arrayList = new ArrayList<>();
        try {
            if (json.getBoolean("Success")) {
                BrandListPojo brandListPojo = null;
                JSONArray jsonArray = json.getJSONArray("Data");
                JSONObject json_obj;
                for (int i = 0; i < jsonArray.length(); i++) {
                    brandListPojo = new BrandListPojo();
                    json_obj = jsonArray.getJSONObject(i);
                    brandListPojo.setPb_no(json_obj.getString("pb_no"));
                    brandListPojo.setCode(json_obj.getString("code"));
                    brandListPojo.setTitle(json_obj.getString("title"));
                    brandListPojo.setImg(json_obj.getString("img"));
                    brandListPojo.setC_time(json_obj.getString("c_time"));
                    brandListPojo.setU_time(json_obj.getString("u_time"));
                    brandListPojo.setExist(json_obj.getBoolean("exist"));

                    arrayList.add(brandListPojo);
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
