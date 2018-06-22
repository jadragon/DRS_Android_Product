package library.GetJsonData;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import library.Http.JSONParser;

public class OrderInfoJsonData {
    private static final String getMemberOrder_url = "http://mall-tapi.gok1945.com/main/mcenter/morder/getMemberOrder.php";
    private static final String getMOrderPay_url = "http://mall-tapi.gok1945.com/main/mcenter/morder/getMOrderPay.php";
    private static final String getMOrderItem_url = "http://mall-tapi.gok1945.com/main/mcenter/morder/getMOrderItem.php";
    private JSONParser jsonParser;
    List<NameValuePair> params;

    public OrderInfoJsonData() {
        jsonParser = new JSONParser();
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("gok", "Dr@_K4y51G2A0w26B8OWkfQ=="));
        params.add(new BasicNameValuePair("lang", "0"));
    }

    /**
     * 3.1.1	讀取會員訂單資訊
     */
    public JSONObject getMemberOrder(String token, int ostatus, int pstatus, int lstatus, int istatus, int page) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("ostatus", "" + ostatus));
        params.add(new BasicNameValuePair("pstatus", "" + pstatus));
        params.add(new BasicNameValuePair("lstatus", "" + lstatus));
        params.add(new BasicNameValuePair("istatus", "" + istatus));
        params.add(new BasicNameValuePair("page", "" + page));
        return jsonParser.getJSONFromUrl(getMemberOrder_url, params);
    }

    /**
     * 3.1.1	讀取會員訂單資訊(懶人版)
     */
    public JSONObject getMemberOrder(String token, int index, int page) {
        int ostatus = 0;
        int pstatus = 0;
        int lstatus = 0;
        int istatus = 0;
        switch (index) {
            case 0:
                ostatus = 0;
                pstatus = 0;
                lstatus = 99;
                istatus = 99;
                break;
            case 1:
                ostatus = 0;
                pstatus = 99;
                lstatus = 0;
                istatus = 99;
                break;
            case 2:
                ostatus = 0;
                pstatus = 99;
                lstatus = 2;
                istatus = 99;
                break;
            case 3:
                ostatus = 0;
                pstatus = 99;
                lstatus = 4;
                istatus = 99;
                break;
            case 4:
                ostatus = 1;
                pstatus = 99;
                lstatus = 99;
                istatus = 99;
                break;
            case 5:
                ostatus = 99;
                pstatus = 99;
                lstatus = 99;
                istatus = 1;
                break;
            case 6:
                ostatus = -2;
                pstatus = 99;
                lstatus = 99;
                istatus = 99;
                break;
            default:
                break;
        }
        return getMemberOrder(token, ostatus, pstatus, lstatus, istatus, page);
    }

    /**
     * 3.1.2	讀取會員訂單付款詳情資訊
     */
    public JSONObject getMOrderPay(String token, String mono) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("mono", mono));
        return jsonParser.getJSONFromUrl(getMOrderPay_url, params);
    }

    /**
     * 3.1.3	讀取會員訂單詳情資訊
     */
    public JSONObject getMOrderItem(String token, String mono) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("mono", mono));
        return jsonParser.getJSONFromUrl(getMOrderItem_url, params);
    }
}
