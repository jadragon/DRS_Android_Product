package library.JsonApi;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

public class PunchApi extends APIInfomation {
    private final String attendance_seach_url = DOMAIN + "judd/main/index/attendance_seach.php";

    public PunchApi() {
        super();
    }


    /**
     * 2.1搜尋出勤打卡
     */
    public JSONObject attendance_seach(String s_no, String name, String en, String start_day, String end_day) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("pos", POS));
        params.add(new BasicNameValuePair("s_no", s_no));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("en", en));
        params.add(new BasicNameValuePair("start_day", start_day));
        params.add(new BasicNameValuePair("end_day", end_day));
        return jsonParser.getJSONFromUrl(attendance_seach_url, params);
    }
}
