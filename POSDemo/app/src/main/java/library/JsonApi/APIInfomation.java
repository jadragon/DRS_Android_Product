package library.JsonApi;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import library.Http.JSONParser;

public class APIInfomation {
    static String DOMAIN = "http://judd.tongyuplus.com/";
    static String POS = "piVaJRaxvZDeLlWk7OwKnA==";
    JSONParser jsonParser;
    List<NameValuePair> params;

    public APIInfomation() {
        jsonParser = new JSONParser();
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("pos", POS));
    }
}
