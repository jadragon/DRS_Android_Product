package library.JsonApi;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

public class CountApi extends APIInfomation {
    private final String store_payment_style_url = DOMAIN + "judd/main/index/checkout/store_payment_style.php";
    private final String employee_name_url = DOMAIN + "judd/main/index/checkout/employee_name.php";
    private final String member_data_url = DOMAIN + "judd/main/index/checkout/member_data.php";
    private final String search_member_order_url = DOMAIN + "judd/main/index/checkout/search_member_order.php";
    private final String member_order_note_url = DOMAIN + "judd/main/index/checkout/member_order_note.php";
    private final String preferential_content_url = DOMAIN + "judd/main/index/checkout/preferential_content.php";
    private final String check_coupon_url = DOMAIN + "judd/main/index/checkout/check_coupon.php";
    private final String product_item_url = DOMAIN + "judd/main/index/checkout/product_item.php";

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
     * 3-3取得會員姓名
     */
    public JSONObject member_data(String m_type) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("pos", POS));
        params.add(new BasicNameValuePair("m_type", m_type));
        return jsonParser.getJSONFromUrl(member_data_url, params);
    }

    /**
     * 3-4查詢訂單
     */
    public JSONObject search_member_order(String mo_type) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("pos", POS));
        params.add(new BasicNameValuePair("mo_type", mo_type));
        return jsonParser.getJSONFromUrl(search_member_order_url, params);
    }

    /**
     * 3-5 修改會員訂單備註
     */
    public JSONObject member_order_note(String moi_no, String note) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("pos", POS));
        params.add(new BasicNameValuePair("moi_no", moi_no));
        params.add(new BasicNameValuePair("note", note));
        return jsonParser.getJSONFromUrl(member_order_note_url, params);
    }

    /**
     * 3-6 優惠內容
     */
    public JSONObject preferential_content(int money) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("pos", POS));
        params.add(new BasicNameValuePair("money", money + ""));
        return jsonParser.getJSONFromUrl(preferential_content_url, params);
    }


    /**
     * 3-7 確認優惠卷
     */
    public JSONObject check_coupon(String mm_no, String coupon) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("pos", POS));
        params.add(new BasicNameValuePair("mm_no", mm_no));
        params.add(new BasicNameValuePair("coupon", coupon));
        return jsonParser.getJSONFromUrl(check_coupon_url, params);
    }

    /**
     * 3-8顯示產品資料<單筆>
     */
    public JSONObject product_item(String pcode) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("pos", POS));
        params.add(new BasicNameValuePair("pcode", pcode));
        return jsonParser.getJSONFromUrl(product_item_url, params);
    }
}
