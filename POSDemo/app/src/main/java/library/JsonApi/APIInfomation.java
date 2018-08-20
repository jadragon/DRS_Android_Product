package library.JsonApi;

import org.apache.http.NameValuePair;

import java.util.List;

import library.Http.JSONParser;

public class APIInfomation {
    static String DOMAIN = "http://judd.tongyuplus.com/";
    static String POS = "piVaJRaxvZDeLlWk7OwKnA==";
    JSONParser jsonParser;
    List<NameValuePair> params;

    public APIInfomation() {
        jsonParser = new JSONParser();
    }
}
