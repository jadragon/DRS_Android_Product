package tw.com.lccnet.app.designateddriving.API;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tw.com.lccnet.app.designateddriving.Http.JSONParser;

public class RestaurantApi implements APISetting {


    /**
     * 2.1	店家類別
     */
    public static JSONObject store_type() {
        List<NameValuePair> params = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        params.add(new BasicNameValuePair("sOrder", sOrder));
        String store_type_url = DOMAIN + "judd_student/main/index/student/store_type.php";
        return jsonParser.getJSONFromUrl(store_type_url, params);
    }


}
