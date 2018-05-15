package library;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetInformationByPHP {
    private static final String slider_url = "http://api.gok1945.com/main/index/slider.php";
    private static final String banner_url = "http://api.gok1945.com/main/product/banner.php";
    private static final String hotkeywords_url = "http://api.gok1945.com/main/index/hotkeywords.php";
    private static final String ptype_url = "http://api.gok1945.com/main/index/ptype.php";
    private static final String brands_url = "http://api.gok1945.com/main/index/brands.php";
    private static final String iplist_url = "http://api.gok1945.com/main/index/iplist.php";
    private static final String delFavoriteProduct_url = "http://api.gok1945.com/main/record/delFavoriteProduct.php";
    private static final String setFavorite_url = "http://api.gok1945.com/main/record/setFavorite.php";
    private static final String plist_url = "http://api.gok1945.com/main/product/plist.php";
    private static final String pcontent_url = "http://api.gok1945.com/main/product/pcontent.php";
    private static final String setCart_url = "http://api.gok1945.com/main/cart/setCart.php";
    private static final String getCart_url = "http://api.gok1945.com/main/cart/getCart.php";
    private static final String addCartProduct_url = "http://api.gok1945.com/main/cart/addCartProduct.php";
    private static final String updatePortrait_url = "http://api.gok1945.com/main/mcenter/person/updatePortrait.php";
    private static final String delCartProduct_url = "http://api.gok1945.com/main/cart/delCartProduct.php";
    private static final String setCartDiscount_url = "http://api.gok1945.com/main/cart/setCartDiscount.php";
    private static final String delCartDiscount_url = "http://api.gok1945.com/main/cart/delCartDiscount.php";

    private static final String goCheckout_url = "http://api.gok1945.com/main/cart/goCheckout.php";
    private static final String getCheckout_url = "http://api.gok1945.com/main/cart/getCheckout.php";
    private JSONParser jsonParser;
    List<NameValuePair> params;

    public GetInformationByPHP() {
        jsonParser = new JSONParser();
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("gok", "Dr@_K4y51G2A0w26B8OWkfQ=="));
        params.add(new BasicNameValuePair("lang", "0"));
    }
    /*
public JSONObject test() {
    StringRequest stringRequest = new StringRequest(Request.Method.PUT,
            slider_url, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                myJsonObject = new JSONObject(response);
                Toast.makeText(ctx, "" + myJsonObject, Toast.LENGTH_SHORT).show();
                Log.d("Response", response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            // error
            Log.d("Error.Response", "" + error);
        }
    }) {

        @Override
        protected Map<String, String> getParams() {
            Map<String, String> params = new HashMap<String, String>();
            params.put("gok", "Dr@_K4y51G2A0w26B8OWkfQ==");
            params.put("lang", "0");

            return params;
        }

    };
    MySingleton.getInstance(ctx).addToRequestQue(stringRequest);
    return myJsonObject;
}
*/

    /**
     * 取消最愛
     */
    public JSONObject delFavoriteProduct(String token, String pno) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("pno", pno));
        return jsonParser.getJSONFromUrl(delFavoriteProduct_url, params);
    }

    /**
     * 設為最愛
     */
    public JSONObject setFavorite(String token, String pno) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("pno", pno));
        return jsonParser.getJSONFromUrl(setFavorite_url, params);
    }

    /**
     * 設為最愛
     */
    public JSONObject getPcontent(String pno) {
        params.add(new BasicNameValuePair("pno", pno));
        return jsonParser.getJSONFromUrl(pcontent_url, params);
    }

    /**
     * 取得廣告欄資料
     */
    public JSONObject getSlider() {
        return jsonParser.getJSONFromUrl(slider_url, params);
    }

    /**
     * 取得購物廣告欄資料
     */
    public JSONObject getBanner(int type) {
        params.add(new BasicNameValuePair("type", "" + type));
        return jsonParser.getJSONFromUrl(banner_url, params);
    }

    /**
     * 取得廣告欄資料
     */
    public JSONObject getHotkeywords() {
        return jsonParser.getJSONFromUrl(hotkeywords_url, params);
    }

    /**
     * 取得分類欄資料
     */
    public JSONObject getPtype() {
        return jsonParser.getJSONFromUrl(ptype_url, params);
    }

    /**
     * 取得品牌欄資料
     */
    public JSONObject getBrands() {
        return jsonParser.getJSONFromUrl(brands_url, params);
    }

    /**
     * 取得每日新品欄資料
     */
    public JSONObject getIplist(int type, int page) {
        params.add(new BasicNameValuePair("type", "" + type));
        params.add(new BasicNameValuePair("page", "" + page));
        return jsonParser.getJSONFromUrl(iplist_url, params);
    }

    /**
     * 取得商品列表欄資料
     */
    public JSONObject getPlist(String ptno, int type, int page) {
        params.add(new BasicNameValuePair("ptno", ptno));
        params.add(new BasicNameValuePair("type", "" + type));
        params.add(new BasicNameValuePair("page", "" + page));
        return jsonParser.getJSONFromUrl(plist_url, params);
    }

    /**
     * 加入購物車
     */
    public JSONObject setCart(String token, String pno, String pino, int total) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("pno", pno));
        params.add(new BasicNameValuePair("pino", pino));
        params.add(new BasicNameValuePair("total", "" + total));
        return jsonParser.getJSONFromUrl(setCart_url, params);
    }

    /**
     * 購買清單 - 讀取購買資訊
     */
    public JSONObject getCart(String token) {
        params.add(new BasicNameValuePair("token", token));
        return jsonParser.getJSONFromUrl(getCart_url, params);
    }

    /**
     * 購買清單 - 商品增加數量或減少數量
     */
    public JSONObject addCartProduct(String token, String morno, int total) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("morno", "" + morno));
        params.add(new BasicNameValuePair("total", "" + total));
        return jsonParser.getJSONFromUrl(addCartProduct_url, params);
    }

    /**
     * 購買清單 - 移除購買商品
     */
    public JSONObject delCartProduct(String token, String morno) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("morno", morno));
        return jsonParser.getJSONFromUrl(delCartProduct_url, params);
    }

    /**
     * 購買清單 - 輸入折扣代碼
     */
    public JSONObject setCartDiscount(String token, String coupon) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("coupon", coupon));
        return jsonParser.getJSONFromUrl(setCartDiscount_url, params);
    }

    /**
     * 購買清單 - 移除購買商品
     */
    public JSONObject delCartDiscount(String token, String moprno) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("moprno", moprno));
        return jsonParser.getJSONFromUrl(delCartDiscount_url, params);
    }

    /**
     * 上傳圖片
     */
    public JSONObject updatePortrait(String token, String img) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("img", "" + img));
        return jsonParser.getJSONFromUrl(updatePortrait_url, params);
    }

    /**
     * 1.3.8	結帳清單 - 讀取結帳資訊
     */
    public JSONObject goCheckout(String token, String mornoArray) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("mornoArray", mornoArray));
        return jsonParser.getJSONFromUrl(goCheckout_url, params);
    }

    /**
     * 1.3.8	結帳清單 - 讀取結帳資訊
     */
    public JSONObject getCheckout(String token) {
        params.add(new BasicNameValuePair("token", token));
        return jsonParser.getJSONFromUrl(getCheckout_url, params);
    }
}
