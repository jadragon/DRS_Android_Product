package com.example.alex.ordersystemdemo.API.List;

import com.example.alex.ordersystemdemo.API.Http.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderApi implements APISetting {
    private final String order_data_url = DOMAIN + "judd_student/main/index/student/order_data.php";
    List<NameValuePair> params;
    JSONParser jsonParser;

    public OrderApi() {
        jsonParser = new JSONParser();
    }

    /**
     * 3.1	訂單資訊
     */
    public JSONObject order_data(String status) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("sOrder", sOrder));
        params.add(new BasicNameValuePair("status", status));
        return jsonParser.getJSONFromUrl(order_data_url, params);
    }


}
