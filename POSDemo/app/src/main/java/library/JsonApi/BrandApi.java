package library.JsonApi;

import android.graphics.Bitmap;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import library.Http.PostByteArrayformImage;

public class BrandApi extends APIInfomation {
    private final String brand_data_url = DOMAIN + "judd/main/index/brand/brand_data.php";
    private final String insert_brand_url = DOMAIN + "judd/main/index/brand/insert_brand.php";
    private final String up_down_brand_url = DOMAIN + "judd/main/index/brand/up_down_brand.php";
    private final String search_name_url = DOMAIN + "judd/main/index/brand/search_name.php";
    private final String search_code_url = DOMAIN + "judd/main/index/brand/search_code.php";
    private final String updata_brand_url = DOMAIN + "judd/main/index/brand/updata_brand.php";

    public BrandApi() {
        super();
    }


    /**
     * 5.1 品牌
     */
    public JSONObject brand_data() {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("pos", POS));
        return jsonParser.getJSONFromUrl(brand_data_url, params);
    }

    /**
     * 5.2新增品牌
     */
    public JSONObject insert_brand(String du_no, String code, String title, Bitmap bitmap) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("pos", POS));
        params.add(new BasicNameValuePair("du_no", du_no));
        params.add(new BasicNameValuePair("code", code));
        params.add(new BasicNameValuePair("title", title));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, bos);
        byte[] data = bos.toByteArray();
        return new PostByteArrayformImage().getJSONFromUrl(insert_brand_url, params, data);
    }

    /**
     * 5.3上架、下架品牌
     */
    public JSONObject up_down_brand(String du_no, String pb_no, String type) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("pos", POS));
        params.add(new BasicNameValuePair("du_no", du_no));
        params.add(new BasicNameValuePair("pb_no", pb_no));
        params.add(new BasicNameValuePair("type", type));
        return jsonParser.getJSONFromUrl(up_down_brand_url, params);
    }

    /**
     * 5.4搜尋品牌名稱
     */
    public JSONObject search_name(String title) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("pos", POS));
        params.add(new BasicNameValuePair("title", title));
        return jsonParser.getJSONFromUrl(search_name_url, params);
    }

    /**
     * 5.5搜尋品牌代碼
     */
    public JSONObject search_code(String code) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("pos", POS));
        params.add(new BasicNameValuePair("code", code));
        return jsonParser.getJSONFromUrl(search_code_url, params);
    }

    /**
     * 5.6更新圖片照片
     */
    public JSONObject updata_brand(String pb_no, String du_no, String code, Bitmap bitmap) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("pos", POS));
        params.add(new BasicNameValuePair("pb_no", pb_no));
        params.add(new BasicNameValuePair("du_no", du_no));
        params.add(new BasicNameValuePair("code", code));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, bos);
        byte[] data = bos.toByteArray();
        return new PostByteArrayformImage().getJSONFromUrl(updata_brand_url, params, data);
    }

}
