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

    /**
     * 解析7.1.2	讀取個人資料:
     * account	String	帳號
     * name	String	姓名
     * memberid	String	身分證字號
     * sex	String	性別
     * birthday	String	生日
     * city	String	縣市
     * area	String	鄉鎮區
     * zipcode	String	郵遞區號
     * address	String	地址
     */
    public static Map<String, String> getPersonDataPdata(JSONObject json) {

        try {
            if (json.getBoolean("Success")) {
                JSONObject data=json.getJSONObject("Data");
                JSONObject pdata=data.getJSONObject("pdata");
                Map<String, String> map = new HashMap<>();
                map.put("account", pdata.getString("account"));
                map.put("name", pdata.getString("name"));
                map.put("memberid", pdata.getString("memberid"));
                map.put("sex", pdata.getString("sex"));
                map.put("birthday", pdata.getString("birthday"));
                map.put("city", pdata.getString("city"));
                map.put("area", pdata.getString("area"));
                map.put("zipcode", pdata.getString("zipcode"));
                map.put("address", pdata.getString("address"));
                return map;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
