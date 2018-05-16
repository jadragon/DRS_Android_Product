package library.AnalyzeJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AnalyzeShopCart {
    /**
     * 判斷是否成功
     */
    public static Boolean checkSuccess(JSONObject json) {
        try {
            return json.getBoolean("Success");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
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

    /**
     * 解析購物車:
     * 現金折扣碼
     * 現金折扣條碼
     * 現金折扣金
     */
    public static ArrayList<Map<String, String>> getCartCoupon(JSONObject json) {
        ArrayList<Map<String, String>> arrayList = new ArrayList<>();
        Map<String, String> map;
        try {
            if (json.getBoolean("Success")) {
                JSONArray jsonArray = json.getJSONArray("Coupon");
                for (int i = 0; i < jsonArray.length(); i++) {
                    map = new HashMap<>();
                    JSONObject json_obj = jsonArray.getJSONObject(i);
                    map.put("moprno", json_obj.getString("moprno"));
                    map.put("mcoupon", json_obj.getString("mcoupon"));
                    map.put("mdiscount", json_obj.getString("mdiscount"));
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
     * 解析購買清單 - 輸入折扣代碼:
     * 現金折扣碼
     * 現金折扣條碼
     * 現金折扣金
     */
    public static Map<String, String> getCartDiscount(JSONObject json) {
        Map<String, String> map;
        try {
            if (json.getBoolean("Success")) {
                JSONArray jsonArray = json.getJSONArray("Data");
                map = new HashMap<>();
                JSONObject json_obj = jsonArray.getJSONObject(0);
                map.put("moprno", json_obj.getString("moprno"));
                map.put("mcoupon", json_obj.getString("mcoupon"));
                map.put("mdiscount", json_obj.getString("mdiscount"));

                return map;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析結帳清單:
     * 商家碼
     * 商家名稱
     * 商家圖
     * 商家小計
     * 優惠標題
     * 優惠折扣
     * 運費資訊(滿$299，宅配、7-11免運)
     * 運送方式(7-11,全家……)
     * 收件人姓名+電話
     * 門市名稱 “懿德門市”
     * 門市店號
     * 地址資訊
     * 郵資
     * 備註
     */
    public static ArrayList<Map<String, String>> getCountInformation(JSONObject json) {
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
                    map.put("discount", json_obj.getString("discount"));
                    map.put("shippingInfo", json_obj.getString("shippingInfo"));
                    map.put("shippingStyle", json_obj.getString("shippingStyle"));
                    map.put("shippingName", json_obj.getString("shippingName"));
                    map.put("shippingSname", json_obj.getString("shippingSname"));
                    map.put("shippingSid", json_obj.getString("shippingSid"));
                    map.put("shippingAddress", json_obj.getString("shippingAddress"));
                    map.put("shippingPay", json_obj.getString("shippingPay"));
                    map.put("note", json_obj.getString("note"));
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
     * 解析結帳清單:
     * 購物碼
     * 訂購數量
     * 商品名
     * 商品圖
     * 顏色
     * 尺寸
     * 重量
     * 牌價
     * 售價
     */
    public static ArrayList<ArrayList<Map<String, String>>> getCountItemArray(JSONObject json) {
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
                        map.put("mcpno", jsonObject.getString("mcpno"));
                        map.put("stotal", jsonObject.getString("stotal"));
                        map.put("pname", jsonObject.getString("pname"));
                        map.put("img", jsonObject.getString("img"));
                        map.put("color", jsonObject.getString("color"));
                        map.put("size", jsonObject.getString("size"));
                        map.put("weight", jsonObject.getString("weight"));
                        map.put("price", jsonObject.getString("price"));
                        map.put("sprice", jsonObject.getString("sprice"));
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
