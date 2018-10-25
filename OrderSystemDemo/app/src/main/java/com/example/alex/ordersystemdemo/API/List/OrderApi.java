package com.example.alex.ordersystemdemo.API.List;

import com.example.alex.ordersystemdemo.API.Http.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderApi implements APISetting {
    private final String checkout_url = DOMAIN + "judd_student/main/index/student/checkout.php";
    private final String order_data_url = DOMAIN + "judd_student/main/index/student/order_data.php";
    List<NameValuePair> params;
    JSONParser jsonParser;

    public OrderApi() {
        jsonParser = new JSONParser();
    }

    /**
     * 3.1	學生下訂單
     */
    public JSONObject checkout(String m_id, String s_id, String m_name, String m_phone, String m_address, String f_content, String f_sum) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("sOrder", sOrder));
        params.add(new BasicNameValuePair("m_id", m_id));
        params.add(new BasicNameValuePair("s_id", s_id));
        params.add(new BasicNameValuePair("m_name", m_name));
        params.add(new BasicNameValuePair("m_phone", m_phone));
        params.add(new BasicNameValuePair("m_address", m_address));
        params.add(new BasicNameValuePair("f_content", f_content));
        params.add(new BasicNameValuePair("f_sum", f_sum));
        return jsonParser.getJSONFromUrl(checkout_url, params);
    }

    /**
     * 4.1	訂單資訊
     */
    public JSONObject order_data(String status) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("sOrder", sOrder));
        params.add(new BasicNameValuePair("status", status));
        return jsonParser.getJSONFromUrl(order_data_url, params);
    }

}
