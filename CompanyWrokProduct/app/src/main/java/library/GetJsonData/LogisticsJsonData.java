package library.GetJsonData;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class LogisticsJsonData extends APIInfomation{
    private  final String getLogistics_url = DOMAIN+"main/mcenter/mlogistics/getLogistics.php";
    private  final String setLogistics_url = DOMAIN+"main/mcenter/mlogistics/setLogistics.php";
    private  final String updateLogistics_url = DOMAIN+"main/mcenter/mlogistics/updateLogistics.php";
    private  final String delLogistics_url = DOMAIN+"main/mcenter/mlogistics/delLogistics.php";
    private  final String setInitLogistics_url = DOMAIN+"main/mcenter/mlogistics/setInitLogistics.php";

    public LogisticsJsonData() {
     super();
    }

    /**
     * 3.2.1	讀取收貨地址
     */
    public JSONObject getLogistics(String token, int type) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("type", "" + type));
        return jsonParser.getJSONFromUrl(getLogistics_url, params);
    }

    /**
     * 3.2.2	新增收貨地址
     */
    public JSONObject setLogistics(
            String token
            , String type
            , String land
            , String logistics
            , String name
            , String mpcode
            , String mp
            , String sname
            , String sid
            , String shit
            , String city
            , String area
            , String zipcode
            , String address
    ) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("type", type));
        params.add(new BasicNameValuePair("land", land));
        params.add(new BasicNameValuePair("logistics", logistics));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("mpcode", mpcode));
        params.add(new BasicNameValuePair("mp", mp));
        params.add(new BasicNameValuePair("sname", sname));
        params.add(new BasicNameValuePair("sid", sid));
        params.add(new BasicNameValuePair("shit", shit));
        params.add(new BasicNameValuePair("city", city));
        params.add(new BasicNameValuePair("area", area));
        params.add(new BasicNameValuePair("zipcode", zipcode));
        params.add(new BasicNameValuePair("address", address));
        return jsonParser.getJSONFromUrl(setLogistics_url, params);
    }

    /**
     * 3.2.3	修改收貨地址
     */
    public JSONObject updateLogistics(
            String token
            , String mlno
            , String name
            , String mpcode
            , String mp
            , String sname
            , String sid
            , String shit
            , String city
            , String area
            , String zipcode
            , String address) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("mlno", mlno));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("mpcode", mpcode));
        params.add(new BasicNameValuePair("mp", mp));
        params.add(new BasicNameValuePair("sname", sname));
        params.add(new BasicNameValuePair("sid", sid));
        params.add(new BasicNameValuePair("shit", shit));
        params.add(new BasicNameValuePair("city", city));
        params.add(new BasicNameValuePair("area", area));
        params.add(new BasicNameValuePair("zipcode", zipcode));
        params.add(new BasicNameValuePair("address", address));
        return jsonParser.getJSONFromUrl(updateLogistics_url, params);
    }

    /**
     * 3.2.4	刪除收貨地址
     */
    public JSONObject delLogistics(String token, String mlno) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("mlno", mlno));
        return jsonParser.getJSONFromUrl(delLogistics_url, params);
    }

    /**
     * 3.2.5	設定預設收貨地址
     */
    public JSONObject setInitLogistics(String token, String mlno, int type) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("mlno", mlno));
        params.add(new BasicNameValuePair("type", ""+type));
        return jsonParser.getJSONFromUrl(setInitLogistics_url, params);
    }


}
