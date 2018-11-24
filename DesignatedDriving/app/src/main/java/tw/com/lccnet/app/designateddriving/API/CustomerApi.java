package tw.com.lccnet.app.designateddriving.API;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tw.com.lccnet.app.designateddriving.Http.JSONParser;

public class CustomerApi implements APISetting {
    private static final String register_url = DOMAIN + "main/customer/register.php";
    private static final String gvcode_url = DOMAIN + "main/customer/gvcode.php";
    private static final String login_url = DOMAIN + "main/customer/login.php";
    private static final String forget_url = DOMAIN + "main/customer/forget.php";
    private static final String about_url = DOMAIN + "main/about/about.php";
    private static final String tservice_url = DOMAIN + "main/about/tservice.php";
    private static final String pservice_url = DOMAIN + "main/about/pservice.php";
    private static final String privacy_url = DOMAIN + "main/about/privacy.php";
    private static final String question_url = DOMAIN + "main/about/question.php";
    private static final String contact_url = DOMAIN + "main/about/contact.php";

    /**
     * 1.1.1	註冊
     */
    public static JSONObject register(String mp, String pawd, String vcode, String uname, String sex) {
        List<NameValuePair> params = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        params.add(new BasicNameValuePair("ntgo", ntgo));
        params.add(new BasicNameValuePair("mp", mp));
        params.add(new BasicNameValuePair("pawd", pawd));
        params.add(new BasicNameValuePair("vcode", vcode));
        params.add(new BasicNameValuePair("uname", uname));
        params.add(new BasicNameValuePair("sex", sex));
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
}
