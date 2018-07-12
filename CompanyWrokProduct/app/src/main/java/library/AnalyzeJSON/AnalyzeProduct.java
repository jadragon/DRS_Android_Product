package library.AnalyzeJSON;

import com.test.tw.wrokproduct.商品.pojo.ProductCommentPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AnalyzeProduct {
    /**
     * 6.1.1	讀取帳戶總覽
     */
    public ArrayList<ProductCommentPojo> getProductCommentPojo(JSONObject json) {
        ArrayList<ProductCommentPojo> arrayList = new ArrayList<>();
        try {
            ProductCommentPojo productCommentPojo = new ProductCommentPojo();
            if (json.getBoolean("Success")) {
                JSONArray jsonArray = json.getJSONArray("Data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    productCommentPojo.setMname(object.getString("mname"));
                    productCommentPojo.setMimg(object.getString("mimg"));
                    productCommentPojo.setComment(object.getString("comment"));
                    productCommentPojo.setComscore(object.getInt("comscore"));
                    productCommentPojo.setComdate(object.getString("comdate"));
                    productCommentPojo.setRecomment(object.getString("recomment"));
                    productCommentPojo.setRecomdate(object.getString("recomdate"));
                    arrayList.add(productCommentPojo);
                }

                return arrayList;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

}
