package library.GetJsonData;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import library.Http.JSONParser;

public class StoreJsonData {
    private final String getStoreOrder_url = "http://mall-tapi.gok1945.com/main/mcenter/sorder/getStoreOrder.php";
    private JSONParser jsonParser;
    List<NameValuePair> params;

    public StoreJsonData() {
        jsonParser = new JSONParser();
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("gok", "Dr@_K4y51G2A0w26B8OWkfQ=="));
        params.add(new BasicNameValuePair("lang", "0"));
    }

    /**
     * 3.1.1	讀取會員訂單資訊
     */
    public JSONObject getStoreOrder(String token, int ostatus, int pstatus, int lstatus, int istatus, int page) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("ostatus", "" + ostatus));
        params.add(new BasicNameValuePair("pstatus", "" + pstatus));
        params.add(new BasicNameValuePair("lstatus", "" + lstatus));
        params.add(new BasicNameValuePair("istatus", "" + istatus));
        params.add(new BasicNameValuePair("page", "" + page));
        return jsonParser.getJSONFromUrl(getStoreOrder_url, params);
    }

    /**
     * 3.1.1	讀取會員訂單資訊(懶人版)
     */
    public JSONObject getStoreOrder(String token, int index, int page) {
        int ostatus = 0;
        int pstatus = 0;
        int lstatus = 0;
        int istatus = 0;
        switch (index) {
            case 0:
                ostatus = -1;
                pstatus = 99;
                lstatus = 99;
                istatus = 0;
                break;
            case 1:
                ostatus = 11;
                pstatus = 99;
                lstatus = 99;
                istatus = 0;
                break;
            case 2:
                ostatus = 0;
                pstatus = 99;
                lstatus = 1;
                istatus = 0;
                break;
            case 3:
                ostatus = 0;
                pstatus = 99;
                lstatus = 11;
                istatus = 0;
                break;
            case 4:
                ostatus = 0;
                pstatus = 0;
                lstatus = 0;
                istatus = 0;
                break;
            case 5:
                ostatus = 0;
                pstatus = 1;
                lstatus = 4;
                istatus = 0;
                break;
            case 6:
                ostatus = 99;
                pstatus = 99;
                lstatus = 99;
                istatus = 1;
                break;
            case 7:
                ostatus = -2;
                pstatus = 99;
                lstatus = 99;
                istatus = 99;
                break;
            default:
                break;
        }
        return getStoreOrder(token, ostatus, pstatus, lstatus, istatus, page);
    }

}
