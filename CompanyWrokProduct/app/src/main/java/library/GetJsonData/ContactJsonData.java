package library.GetJsonData;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class ContactJsonData extends APIInfomation {
    private final String getContact_url = DOMAIN + "main/mcenter/contact/getContact.php";
    private final String getContactCont_url = DOMAIN + "main/mcenter/contact/getContactCont.php";
    private final String setContact_url = DOMAIN + "main/mcenter/contact/setContact.php";
    private final String delContact_url = DOMAIN + "main/mcenter/contact/delContact.php";
    private final String setContactCont_url = DOMAIN + "main/mcenter/contact/setContactCont.php";

    public ContactJsonData() {
        super();
    }

    /**
     * 8.2.1	讀取信件
     */
    public JSONObject getContact(String token, int type, int page) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("type", "" + type));
        params.add(new BasicNameValuePair("page", "" + page));
        return jsonParser.getJSONFromUrl(getContact_url, params);
    }

    /**
     * 8.2.2	讀取信件內文
     */
    public JSONObject getContactCont(String token, String msno) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("msno", msno));
        return jsonParser.getJSONFromUrl(getContactCont_url, params);
    }

    /**
     * 8.2.3	新增信件
     */
    public JSONObject setContact(String token, String title, String note) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("title", title));
        params.add(new BasicNameValuePair("note", note));
        return jsonParser.getJSONFromUrl(setContact_url, params);
    }

    /**
     * 8.2.4	刪除信件
     */
    public JSONObject delContact(String token, String msnoArray) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("msnoArray", msnoArray));
        return jsonParser.getJSONFromUrl(delContact_url, params);
    }

    /**
     * 8.2.5	回覆信件
     */
    public JSONObject setContactCont(String token, String msno, String note) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("msno", msno));
        params.add(new BasicNameValuePair("note", note));
        return jsonParser.getJSONFromUrl(setContactCont_url, params);
    }
}
