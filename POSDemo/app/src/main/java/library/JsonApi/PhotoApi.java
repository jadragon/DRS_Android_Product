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
     * 4.6照片資料
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
}
