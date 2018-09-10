package library.AnalyzeJSON;

import com.example.alex.posdemo.adapter.recylclerview.SelectBrandAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import library.AnalyzeJSON.APIpojo.NewBrandListPojo;

public class Analyze_NewBrandInfo {

    /**
     * 4.1 相簿資料
     */
    public ArrayList<NewBrandListPojo> getTypeRel(JSONObject json) {
        ArrayList<NewBrandListPojo> arrayList = new ArrayList<>();
        try {
            if (json.getBoolean("Success")) {
                NewBrandListPojo newBrandListPojo;
                JSONArray jsonArray = json.getJSONArray("Data");
                JSONObject json_obj;
                for (int i = 0; i < jsonArray.length(); i++) {
                    newBrandListPojo = new NewBrandListPojo(SelectBrandAdapter.TYPE_HEADER);
                    json_obj = jsonArray.getJSONObject(i);
                    newBrandListPojo.setPt_no(json_obj.getString("pt_no"));
                    newBrandListPojo.setTitle(json_obj.getString("title"));
                    newBrandListPojo.setCode(json_obj.getString("code"));
                    arrayList.add(newBrandListPojo);

                    JSONArray l_data_Array = json_obj.getJSONArray("l_data");
                    JSONObject l_data_obj;
                    for (int j = 0; j < l_data_Array.length(); j++) {
                        l_data_obj = l_data_Array.getJSONObject(j);
                        newBrandListPojo = new NewBrandListPojo(SelectBrandAdapter.TYPE_CONTENT);
                        newBrandListPojo.setPt_no(l_data_obj.getString("pt_no"));
                        newBrandListPojo.setTitle(l_data_obj.getString("title"));
                        newBrandListPojo.setCode(l_data_obj.getString("code"));
                        arrayList.add(newBrandListPojo);
                    }

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
