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
    public JSONObject delFavoriteProduct(String token,String pno) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("pno", pno));
        return jsonParser.getJSONFromUrl(delFavoriteProduct_url, params);
    }

    /**
     * 設為最愛
     */
    public JSONObject setFavorite(String token,String pno) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("pno", pno));
        return jsonParser.getJSONFromUrl(setFavorite_url, params);
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
        params.add(new BasicNameValuePair("page", "0" + page));
        return jsonParser.getJSONFromUrl(iplist_url, params);
    }
}
