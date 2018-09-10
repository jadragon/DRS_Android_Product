package library.JsonApi;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewBrandApi extends APIInfomation {
    private final String typeRel_url = DOMAIN + "judd/main/index/product_stock/typeRel.php";

    public NewBrandApi() {
        super();
    }


    /**
     * 6.6商品類別
     */
    public JSONObject typeRel() {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("pos", POS));
        return jsonParser.getJSONFromUrl(typeRel_url, params);
    }
}
