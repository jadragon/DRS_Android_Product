package library.JsonApi;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

public class DistributionApi extends APIInfomation {
    private final String distribution_url = DOMAIN + "judd/main/index/product_stock/distribution.php";

    public DistributionApi() {
        super();
    }

    /**
     * 6.3商品庫存分佈
     */
    public JSONObject distribution(String pbno_array, String name, String pcode, int page) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("pos", POS));
        params.add(new BasicNameValuePair("pbno_array", pbno_array));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("pcode", pcode));
        params.add(new BasicNameValuePair("page", page + ""));
        return jsonParser.getJSONFromUrl(distribution_url, params);
    }

}
