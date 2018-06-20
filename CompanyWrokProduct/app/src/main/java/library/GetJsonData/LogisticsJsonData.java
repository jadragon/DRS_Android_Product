package library.GetJsonData;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import library.Http.JSONParser;

public class LogisticsJsonData {
    private static final String getLogistics_url = "http://mall-tapi.gok1945.com/main/mcenter/mlogistics/getLogistics.php";
    private static final String setLogistics_url = "http://mall-tapi.gok1945.com/main/mcenter/mlogistics/setLogistics.php";
    private static final String updateLogistics_url = "http://mall-tapi.gok1945.com/main/mcenter/mlogistics/updateLogistics.php";
    private static final String delLogistics_url = "http://mall-tapi.gok1945.com/main/mcenter/mlogistics/delLogistics.php";
    private static final String setInitLogistics_url = "http://mall-tapi.gok1945.com/main/mcenter/mlogistics/setInitLogistics.php";
    private JSONParser jsonParser;
    List<NameValuePair> params;

    public LogisticsJsonData() {
        jsonParser = new JSONParser();
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("gok", "Dr@_K4y51G2A0w26B8OWkfQ=="));
        params.add(new BasicNameValuePair("lang", "0"));
    }

    /**
     * 3.2.1	讀取收貨地址
     */
    public JSONObject getLogistics(String token, int type) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("type", "" + type));
        return jsonParser.getJSONFromUrl(getLogistics_url, params);
    }

    /**
     * 3.2.2	新增收貨地址
     */
    public JSONObject setLogistics(
            String token
            , String type
            , String land
            , String logistics
            , String name
            , String mpcode
            , String mp
            , String sname
            , String sid
            , String shit
            , String city
            , String area
            , String zipcode
            , String address
    ) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("type", type));
        params.add(new BasicNameValuePair("land", land));
        params.add(new BasicNameValuePair("logistics", logistics));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("mpcode", mpcode));
        params.add(new BasicNameValuePair("mp", mp));
        params.add(new BasicNameValuePair("sname", sname));
        params.add(new BasicNameValuePair("sid", sid));
        params.add(new BasicNameValuePair("shit", shit));
        params.add(new BasicNameValuePair("city", city));
        params.add(new BasicNameValuePair("area", area));
        params.add(new BasicNameValuePair("zipcode", zipcode));
        params.add(new BasicNameValuePair("address", address));
        return jsonParser.getJSONFromUrl(setLogistics_url, params);
    }

    /**
     * 3.2.3	修改收貨地址
     */
    public JSONObject updateLogistics(
            String token
            , String mlno
            , String name
            , String mpcode
            , String mp
            , String sname
            , String sid
            , String shit
            , String city
            , String area
            , String zipcode
            , String address) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("mlno", mlno));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("mpcode", mpcode));
        params.add(new BasicNameValuePair("mp", mp));
        params.add(new BasicNameValuePair("sname", sname));
        params.add(new BasicNameValuePair("sid", sid));
        params.add(new BasicNameValuePair("shit", shit));
        params.add(new BasicNameValuePair("city", city));
        params.add(new BasicNameValuePair("area", area));
        params.add(new BasicNameValuePair("zipcode", zipcode));
        params.add(new BasicNameValuePair("address", address));
        return jsonParser.getJSONFromUrl(updateLogistics_url, params);
    }

    /**
     * 3.2.4	刪除收貨地址
     */
    public JSONObject delLogistics(String token, String mlno) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("mlno", mlno));
        return jsonParser.getJSONFromUrl(delLogistics_url, params);
    }

    /**
     * 3.2.5	設定預設收貨地址
     */
    public JSONObject setInitLogistics(String token, String mlno, int type) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("mlno", mlno));
        params.add(new BasicNameValuePair("type", ""+type));
        return jsonParser.getJSONFromUrl(setInitLogistics_url, params);
    }


}
