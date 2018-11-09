package com.company.alex.ordersystemdemo.API.List;

import com.company.alex.ordersystemdemo.API.Http.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RestaurantApi implements APISetting {
    private final String store_data_url = DOMAIN + "judd_student/main/index/student/store_data.php";
    private final String menu_url = DOMAIN + "judd_student/main/index/student/menu.php";
    private final String store_type_url = DOMAIN + "judd_student/main/index/student/store_type.php";
    List<NameValuePair> params;
    JSONParser jsonParser;

    public RestaurantApi() {
        jsonParser = new JSONParser();
    }

    /**
     * 2.1	店家
     */
    public JSONObject store_data(String type) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("sOrder", sOrder));
        params.add(new BasicNameValuePair("type", type));
        return jsonParser.getJSONFromUrl(store_data_url, params);
    }

    /**
     * 2.1	店家類別
     */
    public JSONObject store_type() {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("sOrder", sOrder));
        return jsonParser.getJSONFromUrl(store_type_url, params);
    }

    /**
     * 2.2	菜單
     */
    public JSONObject menu(String s_id) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("sOrder", sOrder));
        params.add(new BasicNameValuePair("s_id", s_id));
        return jsonParser.getJSONFromUrl(menu_url, params);
    }


}
