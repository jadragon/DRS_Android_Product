package library.JsonApi;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

public class StockApi extends APIInfomation {
    private final String all_store_brand_url = DOMAIN + "judd/main/index/product_stock/all_store_brand.php";
    private final String stock_data_url = DOMAIN + "judd/main/index/product_stock/stock_data.php";

    public StockApi() {
        super();
    }

    /**
     * 6.1 全部門市與品牌
     */
    public JSONObject remove_album() {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("pos", POS));
        return jsonParser.getJSONFromUrl(all_store_brand_url, params);
    }

    /**
     * 6.2 商品庫存
     */
    public JSONObject stock_data(String sno_array, String pbno_array, String name, String pcode, String total_type, String page) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("pos", POS));
        params.add(new BasicNameValuePair("sno_array", sno_array));
        params.add(new BasicNameValuePair("pbno_array", pbno_array));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("pcode", pcode));
        params.add(new BasicNameValuePair("total_type", total_type));
        params.add(new BasicNameValuePair("page", page));
        return jsonParser.getJSONFromUrl(stock_data_url, params);
    }
}
