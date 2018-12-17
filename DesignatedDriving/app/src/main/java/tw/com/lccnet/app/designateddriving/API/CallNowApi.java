package tw.com.lccnet.app.designateddriving.API;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tw.com.lccnet.app.designateddriving.Http.JSONParser;

public class CallNowApi implements APISetting {

    private static final String calculate1_url = DOMAIN + "main/ccallnow/calculate1.php";
    private static final String match_start_url = DOMAIN + "main/ccallnow/match_start.php";
    private static final String match_driver_url = DOMAIN + "main/ccallnow/match_driver.php";
    private static final String wait_driverInfo_url = DOMAIN + "main/ccallnow/wait_driverInfo.php";
    private static final String wait_setMsg_url = DOMAIN + "main/ccallnow/wait_setMsg.php";
    private static final String wait_chkArrival_url = DOMAIN + "main/ccallnow/wait_chkArrival.php";
    private static final String cancel_order_url = DOMAIN + "main/ccallnow/cancel_order.php";

    /**
     * 1.9.1	試算金額
     */
    public static JSONObject calculate1(String token, String vaddress, String vlng, String vlat, String eaddress, String elng, String elat) {
        List<NameValuePair> params = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        params.add(new BasicNameValuePair("ntgo", ntgo));
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("vaddress", vaddress));
        params.add(new BasicNameValuePair("vlng", vlng));
        params.add(new BasicNameValuePair("vlat", vlat));
        params.add(new BasicNameValuePair("eaddress", eaddress));
        params.add(new BasicNameValuePair("elng", elng));
        params.add(new BasicNameValuePair("elat", elat));
        return jsonParser.getJSONFromUrl(calculate1_url, params);
    }

    /**
     * 1.9.1	試算金額
     */
    public static JSONObject match_start(String token, String vaddress, String vlng, String vlat, String eaddress, String elng, String elat) {
        List<NameValuePair> params = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        params.add(new BasicNameValuePair("ntgo", ntgo));
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("vaddress", vaddress));
        params.add(new BasicNameValuePair("vlng", vlng));
        params.add(new BasicNameValuePair("vlat", vlat));
        params.add(new BasicNameValuePair("eaddress", eaddress));
        params.add(new BasicNameValuePair("elng", elng));
        params.add(new BasicNameValuePair("elat", elat));
        return jsonParser.getJSONFromUrl(match_start_url, params);
    }

    /**
     * 1.9.3	匹配司機(5秒)
     */
    public static JSONObject match_driver(String token, String pono) {
        List<NameValuePair> params = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        params.add(new BasicNameValuePair("ntgo", ntgo));
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("pono", pono));
        return jsonParser.getJSONFromUrl(match_driver_url, params);
    }

    /**
     * 1.9.4	等待司機-司機是否到達狀態、乘客是否放鳥狀態、司機資訊、留言備註 (10秒)
     */
    public static JSONObject wait_driverInfo(String token, String pono) {
        List<NameValuePair> params = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        params.add(new BasicNameValuePair("ntgo", ntgo));
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("pono", pono));
        return jsonParser.getJSONFromUrl(wait_driverInfo_url, params);
    }


    /**
     * 1.9.5	等待司機-發送留言
     */
    public static JSONObject wait_setMsg(String token, String pono, String msg) {
        List<NameValuePair> params = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        params.add(new BasicNameValuePair("ntgo", ntgo));
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("pono", pono));
        params.add(new BasicNameValuePair("msg", msg));
        return jsonParser.getJSONFromUrl(wait_setMsg_url, params);
    }

    /**
     * 1.9.6	等待司機-司機到達(確認)
     */
    public static JSONObject wait_chkArrival(String token, String pono) {
        List<NameValuePair> params = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        params.add(new BasicNameValuePair("ntgo", ntgo));
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("pono", pono));
        return jsonParser.getJSONFromUrl(wait_chkArrival_url, params);
    }

    /**
     * 1.9.7	取消訂單
     */
    public static JSONObject cancel_order(String token, String pono) {
        List<NameValuePair> params = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        params.add(new BasicNameValuePair("ntgo", ntgo));
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("pono", pono));
        return jsonParser.getJSONFromUrl(cancel_order_url, params);
    }
}
