package library.AnalyzeJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
                map.put("Mvip", json.getString("Mvip"));
                map.put("Svip", json.getString("Svip"));
                map.put("Message", json.getString("Message"));
                return map;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析7.1.2	讀取個人資料Pdata:
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
                JSONObject data = json.getJSONObject("Data");
                JSONObject pdata = data.getJSONObject("pdata");
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

    /**
     * 解析7.1.2	讀取個人資料Adata:
     * bcode	String	銀行代碼
     * bname	String	銀行名稱
     * bbank	String	分行名稱
     * bcard	String	銀行帳戶
     * buname	String	帳戶姓名
     */
    public static Map<String, String> getPersonDataAdata(JSONObject json) {

        try {
            if (json.getBoolean("Success")) {
                JSONObject data = json.getJSONObject("Data");
                JSONObject pdata = data.getJSONObject("adata");
                Map<String, String> map = new HashMap<>();
                map.put("bcode", pdata.getString("bcode"));
                map.put("bname", pdata.getString("bname"));
                map.put("bbank", pdata.getString("bbank"));
                map.put("bcard", pdata.getString("bcard"));
                map.put("buname", pdata.getString("buname"));
                return map;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析7.1.2	讀取個人資料Bdata:
     * mactivate	Number	手機 0未綁 1已綁
     * mpcode	String	手機國碼
     * mp	String	手機
     * eactivate	Number	信箱 0未綁 1已綁
     * email	String	電子信箱
     * factivate	Number	FB 0未綁 1已綁
     * gactivate	Number	G+ 0未綁 1已綁
     */
    public static Map<String, String> getPersonDataBdata(JSONObject json) {

        try {
            if (json.getBoolean("Success")) {
                JSONObject data = json.getJSONObject("Data");
                JSONObject pdata = data.getJSONObject("bdata");
                Map<String, String> map = new HashMap<>();
                map.put("mactivate", pdata.getString("mactivate"));
                map.put("mpcode", pdata.getString("mpcode"));
                map.put("mp", pdata.getString("mp"));
                map.put("eactivate", pdata.getString("eactivate"));
                map.put("email", pdata.getString("email"));
                map.put("factivate", pdata.getString("factivate"));
                map.put("gactivate", pdata.getString("gactivate"));
                return map;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析9.1.2	讀取銀行資料:
     * bcode	String	銀行代碼
     * bname	String	銀行名稱
     */
    public static ArrayList<Map<String, String>> getBankData(JSONObject json) {

        try {
            if (json.getBoolean("Success")) {
                ArrayList<Map<String, String>> arrayList = new ArrayList<>();
                JSONArray data = json.getJSONArray("Data");
                Map<String, String> map;
                for (int i = 0; i < data.length(); i++) {
                    map = new HashMap<>();
                    map.put("bcode", data.getJSONObject(i).getString("bcode"));
                    map.put("bname", data.getJSONObject(i).getString("bname"));
                    arrayList.add(map);
                }
                return arrayList;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
