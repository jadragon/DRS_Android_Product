package library.AnalyzeJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import library.AnalyzeJSON.APIpojo.CouponPojo;
import library.AnalyzeJSON.APIpojo.ProductListPojo;
import library.AnalyzeJSON.APIpojo.Store_PaymentStylePojo;

public class Analyze_CountInfo {

    /**
     * 3-1 取得門市與付款方式
     */
    public Store_PaymentStylePojo getStore_Payment_Style(JSONObject json) {
        try {
            if (json.getBoolean("Success")) {
                Store_PaymentStylePojo store_paymentStylePojo = new Store_PaymentStylePojo();
                JSONArray jsonArray = json.getJSONObject("Data").getJSONArray("store");
                JSONObject json_obj;
                ArrayList<String> arrayList1 = null;
                ArrayList<String> arrayList2 = null;
                //store
                for (int i = 0; i < jsonArray.length(); i++) {
                    arrayList1 = new ArrayList<>();
                    arrayList2 = new ArrayList<>();
                    json_obj = jsonArray.getJSONObject(i);
                    arrayList1.add(json_obj.getString("s_no"));
                    arrayList2.add(json_obj.getString("store"));
                }
                //payment
                store_paymentStylePojo.setStore(store_paymentStylePojo.new StoreStypePojoItem(arrayList1, arrayList2));
                jsonArray = json.getJSONObject("Data").getJSONArray("payment");
                for (int i = 0; i < jsonArray.length(); i++) {
                    arrayList1 = new ArrayList<>();
                    arrayList2 = new ArrayList<>();
                    json_obj = jsonArray.getJSONObject(i);
                    arrayList1.add(json_obj.getString("p_no"));
                    arrayList2.add(json_obj.getString("style"));
                }
                store_paymentStylePojo.setPayment(store_paymentStylePojo.new PaymentStypePojoItem(arrayList1, arrayList2));
                return store_paymentStylePojo;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 3-4 查詢會員訂單
     */
    public ArrayList<ProductListPojo> getSearch_Member_Order(JSONObject json) {
        ArrayList<ProductListPojo> arrayList = new ArrayList<>();
        try {
            if (json.getBoolean("Success")) {
                ProductListPojo productListPojo = null;
                JSONArray jsonArray = json.getJSONArray("Data");
                JSONObject json_obj;
                //store
                for (int i = 0; i < jsonArray.length(); i++) {
                    productListPojo = new ProductListPojo();
                    json_obj = jsonArray.getJSONObject(i);
                    productListPojo.setMoi_no(json_obj.getString("moi_no"));
                    productListPojo.setPname(json_obj.getString("pname"));
                    productListPojo.setPcode(json_obj.getString("pcode"));
                    productListPojo.setColor(json_obj.getString("color"));
                    productListPojo.setSize(json_obj.getString("size"));
                    productListPojo.setFprice(json_obj.getInt("fprice"));
                    productListPojo.setPrice(json_obj.getInt("price"));
                    productListPojo.setSamount(json_obj.getInt("samount"));
                    productListPojo.setSum(json_obj.getInt("sum"));
                    productListPojo.setNote(json_obj.getString("note"));
                    arrayList.add(productListPojo);
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
     * 3-6 優惠內容(bdiscount)
     */

    public ArrayList<CouponPojo> getPreferential_ContentBD(JSONObject json) {
        ArrayList<CouponPojo> arrayList = new ArrayList<>();
        try {
            if (json.getBoolean("Success")) {
                CouponPojo couponPojo = null;
                JSONArray jsonArray = json.getJSONObject("Data").getJSONArray("bdiscount");
                JSONObject json_obj;
                for (int i = 0; i < jsonArray.length(); i++) {
                    couponPojo = new CouponPojo();
                    json_obj = jsonArray.getJSONObject(i);
                    couponPojo.setMb_no(json_obj.getString("mb_no"));
                    couponPojo.setName(json_obj.getString("name"));
                    couponPojo.setDiscount(json_obj.getString("discount"));
                    arrayList.add(couponPojo);
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
     * 3-6 優惠內容(fdiscount)
     */
    public ArrayList<CouponPojo> getPreferential_ContentFD(JSONObject json) {
        ArrayList<CouponPojo> arrayList = new ArrayList<>();
        try {
            if (json.getBoolean("Success")) {
                CouponPojo couponPojo = null;
                JSONArray jsonArray = json.getJSONObject("Data").getJSONArray("fdiscount");
                JSONObject json_obj;
                for (int i = 0; i < jsonArray.length(); i++) {
                    couponPojo = new CouponPojo();
                    json_obj = jsonArray.getJSONObject(i);
                    couponPojo.setMf_no(json_obj.getString("mf_no"));
                    couponPojo.setName(json_obj.getString("name"));
                    couponPojo.setDiscount(json_obj.getString("discount"));
                    arrayList.add(couponPojo);
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
     * 3-6 優惠內容(mcoupon)
     */
    public ArrayList<CouponPojo> getPreferential_ContentMC(JSONObject json) {
        ArrayList<CouponPojo> arrayList = new ArrayList<>();
        try {
            if (json.getBoolean("Success")) {
                CouponPojo couponPojo = null;
                JSONArray jsonArray = json.getJSONObject("Data").getJSONArray("mcoupon");
                JSONObject json_obj;
                for (int i = 0; i < jsonArray.length(); i++) {
                    couponPojo = new CouponPojo();
                    json_obj = jsonArray.getJSONObject(i);
                    couponPojo.setMm_no(json_obj.getString("mm_no"));
                    couponPojo.setName(json_obj.getString("name"));
                    couponPojo.setType(json_obj.getString("type"));
                    couponPojo.setDiscount(json_obj.getString("money"));
                    couponPojo.setMoney(json_obj.getString("money"));
                    arrayList.add(couponPojo);
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
     * 3-8顯示產品資料<單筆>
     */
    public ArrayList<ProductListPojo> getProduct_Item(JSONObject json) {
        ArrayList<ProductListPojo> arrayList = new ArrayList<>();
        try {
            if (json.getBoolean("Success")) {
                ProductListPojo productListPojo = null;
                JSONArray jsonArray = json.getJSONArray("Data");
                JSONObject json_obj;
                //store
                for (int i = 0; i < jsonArray.length(); i++) {
                    productListPojo = new ProductListPojo();
                    json_obj = jsonArray.getJSONObject(i);
                    productListPojo.setP_no(json_obj.getString("p_no"));
                    productListPojo.setPi_no(json_obj.getString("pi_no"));
                    productListPojo.setPname(json_obj.getString("pname"));
                    productListPojo.setPcode(json_obj.getString("pcode"));
                    productListPojo.setColor(json_obj.getString("color"));
                    productListPojo.setSize(json_obj.getString("size"));
                    productListPojo.setFprice(json_obj.getInt("fprice"));
                    productListPojo.setPrice(json_obj.getInt("price"));
                    productListPojo.setStotal(json_obj.getInt("stotal"));
                    productListPojo.setTotal(json_obj.getInt("total"));
                    arrayList.add(productListPojo);
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
