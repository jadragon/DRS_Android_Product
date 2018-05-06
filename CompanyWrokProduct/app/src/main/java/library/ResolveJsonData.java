package library;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
                maps.put("img", json_obj.getString("img"));
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
     * 解析產品內頁:
     * WebView
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

    /**
     * 解析購物車:
     * 商家碼
     * 商家名稱
     * 商家圖
     * 商家小計
     * 優惠標題
     * 運費資訊
     * 商品項目
     */
    public static ArrayList<Map<String, String>> getCartInformation(JSONObject json) {
        ArrayList<Map<String, String>> arrayList = new ArrayList<>();
        Map<String, String> map;
        try {
            if (json.getBoolean("Success")) {
                JSONArray jsonArray = json.getJSONArray("Data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    map = new HashMap<>();
                    JSONObject json_obj = jsonArray.getJSONObject(i);
                    map.put("sno", json_obj.getString("sno"));
                    map.put("sname", json_obj.getString("sname"));
                    map.put("simg", json_obj.getString("simg"));
                    map.put("subtotal", json_obj.getString("subtotal"));
                    map.put("discountInfo", json_obj.getString("discountInfo"));
                    map.put("shippingInfo", json_obj.getString("shippingInfo"));
                    arrayList.add(map);
                }
                return arrayList;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析購物車:
     * 購物碼
     * 訂購數量
     * 商品名
     * 商品圖
     * 顏色
     * 尺寸
     * 重量
     * 牌價
     * 售價
     * 庫存
     */
    public static ArrayList<ArrayList<Map<String, String>>> getCartItemArray(JSONObject json) {
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
                        map.put("morno", jsonObject.getString("morno"));
                        map.put("stotal", jsonObject.getString("stotal"));
                        map.put("pname", jsonObject.getString("pname"));
                        map.put("img", jsonObject.getString("img"));
                        map.put("color", jsonObject.getString("color"));
                        map.put("size", jsonObject.getString("size"));
                        map.put("weight", jsonObject.getString("weight"));
                        map.put("price", jsonObject.getString("price"));
                        map.put("sprice", jsonObject.getString("sprice"));
                        map.put("mtotal", jsonObject.getString("mtotal"));
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
}
