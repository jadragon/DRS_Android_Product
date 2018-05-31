package library.AnalyzeJSON;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AnalyzeMember {
    /**
     * 判斷是否成功
     */
    public static Boolean checkSuccess(JSONObject json) {
        try {
            return json.getBoolean("Success");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 解析2.1.3	會員登入:
     * Success	Boolean	true or false
     * Token	String	token是每次查詢需要傳送的會員代碼
     * Account	String	帳號
     * Name	String	姓名
     * Picture	String	個人照
     * Message	String	訊息
     * modifydate	String	時間
     */
    public static Map<String, String> getLogin(JSONObject json) {

        try {
            if (json.getBoolean("Success")) {
                Map<String, String> map = new HashMap<>();
                map.put("Token", json.getString("Token"));
                map.put("Account", json.getString("Account"));
                map.put("Name", json.getString("Name"));
                map.put("Picture", json.getString("Picture"));
                map.put("Message", json.getString("Message"));
                return map;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
