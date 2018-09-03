package library.AnalyzeJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import library.AnalyzeJSON.APIpojo.Distribution2Pojo;
import library.AnalyzeJSON.APIpojo.DistributionContentPojo;

public class Analyze_DistributionInfo {

    /**
     * 6.1全部門市與品牌
     */
    public ArrayList<DistributionContentPojo> getDistributionContent(JSONObject json) {
        ArrayList<DistributionContentPojo> arrayList = new ArrayList<>();
        try {
            if (json.getBoolean("Success")) {
                DistributionContentPojo distributionContentPojo = null;
                JSONArray jsonArray = json.getJSONObject("Data").getJSONArray("content");
                JSONObject json_obj;
                for (int i = 0; i < jsonArray.length(); i++) {
                    distributionContentPojo = new DistributionContentPojo();
                    json_obj = jsonArray.getJSONObject(i);
                    distributionContentPojo.setBrand_title(json_obj.getString("brand_title"));
                    distributionContentPojo.setImg(json_obj.getString("img"));
                    distributionContentPojo.setName(json_obj.getString("name"));
                    distributionContentPojo.setPcode(json_obj.getString("pcode"));
                    distributionContentPojo.setColor(json_obj.getString("color"));
                    distributionContentPojo.setSize(json_obj.getString("size"));
                    distributionContentPojo.setStotal(json_obj.getString("stotal"));
                    arrayList.add(distributionContentPojo);
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
     * 6.1全部門市與品牌
     */
    public ArrayList<ArrayList<Distribution2Pojo>> getDistribution2Pojo(JSONObject json) {
        ArrayList<ArrayList<Distribution2Pojo>> arrayList = new ArrayList<>();
        try {
            if (json.getBoolean("Success")) {
                Distribution2Pojo distribution2Pojo = null;
                JSONArray jsonArray = json.getJSONObject("Data").getJSONArray("content");
                JSONObject json_obj;
                ArrayList<Distribution2Pojo> distributionList;
                for (int i = 0; i < jsonArray.length(); i++) {
                    json_obj = jsonArray.getJSONObject(i);
                    distributionList = new ArrayList<>();
                    JSONArray distributionArray = json_obj.getJSONArray("distribution");
                    JSONObject distributionObj;
                    for (int j = 0; j < distributionArray.length(); j++) {
                        distributionObj = distributionArray.getJSONObject(i);
                        distribution2Pojo = new Distribution2Pojo();
                        distribution2Pojo.setStore(distributionObj.getString("store"));
                        distribution2Pojo.setTotal(distributionObj.getString("total"));
                        distributionList.add(distribution2Pojo);
                    }
                    arrayList.add(distributionList);
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
