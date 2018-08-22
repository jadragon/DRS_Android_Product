package library.JsonApi;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

public class CountApi extends APIInfomation {
    private final String store_payment_style_url = DOMAIN + "judd/main/index/checkout/store_payment_style.php";
    private final String employee_name_url = DOMAIN + "judd/main/index/checkout/employee_name.php";
    private final String member_data_url = DOMAIN + "judd/main/index/checkout/member_data.php";
    private final String search_member_order_url = DOMAIN + "judd/main/index/checkout/search_member_order.php";
    public CountApi() {
        super();
    }


    /**
     * 3-1 取得門市與付款方式
     */
    public JSONObject store_payment_style(String s_no) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("pos", POS));
        params.add(new BasicNameValuePair("s_no", s_no));
        return jsonParser.getJSONFromUrl(store_payment_style_url, params);
    }

    /**
     * 3-2 取得員工姓名
     */
    public JSONObject employee_name(String en) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("pos", POS));
        params.add(new BasicNameValuePair("en", en));
        return jsonParser.getJSONFromUrl(employee_name_url, params);
    }
    /**
     *3-3取得會員姓名
     */
    public JSONObject member_data(String m_type) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("pos", POS));
        params.add(new BasicNameValuePair("m_type", m_type));
        return jsonParser.getJSONFromUrl(member_data_url, params);
    }
    /**
     *3-4查詢訂單
     */
    public JSONObject search_member_order(String mo_type) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("pos", POS));
        params.add(new BasicNameValuePair("mo_type", mo_type));
        return jsonParser.getJSONFromUrl(search_member_order_url, params);
    }
}
