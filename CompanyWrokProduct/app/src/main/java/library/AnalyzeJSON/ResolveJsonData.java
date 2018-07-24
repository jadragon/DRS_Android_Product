package library.AnalyzeJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pojo.ProductInfoPojo;

public class ResolveJsonData {

    /**
     * 解析首頁:
     * 熱門搜尋、分類、品牌、
     * 解析購物:
     * 每日新品、熱門商品、價格由低至高、價格由高至低
     */
    public static   ArrayList<ProductInfoPojo> getJSONData(JSONObject json) {
        ArrayList<ProductInfoPojo> arrayList = new ArrayList<>();
        ProductInfoPojo productInfoPojo;
        JSONObject json_obj;
        JSONArray json_data;
        try {
            String success = json.getString("Success");
            if (success.equals("true")) {
                json_data = json.getJSONArray("Data");

                for (int i = 0; i < json_data.length(); i++) {
                    productInfoPojo=new ProductInfoPojo();
                    json_obj = json_data.getJSONObject(i);
                    try {
                        productInfoPojo.setTitle(json_obj.getString("title"));
                    } catch (Exception e) {
                        productInfoPojo.setTitle(json_obj.getString("pname"));
                    }

                    try {
                        productInfoPojo.setImage(json_obj.getString("img"));
                    } catch (Exception e) {
                        productInfoPojo.setImage(json_obj.getString("bimg"));
                    }

                    try {
                        productInfoPojo.setPno(json_obj.getString("pno"));
                        productInfoPojo.setDescs(json_obj.getString("descs"));
                        productInfoPojo.setRprice( json_obj.getString("rprice"));
                        productInfoPojo.setRsprice( json_obj.getString("rsprice"));
                        productInfoPojo.setIsnew( json_obj.getString("isnew"));
                        productInfoPojo.setIshot( json_obj.getString("ishot"));
                        productInfoPojo.setIstime(json_obj.getString("istime"));
                        productInfoPojo.setDiscount( json_obj.getString("discount"));
                        productInfoPojo.setShipping(json_obj.getString("shipping"));
                        productInfoPojo.setFavorite(json_obj.getString("favorite").equals("true"));
                        productInfoPojo.setScore( Integer.parseInt(json_obj.getString("score")));
                    } catch (Exception e) {
                    }
                    arrayList.add(productInfoPojo);
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
                maps.put("pno", json_obj.getString("pno"));
                maps.put("content", json_obj.getString("content"));
                maps.put("img", json_obj.getString("img"));
                maps.put("pname", json_obj.getString("pname"));
                maps.put("descs", json_obj.getString("descs"));
                maps.put("rprice", json_obj.getString("rprice"));
                maps.put("rsprice", json_obj.getString("rsprice"));
                maps.put("score", json_obj.getString("score"));
                maps.put("isnew", json_obj.getString("isnew"));
                maps.put("ishot", json_obj.getString("ishot"));
                maps.put("istime", json_obj.getString("istime"));
                maps.put("favorite", json_obj.getString("favorite"));
                maps.put("rpolicy", json_obj.getString("rpolicy"));
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
     * 解析產品內頁:
     * 運送方式
     */
    public static ArrayList<Map<String, String>> getPcContentShippingArray(JSONObject json) {
        ArrayList<Map<String, String>> arrayList = new ArrayList<>();
        Map<String, String> map;
        try {
            String success = json.getString("Success");
            if (success.equals("true")) {
                JSONArray shippingArray = json.getJSONArray("Data").getJSONObject(0).getJSONArray("shippingArray");
                for (int j = 0; j < shippingArray.length(); j++) {
                    map = new HashMap<>();
                    JSONObject imgArray_obj = shippingArray.getJSONObject(j);
                    map.put("land", imgArray_obj.getString("land"));
                    map.put("logistics", imgArray_obj.getString("logistics"));
                    map.put("lpay", imgArray_obj.getString("lpay"));
                    map.put("info", imgArray_obj.getString("info"));
                    arrayList.add(map);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    /**
     * 解析產品內頁:
     * 商品款式
     */
    public static ArrayList<Map<String, String>> getPcContentItemArray(JSONObject json) {
        ArrayList<Map<String, String>> arrayList = new ArrayList<>();
        Map<String, String> map;
        try {
            String success = json.getString("Success");
            if (success.equals("true")) {
                JSONArray itemArray = json.getJSONArray("Data").getJSONObject(0).getJSONArray("itemArray");
                for (int j = 0; j < itemArray.length(); j++) {
                    map = new HashMap<>();
                    JSONObject imgArray_obj = itemArray.getJSONObject(j);
                    map.put("pino", imgArray_obj.getString("pino"));
                    map.put("color", imgArray_obj.getString("color"));
                    map.put("size", imgArray_obj.getString("size"));
                    map.put("price", imgArray_obj.getString("price"));
                    map.put("sprice", imgArray_obj.getString("sprice"));
                    map.put("total", imgArray_obj.getString("total"));
                    arrayList.add(map);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }


}
