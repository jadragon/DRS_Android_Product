package library.AnalyzeJSON;

import com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.pojo.ContentPojo;
import com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.pojo.FooterPojo;
import com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.pojo.HeaderPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AnalyzeOrderInfo {

    /**
     * 3.1.1	讀取會員訂單資訊(header):
     * ordernum	String	訂單編號
     * odate	String	訂單日期
     * sname	String	商家名稱
     */
    public static ArrayList<HeaderPojo> getMemberOrderHeader(JSONObject json) {
        ArrayList<HeaderPojo> arrayList = new ArrayList<>();
        HeaderPojo headerPojo;
        try {
            if (json.getBoolean("Success")) {
                JSONArray jsonArray = json.getJSONArray("Data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json_obj = jsonArray.getJSONObject(i);
                    json_obj = json_obj.getJSONObject("header");
                    headerPojo = new HeaderPojo();
                    headerPojo.setOrdernum(json_obj.getString("ordernum"));
                    headerPojo.setOdate(json_obj.getString("odate"));
                    headerPojo.setSname(json_obj.getString("sname"));
                    arrayList.add(headerPojo);
                }
                return arrayList;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    /**
     * 3.1.1	讀取會員訂單資訊(content):
     * ordernum	String	訂單編號
     * odate	String	訂單日期
     * sname	String	商家名稱
     */
    public static ArrayList<ArrayList<ContentPojo>> getMemberOrderContent(JSONObject json) {
        ArrayList<ArrayList<ContentPojo>> allList = new ArrayList<>();
        ArrayList<ContentPojo> arrayList;
        ContentPojo contentPojo;
        try {
            if (json.getBoolean("Success")) {
                JSONArray jsonArray = json.getJSONArray("Data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    arrayList = new ArrayList<>();
                    JSONObject json_obj = jsonArray.getJSONObject(i);
                    JSONArray content_array = json_obj.getJSONArray("content");
                    for (int j = 0; j < content_array.length(); j++) {
                        contentPojo = new ContentPojo();
                        JSONObject content_obj = content_array.getJSONObject(j);
                        contentPojo.setPname(content_obj.getString("pname"));
                        contentPojo.setPimg(content_obj.getString("pimg"));
                        contentPojo.setColor(content_obj.getString("color"));
                        contentPojo.setSize(content_obj.getString("size"));
                        contentPojo.setOiname(content_obj.getString("oiname"));
                        contentPojo.setOicolor(content_obj.getString("oicolor"));
                        contentPojo.setPrice(content_obj.getInt("price"));
                        contentPojo.setSprice(content_obj.getInt("sprice"));
                        contentPojo.setStotal(content_obj.getInt("stotal"));
                        arrayList.add(contentPojo);
                    }
                    allList.add(arrayList);
                }
                return allList;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return allList;
    }

    /**
     * 3.1.1	讀取會員訂單資訊(footer):
     * mono	String	訂單碼
     * ttotal	Number	總數量
     * tprice	Number	總金額
     * ostatus	Number	訂單狀態
     * oname	String	訂單狀態名
     * ocolor	String	訂單狀態顏色色碼
     * pstatus	Number	付款狀態
     * pname	String	付款狀態名
     * pcolor	String	付款狀態顏色色碼
     * lstatus	Number	配送狀態
     * lpname	String	配送狀態名
     * lcolor	String	配送狀態顏色色碼
     * istatus	Number	交易狀態
     * iname	String	交易狀態名
     * icolor	String	交易狀態顏色色碼
     * pinfo	String	付款方式
     */
    public static ArrayList<FooterPojo> getMemberOrderFooter(JSONObject json) {
        ArrayList<FooterPojo> arrayList = new ArrayList<>();
        FooterPojo footerPojo;
        try {
            if (json.getBoolean("Success")) {
                JSONArray jsonArray = json.getJSONArray("Data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json_obj = jsonArray.getJSONObject(i);
                    json_obj = json_obj.getJSONObject("footer");
                    footerPojo = new FooterPojo();
                    footerPojo.setMono(json_obj.getString("mono"));
                    footerPojo.setTtotal(json_obj.getInt("ttotal"));
                    footerPojo.setTprice(json_obj.getInt("tprice"));
                    footerPojo.setOstatus(json_obj.getInt("ostatus"));
                    footerPojo.setOname(json_obj.getString("oname"));
                    footerPojo.setOcolor(json_obj.getString("ocolor"));
                    footerPojo.setPstatus(json_obj.getInt("pstatus"));
                    footerPojo.setPname(json_obj.getString("pname"));
                    footerPojo.setPcolor(json_obj.getString("pcolor"));
                    footerPojo.setLstatus(json_obj.getInt("lstatus"));
                    footerPojo.setLpname(json_obj.getString("lname"));
                    footerPojo.setLcolor(json_obj.getString("lcolor"));
                    footerPojo.setIstatus(json_obj.getInt("istatus"));
                    footerPojo.setIname(json_obj.getString("iname"));
                    footerPojo.setIcolor(json_obj.getString("icolor"));
                    footerPojo.setPinfo(json_obj.getString("pinfo"));
                    arrayList.add(footerPojo);
                }
                return arrayList;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

}
