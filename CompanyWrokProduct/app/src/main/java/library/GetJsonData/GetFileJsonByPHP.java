package library.GetJsonData;


import android.content.Context;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import library.Http.GetFileJson;

public class GetFileJsonByPHP {
    private  final String getAddress_url = "http://mall-tapi.gok1945.com/main/cart/getAddress.php";
    private GetFileJson jsonParser;
    List<NameValuePair> params;

    public GetFileJsonByPHP(Context context) {
        jsonParser = new GetFileJson(context);
        params = new ArrayList<>();

    }



    /**
     * 取得住址
     */
    public String getAddress() {
        params.add(new BasicNameValuePair("gok", "Dr@_K4y51G2A0w26B8OWkfQ=="));
        params.add(new BasicNameValuePair("lang", "0"));
        params.add(new BasicNameValuePair("modifydate", "20180101000000"));
        return jsonParser.getJSONFromUrl(getAddress_url, params);
    }


}
