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
    public MyCommenttopPojo getMyCommenttopPojo(JSONObject json) {
        try {
            MyCommenttopPojo myCommenttopPojo = new MyCommenttopPojo();
            if (json.getBoolean("Success")) {
                JSONObject jsonObject = json.getJSONObject("Data");
                JSONObject object = jsonObject.getJSONObject("top");
                myCommenttopPojo.setTscore((object.getDouble("tscore")));
                myCommenttopPojo.setTnum(object.getInt("tnum"));
                myCommenttopPojo.setNum1(object.getInt("num1"));
                myCommenttopPojo.setNum2(object.getInt("num2"));
                myCommenttopPojo.setNum3(object.getInt("num3"));
                myCommenttopPojo.setNum4(object.getInt("num4"));
                myCommenttopPojo.setNum5(object.getInt("num5"));
                return myCommenttopPojo;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 3.3.1	讀取我的評價資訊item
     */
    public ArrayList<MyCommentitemPojo> getMyCommentitemPojo(JSONObject json) {
        ArrayList<MyCommentitemPojo> arrayList = new ArrayList<>();
        try {
            MyCommentitemPojo myCommentPojo;
            if (json.getBoolean("Success")) {
                JSONObject jsonObject = json.getJSONObject("Data");
                JSONArray jsonArray = jsonObject.getJSONArray("item");
                for (int i = 0; i < jsonArray.length(); i++) {
                    myCommentPojo = new MyCommentitemPojo();
                    JSONObject object = jsonArray.getJSONObject(i);
                    myCommentPojo.setMoino(object.getString("moino"));
                    try {
                        myCommentPojo.setSname(object.getString("sname"));
                        myCommentPojo.setSimg(object.getString("simg"));
                    } catch (JSONException e) {
                        myCommentPojo.setSname(object.getString("mname"));
                        myCommentPojo.setSimg(object.getString("mimg"));
                    }
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
