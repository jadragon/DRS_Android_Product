package library.AnalyzeJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import library.AnalyzeJSON.APIpojo.AttendanceContentPojo;
import library.AnalyzeJSON.APIpojo.AttendanceStorePojo;

public class Analyze_PunchInfo {

    /**
     * 搜尋出勤打卡(Store)
     */
    public AttendanceStorePojo getAttendanceStore(JSONObject json) {
        try {
            if (json.getBoolean("Success")) {
                AttendanceStorePojo attendanceStorePojo = new AttendanceStorePojo();
                JSONArray jsonArray = json.getJSONObject("Data").getJSONArray("store");
                JSONObject json_obj;
                String[] s_no = new String[jsonArray.length()];
                String[] store = new String[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    json_obj = jsonArray.getJSONObject(i);
                    s_no[i] = json_obj.getString("s_no");
                    store[i] = json_obj.getString("store");
                }
                attendanceStorePojo.setS_no(s_no);
                attendanceStorePojo.setStore(store);
                return attendanceStorePojo;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 搜尋出勤打卡(Content)
     */
    public ArrayList<AttendanceContentPojo> getAttendanceContent(JSONObject json) {
        ArrayList<AttendanceContentPojo> arrayList = new ArrayList<>();
        try {
            if (json.getBoolean("Success")) {
                AttendanceContentPojo attendenceContentPojo;
                JSONArray jsonArray = json.getJSONObject("Data").getJSONArray("content");
                JSONObject json_obj;
                for (int i = 0; i < jsonArray.length(); i++) {
                    json_obj = jsonArray.getJSONObject(i);
                    attendenceContentPojo = new AttendanceContentPojo();
                    attendenceContentPojo.setClassnum(json_obj.getInt("classnum"));
                    attendenceContentPojo.setCommute(json_obj.getInt("commute"));
                    attendenceContentPojo.setNote(json_obj.getString("note"));
                    attendenceContentPojo.setPuch_time(json_obj.getString("puch_time"));
                    attendenceContentPojo.setName(json_obj.getString("name"));
                    attendenceContentPojo.setEn(json_obj.getString("en"));
                    attendenceContentPojo.setStore(json_obj.getString("store"));
                    arrayList.add(attendenceContentPojo);
                }
                return arrayList;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }


}
