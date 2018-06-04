package library.GetJsonData;

import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import library.Http.JSONParser;

public class MemberJsonData {
    private static final String register_url = "http://api.gok1945.com/main/member/register.php";
    private static final String gvcode_url = "http://api.gok1945.com/main/member/gvcode.php";
    private static final String login_url = "http://api.gok1945.com/main/member/login.php";
    private static final String forget_url = "http://api.gok1945.com/main/member/forget.php";
    private JSONParser jsonParser;
    List<NameValuePair> params;

    public MemberJsonData() {
        jsonParser = new JSONParser();
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("gok", "Dr@_K4y51G2A0w26B8OWkfQ=="));
        params.add(new BasicNameValuePair("lang", "0"));
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
        Log.e("gggggg", type + "\n" + mpcode + "\n" + account + "\n" + pawd);
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
        Log.e("type",type+"");
        params.add(new BasicNameValuePair("type", type + ""));
        params.add(new BasicNameValuePair("mpcode", mpcode));
        params.add(new BasicNameValuePair("account", account));
        return jsonParser.getJSONFromUrl(forget_url, params);
    }
}
