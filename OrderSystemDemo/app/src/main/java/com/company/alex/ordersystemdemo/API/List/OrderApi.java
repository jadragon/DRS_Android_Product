package com.company.alex.ordersystemdemo.API.List;

import com.company.alex.ordersystemdemo.API.Http.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderApi implements APISetting {
    private final String order_data_url = DOMAIN + "judd_student/main/index/student/order_data.php";
    private final String checkout_url = DOMAIN + "judd_student/main/index/student/checkout.php";
    private final String order_cancel_url = DOMAIN + "judd_student/main/index/student/order_cancel.php";
    private final String order_accept_url = DOMAIN + "judd_student/main/index/student/order_accept.php";
    private final String order_delivery_man_url = DOMAIN + "judd_student/main/index/student/order_delivery_man.php";
    private final String order_gainFood_url = DOMAIN + "judd_student/main/index/student/order_gainFood.php";
    private final String order_complete_url = DOMAIN + "judd_student/main/index/student/order_complete.php";

    private final String store_status_url = DOMAIN + "judd_student/main/index/student/store_status.php";
    private final String search_storeStatus_url = DOMAIN + "judd_student/main/index/student/search_storeStatus.php";
    private final String board_url = DOMAIN + "judd_student/main/index/student/board.php";
    List<NameValuePair> params;
    JSONParser jsonParser;

    public OrderApi() {
        jsonParser = new JSONParser();
    }

    /**
     * 1.4	公布欄
     */
    public JSONObject board() {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("sOrder", sOrder));
        return jsonParser.getJSONFromUrl(board_url, params);
    }

    /**
     * 4.1	訂單資訊
     */
    public JSONObject order_data(String msd_id, String status, String type) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("sOrder", sOrder));
        params.add(new BasicNameValuePair("msd_id", msd_id));
        params.add(new BasicNameValuePair("status", status));
        params.add(new BasicNameValuePair("type", type));
        return jsonParser.getJSONFromUrl(order_data_url, params);
    }

    /**
     * 3.1	學生下訂單
     */
    public JSONObject checkout(String m_id, String s_id, String m_name, String m_phone, String m_note, String m_address, String f_content, String f_sum) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("sOrder", sOrder));
        params.add(new BasicNameValuePair("m_id", m_id));
        params.add(new BasicNameValuePair("s_id", s_id));
        params.add(new BasicNameValuePair("m_name", m_name));
        params.add(new BasicNameValuePair("m_phone", m_phone));
        params.add(new BasicNameValuePair("m_note", m_note));
        params.add(new BasicNameValuePair("m_address", m_address));
        params.add(new BasicNameValuePair("f_content", f_content));
        params.add(new BasicNameValuePair("f_sum", f_sum));
        return jsonParser.getJSONFromUrl(checkout_url, params);
    }

    /**
     * 4.2	取消訂單
     */
    public JSONObject order_cancel(String o_id, String m_id) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("sOrder", sOrder));
        params.add(new BasicNameValuePair("o_id", o_id));
        params.add(new BasicNameValuePair("m_id", m_id));
        return jsonParser.getJSONFromUrl(order_cancel_url, params);
    }

    /**
     * 4.3	接受訂單
     */
    public JSONObject order_accept(String o_id, String s_id, String s_complete_time) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("sOrder", sOrder));
        params.add(new BasicNameValuePair("o_id", o_id));
        params.add(new BasicNameValuePair("s_id", s_id));
        params.add(new BasicNameValuePair("s_complete_time", s_complete_time));
        return jsonParser.getJSONFromUrl(order_accept_url, params);
    }

    /**
     * 4.4	外送人員接單
     */
    public JSONObject order_delivery_man(String o_id, String d_id, String d_complete_time) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("sOrder", sOrder));
        params.add(new BasicNameValuePair("o_id", o_id));
        params.add(new BasicNameValuePair("d_id", d_id));
        params.add(new BasicNameValuePair("d_complete_time", d_complete_time));
        return jsonParser.getJSONFromUrl(order_delivery_man_url, params);
    }

    /**
     * 4.5	店家餐點轉移給外送員
     */
    public JSONObject order_gainFood(String o_id, String s_id) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("sOrder", sOrder));
        params.add(new BasicNameValuePair("o_id", o_id));
        params.add(new BasicNameValuePair("s_id", s_id));
        return jsonParser.getJSONFromUrl(order_gainFood_url, params);
    }

    /**
     * 4.6	學生取餐
     */
    public JSONObject order_complete(String o_id, String m_id) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("sOrder", sOrder));
        params.add(new BasicNameValuePair("o_id", o_id));
        params.add(new BasicNameValuePair("m_id", m_id));
        return jsonParser.getJSONFromUrl(order_complete_url, params);
    }

    /**
     * 5.1	商家設定接單狀態
     */
    public JSONObject store_status(String s_id, String d_status) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("sOrder", sOrder));
        params.add(new BasicNameValuePair("s_id", s_id));
        params.add(new BasicNameValuePair("d_status", d_status));
        return jsonParser.getJSONFromUrl(store_status_url, params);
    }

    /**
     * 5.2	取得接單狀態
     */
    public JSONObject search_storeStatus(String s_id) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("sOrder", sOrder));
        params.add(new BasicNameValuePair("s_id", s_id));
        return jsonParser.getJSONFromUrl(search_storeStatus_url, params);
    }
}
