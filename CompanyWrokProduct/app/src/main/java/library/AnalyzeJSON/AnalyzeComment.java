package library.AnalyzeJSON;

import com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.pojo.MyCommentitemPojo;
import com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.pojo.MyCommenttopPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AnalyzeComment {

    /**
     * 3.3.1	讀取我的評價資訊top
     */
    public ArrayList<MyCommenttopPojo> getMyCommenttopPojo(JSONObject json) {
        ArrayList<MyCommenttopPojo> arrayList = new ArrayList<>();
        try {
            MyCommenttopPojo myCommenttopPojo = new MyCommenttopPojo();
            if (json.getBoolean("Success")) {
                JSONObject jsonObject = json.getJSONObject("Data");
                JSONArray jsonArray = jsonObject.getJSONArray("top");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    myCommenttopPojo.setTscore(((float) object.getDouble("tscore")));
                    myCommenttopPojo.setTnum(object.getInt("tnum"));
                    myCommenttopPojo.setNum1(object.getInt("num1"));
                    myCommenttopPojo.setNum2(object.getInt("num2"));
                    myCommenttopPojo.setNum3(object.getInt("num3"));
                    myCommenttopPojo.setNum4(object.getInt("num4"));
                    myCommenttopPojo.setNum5(object.getInt("num5"));
                    arrayList.add(myCommenttopPojo);
                }
                return arrayList;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }
    /**
     * 3.3.1	讀取我的評價資訊item
     */
    public ArrayList<MyCommentitemPojo> getMyCommentitemPojo(JSONObject json) {
        ArrayList<MyCommentitemPojo> arrayList = new ArrayList<>();
        try {
            MyCommentitemPojo myCommentPojo = new MyCommentitemPojo();
            if (json.getBoolean("Success")) {
                JSONObject jsonObject = json.getJSONObject("Data");
                JSONArray jsonArray = jsonObject.getJSONArray("item");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    myCommentPojo.setMoino(object.getString("moino"));
                    myCommentPojo.setSname(object.getString("sname"));
                    myCommentPojo.setSimg(object.getString("simg"));
                    myCommentPojo.setPname(object.getString("pname"));
                    myCommentPojo.setImg(object.getString("img"));
                    myCommentPojo.setColor(object.getString("color"));
                    myCommentPojo.setSize(object.getString("size"));
                    myCommentPojo.setComment(object.getString("comment"));
                    myCommentPojo.setComscore(object.getInt("comscore"));
                    myCommentPojo.setComtimes(object.getInt("comtimes"));
                    myCommentPojo.setComdate(object.getString("comdate"));
                    myCommentPojo.setRecomment(object.getString("recomment"));
                    myCommentPojo.setRecomdate(object.getString("recomdate"));
                    arrayList.add(myCommentPojo);
                }
                return arrayList;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

}
