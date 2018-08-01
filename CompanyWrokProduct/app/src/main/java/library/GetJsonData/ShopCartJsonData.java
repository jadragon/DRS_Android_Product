package library.GetJsonData;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class ShopCartJsonData extends APIInfomation {
    private final String setCart_url = DOMAIN + "main/cart/setCart.php";
    private final String getCart_url = DOMAIN + "main/cart/getCart.php";
    private final String addCartProduct_url = DOMAIN + "main/cart/addCartProduct.php";
    private final String delCartProduct_url = DOMAIN + "main/cart/delCartProduct.php";
    private final String setCartDiscount_url = DOMAIN + "main/cart/setCartDiscount.php";
    private final String delCartDiscount_url = DOMAIN + "main/cart/delCartDiscount.php";
    private final String goCheckout_url = DOMAIN + "main/cart/goCheckout.php";
    private final String getCheckout_url = DOMAIN + "main/cart/getCheckout.php";
    private final String setStoreNote_url = DOMAIN + "main/cart/setStoreNote.php";
    private final String getStoreLogistics_url = DOMAIN + "main/cart/getStoreLogistics.php";
    private final String setStoreMemberLogistics_url = DOMAIN + "main/cart/setStoreMemberLogistics.php";
    private final String setMemberLogistics_url = DOMAIN + "main/cart/setMemberLogistics.php";

    private final String getMemberPayments_url = DOMAIN + "main/cart/getMemberPayment.php";
    private final String setMemberPayment_url = DOMAIN + "main/cart/setMemberPayment.php";
    private final String setVat_url = DOMAIN + "main/cart/setVat.php";
    private final String setGoldFlow_url = DOMAIN + "main/cart/setGoldFlow.php";

    public ShopCartJsonData() {
        super();
    }

    /**
     * 1.3.1	加入購物車
     */
    public JSONObject setCart(String token, String pno, String pino, int total) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("pno", pno));
        params.add(new BasicNameValuePair("pino", pino));
        params.add(new BasicNameValuePair("total", "" + total));
        return jsonParser.getJSONFromUrl(setCart_url, params);
    }

    /**
     * 1.3.2	購買清單 - 讀取購買資訊
     */
    public JSONObject getCart(String token, String type) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("type", type));
        return jsonParser.getJSONFromUrl(getCart_url, params);
    }

    /**
     * 1.3.3	購買清單 - 商品增加數量或減少數量
     */
    public JSONObject addCartProduct(String token, String morno, int total) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("morno", "" + morno));
        params.add(new BasicNameValuePair("total", "" + total));
        return jsonParser.getJSONFromUrl(addCartProduct_url, params);
    }

    /**
     * 1.3.4	購買清單 - 移除購買商品
     */
    public JSONObject delCartProduct(String token, String morno) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("morno", morno));
        return jsonParser.getJSONFromUrl(delCartProduct_url, params);
    }

    /**
     * 1.3.5	購買清單 - 輸入折扣代碼
     */
    public JSONObject setCartDiscount(String token, String coupon, int pay) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("coupon", coupon));
        params.add(new BasicNameValuePair("pay", pay + ""));
        return jsonParser.getJSONFromUrl(setCartDiscount_url, params);
    }

    /**
     * 1.3.6	購買清單 - 取消折扣代碼
     */
    public JSONObject delCartDiscount(String token, String moprno) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("moprno", moprno));
        return jsonParser.getJSONFromUrl(delCartDiscount_url, params);
    }

    /**
     * 1.3.7	購買清單 - 去結帳
     */
    public JSONObject goCheckout(String token, String mornoArray) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("mornoArray", mornoArray));
        return jsonParser.getJSONFromUrl(goCheckout_url, params);
    }

    /**
     * 1.3.8	結帳清單 - 讀取結帳資訊
     */
    public JSONObject getCheckout(String token) {
        params.add(new BasicNameValuePair("token", token));
        return jsonParser.getJSONFromUrl(getCheckout_url, params);
    }

    /**
     * 1.3.9	結帳清單 - 讀取商家運送方式
     */
    public JSONObject getStoreLogistics(String token, String sno) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("sno", sno));
        return jsonParser.getJSONFromUrl(getStoreLogistics_url, params);
    }

    /**
     * 1.3.10	結帳清單 - 設定商家中, 買家運送方式
     */
    public JSONObject setStoreMemberLogistics(String token, String sno, String plno, String mlno) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("sno", sno));
        params.add(new BasicNameValuePair("plno", plno));
        params.add(new BasicNameValuePair("mlno", mlno));
        return jsonParser.getJSONFromUrl(setStoreMemberLogistics_url, params);
    }


    /**
     * 1.3.11-1	結帳清單 - 新增買家物流資訊
     */
    public JSONObject setMemberLogistics(String token, String sno, String plno, String type, String land,
                                         String logistics, String name, String mpcode, String mp, String sname, String sid,
                                         String shit, String city, String area, String zipcode, String address) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("sno", sno));
        params.add(new BasicNameValuePair("plno", plno));
        params.add(new BasicNameValuePair("type", type));
        params.add(new BasicNameValuePair("land", land));
        params.add(new BasicNameValuePair("logistics", logistics));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("mpcode", mpcode));
        params.add(new BasicNameValuePair("mp", mp));
        params.add(new BasicNameValuePair("sname", sname));
        params.add(new BasicNameValuePair("sid", sid));
        params.add(new BasicNameValuePair("shit", shit));
        params.add(new BasicNameValuePair("city", city));
        params.add(new BasicNameValuePair("area", area));
        params.add(new BasicNameValuePair("zipcode", zipcode));
        params.add(new BasicNameValuePair("address", address));

        return jsonParser.getJSONFromUrl(setMemberLogistics_url, params);
    }

    /**
     * 1.3.11-2	結帳清單 - 新增買家物流資訊
     */
    public JSONObject setMemberLogistics(String token, String sno, String plno, String type, String land,
                                         String logistics, String name, String mpcode, String mp,
                                         String shit, String city, String area, String zipcode, String address) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("sno", sno));
        params.add(new BasicNameValuePair("plno", plno));
        params.add(new BasicNameValuePair("type", type));
        params.add(new BasicNameValuePair("land", land));
        params.add(new BasicNameValuePair("logistics", logistics));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("mpcode", mpcode));
        params.add(new BasicNameValuePair("mp", mp));
        params.add(new BasicNameValuePair("shit", shit));
        params.add(new BasicNameValuePair("city", city));
        params.add(new BasicNameValuePair("area", area));
        params.add(new BasicNameValuePair("zipcode", zipcode));
        params.add(new BasicNameValuePair("address", address));
        return jsonParser.getJSONFromUrl(setMemberLogistics_url, params);
    }

    /**
     * 1.3.12	結帳清單–讀取買家可付款方式
     */
    public JSONObject getMemberPayments(String token) {
        params.add(new BasicNameValuePair("token", token));
        return jsonParser.getJSONFromUrl(getMemberPayments_url, params);
    }

    /**
     * 1.3.13	結帳清單–設定買家付款方式
     */
    public JSONObject setMemberPayment(String token, long xkeyin, long ykeyin, long ekeyin, String pno) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("xkeyin", xkeyin + ""));
        params.add(new BasicNameValuePair("ykeyin", ykeyin + ""));
        params.add(new BasicNameValuePair("ekeyin", ekeyin + ""));
        params.add(new BasicNameValuePair("pno", pno));

        return jsonParser.getJSONFromUrl(setMemberPayment_url, params);
    }

    /**
     * 1.3.14	結帳清單 – 設定商家備註
     */
    public JSONObject setStoreNote(String token, String sno, String note) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("sno", sno));
        params.add(new BasicNameValuePair("note", note));
        return jsonParser.getJSONFromUrl(setStoreNote_url, params);
    }

    /**
     * 1.3.18	結帳清單–設定發票資訊
     */
    public JSONObject setVat(String token, int invoice, String ctitle, String vat) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("invoice", invoice + ""));
        params.add(new BasicNameValuePair("ctitle", ctitle));
        params.add(new BasicNameValuePair("vat", vat));
        return jsonParser.getJSONFromUrl(setVat_url, params);
    }

    /**
     * 1.3.15	訂單完成 - 處理金流流程
     */
    public JSONObject setGoldFlow(String token) {
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("device", "2"));
        return jsonParser.getJSONFromUrl(setGoldFlow_url, params);
    }

}
