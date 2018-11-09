package com.company.alex.ordersystemdemo.API.List;

import com.company.alex.ordersystemdemo.API.Http.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DelivertApi implements APISetting {
    private final String deliver_login_url = DOMAIN + "judd_student/main/index/student/deliver_login.php";
    List<NameValuePair> params;
    JSONParser jsonParser;

    public DelivertApi() {
        jsonParser = new JSONParser();
    }

    /**
     * 1.3	外送員登入
     */
    public JSONObject deliver_login(String d_id, String pwd) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("sOrder", sOrder));
        params.add(new BasicNameValuePair("d_id", d_id));
        params.add(new BasicNameValuePair("pwd", pwd));
        return jsonParser.getJSONFromUrl(deliver_login_url, params);
    }


}
