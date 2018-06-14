package library.GetJsonData;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import library.Http.JSONParser;

public class HelpCenterJsonData {
    private static final String category_url = "http://api.gok1945.com/main/footer/category.php";
    private static final String searchCategory_url = "http://api.gok1945.com/main/footer/searchCategory.php";
    private static final String searchMbCategory_url = "http://api.gok1945.com/main/footer/searchMbCategory.php";
    private static final String citem_url = "http://api.gok1945.com/main/footer/citem.php";
    private JSONParser jsonParser;
    List<NameValuePair> params;

    public HelpCenterJsonData() {
        jsonParser = new JSONParser();
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("gok", "Dr@_K4y51G2A0w26B8OWkfQ=="));
        params.add(new BasicNameValuePair("lang", "0"));
    }

    /**
     * 8.3.1	讀取固定項目(第一層、第二層)
     */
    public JSONObject getCategory() {
        return jsonParser.getJSONFromUrl(category_url, params);
    }

    /**
     * 8.3.2	讀取搜尋項目(第二層)–方法一
     */
    public JSONObject searchCategory(String title) {
        params.add(new BasicNameValuePair("title", title));
        return jsonParser.getJSONFromUrl(searchCategory_url, params);
    }

    /**
     * 8.3.3	讀取搜尋項目(第二層) –方法二
     */
    public JSONObject searchMbCategory(String title) {
        params.add(new BasicNameValuePair("title", title));
        return jsonParser.getJSONFromUrl(searchMbCategory_url, params);
    }

    /**
     * 8.3.4	讀取標題內文
     */
    public JSONObject getCitem(String icno) {
        params.add(new BasicNameValuePair("icno", icno));
        return jsonParser.getJSONFromUrl(citem_url, params);
    }
}
