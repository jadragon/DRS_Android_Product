package library.AnalyzeJSON;

import org.json.JSONException;
import org.json.JSONObject;

import library.AnalyzeJSON.APIpojo.HomeHeaderPojo;
import library.AnalyzeJSON.APIpojo.LoginInfoPojo;

public class Analyze_UserInfo {

    /**
     * 1.1	會員登入
     */
    public LoginInfoPojo getLoginInfo(JSONObject json) {
        try {
            if (json.getBoolean("Success")) {
                LoginInfoPojo loginInfoPojo = new LoginInfoPojo();
                JSONObject json_obj = json.getJSONObject("Data");
                loginInfoPojo.setName(json_obj.getString("name"));
                loginInfoPojo.setEn(json_obj.getString("en"));
                loginInfoPojo.setS_no(json_obj.getString("s_no"));
                loginInfoPojo.setStore(json_obj.getString("store"));
                loginInfoPojo.setDu_no(json_obj.getString("du_no"));
                loginInfoPojo.setDname(json_obj.getString("dname"));
                return loginInfoPojo;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 1.2	首頁_業績+訂單
     */
    public HomeHeaderPojo getHomeHeader(JSONObject json) {
        try {
            if (json.getBoolean("Success")) {
                HomeHeaderPojo homeHeaderPojo = new HomeHeaderPojo();
                JSONObject json_obj = json.getJSONObject("Data");
                homeHeaderPojo.setN_order(json_obj.getString("n_order"));
                homeHeaderPojo.setN_sale(json_obj.getString("n_sale"));
                homeHeaderPojo.setY_orde(json_obj.getString("y_order"));
                homeHeaderPojo.setY_sale(json_obj.getString("y_sale"));
                return homeHeaderPojo;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 1.3	公司-公布欄
     */
    public String getBoard(JSONObject json) {
        try {
            if (json.getBoolean("Success")) {
                JSONObject json_obj = json.getJSONObject("Data");
                return json_obj.getString("content");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }
}
