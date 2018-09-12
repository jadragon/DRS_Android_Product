package liabiry.GetJsonData;

import org.json.JSONObject;

import liabiry.Http.PostByteArrayformImage;

public class UploadFileJsonData extends APIInfomation {
    private final String updatePortrait_url = DOMAIN + "main/mcenter/person/updatePortrait.php";
    private PostByteArrayformImage postByteArrayformImage;

    public UploadFileJsonData() {
        postByteArrayformImage = new PostByteArrayformImage();
    }

    /**
     * 7.1.1	修改頭像資訊
     */
    public JSONObject updatePortrait(String token, byte[] bitmapByte) {
        return postByteArrayformImage.getJSONFromUrl(updatePortrait_url, token, bitmapByte);
    }
}
