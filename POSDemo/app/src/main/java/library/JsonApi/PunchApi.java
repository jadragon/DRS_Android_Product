package library.JsonApi;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

public class PunchApi extends APIInfomation {
    private final String attendance_seach_url = DOMAIN + "judd/main/index/attendance/seach.php";
    private final String attendance_puch_url = DOMAIN + "judd/main/index/attendance/puch.php";

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

    /**
     * 2.2出勤打卡
     */
    public JSONObject attendance_puch(int classnum, int commute, String note, String account, String pawd) {
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("pos", POS));
        params.add(new BasicNameValuePair("classnum", classnum+""));
        params.add(new BasicNameValuePair("commute", commute+""));
        params.add(new BasicNameValuePair("note", note));
        params.add(new BasicNameValuePair("account", account));
        params.add(new BasicNameValuePair("pawd", pawd));
        return jsonParser.getJSONFromUrl(attendance_puch_url, params);
    }
}
