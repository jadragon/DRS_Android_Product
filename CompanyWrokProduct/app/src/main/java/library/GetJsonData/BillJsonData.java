package library.GetJsonData;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class BillJsonData extends APIInfomation {
    private final String getBilling_url = DOMAIN + "main/mcenter/billing/getBilling.php";
    private final String getPolk_url = DOMAIN + "main/mcenter/billing/getPolk.php";
    private final String getKuva_url = DOMAIN + "main/mcenter/billing/getKuva.php";
    private final String getAcoin_url = DOMAIN + "main/mcenter/billing/getAcoin.php";
    private final String getEwallet_url = DOMAIN + "main/mcenter/billing/getEwallet.php";
    private final String getPolkRate_url = DOMAIN + "main/mcenter/billing/getPolkRate.php";
    private final String getPolkTrans_url = DOMAIN + "main/mcenter/billing/getPolkTrans.php";
    private final String getKuvaRate_url = DOMAIN + "main/mcenter/billing/getKuvaRate.php";
    private final String getKuvaTrans_url = DOMAIN + "main/mcenter/billing/getKuvaTrans.php";
    private final String getEwalletRate_url = DOMAIN + "main/mcenter/billing/getEwalletRate.php";
    private final String getEwalletTrans_url = DOMAIN + "main/mcenter/billing/getEwalletTrans.php";
    private final String getCoupon_url = DOMAIN + "main/mcenter/billing/getCoupon.php";
    private final String delCoupon_url = DOMAIN + "main/mcenter/billing/delCoupon.php";


    public BillJsonData() {
        super();
    }

    /**
     * 6.2.1	讀取波克點值
     */
    public JSONObject getBilling(String token) {
        params.add(new BasicNameValuePair("token", token));
        return jsonParser.getJSONFromUrl(getBilling_url, params);
    }

    /**
     * 6.2.1	讀取波克點值
     */
    public JSONObject getPolk(String token, int type) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("type", type + ""));
        return jsonParser.getJSONFromUrl(getPolk_url, params);
    }


    /**
     * 6.3.1	讀取庫瓦點值
     */
    public JSONObject getKuva(String token, int type) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("type", type + ""));
        return jsonParser.getJSONFromUrl(getKuva_url, params);
    }

    /**
     * 6.4.1	讀取A幣
     */
    public JSONObject getAcoin(String token, int type) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("type", type + ""));
        return jsonParser.getJSONFromUrl(getAcoin_url, params);
    }

    /**
     * 6.5.1	讀取電子錢包
     */
    public JSONObject getEwallet(String token, int type) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("type", type + ""));
        return jsonParser.getJSONFromUrl(getEwallet_url, params);
    }

    /**
     * 6.6.1	讀取波克點值及轉換比值
     */
    public JSONObject getPolkRate(String token) {
        params.add(new BasicNameValuePair("token", token));
        return jsonParser.getJSONFromUrl(getPolkRate_url, params);
    }

    /**
     * 6.6.2	設定波克點值轉換其它
     */
    public JSONObject getPolkTrans(String token, String kuva_num) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("kuva_num", kuva_num + ""));
        return jsonParser.getJSONFromUrl(getPolkTrans_url, params);
    }

    /**
     * 6.6.3	讀取庫瓦點值及轉換比值
     */
    public JSONObject getKuvaRate(String token) {
        params.add(new BasicNameValuePair("token", token));
        return jsonParser.getJSONFromUrl(getKuvaRate_url, params);
    }

    /**
     * 6.6.4	設定庫瓦點值轉換其它
     */
    public JSONObject getKuvaTrans(String token, String polk_num, String acoin_num) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("polk_num", polk_num + ""));
        params.add(new BasicNameValuePair("acoin_num", acoin_num + ""));
        return jsonParser.getJSONFromUrl(getKuvaTrans_url, params);
    }

    /**
     * 6.7.1	讀取電子錢包及轉換比值
     */
    public JSONObject getEwalletRate(String token) {
        params.add(new BasicNameValuePair("token", token));
        return jsonParser.getJSONFromUrl(getEwalletRate_url, params);
    }

    /**
     * 6.7.2	設定電子錢包轉換其它
     */
    public JSONObject getEwalletTrans(String token, String cash_num) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("cash_num", cash_num));
        return jsonParser.getJSONFromUrl(getEwalletTrans_url, params);
    }

    /**
     * 6.7.2	設定電子錢包轉換其它
     */
    public JSONObject getCoupon(String token) {
        params.add(new BasicNameValuePair("token", token));
        return jsonParser.getJSONFromUrl(getCoupon_url, params);
    }

    /**
     * 6.8.2	刪除現金折價券
     */
    public JSONObject delCoupon(String token, String mcno) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("mcno", mcno));
        return jsonParser.getJSONFromUrl(delCoupon_url, params);
    }
}
