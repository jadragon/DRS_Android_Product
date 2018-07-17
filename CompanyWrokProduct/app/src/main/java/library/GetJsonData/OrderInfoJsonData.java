package library.GetJsonData;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import library.Http.JSONParser;

public class OrderInfoJsonData {
    private final String getMemberOrder_url = "http://mall-tapi.gok1945.com/main/mcenter/morder/getMemberOrder.php";
    private final String getMOrderPay_url = "http://mall-tapi.gok1945.com/main/mcenter/morder/getMOrderPay.php";
    private final String getMOrderItem_url = "http://mall-tapi.gok1945.com/main/mcenter/morder/getMOrderItem.php";
    private final String applyCancel_url = "http://mall-tapi.gok1945.com/main/mcenter/morder/applyCancel.php";
    private final String cancelMOrder_url = "http://mall-tapi.gok1945.com/main/mcenter/morder/cancelMOrder.php";
    private final String extendReceipt_url = "http://mall-tapi.gok1945.com/main/mcenter/morder/extendReceipt.php";
    private final String confirmReceipt_url = "http://mall-tapi.gok1945.com/main/mcenter/morder/confirmReceipt.php";
    private final String applyReturn_url = "http://mall-tapi.gok1945.com/main/mcenter/morder/applyReturn.php";
    private final String complaintStore_url = "http://mall-tapi.gok1945.com/main/mcenter/morder/complaintStore.php";
    private final String getOrderComment_url = "http://mall-tapi.gok1945.com/main/mcenter/comment/getOrderComment.php";
    private final String setOrderComment_url = "http://mall-tapi.gok1945.com/main/mcenter/comment/setOrderComment.php";
    private final String getMOrderReturnItem_url = "http://mall-tapi.gok1945.com/main/mcenter/morder/getMOrderReturnItem.php";
    private final String applyReturnLnum_url = "http://mall-tapi.gok1945.com/main/mcenter/morder/applyReturnLnum.php";
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
                lstatus = 0;
                istatus = 0;
                break;
            case 1:
                ostatus = 12;
                pstatus = 99;
                lstatus = 1;
                istatus = 0;
                break;
            case 2:
                ostatus = 0;
                pstatus = 99;
                lstatus = 11;
                istatus = 0;
                break;
            case 3:
                ostatus = 0;
                pstatus = 1;
                lstatus = 4;
                istatus = 0;
                break;
            case 4:
                ostatus = 11;
                pstatus = 99;
                lstatus = 99;
                istatus = 0;
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

    /**
     * 3.1.4	申請取消訂單
     */
    public JSONObject applyCancel(String token, String mono, String note) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("mono", mono));
        params.add(new BasicNameValuePair("note", note));
        return jsonParser.getJSONFromUrl(applyCancel_url, params);
    }

    /**
     * 3.1.5	取消訂單
     */
    public JSONObject cancelMOrder(String token, String mono, String note) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("mono", mono));
        params.add(new BasicNameValuePair("note", note));
        return jsonParser.getJSONFromUrl(cancelMOrder_url, params);
    }

    /**
     * 3.1.6	申請延長收貨
     */
    public JSONObject extendReceipt(String token, String mono) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("mono", mono));
        return jsonParser.getJSONFromUrl(extendReceipt_url, params);
    }

    /**
     * 3.1.7	確認收貨
     */
    public JSONObject confirmReceipt(String token, String mono) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("mono", mono));
        return jsonParser.getJSONFromUrl(confirmReceipt_url, params);
    }

    /**
     * 3.1.8	申請退換貨
     */
    public JSONObject applyReturn(String token,String type, String mono, String moinoArray, String numArray, String note, String img1, String img2, String img3, String img4) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("type", type));
        params.add(new BasicNameValuePair("mono", mono));
        params.add(new BasicNameValuePair("moinoArray", moinoArray));
        params.add(new BasicNameValuePair("numArray", numArray));
        params.add(new BasicNameValuePair("note", note));
        return jsonParser.getJSONFromUrl(applyReturn_url, params);
    }

    /**
     * 3.1.9	投訴商家
     */
    public JSONObject complaintStore(String token, String mono, String note) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("mono", mono));
        params.add(new BasicNameValuePair("note", note));
        return jsonParser.getJSONFromUrl(complaintStore_url, params);
    }

    /**
     * 3.1.20	讀取訂單評價資訊
     */
    public JSONObject getOrderComment(String token, String mono) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("mono", mono));
        return jsonParser.getJSONFromUrl(getOrderComment_url, params);
    }

    /**
     * 3.1.20	讀取訂單評價資訊
     */
    public JSONObject setOrderComment(String token, JSONArray data) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("data", data + ""));
        return jsonParser.getJSONFromUrl(setOrderComment_url, params);
    }


    /**
     * 3.1.22	退換貨進度 – 讀取訂單商品列表
     */
    public JSONObject getMOrderReturnItem(String token, String mono) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("mono", mono));
        return jsonParser.getJSONFromUrl(getMOrderReturnItem_url, params);
    }


    /**
     * 3.1.23	退換貨進度 – 輸入寄貨物流編號
     */
    public JSONObject applyReturnLnum(String token, String mono, String lnum) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("mono", mono));
        params.add(new BasicNameValuePair("lnum", lnum));
        return jsonParser.getJSONFromUrl(applyReturnLnum_url, params);
    }
}
