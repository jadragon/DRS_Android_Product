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

    private static final String wait_carcheck_url = DOMAIN + "main/ccallnow/wait_carcheck.php";
    private static final String wait_catreset_url = DOMAIN + "main/ccallnow/wait_catreset.php";
    private static final String wait_carconfirm_url = DOMAIN + "main/ccallnow/wait_carconfirm.php";
    private static final String start_change_url = DOMAIN + "main/ccallnow/start_change.php";
    private static final String start_watiConfirm_url = DOMAIN + "main/ccallnow/start_watiConfirm.php";
    private static final String start_newOrder_url = DOMAIN + "main/ccallnow/start_newOrder.php";
    private static final String start_arrival_url = DOMAIN + "main/ccallnow/start_arrival.php";
    private static final String checkout_orderList_url = DOMAIN + "main/ccallnow/checkout_orderList.php";
    private static final String checkout_getCoupon_url = DOMAIN + "main/ccallnow/checkout_getCoupon.php";
    private static final String checkout_useCoupon_url = DOMAIN + "main/ccallnow/checkout_useCoupon.php";
    private static final String checkout_setPay_url = DOMAIN + "main/ccallnow/checkout_setPay.php";
    private static final String checkout_getPayStatus_url = DOMAIN + "main/ccallnow/checkout_getPayStatus.php";
    private static final String checkout_setEvaluation_url = DOMAIN + "main/ccallnow/checkout_setEvaluation.php";

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

    /**
     * 1.9.8	等待車輛檢查(5秒)
     */
    public static JSONObject wait_carcheck(String token, String pono) {
        List<NameValuePair> params = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        params.add(new BasicNameValuePair("ntgo", ntgo));
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("pono", pono));
        return jsonParser.getJSONFromUrl(wait_carcheck_url, params);
    }

    /**
     * 1.9.9	等待車輛檢查-重新檢查
     */
    public static JSONObject wait_catreset(String token, String pono) {
        List<NameValuePair> params = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        params.add(new BasicNameValuePair("ntgo", ntgo));
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("pono", pono));
        return jsonParser.getJSONFromUrl(wait_catreset_url, params);
    }

    /**
     * 1.9.10	等待車輛檢查-確認結束
     */
    public static JSONObject wait_carconfirm(String token, String pono) {
        List<NameValuePair> params = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        params.add(new BasicNameValuePair("ntgo", ntgo));
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("pono", pono));
        return jsonParser.getJSONFromUrl(wait_carconfirm_url, params);
    }

    /**
     * 1.9.11	開始駕駛-目的地更改
     */
    public static JSONObject start_change(String token, String pono, String address) {
        List<NameValuePair> params = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        params.add(new BasicNameValuePair("ntgo", ntgo));
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("pono", pono));
        params.add(new BasicNameValuePair("address", address));
        return jsonParser.getJSONFromUrl(start_change_url, params);
    }

    /**
     * 1.9.12	開始駕駛-等待更改確認(5秒)
     */
    public static JSONObject start_watiConfirm(String token, String pono) {
        List<NameValuePair> params = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        params.add(new BasicNameValuePair("ntgo", ntgo));
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("pono", pono));
        return jsonParser.getJSONFromUrl(start_watiConfirm_url, params);
    }

    /**
     * 1.9.13	開始駕駛-重新修改目的地位置-產生新的訂單
     */
    public static JSONObject start_newOrder(String token, String pono, String vaddress, String vlng, String vlat, String eaddress, String elng, String elat) {
        List<NameValuePair> params = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        params.add(new BasicNameValuePair("ntgo", ntgo));
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("pono", pono));
        params.add(new BasicNameValuePair("vaddress", vaddress));
        params.add(new BasicNameValuePair("vlng", vlng));
        params.add(new BasicNameValuePair("vlat", vlat));
        params.add(new BasicNameValuePair("eaddress", eaddress));
        params.add(new BasicNameValuePair("elng", elng));
        params.add(new BasicNameValuePair("elat", elat));
        return jsonParser.getJSONFromUrl(start_newOrder_url, params);
    }

    /**
     * 1.9.14	開始駕駛-到達目的地(5秒)
     */
    public static JSONObject start_arrival(String token, String pono) {
        List<NameValuePair> params = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        params.add(new BasicNameValuePair("ntgo", ntgo));
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("pono", pono));
        return jsonParser.getJSONFromUrl(start_arrival_url, params);
    }

    /**
     * 1.9.15	結帳–訂單列表
     */
    public static JSONObject checkout_orderList(String token, String pono) {
        List<NameValuePair> params = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        params.add(new BasicNameValuePair("ntgo", ntgo));
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("pono", pono));
        return jsonParser.getJSONFromUrl(checkout_orderList_url, params);
    }

    /**
     * 1.9.16	結帳–讀取優惠券資料
     */
    public static JSONObject checkout_getCoupon(String token, String pono) {
        List<NameValuePair> params = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        params.add(new BasicNameValuePair("ntgo", ntgo));
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("pono", pono));
        return jsonParser.getJSONFromUrl(checkout_getCoupon_url, params);
    }

    /**
     * 1.9.17	結帳–使用優惠券
     */
    public static JSONObject checkout_useCoupon(String token, String pono, String mcno) {
        List<NameValuePair> params = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        params.add(new BasicNameValuePair("ntgo", ntgo));
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("pono", pono));
        params.add(new BasicNameValuePair("mcno", mcno));
        return jsonParser.getJSONFromUrl(checkout_useCoupon_url, params);
    }

    /**
     * 1.9.18	結帳–支付
     */
    public static JSONObject checkout_setPay(String token, String pono, String pterms, String signfiles) {
        List<NameValuePair> params = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        params.add(new BasicNameValuePair("ntgo", ntgo));
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("pono", pono));
        params.add(new BasicNameValuePair("pterms", pterms));
        // TODO: 2018/12/17 File
        params.add(new BasicNameValuePair("signfiles", signfiles));

        return jsonParser.getJSONFromUrl(checkout_setPay_url, params);
    }

    /**
     * 1.9.19	結帳-等待付款狀態
     */
    public static JSONObject checkout_getPayStatus(String token, String pono) {
        List<NameValuePair> params = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        params.add(new BasicNameValuePair("ntgo", ntgo));
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("pono", pono));
        return jsonParser.getJSONFromUrl(checkout_getPayStatus_url, params);
    }

    /**
     * 1.9.20	結帳-評價
     */
    public static JSONObject checkout_setEvaluation(String token, String pono, String score, String val) {
        List<NameValuePair> params = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        params.add(new BasicNameValuePair("ntgo", ntgo));
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("pono", pono));
        params.add(new BasicNameValuePair("score", score));
        params.add(new BasicNameValuePair("val", val));
        return jsonParser.getJSONFromUrl(checkout_setEvaluation_url, params);
    }

}