package library;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class GetWebView {
    private static final String register_url = "http://api.gok1945.com/main/snetwork/register.php";
    private static final String activation_url = "http://api.gok1945.com/main/snetwork/activation.php";
    private static final String inactivated_url = "http://api.gok1945.com/main/snetwork/inactivated.php";
    private static final String directPush_url = "http://api.gok1945.com/main/snetwork/directPush.php";
    private static final String placement_url = "http://api.gok1945.com/main/snetwork/placement.php";
    private static final String upgrade_url = "http://api.gok1945.com/main/snetwork/upgrade.php";
    private static final String recommend_url = "http://api.gok1945.com/main/snetwork/recommend.php";
    private static final String agency_url = "http://api.gok1945.com/main/snetwork/agency.php";

    private WebVewJsonParser jsonParser;
    List<NameValuePair> params;

    public GetWebView() {
        jsonParser = new WebVewJsonParser();
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("gok", "Dr@_K4y51G2A0w26B8OWkfQ=="));
        params.add(new BasicNameValuePair("lang", "0"));
        params.add(new BasicNameValuePair("device", "3"));
    }

    /**
     * 會員註冊
     */
    public String register(String token) {
        params.add(new BasicNameValuePair("token", token));
        return jsonParser.getJSONFromUrl(register_url, params);
    }

    /**
     * 未激活會員
     */

    public String activation(String token) {
        params.add(new BasicNameValuePair("token", token));
        return jsonParser.getJSONFromUrl(activation_url, params);
    }

    /**
     * 激活會員
     */
    public String inactivated(String token) {
        params.add(new BasicNameValuePair("token", token));
        return jsonParser.getJSONFromUrl(inactivated_url, params);
    }

    /**
     * 直推會員
     */
    public String directPush(String token) {
        params.add(new BasicNameValuePair("token", token));
        return jsonParser.getJSONFromUrl(directPush_url, params);
    }

    /**
     * 接點網路
     */
    public String placement(String token) {
        params.add(new BasicNameValuePair("token", token));
        return jsonParser.getJSONFromUrl(placement_url, params);
    }

    /**
     * 原點升級
     */
    public String upgrade(String token) {
        params.add(new BasicNameValuePair("token", token));
        return jsonParser.getJSONFromUrl(upgrade_url, params);
    }

    /**
     * 推薦會員
     */
    public String recommend(String token) {
        params.add(new BasicNameValuePair("token", token));
        return jsonParser.getJSONFromUrl(recommend_url, params);
    }

    /**
     * 申請區域代理
     */
    public String agency(String token) {
        params.add(new BasicNameValuePair("token", token));
        return jsonParser.getJSONFromUrl(agency_url, params);
    }

}
