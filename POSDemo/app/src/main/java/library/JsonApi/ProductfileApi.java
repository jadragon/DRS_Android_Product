package library.JsonApi;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductfileApi extends APIInfomation {
    private final String product_filing_url = DOMAIN + "judd/main/index/product_stock/product_filing.php";
    private final String up_down_product_url = DOMAIN + "judd/main/index/product_stock/up_down_product.php";

    public ProductfileApi() {
        super();
    }

    /**
     * 6.4商品建檔資料
     */
    public JSONObject product_filing(String pbno_array, String name, String pcode, String title, int page) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("pos", POS));
        params.add(new BasicNameValuePair("pbno_array", pbno_array));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("pcode", pcode));
        params.add(new BasicNameValuePair("title", title));
        params.add(new BasicNameValuePair("page", page + ""));
        return jsonParser.getJSONFromUrl(product_filing_url, params);
    }

    /**
     * 6.5商品上下架
     */
    public JSONObject up_down_product(String p_no, String pi_no, String du_no, int type) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("pos", POS));
        params.add(new BasicNameValuePair("p_no", p_no));
        params.add(new BasicNameValuePair("pi_no", pi_no));
        params.add(new BasicNameValuePair("du_no", du_no));
        params.add(new BasicNameValuePair("type", type + ""));
        return jsonParser.getJSONFromUrl(up_down_product_url, params);
    }
}
