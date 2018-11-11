package library.GetJsonData;

import android.graphics.Bitmap;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import library.Http.PostByteArrayformImage;

public class UploadFileJsonData extends APIInfomation {
    private final String updatePortrait_url = DOMAIN + "main/mcenter/person/updatePortrait.php";
    private PostByteArrayformImage postByteArrayformImage;

    public UploadFileJsonData() {
        postByteArrayformImage = new PostByteArrayformImage();
    }

    /**
     * 7.1.1	修改頭像資訊
     */
    public JSONObject updatePortrait(String token, byte[] data) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("gok", GOK));
        params.add(new BasicNameValuePair("lang", LANG));
        params.add(new BasicNameValuePair("token", token));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        return new PostByteArrayformImage().getJSONFromUrl(updatePortrait_url, params, data);
    }

    /**
     * 7.1.1	修改頭像資訊

     public JSONObject updatePortrait(String token, byte[] bitmapByte) {
     return postByteArrayformImage.getJSONFromUrl(updatePortrait_url, token, bitmapByte);
     }
     */
}
