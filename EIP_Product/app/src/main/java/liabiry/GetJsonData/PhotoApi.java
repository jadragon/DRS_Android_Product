package liabiry.GetJsonData;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

import liabiry.Http.PostByteArrayformImage;
import liabiry.Http.SSLCertPostImageUtil;

public class PhotoApi extends APIInfomation {
    static String GOK = "Dr@_K4y51G2A0w26B8OWkfQ==";
    static String LANG = "0";
    private final String insert_photo_url = DOMAIN + "judd/main/index/album_photo/insert_photo.php";
    private final String updatePortrait_url = "https://api.gok1945.com/" + "main/mcenter/person/updatePortrait.php";

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

    /**
     * 7.1.1	修改頭像資訊
     */
    public JSONObject updatePortrait(String token, byte[] bitmapByte) throws Exception {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("gok", GOK));
        params.add(new BasicNameValuePair("lang", LANG));
        params.add(new BasicNameValuePair("token", token));
        return new SSLCertPostImageUtil().getJSONFromUrl(updatePortrait_url, params, bitmapByte);
    }
}
