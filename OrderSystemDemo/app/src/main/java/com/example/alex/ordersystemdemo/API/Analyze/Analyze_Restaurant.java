package com.example.alex.ordersystemdemo.API.Analyze;

import com.example.alex.ordersystemdemo.API.Analyze.Pojo.MenuPojo;
import com.example.alex.ordersystemdemo.API.Analyze.Pojo.StoreDataPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Analyze_Restaurant {

    /**
     * 2.1	店家
     */
    public ArrayList<StoreDataPojo> getStore_data(JSONObject json) {
        ArrayList<StoreDataPojo> arrayList = new ArrayList<>();
        try {
            if (json.getBoolean("Success")) {
                StoreDataPojo storeDataPojo;
                JSONArray jsonArray = json.getJSONArray("Data");
                JSONObject json_obj;
                for (int i = 0; i < jsonArray.length(); i++) {
                    storeDataPojo = new StoreDataPojo();
                    json_obj = jsonArray.getJSONObject(i);
                    storeDataPojo.setS_id(json_obj.getString("s_id"));
                    storeDataPojo.setName(json_obj.getString("name"));
                    storeDataPojo.setPhone(json_obj.getString("phone"));
                    storeDataPojo.setAddress(json_obj.getString("address"));
                    storeDataPojo.setImage(json_obj.getString("image"));
                    storeDataPojo.setTime(json_obj.getString("time"));
                    storeDataPojo.setD_default(json_obj.getString("d_default"));
                    storeDataPojo.setD_status(json_obj.getString("d_status"));
                    arrayList.add(storeDataPojo);
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

    /**
     * 2.2	菜單
     */
    public ArrayList<MenuPojo> getMenu(JSONObject json) {
        ArrayList<MenuPojo> arrayList = new ArrayList<>();
        try {
            if (json.getBoolean("Success")) {
                MenuPojo menuPojo;
                JSONArray jsonArray = json.getJSONArray("Data");
                JSONObject json_obj;
                for (int i = 0; i < jsonArray.length(); i++) {
                    menuPojo = new MenuPojo();
                    json_obj = jsonArray.getJSONObject(i);
                    menuPojo.setSo_id(json_obj.getString("so_id"));
                    menuPojo.setS_id(json_obj.getString("s_id"));
                    menuPojo.setFood(json_obj.getString("food"));
                    menuPojo.setMoney(json_obj.getInt("money"));
                    arrayList.add(menuPojo);
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
