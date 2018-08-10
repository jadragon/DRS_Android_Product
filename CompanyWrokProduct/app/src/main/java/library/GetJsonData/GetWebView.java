package library.GetJsonData;


import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

import library.Http.CertJSONParser;
import library.Http.WebVewJsonParser;

public class GetWebView extends APIInfomation {
    private  final String register_url = DOMAIN+"main/snetwork/register.php";
    private  final String activation_url = DOMAIN+"main/snetwork/activation.php";
    private  final String inactivated_url = DOMAIN+"main/snetwork/inactivated.php";
    private  final String directPush_url = DOMAIN+"main/snetwork/directPush.php";
    private  final String placement_url = DOMAIN+"main/snetwork/placement.php";
    private  final String upgrade_url = DOMAIN+"main/snetwork/upgrade.php";
    private  final String recommend_url = DOMAIN+"main/snetwork/recommend.php";
    private  final String agency_url = DOMAIN+"main/snetwork/agency.php";
    private  final String getMap_url = DOMAIN+"main/cart/getMap.php";
    private  final String setGoldFlow_url = DOMAIN+"main/cart/setGoldFlow.php";
    private WebVewJsonParser jsonParser;
    private CertJSONParser certJSONParser;

    public GetWebView() {
        certJSONParser = new CertJSONParser();
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("gok",  GOK));
        params.add(new BasicNameValuePair("lang", LANG));
        params.add(new BasicNameValuePair("device", "3"));
    }

    public GetWebView(String logistics) {
        jsonParser = new WebVewJsonParser();
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("logistics", logistics));
        params.add(new BasicNameValuePair("device", "1"));
    }

    public String getHtmlByPosition(String token, int position) {
        switch (position) {
            case 0:
                return placement(token);
            case 1:
                return register(token);

            case 2:
                return inactivated(token);

            case 3:
                return activation(token);

            case 4:
                return directPush(token);

            case 5:
                return upgrade(token);

            case 6:
                return recommend(token);

            case 7:
                return agency(token);

            default:
                return null;

        }
    }

    /**
     * 會員註冊
     */
    private String register(String token) {
        params.add(new BasicNameValuePair("token", token));
        return certJSONParser.getJSONFromUrl(register_url, params);
    }

    /**
     * 未激活會員
     */

    private String activation(String token) {
        params.add(new BasicNameValuePair("token", token));
        return certJSONParser.getJSONFromUrl(activation_url, params);
    }

    /**
     * 激活會員
     */
    private String inactivated(String token) {
        params.add(new BasicNameValuePair("token", token));
        return certJSONParser.getJSONFromUrl(inactivated_url, params);
    }

    /**
     * 直推會員
     */
    private String directPush(String token) {
        params.add(new BasicNameValuePair("token", token));
        return certJSONParser.getJSONFromUrl(directPush_url, params);
    }

    /**
     * 接點網路
     */
    private String placement(String token) {
        params.add(new BasicNameValuePair("token", token));
        return certJSONParser.getJSONFromUrl(placement_url, params);
    }

    /**
     * 原點升級
     */
    private String upgrade(String token) {
        params.add(new BasicNameValuePair("token", token));
        return certJSONParser.getJSONFromUrl(upgrade_url, params);
    }

    /**
     * 推薦會員
     */
    private String recommend(String token) {
        params.add(new BasicNameValuePair("token", token));
        return certJSONParser.getJSONFromUrl(recommend_url, params);
    }

    /**
     * 申請區域代理
     */
    private String agency(String token) {
        params.add(new BasicNameValuePair("token", token));
        return certJSONParser.getJSONFromUrl(agency_url, params);
    }

    /**
     * 超商位置
     */
    public String getMap() {
        return jsonParser.getJSONFromUrl(getMap_url, params);
    }

    /**
     * 1.3.15	訂單完成 - 處理金流流程
     */
    public String setGoldFlow(String token) {
        params.add(new BasicNameValuePair("token", token));
        return jsonParser.getJSONFromUrl(setGoldFlow_url, params);
    }
}
