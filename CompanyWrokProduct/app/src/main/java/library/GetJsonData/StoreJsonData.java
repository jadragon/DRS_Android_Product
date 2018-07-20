package library.GetJsonData;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import library.Http.JSONParser;

public class StoreJsonData {
    private final String getStoreOrder_url = "http://mall-tapi.gok1945.com/main/mcenter/sorder/getStoreOrder.php";
    private final String applyCancel_url = "http://mall-tapi.gok1945.com/main/mcenter/sorder/applyCancel.php";
    private final String applyReturn_url = "http://mall-tapi.gok1945.com/main/mcenter/sorder/applyReturn.php";
    private final String confirmReceipt_url = "http://mall-tapi.gok1945.com/main/mcenter/sorder/confirmReceipt.php";
    private final String stockingCompleted_url = "http://mall-tapi.gok1945.com/main/mcenter/sorder/stockingCompleted.php";
    private final String complaintMember_url = "http://mall-tapi.gok1945.com/main/mcenter/scomment/complaintMember.php";
    private final String getOrderComment_url = "http://mall-tapi.gok1945.com/main/mcenter/scomment/getOrderComment.php";
    private final String setOrderComment_url = "http://mall-tapi.gok1945.com/main/mcenter/scomment/setOrderComment.php";
    private final String getStoreComment_url = "http://mall-tapi.gok1945.com/main/mcenter/comment/getStoreComment.php";
    private final String setStoreComment_url = "http://mall-tapi.gok1945.com/main/mcenter/comment/setStoreComment.php";
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

    /**
     * 5.5.4	申請取消 - 同意、不同意
     */
    public JSONObject applyCancel(String token, String mono, int astatus, String note) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("mono", "" + mono));
        params.add(new BasicNameValuePair("astatus", "" + astatus));
        params.add(new BasicNameValuePair("note", "" + note));
        return jsonParser.getJSONFromUrl(applyCancel_url, params);
    }


    /**
     * 5.5.5	退換貨進度 – 同意換貨、不同意換貨
     */
    public JSONObject applyReturn(String token, String mono, int astatus, String note) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("mono", "" + mono));
        params.add(new BasicNameValuePair("astatus", "" + astatus));
        params.add(new BasicNameValuePair("note", "" + note));
        return jsonParser.getJSONFromUrl(applyReturn_url, params);
    }

    /**
     * 5.5.5	退換貨進度 – 同意換貨、不同意換貨
     */
    public JSONObject confirmReceipt(String token, String mono) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("mono", "" + mono));
        return jsonParser.getJSONFromUrl(confirmReceipt_url, params);
    }

    /**
     * 5.5.8	備貨中 – 備貨完成
     */
    public JSONObject stockingCompleted(String token, String mono) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("mono", "" + mono));
        return jsonParser.getJSONFromUrl(stockingCompleted_url, params);
    }

    /**
     * 5.5.9	已取件、已完成 - 讀取訂單評價資訊
     */
    public JSONObject getOrderComment(String token, String mono) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("mono", "" + mono));
        return jsonParser.getJSONFromUrl(getOrderComment_url, params);
    }

    /**
     * 5.5.10	已取件、已完成 - 設定訂單評價資訊
     */
    public JSONObject setOrderComment(String token, JSONArray data) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("data", "" + data));
        return jsonParser.getJSONFromUrl(setOrderComment_url, params);
    }

    /**
     * 5.5.11	已取件、已完成 – 投訴買家
     */
    public JSONObject complaintMember(String token, String mono, String note) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("mono", mono));
        params.add(new BasicNameValuePair("note", note));
        return jsonParser.getJSONFromUrl(complaintMember_url, params);
    }

    /**
     * 5.7.1	讀取商家評價資訊
     */
    public JSONObject getStoreComment(String token, int rule) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("rule", rule + ""));
        return jsonParser.getJSONFromUrl(getStoreComment_url, params);
    }

    /**
     * 5.7.3	回覆商家評價資訊
     */
    public JSONObject setStoreComment(String token, String moino, String comment) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("moino", moino));
        params.add(new BasicNameValuePair("comment", comment));
        return jsonParser.getJSONFromUrl(setStoreComment_url, params);
    }
}
