package com.example.alex.ordersystemdemo.API.List;

import com.example.alex.ordersystemdemo.API.Http.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StudentApi implements APISetting {
    private final String student_login_url = DOMAIN + "judd_student/main/index/student/student_login.php";
    List<NameValuePair> params;
    JSONParser jsonParser;

    public StudentApi() {
        jsonParser = new JSONParser();
    }

    /**
     * 1.1	學生登入
     */
    public JSONObject student_login(String token, String name, String email, String type) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("sOrder", sOrder));
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("type", type));
        return jsonParser.getJSONFromUrl(student_login_url, params);
    }


}
