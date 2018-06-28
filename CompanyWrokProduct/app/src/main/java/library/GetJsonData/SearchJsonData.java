package library.GetJsonData;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import library.Http.JSONParser;

public class SearchJsonData {
    private static final String search_url = "http://mall-tapi.gok1945.com/main/index/search.php";
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
    public JSONObject search(String keyword) {
        params.add(new BasicNameValuePair("keyword", keyword));
        return jsonParser.getJSONFromUrl(search_url, params);
    }

}
