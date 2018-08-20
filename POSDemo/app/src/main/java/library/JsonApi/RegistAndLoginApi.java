package library.JsonApi;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

public class RegistAndLoginApi extends APIInfomation {
    private final String login_user_url = DOMAIN + "judd/main/index/login_user.php";
    private final String home_header_url = DOMAIN + "judd/main/index/home_header.php";
    private final String board_url = DOMAIN + "judd/main/index/company_board.php";
    public RegistAndLoginApi() {
        super();
    }

    /**
     * 1.1	會員登入
     */
    public JSONObject login_user(String account, String pawd) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("pos", POS));
        params.add(new BasicNameValuePair("account", account));
        params.add(new BasicNameValuePair("pawd", pawd));
        return jsonParser.getJSONFromUrl(login_user_url, params);
    }

    /**
     * 1.2	首頁_業績+訂單
     */
    public JSONObject home_header(String s_no) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("pos", POS));
        params.add(new BasicNameValuePair("s_no", s_no));
        return jsonParser.getJSONFromUrl(home_header_url, params);
    }
    /**
     * 1.3	公司-公布欄
     */
    public JSONObject board() {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("pos", POS));
        return jsonParser.getJSONFromUrl(board_url, params);
    }
}
