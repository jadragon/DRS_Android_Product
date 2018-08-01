package library.GetJsonData;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class HelpCenterJsonData extends APIInfomation{
    private  final String category_url = DOMAIN+"main/footer/category.php";
    private  final String searchCategory_url = DOMAIN+"main/footer/searchCategory.php";
    private  final String searchMbCategory_url = DOMAIN+"main/footer/searchMbCategory.php";
    private  final String citem_url = DOMAIN+"main/footer/citem.php";
    public HelpCenterJsonData() {
       super();
    }

    /**
     * 8.3.1	讀取固定項目(第一層、第二層)
     */
    public JSONObject getCategory() {
        return jsonParser.getJSONFromUrl(category_url, params);
    }

    /**
     * 8.3.2	讀取搜尋項目(第二層)–方法一
     */
    public JSONObject searchCategory(String title) {
        params.add(new BasicNameValuePair("title", title));
        return jsonParser.getJSONFromUrl(searchCategory_url, params);
    }

    /**
     * 8.3.3	讀取搜尋項目(第二層) –方法二
     */
    public JSONObject searchMbCategory(String title) {
        params.add(new BasicNameValuePair("title", title));
        return jsonParser.getJSONFromUrl(searchMbCategory_url, params);
    }

    /**
     * 8.3.4	讀取標題內文
     */
    public JSONObject getCitem(String icno) {
        params.add(new BasicNameValuePair("icno", icno));
        return jsonParser.getJSONFromUrl(citem_url, params);
    }
}
