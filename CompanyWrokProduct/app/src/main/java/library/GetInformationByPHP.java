package library;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetInformationByPHP {
private static final String slider_url= "http://api.gok1945.com/main/index/slider.php";
    private static final String hotkeywords_url= "http://api.gok1945.com/main/index/hotkeywords.php";
    private static final String ptype_url= "http://api.gok1945.com/main/index/ptype.php";
    private static final String brands_url= "http://api.gok1945.com/main/index/brands.php";

    private  JSONParser jsonParser;
    List<NameValuePair> params;
    public GetInformationByPHP() {
        jsonParser=  new JSONParser();
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("gok", "Dr@_K4y51G2A0w26B8OWkfQ=="));
        params.add(new BasicNameValuePair("lang", "0"));
    }

    /**
     *取得廣告欄資料
     * */
    public JSONObject getSlider() {
      return  jsonParser.getJSONFromUrl(slider_url, params);
    }
    /**
     *取得廣告欄資料
     * */
    public JSONObject getHotkeywords() {
        return  jsonParser.getJSONFromUrl(hotkeywords_url, params);
    }
    /**
     *取得分類欄資料
     * */
    public JSONObject getPtype() {
        return  jsonParser.getJSONFromUrl(ptype_url, params);
    }
    /**
     *取得品牌欄資料
     * */
    public JSONObject getBrands() {
        return  jsonParser.getJSONFromUrl(brands_url, params);
    }
}
