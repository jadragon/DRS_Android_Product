package library.JsonApi;

import android.graphics.Bitmap;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import library.Http.PostByteArrayformImage;

public class PhotoApi extends APIInfomation {
    private final String photo_data_url = DOMAIN + "judd/main/index/album_photo/photo_data.php";
    private final String insert_photo_url = DOMAIN + "judd/main/index/album_photo/insert_photo.php";
    private final String update_photo_name_url = DOMAIN + "judd/main/index/album_photo/update_photo_name.php";
    private final String remove_photo_url = DOMAIN + "judd/main/index/album_photo/remove_photo.php";
    private final String search_photo_url = DOMAIN + "judd/main/index/album_photo/search_photo.php";

    public PhotoApi() {
        super();
    }


    /**
     * 4.6照片資料
     */
    public JSONObject photo_data(String a_no) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("pos", POS));
        params.add(new BasicNameValuePair("a_no", a_no));
        return jsonParser.getJSONFromUrl(photo_data_url, params);
    }

    /**
     * 4.7新增照片
     */
    public JSONObject insert_photo(String a_no, String du_no, String oname, Bitmap bitmap) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("pos", POS));
        params.add(new BasicNameValuePair("a_no", a_no));
        params.add(new BasicNameValuePair("du_no", du_no));
        params.add(new BasicNameValuePair("oname", oname));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, bos);
        byte[] data = bos.toByteArray();
        return new PostByteArrayformImage().getJSONFromUrl(insert_photo_url, params, data);
    }

    /**
     * 4.8 修改照片名稱
     */
    public JSONObject update_photo_name(String ad_no, String du_no, String oname) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("pos", POS));
        params.add(new BasicNameValuePair("ad_no", ad_no));
        params.add(new BasicNameValuePair("du_no", du_no));
        params.add(new BasicNameValuePair("oname", oname));
        return jsonParser.getJSONFromUrl(update_photo_name_url, params);
    }

    /**
     * 4.9搜尋照片名稱
     */
    public JSONObject search_photo(String oname) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("pos", POS));
        params.add(new BasicNameValuePair("oname", oname));
        return jsonParser.getJSONFromUrl(search_photo_url, params);
    }

    /**
     * 4.10刪除照片
     */
    public JSONObject remove_photo(String ad_noArray) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("pos", POS));
        params.add(new BasicNameValuePair("ad_noArray", ad_noArray));
        return jsonParser.getJSONFromUrl(remove_photo_url, params);
    }
}
