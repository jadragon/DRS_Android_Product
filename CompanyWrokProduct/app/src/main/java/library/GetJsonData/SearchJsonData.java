package library.GetJsonData;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import library.Http.JSONParser;

public class SearchJsonData {
    private final String search_url = "http://mall-tapi.gok1945.com/main/index/search.php";
    private final String search_click_url = "http://mall-tapi.gok1945.com/main/index/search_click.php";
    private final String search_list_url = "http://mall-tapi.gok1945.com/main/index/search_list.php";
    private JSONParser jsonParser;
    List<NameValuePair> params;

    public SearchJsonData() {
        jsonParser = new JSONParser();
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("gok", "Dr@_K4y51G2A0w26B8OWkfQ=="));
        params.add(new BasicNameValuePair("lang", "0"));
    }

    /**
     * 1.1.6	搜尋 - Search Bar 前5個關鍵字
     */
    public JSONObject search(String token,String keyword) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("keyword", keyword));
        return jsonParser.getJSONFromUrl(search_url, params);
    }

    /**
     1.1.7	搜尋 - Search Bar 熱門搜尋點擊
     */
    public JSONObject search_click(String keyword) {
        params.add(new BasicNameValuePair("keyword", keyword));
        return jsonParser.getJSONFromUrl(search_click_url, params);
    }

    /**
     * 1.1.8	搜尋關鍵字 – 顯示商品列表
     */
    public JSONObject search_list(String token,String keyword, int page) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("keyword", keyword));
        params.add(new BasicNameValuePair("page", page + ""));
        return jsonParser.getJSONFromUrl(search_list_url, params);
    }
}
