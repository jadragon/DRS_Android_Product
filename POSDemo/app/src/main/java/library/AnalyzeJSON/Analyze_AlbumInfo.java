package library.AnalyzeJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import library.AnalyzeJSON.APIpojo.AlbumListPojo;

public class Analyze_AlbumInfo {

    /**
     * 4.1 相簿資料
     */
    public ArrayList<AlbumListPojo> getAlbum_Data(JSONObject json) {
        ArrayList<AlbumListPojo> arrayList = new ArrayList<>();
        try {
            if (json.getBoolean("Success")) {
                AlbumListPojo albumListPojo = null;
                JSONArray jsonArray = json.getJSONArray("Data");
                JSONObject json_obj;
                for (int i = 0; i < jsonArray.length(); i++) {
                    albumListPojo = new AlbumListPojo();
                    json_obj = jsonArray.getJSONObject(i);
                    try {
                        albumListPojo.setA_no(json_obj.getString("a_no"));
                        albumListPojo.setName(json_obj.getString("name"));
                        albumListPojo.setCount(json_obj.getInt("count"));
                        albumListPojo.setImg(json_obj.getString("img"));
                    } catch (JSONException e) {
                        albumListPojo.setA_no(json_obj.getString("ad_no"));
                        albumListPojo.setName(json_obj.getString("oname"));
                        albumListPojo.setImg(json_obj.getString("img"));
                    }

                    arrayList.add(albumListPojo);
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
