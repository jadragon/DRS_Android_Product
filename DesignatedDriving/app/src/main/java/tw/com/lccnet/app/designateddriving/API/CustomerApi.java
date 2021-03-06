package tw.com.lccnet.app.designateddriving.API;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import tw.com.lccnet.app.designateddriving.Http.ImageJSONParser;
import tw.com.lccnet.app.designateddriving.Http.JSONParser;

public class CustomerApi implements APISetting {
    private static final String getAddress_url = "https://api.gok1945.com/" + "main/cart/getAddress.php";


    private static final String register_url = DOMAIN + "main/customer/register.php";
    private static final String gvcode_url = DOMAIN + "main/customer/gvcode.php";
    private static final String login_url = DOMAIN + "main/customer/login.php";
    private static final String forget_url = DOMAIN + "main/customer/forget.php";

    private static final String getBasicData_url = DOMAIN + "main/customer/getBasicData.php";
    private static final String updateBasicData_url = DOMAIN + "main/customer/updateBasicData.php";
    private static final String updateBasicPawd_url = DOMAIN + "main/customer/updateBasicPawd.php";

    private static final String about_url = DOMAIN + "main/about/about.php";
    private static final String tservice_url = DOMAIN + "main/about/tservice.php";
    private static final String pservice_url = DOMAIN + "main/about/pservice.php";
    private static final String privacy_url = DOMAIN + "main/about/privacy.php";
    private static final String question_url = DOMAIN + "main/about/question.php";
    private static final String contact_url = DOMAIN + "main/about/contact.php";
    private static final String calculate1_url = DOMAIN + "main/calculate/calculate1.php";
    private static final String calculate2_url = DOMAIN + "main/calculate/calculate2.php";
    private static final String calculate3_url = DOMAIN + "main/calculate/calculate3.php";
    private static final String myCoupon_url = DOMAIN + "main/coupon/myCoupon.php";

    private static final String couponList_url = DOMAIN + "main/coupon/couponList.php";
    private static final String setCoupon_url = DOMAIN + "main/coupon/setCoupon.php";
    private static final String news_url = DOMAIN + "main/news/news.php";
    private static final String ordermeal_url = DOMAIN + "main/other/ordermeal.php";

    /**
     * 1.1.1	註冊
     */
    public static JSONObject register(String mp, String pawd, String vcode, String uname, String sex, String rmp) {
        List<NameValuePair> params = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        params.add(new BasicNameValuePair("ntgo", ntgo));
        params.add(new BasicNameValuePair("mp", mp));
        params.add(new BasicNameValuePair("pawd", pawd));
        params.add(new BasicNameValuePair("vcode", vcode));
        params.add(new BasicNameValuePair("uname", uname));
        params.add(new BasicNameValuePair("sex", sex));
        params.add(new BasicNameValuePair("rmp", rmp));
        return jsonParser.getJSONFromUrl(register_url, params);
    }

    /**
     * 1.1.2	取得驗證碼
     */
    public static JSONObject gvcode(String mp) {
        List<NameValuePair> params = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        params.add(new BasicNameValuePair("ntgo", ntgo));
        params.add(new BasicNameValuePair("mp", mp));
        return jsonParser.getJSONFromUrl(gvcode_url, params);
    }

    /**
     * 1.1.3	登入
     */
    public static JSONObject login(String mp, String pawd) {
        List<NameValuePair> params = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        params.add(new BasicNameValuePair("ntgo", ntgo));
        params.add(new BasicNameValuePair("mp", mp));
        params.add(new BasicNameValuePair("pawd", pawd));
        return jsonParser.getJSONFromUrl(login_url, params);
    }

    /**
     * 1.1.4	忘記密碼
     */
    public static JSONObject forget(String mp) {
        List<NameValuePair> params = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        params.add(new BasicNameValuePair("ntgo", ntgo));
        params.add(new BasicNameValuePair("mp", mp));
        return jsonParser.getJSONFromUrl(forget_url, params);
    }

    /**
     * 1.2.1	讀取基本資料
     */
    public static JSONObject getBasicData(String token) {
        List<NameValuePair> params = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        params.add(new BasicNameValuePair("ntgo", ntgo));
        params.add(new BasicNameValuePair("token", token));
        return jsonParser.getJSONFromUrl(getBasicData_url, params);
    }

    /**
     * 1.2.2	修改基本資料
     */
    public static JSONObject updateBasicData(String token, String uname, byte[] picture, String sex, String birthday, String email, String contact, String cmp) {
        ImageJSONParser imageJSONParser = new ImageJSONParser();
        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        Charset chars = Charset.forName("UTF-8");
        ByteArrayBody bab = new ByteArrayBody(picture, 123 + ".PNG");
        entity.addPart("picture", bab);
        try {
            entity.addPart("ntgo", new StringBody(ntgo,chars));
            entity.addPart("token", new StringBody(token,chars));
            entity.addPart("uname", new StringBody(uname,chars));
            entity.addPart("sex", new StringBody(sex,chars));
            entity.addPart("birthday", new StringBody(birthday,chars));
            entity.addPart("email", new StringBody(email,chars));
            entity.addPart("contact", new StringBody(contact,chars));
            entity.addPart("cmp", new StringBody(cmp,chars));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return imageJSONParser.getJSONFromUrl(updateBasicData_url, entity);
    }

    /**
     * 1.2.3	修改密碼
     */
    public static JSONObject updateBasicPawd(String token, String pawd) {
        List<NameValuePair> params = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        params.add(new BasicNameValuePair("ntgo", ntgo));
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("pawd", pawd));
        return jsonParser.getJSONFromUrl(updateBasicPawd_url, params);
    }


    /**
     * 費用試算-1.3.1	立即呼叫
     */
    public static JSONObject calculate1(String type, String address1, String address2) {
        List<NameValuePair> params = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        params.add(new BasicNameValuePair("ntgo", ntgo));
        params.add(new BasicNameValuePair("type", type));
        params.add(new BasicNameValuePair("address1", address1));
        params.add(new BasicNameValuePair("address2", address2));
        return jsonParser.getJSONFromUrl(calculate1_url, params);
    }

    /**
     * 費用試算-1.3.2	長途代駕
     */
    public static JSONObject calculate2(String address1, String address2) {
        List<NameValuePair> params = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        params.add(new BasicNameValuePair("ntgo", ntgo));
        params.add(new BasicNameValuePair("address1", address1));
        params.add(new BasicNameValuePair("address2", address2));
        return jsonParser.getJSONFromUrl(calculate2_url, params);
    }

    /**
     * 1.3.1	費用試算-立即呼叫
     */
    public static JSONObject calculate3(String type, String minutes) {
        List<NameValuePair> params = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        params.add(new BasicNameValuePair("ntgo", ntgo));
        params.add(new BasicNameValuePair("type", type));
        params.add(new BasicNameValuePair("minutes", minutes));
        return jsonParser.getJSONFromUrl(calculate3_url, params);
    }

    /**
     * 1.4.1	我的優惠券
     */
    public static JSONObject myCoupon(String token) {
        List<NameValuePair> params = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        params.add(new BasicNameValuePair("ntgo", ntgo));
        params.add(new BasicNameValuePair("token", token));
        return jsonParser.getJSONFromUrl(myCoupon_url, params);
    }

    /**
     * 1.4.2	搶奪優惠券 – 優惠券列表X
     */
    public static JSONObject couponList() {
        List<NameValuePair> params = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        params.add(new BasicNameValuePair("ntgo", ntgo));
        return jsonParser.getJSONFromUrl(couponList_url, params);
    }

    /**
     * 1.4.3	搶奪優惠券 – 獲取優惠券X
     */
    public static JSONObject setCoupon(String token, String mmno) {
        List<NameValuePair> params = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        params.add(new BasicNameValuePair("ntgo", ntgo));
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("mmno", mmno));
        return jsonParser.getJSONFromUrl(setCoupon_url, params);
    }

    /**
     * 1.5.1	最新消息資訊
     */
    public static JSONObject news() {
        List<NameValuePair> params = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        params.add(new BasicNameValuePair("ntgo", ntgo));
        return jsonParser.getJSONFromUrl(news_url, params);
    }

    /**
     * 1.7.1	關於我們
     */
    public static JSONObject about() {
        List<NameValuePair> params = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        params.add(new BasicNameValuePair("ntgo", ntgo));
        return jsonParser.getJSONFromUrl(about_url, params);
    }

    /**
     * 1.7.2	服務條款
     */
    public static JSONObject tservice() {
        List<NameValuePair> params = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        params.add(new BasicNameValuePair("ntgo", ntgo));
        return jsonParser.getJSONFromUrl(tservice_url, params);
    }

    /**
     * 1.7.3	服務宗旨
     */
    public static JSONObject pservice() {
        List<NameValuePair> params = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        params.add(new BasicNameValuePair("ntgo", ntgo));
        return jsonParser.getJSONFromUrl(pservice_url, params);
    }

    /**
     * 1.7.4	隱私權政策
     */
    public static JSONObject privacy() {
        List<NameValuePair> params = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        params.add(new BasicNameValuePair("ntgo", ntgo));
        return jsonParser.getJSONFromUrl(privacy_url, params);
    }

    /**
     * 1.7.5	常見問題
     */
    public static JSONObject question() {
        List<NameValuePair> params = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        params.add(new BasicNameValuePair("ntgo", ntgo));
        return jsonParser.getJSONFromUrl(question_url, params);
    }

    /**
     * 1.7.6	聯絡我們
     */
    public static JSONObject contact(String token, String title, String content) {
        List<NameValuePair> params = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        params.add(new BasicNameValuePair("ntgo", ntgo));
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("title", title));
        params.add(new BasicNameValuePair("content", content));
        return jsonParser.getJSONFromUrl(contact_url, params);
    }

    /**
     * 1.8.1	點餐送餐
     */
    public static JSONObject ordermeal() {
        List<NameValuePair> params = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        params.add(new BasicNameValuePair("ntgo", ntgo));
        return jsonParser.getJSONFromUrl(ordermeal_url, params);
    }

    /**
     * 1.3.16	讀取地址
     */
    public static JSONObject getAddress(String modifydate) {
        List<NameValuePair> params = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        params.add(new BasicNameValuePair("gok", "Dr@_K4y51G2A0w26B8OWkfQ=="));
        params.add(new BasicNameValuePair("lang", "0"));
        params.add(new BasicNameValuePair("modifydate", modifydate));
        return jsonParser.getJSONFromUrl(getAddress_url, params);
    }

}
