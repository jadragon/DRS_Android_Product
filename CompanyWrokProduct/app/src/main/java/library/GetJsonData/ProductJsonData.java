package library.GetJsonData;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import library.Http.JSONParser;

public class ProductJsonData {
    private final String getAddress_url = "http://mall-tapi.gok1945.com/main/cart/getAddress.php";
    private final String slider_url = "http://mall-tapi.gok1945.com/main/index/slider.php";
    private final String banner_url = "http://mall-tapi.gok1945.com/main/product/banner.php";
    private final String hotkeywords_url = "http://mall-tapi.gok1945.com/main/index/hotkeywords.php";
    private final String ptype_url = "http://mall-tapi.gok1945.com/main/index/ptype.php";
    private final String brands_url = "http://mall-tapi.gok1945.com/main/index/brands.php";
    private final String iplist_url = "http://mall-tapi.gok1945.com/main/index/iplist.php";
    private final String delFavoriteProduct_url = "http://mall-tapi.gok1945.com/main/record/delFavoriteProduct.php";
    private final String setFavorite_url = "http://mall-tapi.gok1945.com/main/record/setFavorite.php";
    private final String plist_url = "http://mall-tapi.gok1945.com/main/product/plist.php";
    private final String getProductComment_url = "http://mall-tapi.gok1945.com/main/mcenter/comment/getProductComment.php";
    private final String pcontent_url = "http://mall-tapi.gok1945.com/main/product/pcontent.php";
    private final String getBrowse_url = "http://mall-tapi.gok1945.com/main/record/getBrowse.php";
    private final String getFavorite_url = "http://mall-tapi.gok1945.com/main/record/getFavorite.php";
    private JSONParser jsonParser;
    List<NameValuePair> params;

    public ProductJsonData() {
        jsonParser = new JSONParser();
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("gok", "Dr@_K4y51G2A0w26B8OWkfQ=="));
        params.add(new BasicNameValuePair("lang", "0"));
    }


    /**
     * 4.2.2	寫入我的最愛
     */
    public JSONObject setFavorite(String token, String pno) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("pno", pno));
        return jsonParser.getJSONFromUrl(setFavorite_url, params);
    }

    /**
     * 4.2.3	移除我的最愛 - 我的最愛列表
     */
    public JSONObject getPcontent(String token, String pno) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("pno", pno));
        return jsonParser.getJSONFromUrl(pcontent_url, params);
    }

    /**
     * 4.2.4	移除我的最愛 - 商品列表、商品內頁
     */
    public JSONObject delFavoriteProduct(String token, String pno) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("pno", pno));
        return jsonParser.getJSONFromUrl(delFavoriteProduct_url, params);
    }

    /**
     * 1.1.1	首頁Banner
     */
    public JSONObject getSlider() {
        return jsonParser.getJSONFromUrl(slider_url, params);
    }

    /**
     * 1.1.2	熱門關鍵字
     */
    public JSONObject getHotkeywords(String token) {
        params.add(new BasicNameValuePair("token", token));
        return jsonParser.getJSONFromUrl(hotkeywords_url, params);
    }


    /**
     * 1.1.3	品牌資訊
     */
    public JSONObject getBrands() {
        return jsonParser.getJSONFromUrl(brands_url, params);
    }

    /**
     * 1.1.4	商品品項
     */
    public JSONObject getPtype() {
        return jsonParser.getJSONFromUrl(ptype_url, params);
    }

    /**
     * 1.1.5	首頁商品 & 購物
     */
    public JSONObject getIplist(int type, String token, int page) {
        params.add(new BasicNameValuePair("type", "" + type));
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("page", "" + page));
        return jsonParser.getJSONFromUrl(iplist_url, params);
    }

    /**
     * 1.2.3	商品列表 - Banner
     */
    public JSONObject getBanner(int type) {
        params.add(new BasicNameValuePair("type", "" + type));
        return jsonParser.getJSONFromUrl(banner_url, params);
    }

    /**
     * 1.2.1	商品列表
     */
    public JSONObject getPlist(String ptno, int type, String token, int page) {
        params.add(new BasicNameValuePair("ptno", ptno));
        params.add(new BasicNameValuePair("type", "" + type));
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("page", "" + page));
        return jsonParser.getJSONFromUrl(plist_url, params);
    }
    /**
     1.2.6	讀取商品評價資訊
     */
    public JSONObject getProductComment(String pno) {
        params.add(new BasicNameValuePair("pno", pno));
        return jsonParser.getJSONFromUrl(getProductComment_url, params);
    }

    /**
     * 1.3.16	讀取地址
     */
    public JSONObject getAddress(String modifydate) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("gok", "Dr@_K4y51G2A0w26B8OWkfQ=="));
        params.add(new BasicNameValuePair("lang", "0"));
        params.add(new BasicNameValuePair("modifydate", modifydate));
        return jsonParser.getJSONFromUrl(getAddress_url, params);
    }

    /**
     * 4.1.1	讀取個人瀏覽資訊
     */
    public JSONObject getBrowse(String token) {
        params.add(new BasicNameValuePair("token", token));
        return jsonParser.getJSONFromUrl(getBrowse_url, params);
    }

    /**
     * 4.2.1	讀取我的最愛資訊
     */
    public JSONObject getFavorite(String token) {
        params.add(new BasicNameValuePair("token", token));
        return jsonParser.getJSONFromUrl(getFavorite_url, params);
    }
}
