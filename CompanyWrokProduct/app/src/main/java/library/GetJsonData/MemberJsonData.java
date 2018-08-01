package library.GetJsonData;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class MemberJsonData extends APIInfomation {
    private final String register_url = DOMAIN + "main/member/register.php";
    private final String gvcode_url = DOMAIN + "main/member/gvcode.php";
    private final String login_url = DOMAIN + "main/member/login.php";
    private final String forget_url = DOMAIN + "main/member/forget.php";
    private final String getPersonData_url = DOMAIN + "main/mcenter/person/getPersonData.php";
    private final String updateBasicData_url = DOMAIN + "main/mcenter/person/updateBasicData.php";
    private final String getBankData_url = DOMAIN + "main/other/getBankData.php";
    private final String updateBillingData_url = DOMAIN + "main/mcenter/person/updateBillingData.php";
    private final String updatePersonPawd_url = DOMAIN + "main/mcenter/person/updatePersonPawd.php";

    public MemberJsonData() {
        super();
    }


    /**
     * 2.1.1	會員註冊
     */
    public JSONObject register(int type, String account, String pawd, String mpcode, String aid, String vcode, String name, int sex, String picture) {
        params.add(new BasicNameValuePair("type", type + ""));
        params.add(new BasicNameValuePair("account", account));
        params.add(new BasicNameValuePair("pawd", pawd));
        params.add(new BasicNameValuePair("mpcode", mpcode));
        params.add(new BasicNameValuePair("aid", aid));
        params.add(new BasicNameValuePair("vcode", vcode));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("sex", sex + ""));
        params.add(new BasicNameValuePair("picture", picture));
        return jsonParser.getJSONFromUrl(register_url, params);
    }

    /**
     * 2.1.2	會員註冊-取得驗證碼
     */
    public JSONObject gvcode(int type, String mpcode, String account) {
        params.add(new BasicNameValuePair("type", type + ""));
        params.add(new BasicNameValuePair("mpcode", mpcode));
        params.add(new BasicNameValuePair("account", account));
        return jsonParser.getJSONFromUrl(gvcode_url, params);
    }

    /**
     * 2.1.3	會員登入
     */
    public JSONObject login(int type, String mpcode, String account, String pawd) {
        params.add(new BasicNameValuePair("type", type + ""));
        params.add(new BasicNameValuePair("mpcode", mpcode));
        params.add(new BasicNameValuePair("account", account));
        params.add(new BasicNameValuePair("pawd", pawd));
        return jsonParser.getJSONFromUrl(login_url, params);
    }

    /**
     * 2.1.4	忘記密碼
     */
    public JSONObject forget(int type, String mpcode, String account) {
        params.add(new BasicNameValuePair("type", type + ""));
        params.add(new BasicNameValuePair("mpcode", mpcode));
        params.add(new BasicNameValuePair("account", account));
        return jsonParser.getJSONFromUrl(forget_url, params);
    }

    /**
     * 7.1.2	讀取個人資料
     */
    public JSONObject getPersonData(String token) {
        params.add(new BasicNameValuePair("token", token));
        return jsonParser.getJSONFromUrl(getPersonData_url, params);
    }

    /**
     * 7.1.3	修改基本資料
     */
    public JSONObject updateBasicData(
            String token
            , String name
            , String memberid
            , int sex
            , String birthday
            , String city
            , String area
            , String zipcode
            , String address
    ) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("memberid", memberid));
        params.add(new BasicNameValuePair("sex", sex + ""));
        params.add(new BasicNameValuePair("birthday", birthday));

        params.add(new BasicNameValuePair("city", city));
        params.add(new BasicNameValuePair("area", area));
        params.add(new BasicNameValuePair("zipcode", zipcode));
        params.add(new BasicNameValuePair("address", address));

        return jsonParser.getJSONFromUrl(updateBasicData_url, params);
    }

    /**
     * 7.1.4	修改帳務資料
     */
    public JSONObject updateBillingData(
            String token
            , String bcode
            , String bbank
            , String bcard
            , String buname
    ) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("bcode", bcode));
        params.add(new BasicNameValuePair("bbank", bbank));
        params.add(new BasicNameValuePair("bcard", bcard));
        params.add(new BasicNameValuePair("buname", buname));
        return jsonParser.getJSONFromUrl(updateBillingData_url, params);
    }

    /**
     * 7.2.1	修改密碼
     */
    public JSONObject updatePersonPawd(
            String token
            , String oldpawd
            , String newpawd
    ) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("oldpawd", oldpawd));
        params.add(new BasicNameValuePair("newpawd", newpawd));
        return jsonParser.getJSONFromUrl(updatePersonPawd_url, params);
    }

    /**
     * 9.1.2	讀取銀行資料
     */
    public JSONObject getBankData() {
        return jsonParser.getJSONFromUrl(getBankData_url, params);
    }

}
