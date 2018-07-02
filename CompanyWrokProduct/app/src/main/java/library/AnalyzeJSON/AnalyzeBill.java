package library.AnalyzeJSON;

import com.test.tw.wrokproduct.帳務管理.pojo.BillPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AnalyzeBill {

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
}
