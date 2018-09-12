package liabiry.GetJsonData;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class APIInfomation {
    static String DOMAIN = "https://api.gok1945.com/";
    static String GOK = "Dr@_K4y51G2A0w26B8OWkfQ==";
    static String LANG = "0";
    List<NameValuePair> params;
    public APIInfomation() {
        params = new ArrayList<>();

        params.add(new BasicNameValuePair("gok", GOK));
        params.add(new BasicNameValuePair("lang", LANG));
    }
}
