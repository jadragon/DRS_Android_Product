package library.JsonApi;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

public class AlbumApi extends APIInfomation {
    private final String album_data_url = DOMAIN + "judd/main/index/album/album_data.php";
    private final String update_album_name_url = DOMAIN + "judd/main/index/album/update_album_name.php";
    private final String insert_album_url = DOMAIN + "judd/main/index/album/insert_album.php";
    private final String remove_album_url = DOMAIN + "judd/main/index/album/remove_album.php";

    public AlbumApi() {
        super();
    }


    /**
     * 4.1 相簿資料
     */
    public JSONObject album_data() {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("pos", POS));
        return jsonParser.getJSONFromUrl(album_data_url, params);
    }

    /**
     * 4.3更新相簿名稱
     */
    public JSONObject update_album_name(String a_no, String name) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("pos", POS));
        params.add(new BasicNameValuePair("a_no", a_no));
        params.add(new BasicNameValuePair("name", name));
        return jsonParser.getJSONFromUrl(update_album_name_url, params);
    }

    /**
     * 4.4新增相簿名稱
     */
    public JSONObject insert_album(String du_no, String name) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("pos", POS));
        params.add(new BasicNameValuePair("du_no", du_no));
        params.add(new BasicNameValuePair("name", name));
        return jsonParser.getJSONFromUrl(insert_album_url, params);
    }

    /**
     * 4.5刪除相簿名稱
     */
    public JSONObject remove_album(String a_noArray) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("pos", POS));
        params.add(new BasicNameValuePair("a_noArray", a_noArray));
        return jsonParser.getJSONFromUrl(remove_album_url, params);
    }
}
