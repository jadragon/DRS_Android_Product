package liabiry.GetJsonData;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

import liabiry.Http.PostByteArrayformImage;

public class PhotoApi extends APIInfomation {

    private final String insert_photo_url = DOMAIN + "judd/main/index/album_photo/insert_photo.php";

    /**
     * 4.7新增照片
     */
    public JSONObject insert_photo(String a_no, String du_no, String oname, byte[] data) throws Exception {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("pos", POS));
        params.add(new BasicNameValuePair("a_no", a_no));
        params.add(new BasicNameValuePair("du_no", du_no));
        params.add(new BasicNameValuePair("oname", oname));
        return new PostByteArrayformImage().getJSONFromUrl(insert_photo_url, params, data);
    }

}
