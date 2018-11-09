package com.company.alex.ordersystemdemo.API.Analyze;

import com.company.alex.ordersystemdemo.API.Analyze.Pojo.OrderDataPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Analyze_Order {

    /**
     * 3.1	訂單資訊
     */
    public ArrayList<OrderDataPojo> getOrder_data(JSONObject json) {
        ArrayList<OrderDataPojo> arrayList = new ArrayList<>();
        try {
            if (json.getBoolean("Success")) {
                OrderDataPojo orderDataPojo;
                JSONArray jsonArray = json.getJSONArray("Data");
                JSONObject json_obj;
                for (int i = 0; i < jsonArray.length(); i++) {
                    orderDataPojo = new OrderDataPojo();
                    json_obj = jsonArray.getJSONObject(i);
                    orderDataPojo.setO_id(json_obj.getString("o_id"));
                    orderDataPojo.setO_number(json_obj.getString("order_number"));
                    orderDataPojo.setM_id(json_obj.getString("m_id"));
                    orderDataPojo.setS_id(json_obj.getString("s_id"));
                    orderDataPojo.setD_id(json_obj.getString("d_id"));
                    orderDataPojo.setM_name(json_obj.getString("m_name"));
                    orderDataPojo.setM_phone(json_obj.getString("m_phone"));
                    orderDataPojo.setM_note(json_obj.getString("m_note"));
                    orderDataPojo.setM_address(json_obj.getString("m_address"));
                    orderDataPojo.setS_name(json_obj.getString("s_name"));
                    orderDataPojo.setS_phone(json_obj.getString("s_phone"));
                    orderDataPojo.setS_address(json_obj.getString("s_address"));
                    orderDataPojo.setS_complete_time(json_obj.getString("s_complete_time"));
                    orderDataPojo.setD_name(json_obj.getString("d_name"));
                    orderDataPojo.setD_phone(json_obj.getString("d_phone"));
                    orderDataPojo.setF_content(json_obj.getString("f_content"));
                    orderDataPojo.setF_sum(json_obj.getString("f_sum"));
                    orderDataPojo.setM_time(json_obj.getString("m_time"));
                    orderDataPojo.setS_time(json_obj.getString("s_time"));
                    orderDataPojo.setD_time(json_obj.getString("d_time"));
                    arrayList.add(orderDataPojo);
                }
                return arrayList;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return arrayList;
    }


}
