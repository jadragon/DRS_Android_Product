package com.company.alex.ordersystemdemo.API.List;

import com.company.alex.ordersystemdemo.API.Http.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StoreApi implements APISetting {
    private final String store_login_url = DOMAIN + "judd_student/main/index/student/store_login.php";
    List<NameValuePair> params;
    JSONParser jsonParser;

    public StoreApi() {
        jsonParser = new JSONParser();
    }

    /**
     * 1.2	商家登入
     */
    public JSONObject store_login(String s_id, String pwd) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("sOrder", sOrder));
        params.add(new BasicNameValuePair("s_id", s_id));
        params.add(new BasicNameValuePair("pwd", pwd));
        return jsonParser.getJSONFromUrl(store_login_url, params);
    }


}
