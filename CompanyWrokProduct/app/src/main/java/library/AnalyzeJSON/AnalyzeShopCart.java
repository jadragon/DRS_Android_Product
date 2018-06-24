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
     * 1.3.8	結帳清單-讀取結帳資訊:
     * sno	String	商家碼
     * sname	String	商家名稱
     * simg	String	商家圖
     * subtotal	Number	商家小計
     * discountInfo	String	優惠標題
     * discount	Number	優惠折扣
     * shippingInfo	String	運費資訊(滿$299，宅配、7-11免運)
     * shippingStyle	String	運送方式(7-11,全家……)
     * shippingName	String	收件人姓名+電話
     * shippingSname	String	門市名稱“懿德門市”
     * shippingSid	String	門市店號
     * shippingAddress	String	地址資訊
     * shippingPay	Number	郵資
     * note	String	備註
     */
    public static ArrayList<Map<String, String>> getCheckoutData(JSONObject json) {
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
     * 1.3.8	結帳清單-讀取結帳資訊
     * mcrno	String	購物碼
     * stotal	String	訂購數量
     * pname	String	商品名
     * img	String	商品圖
     * color	String	顏色
     * size	String	尺寸
     * weight	String	重量
     * price	Number	牌價
     * sprice	Number	售價
     */
    public static ArrayList<ArrayList<Map<String, String>>> getCheckoutItemArray(JSONObject json) {
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
                        try {
                            map.put("mcpno", jsonObject.getString("mcpno"));
                        } catch (Exception e) {
                            e.printStackTrace();
                            map.put("mcpno", jsonObject.getString("mrpno"));
                        }

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

    /**
     * 1.3.8	結帳清單-讀取結帳資訊:
     * sno	String	商家碼
     * sname	String	商家名稱
     * simg	String	商家圖
     * subtotal	Number	商家小計
     * discountInfo	String	優惠標題
     * discount	Number	優惠折扣
     * shippingInfo	String	運費資訊(滿$299，宅配、7-11免運)
     * shippingStyle	String	運送方式(7-11,全家……)
     * shippingName	String	收件人姓名+電話
     * shippingSname	String	門市名稱“懿德門市”
     * shippingSid	String	門市店號
     * shippingAddress	String	地址資訊
     * shippingPay	Number	郵資
     * note	String	備註
     */
    public static ArrayList<Map<String, String>> getCheckoutCoupon(JSONObject json) {
        ArrayList<Map<String, String>> arrayList = new ArrayList<>();
        Map<String, String> map;
        try {
            if (json.getBoolean("Success")) {
                JSONArray jsonArray = json.getJSONArray("Coupon");
                for (int i = 0; i < jsonArray.length(); i++) {
                    map = new HashMap<>();
                    JSONObject json_obj = jsonArray.getJSONObject(i);
                    try {
                        map.put("moprno", json_obj.getString("moprno"));
                    } catch (Exception e) {

                    }
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
     * 1.3.8	結帳清單-讀取結帳資訊:
     * opay	Number	應付小計
     * pterms	String	付款方式(無,線上刷卡,ATM…..)
     * xmoney	Number	波克折抵
     * ymoney	Number	庫瓦折抵
     * ewallet	Number	電子錢包折抵
     * rpay	Number	總付款金額
     */
    public static ArrayList<Map<String, String>> getCheckoutPay(JSONObject json) {
        ArrayList<Map<String, String>> arrayList = new ArrayList<>();
        Map<String, String> map;
        try {
            if (json.getBoolean("Success")) {
                JSONArray jsonArray = json.getJSONArray("Pay");
                for (int i = 0; i < jsonArray.length(); i++) {
                    map = new HashMap<>();
                    JSONObject json_obj = jsonArray.getJSONObject(i);
                    map.put("opay", json_obj.getString("opay"));
                    map.put("pterms", json_obj.getString("pterms"));
                    map.put("xmoney", json_obj.getString("xmoney"));
                    map.put("ymoney", json_obj.getString("ymoney"));
                    map.put("ewallet", json_obj.getString("ewallet"));
                    map.put("rpay", json_obj.getString("rpay"));
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
     * 1.3.8	結帳清單-讀取結帳資訊:
     * invoice	Number	發票種類(0紙本發票,1捐贈發票,2電子發票)
     * ctitle	String	公司抬頭
     * vat	String	公司統編
     */
    public static ArrayList<Map<String, String>> getCheckoutInvoice(JSONObject json) {
        ArrayList<Map<String, String>> arrayList = new ArrayList<>();
        Map<String, String> map;
        try {
            if (json.getBoolean("Success")) {
                JSONArray jsonArray = json.getJSONArray("Invoice");
                for (int i = 0; i < jsonArray.length(); i++) {
                    map = new HashMap<>();
                    JSONObject json_obj = jsonArray.getJSONObject(i);
                    map.put("invoice", json_obj.getString("invoice"));
                    map.put("ctitle", json_obj.getString("ctitle"));
                    map.put("vat", json_obj.getString("vat"));
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
     * 1.3.9	結帳清單 - 讀取商家運送方式:Data[]
     * sno      String	商家碼
     * slno 	String	商家物流碼
     * plno	String	買家物流碼
     * type	    Number	類型0門市1宅配
     * land	    Number	送貨島嶼0無1本島2離島3海外
     * logistics    Number	送貨方式 0.無 1.7-11 2.全家 3.萊爾富 4.黑貓宅配 5.郵局 6.賣家宅配 7.海外配送
     * logisticsVal	String	送貨名稱 “7-11”
     * lpay	    Number	郵資
     * isused	Number	物流使用狀態 0未使用1使用中(當第一次無啟用時, [0][‘isused’]為1)
     * sname	String	門市名稱 “懿德門市”
     * iscom	Number	NONE
     */
    public static ArrayList<Map<String, String>> getStoreLogisticsData(JSONObject json) {
        ArrayList<Map<String, String>> arrayList = new ArrayList<>();
        Map<String, String> map;
        try {
            if (json.getBoolean("Success")) {
                JSONArray jsonArray = json.getJSONArray("Data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    map = new HashMap<>();
                    JSONObject json_obj = jsonArray.getJSONObject(i);
                    map.put("sno", json_obj.getString("sno"));
                    map.put("slno", json_obj.getString("slno"));
                    map.put("plno", json_obj.getString("plno"));
                    map.put("type", json_obj.getString("type"));
                    map.put("land", json_obj.getString("land"));
                    map.put("logistics", json_obj.getString("logistics"));
                    map.put("logisticsVal", json_obj.getString("logisticsVal"));
                    map.put("lpay", json_obj.getString("lpay"));
                    map.put("isused", json_obj.getString("isused"));
                    map.put("sname", json_obj.getString("sname"));
                    map.put("iscom", json_obj.getString("iscom"));
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
     * 1.3.9	結帳清單 - 讀取商家運送方式:myLogisticsArray[]
     * mlno	String	我的物流碼
     * name	String	姓名+電話
     * sname	String	門市名稱 “懿德門市”
     * sid	String	門市店號
     * address	String	地址
     * isused	Number	物流使用狀態 0未使用1使用中
     */
    public static ArrayList<ArrayList<Map<String, String>>> getmyLogisticsArray(JSONObject json) {
        ArrayList<ArrayList<Map<String, String>>> alllist;
        ArrayList<Map<String, String>> arrayList;
        Map<String, String> map;
        try {
            alllist = new ArrayList<>();
            if (json.getBoolean("Success")) {
                for (int i = 0; i < json.getJSONArray("Data").length(); i++) {
                    arrayList = new ArrayList<>();
                    if (!json.getJSONArray("Data").getJSONObject(i).getString("myLogisticsArray").equals("null")) {
                        JSONArray jsonArray = json.getJSONArray("Data").getJSONObject(i).getJSONArray("myLogisticsArray");
                        for (int j = 0; j < jsonArray.length(); j++) {
                            map = new HashMap<>();
                            JSONObject jsonObject = jsonArray.getJSONObject(j);
                            map.put("mlno", jsonObject.getString("mlno"));
                            map.put("name", jsonObject.getString("name"));
                            map.put("sname", jsonObject.getString("sname"));
                            map.put("sid", jsonObject.getString("sid"));
                            map.put("address", jsonObject.getString("address"));
                            map.put("isused", jsonObject.getString("isused"));
                            arrayList.add(map);
                        }
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
     * 1.3.12	結帳清單 - 讀取商家運送方式:Data[]
     * pno	String	付款碼
     * pname	String	付款名稱
     * info	String	說明
     * isused	Number	付款使用狀態 0未使用1使用中
     * <p>
     * NoneUse	String	無使用付款方式代碼
     */
    public static ArrayList<Map<String, String>> getMemberPaymentsData(JSONObject json) {
        ArrayList<Map<String, String>> arrayList = new ArrayList<>();
        Map<String, String> map;
        try {
            if (json.getBoolean("Success")) {
                JSONArray jsonArray = json.getJSONArray("Data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    map = new HashMap<>();
                    JSONObject json_obj = jsonArray.getJSONObject(i);
                    map.put("pno", json_obj.getString("pno"));
                    map.put("pname", json_obj.getString("pname"));
                    map.put("info", json_obj.getString("info"));
                    map.put("isused", json_obj.getString("isused"));
                    arrayList.add(map);
                }
                map = new HashMap<>();
                map.put("pno", json.getString("NoneUse"));
                arrayList.add(map);
                return arrayList;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 1.3.12	結帳清單 - 讀取商家運送方式:Pay[]
     * opay	Number	應付小計
     * xmoney	Number	X剩餘點數
     * xkeyin	Number	X 輸入點數
     * ymoney	Number	Y剩餘點數
     * ykeyin	Number	Y 輸入點數
     * ewallet	Number	電子錢包餘額
     * ekeyin	Number	電子錢輸入金額
     * xtrans	Number	X值, 金額 = X輸入點數 / X值
     * ytrans	Number	Y值, 金額 = Y輸入點數/Y值
     */
    public static ArrayList<Map<String, String>> getMemberPaymentsPay(JSONObject json) {
        ArrayList<Map<String, String>> arrayList = new ArrayList<>();
        Map<String, String> map;
        try {
            if (json.getBoolean("Success")) {
                JSONArray jsonArray = json.getJSONArray("Pay");
                for (int i = 0; i < jsonArray.length(); i++) {
                    map = new HashMap<>();
                    JSONObject json_obj = jsonArray.getJSONObject(i);
                    map.put("opay", json_obj.getString("opay"));
                    map.put("xmoney", json_obj.getString("xmoney"));
                    map.put("xkeyin", json_obj.getString("xkeyin"));
                    map.put("ymoney", json_obj.getString("ymoney"));
                    map.put("ykeyin", json_obj.getString("ykeyin"));
                    map.put("ewallet", json_obj.getString("ewallet"));
                    map.put("ekeyin", json_obj.getString("ekeyin"));
                    map.put("xtrans", json_obj.getString("xtrans"));
                    map.put("ytrans", json_obj.getString("ytrans"));
                    arrayList.add(map);
                }
                return arrayList;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
