package library.JsonApi;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class RegistAndLoginApi extends APIInfomation {
    private final String login_user_url = DOMAIN + "judd/main/index/login_user.php";

    public RegistAndLoginApi() {
        super();
    }

    /**
     * 1.1	會員登入
     */
    public JSONObject login_user(String account, String pawd) {
        params.add(new BasicNameValuePair("account", account));
        params.add(new BasicNameValuePair("pawd", pawd));
        return jsonParser.getJSONFromUrl(login_user_url, params);
    }


}
