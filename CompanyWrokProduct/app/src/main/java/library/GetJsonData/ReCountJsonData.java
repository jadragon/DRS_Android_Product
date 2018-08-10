package library.GetJsonData;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import library.Http.CertJSONParser;

public class ReCountJsonData extends APIInfomation {
    public static final int COUNT = 0;
    public static final int RECOUNT = 1;
    private final String count_url = DOMAIN + "main/cart/";
    private final String recount_url = DOMAIN + "main/mcenter/morder/";

    private final String goCheckout_url = "goCheckout.php";
    private final String getCheckout_url = "getCheckout.php";
    private final String getStoreLogistics_url = "getStoreLogistics.php";
    private final String setStoreMemberLogistics_url = "setStoreMemberLogistics.php";
    private final String setMemberLogistics_url = "setMemberLogistics.php";
    private final String getMemberPayment_url = "getMemberPayment.php";
    private final String setMemberPayment_url = "setMemberPayment.php";
    private final String setStoreNote_url = "setStoreNote.php";
    private final String setGoldFlow_url = "setGoldFlow.php";
    private final String setVat_url = "setVat.php";

    public ReCountJsonData() {
        super();
    }

    /**
     * 3.1.10	重新結帳 - 去結帳
     */
    public JSONObject goCheckout(int count_type, String token, String mono) {
        params.add(new BasicNameValuePair("token", token));
        if (count_type == COUNT) {
            params.add(new BasicNameValuePair("mornoArray", mono));
            return jsonParser.getJSONFromUrl(count_url + goCheckout_url, params);
        } else {
            params.add(new BasicNameValuePair("mono", mono));
            return jsonParser.getJSONFromUrl(recount_url + goCheckout_url, params);
        }
    }

    /**
     * 3.1.11	重新結帳 - 讀取結帳資訊
     */
    public JSONObject getCheckout(int count_type, String token) {
        params.add(new BasicNameValuePair("token", token));
        if (count_type == COUNT) {
            return jsonParser.getJSONFromUrl(count_url + getCheckout_url, params);
        } else {
            return jsonParser.getJSONFromUrl(recount_url + getCheckout_url, params);
        }
    }

    /**
     * 3.1.12	重新結帳 - 讀取商家運送方式
     */
    public JSONObject getStoreLogistics(int count_type, String token, String sno) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("sno", sno));
        if (count_type == COUNT) {
            return jsonParser.getJSONFromUrl(count_url + getStoreLogistics_url, params);
        } else {
            return jsonParser.getJSONFromUrl(recount_url + getStoreLogistics_url, params);
        }
    }

    /**
     * 3.1.13	重新結帳 - 設定商家中, 買家運送方式
     */
    public JSONObject setStoreMemberLogistics(int count_type, String token, String sno, String plno, String mlno) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("sno", sno));
        params.add(new BasicNameValuePair("plno", plno));
        params.add(new BasicNameValuePair("mlno", mlno));
        if (count_type == COUNT) {
            return jsonParser.getJSONFromUrl(count_url + setStoreMemberLogistics_url, params);
        } else {
            return jsonParser.getJSONFromUrl(recount_url + setStoreMemberLogistics_url, params);
        }
    }

    /**
     * 3.1.14-1	重新結帳 - 新增買家物流資訊
     */
    public JSONObject setMemberLogistics(int count_type, String token, String sno, String plno, String type, String land,
                                         String logistics, String name, String mpcode, String mp, String sname, String sid,
                                         String shit, String city, String area, String zipcode, String address) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("sno", sno));
        params.add(new BasicNameValuePair("plno", plno));
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
        if (count_type == COUNT) {
            return jsonParser.getJSONFromUrl(count_url + setMemberLogistics_url, params);
        } else {
            return jsonParser.getJSONFromUrl(recount_url + setMemberLogistics_url, params);
        }
    }

    /**
     * 3.1.14-2	重新結帳 - 新增買家物流資訊
     */
    public JSONObject setMemberLogistics(int count_type, String token, String sno, String plno, String type, String land,
                                         String logistics, String name, String mpcode, String mp,
                                         String shit, String city, String area, String zipcode, String address) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("sno", sno));
        params.add(new BasicNameValuePair("plno", plno));
        params.add(new BasicNameValuePair("type", type));
        params.add(new BasicNameValuePair("land", land));
        params.add(new BasicNameValuePair("logistics", logistics));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("mpcode", mpcode));
        params.add(new BasicNameValuePair("mp", mp));
        params.add(new BasicNameValuePair("shit", shit));
        params.add(new BasicNameValuePair("city", city));
        params.add(new BasicNameValuePair("area", area));
        params.add(new BasicNameValuePair("zipcode", zipcode));
        params.add(new BasicNameValuePair("address", address));
        if (count_type == COUNT) {
            return jsonParser.getJSONFromUrl(count_url + setMemberLogistics_url, params);
        } else {
            return jsonParser.getJSONFromUrl(recount_url + setMemberLogistics_url, params);
        }
    }

    /**
     * 3.1.15	重新結帳 - 讀取買家可付款方式
     */
    public JSONObject getMemberPayment(int count_type, String token) {
        params.add(new BasicNameValuePair("token", token));
        if (count_type == COUNT) {
            return jsonParser.getJSONFromUrl(count_url + getMemberPayment_url, params);
        } else {
            return jsonParser.getJSONFromUrl(recount_url + getMemberPayment_url, params);
        }
    }

    /**
     * 3.1.16	重新結帳 - 設定買家付款方式
     */
    public JSONObject setMemberPayment(int count_type, String token, long xkeyin, long ykeyin, long ekeyin, String pno) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("xkeyin", xkeyin + ""));
        params.add(new BasicNameValuePair("ykeyin", ykeyin + ""));
        params.add(new BasicNameValuePair("ekeyin", ekeyin + ""));
        params.add(new BasicNameValuePair("pno", pno));
        if (count_type == COUNT) {
            return jsonParser.getJSONFromUrl(count_url + setMemberPayment_url, params);
        } else {
            return jsonParser.getJSONFromUrl(recount_url + setMemberPayment_url, params);
        }
    }

    /**
     * 3.1.17	重新結帳 - 設定商家備註
     */
    public JSONObject setStoreNote(int count_type, String token, String sno, String note) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("sno", sno));
        params.add(new BasicNameValuePair("note", note));
        if (count_type == COUNT) {
            return jsonParser.getJSONFromUrl(count_url + setStoreNote_url, params);
        } else {
            return jsonParser.getJSONFromUrl(recount_url + setStoreNote_url, params);
        }
    }

    /**
     * 3.1.18	重新結帳 - 處理金流流程
     */
    public String setGoldFlow(int count_type, String token) {
        CertJSONParser certJSONParser = new CertJSONParser();
        params.add(new BasicNameValuePair("device", "1"));
        params.add(new BasicNameValuePair("token", token));
        if (count_type == COUNT) {
            return certJSONParser.getJSONFromUrl(count_url + setGoldFlow_url, params);
        } else {
            return certJSONParser.getJSONFromUrl(recount_url + setGoldFlow_url, params);
        }
    }

    /**
     * 3.1.19	重新結帳 – 設定發票資訊
     */
    public JSONObject setVat(int count_type, String token, int invoice, String ctitle, String vat) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("invoice", invoice + ""));
        params.add(new BasicNameValuePair("ctitle", ctitle));
        params.add(new BasicNameValuePair("vat", vat));
        if (count_type == COUNT) {
            return jsonParser.getJSONFromUrl(count_url + setVat_url, params);
        } else {
            return jsonParser.getJSONFromUrl(recount_url + setVat_url, params);
        }
    }

}
