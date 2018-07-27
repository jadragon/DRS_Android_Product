package library.AnalyzeJSON;

import com.test.tw.wrokproduct.我的帳戶.帳務管理.pojo.BillPojo;
import com.test.tw.wrokproduct.我的帳戶.帳務管理.帳戶總覽.pojo.GetBillingPojo;
import com.test.tw.wrokproduct.我的帳戶.帳務管理.現金折價券.pojo.CouponPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AnalyzeBill {
    /**
     * 6.1.1	讀取帳戶總覽
     */
    public GetBillingPojo getBillingPojo(JSONObject json) {

        try {
            GetBillingPojo getBillingPojo = new GetBillingPojo();
            if (json.getBoolean("Success")) {
                JSONObject json_obj = json.getJSONObject("Data");
                JSONObject object = json_obj.getJSONObject("bankinfo");
                getBillingPojo.setBuname(object.getString("buname"));
                getBillingPojo.setBname(object.getString("bname"));
                getBillingPojo.setBcard(object.getString("bcard"));
                object = json_obj.getJSONObject("pointinfo");
                getBillingPojo.setXpoint(object.getInt("xpoint"));
                getBillingPojo.setYpoint(object.getInt("ypoint"));
                getBillingPojo.setAcoin(object.getInt("acoin"));
                getBillingPojo.setEwallet(object.getInt("ewallet"));
                getBillingPojo.setCoupon(object.getString("coupon"));
                return getBillingPojo;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 6.2.1	讀取波克點值
     */
    public static ArrayList<BillPojo> getBillPojo(JSONObject json) {
        ArrayList<BillPojo> arrayList = new ArrayList<>();
        BillPojo billPojo;
        try {
            if (json.getBoolean("Success")) {
                JSONArray jsonArray = json.getJSONObject("Data").getJSONArray("mdata");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json_obj = jsonArray.getJSONObject(i);
                    billPojo = new BillPojo();
                    try {
                        billPojo.setBstatus(json_obj.getInt("bstatus"));
                        billPojo.setBtitle(json_obj.getString("btitle"));
                    } catch (Exception e) {

                    }
                    billPojo.setCdate(json_obj.getString("cdate"));
                    billPojo.setIetype(json_obj.getInt("ietype"));
                    billPojo.setRate(json_obj.getString("rate"));
                    billPojo.setIemoney(json_obj.getInt("iemoney"));
                    billPojo.setBmoney(json_obj.getInt("bmoney"));
                    billPojo.setAmoney(json_obj.getInt("amoney"));
                    arrayList.add(billPojo);
                }
                return arrayList;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    /**
     * 6.8.1	讀取現金折價券
     */
    public ArrayList<CouponPojo> getCouponPojo(JSONObject json) {
        ArrayList<CouponPojo> arrayList = new ArrayList<>();
        CouponPojo couponPojo;
        try {
            if (json.getBoolean("Success")) {
                JSONArray jsonArray = json.getJSONArray("Data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json_obj = jsonArray.getJSONObject(i);
                    couponPojo = new CouponPojo();
                    couponPojo.setMcno(json_obj.getString("mcno"));
                    couponPojo.setCoupon(json_obj.getString("coupon"));
                    couponPojo.setIsuse(json_obj.getInt("isuse"));
                    couponPojo.setMoney(json_obj.getInt("money"));
                    couponPojo.setCost(json_obj.getInt("cost"));
                    couponPojo.setEdate(json_obj.getString("edate"));
                    arrayList.add(couponPojo);
                }
                return arrayList;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }
}
