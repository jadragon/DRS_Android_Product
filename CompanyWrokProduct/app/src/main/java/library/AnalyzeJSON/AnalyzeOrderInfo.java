package library.AnalyzeJSON;

import com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.pojo.AppreciatePojo;
import com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.pojo.MOrderItemContentPojo;
import com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.pojo.MOrderItemPojo;
import com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.pojo.MOrderPayPojo;
import com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.pojo.MemberOrderContentPojo;
import com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.pojo.MemberOrderFooterPojo;
import com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.pojo.MemberOrderHeaderPojo;
import com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.pojo.ReturnAndRefundContentPojo;
import com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.pojo.ReturnAndRefundHeaderPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AnalyzeOrderInfo {

    /**
     * 3.1.1	讀取會員訂單資訊(header):
     * ordernum	String	訂單編號
     * odate	String	訂單日期
     * sname	String	商家名稱
     */
    public static ArrayList<MemberOrderHeaderPojo> getMemberOrderHeader(JSONObject json) {
        ArrayList<MemberOrderHeaderPojo> arrayList = new ArrayList<>();
        MemberOrderHeaderPojo memberOrderHeaderPojo;
        try {
            if (json.getBoolean("Success")) {
                JSONArray jsonArray = json.getJSONArray("Data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json_obj = jsonArray.getJSONObject(i);
                    json_obj = json_obj.getJSONObject("header");
                    memberOrderHeaderPojo = new MemberOrderHeaderPojo();
                    memberOrderHeaderPojo.setOrdernum(json_obj.getString("ordernum"));
                    memberOrderHeaderPojo.setOdate(json_obj.getString("odate"));
                    try {
                        memberOrderHeaderPojo.setSname(json_obj.getString("sname"));
                    } catch (JSONException e) {
                        memberOrderHeaderPojo.setSname(json_obj.getString("uname"));
                    }
                    arrayList.add(memberOrderHeaderPojo);
                }
                return arrayList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    /**
     * 3.1.1	讀取會員訂單資訊(content):
     * ordernum	String	訂單編號
     * odate	String	訂單日期
     * sname	String	商家名稱
     */
    public static ArrayList<ArrayList<MemberOrderContentPojo>> getMemberOrderContent(JSONObject json) {
        ArrayList<ArrayList<MemberOrderContentPojo>> allList = new ArrayList<>();
        ArrayList<MemberOrderContentPojo> arrayList;
        MemberOrderContentPojo memberOrderContentPojo;
        try {
            if (json.getBoolean("Success")) {
                JSONArray jsonArray = json.getJSONArray("Data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    arrayList = new ArrayList<>();
                    JSONObject json_obj = jsonArray.getJSONObject(i);
                    JSONArray content_array = json_obj.getJSONArray("content");
                    for (int j = 0; j < content_array.length(); j++) {
                        memberOrderContentPojo = new MemberOrderContentPojo();
                        JSONObject content_obj = content_array.getJSONObject(j);
                        memberOrderContentPojo.setPname(content_obj.getString("pname"));
                        memberOrderContentPojo.setPimg(content_obj.getString("pimg"));
                        memberOrderContentPojo.setColor(content_obj.getString("color"));
                        memberOrderContentPojo.setSize(content_obj.getString("size"));
                        memberOrderContentPojo.setOiname(content_obj.getString("oiname"));
                        memberOrderContentPojo.setOicolor(content_obj.getString("oicolor"));
                        memberOrderContentPojo.setPrice(content_obj.getInt("price"));
                        memberOrderContentPojo.setSprice(content_obj.getInt("sprice"));
                        memberOrderContentPojo.setStotal(content_obj.getInt("stotal"));
                        arrayList.add(memberOrderContentPojo);
                    }
                    allList.add(arrayList);
                }
                return allList;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return allList;
    }

    /**
     * 3.1.1	讀取會員訂單資訊(footer):
     * mono	String	訂單碼
     * ttotal	Number	總數量
     * tprice	Number	總金額
     * ostatus	Number	訂單狀態
     * oname	String	訂單狀態名
     * ocolor	String	訂單狀態顏色色碼
     * pstatus	Number	付款狀態
     * pname	String	付款狀態名
     * pcolor	String	付款狀態顏色色碼
     * lstatus	Number	配送狀態
     * lpname	String	配送狀態名
     * lcolor	String	配送狀態顏色色碼
     * istatus	Number	交易狀態
     * iname	String	交易狀態名
     * icolor	String	交易狀態顏色色碼
     * pinfo	String	付款方式
     */
    public static ArrayList<MemberOrderFooterPojo> getMemberOrderFooter(JSONObject json) {
        ArrayList<MemberOrderFooterPojo> arrayList = new ArrayList<>();
        MemberOrderFooterPojo memberOrderFooterPojo;
        try {
            if (json.getBoolean("Success")) {
                JSONArray jsonArray = json.getJSONArray("Data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json_obj = jsonArray.getJSONObject(i);
                    json_obj = json_obj.getJSONObject("footer");
                    memberOrderFooterPojo = new MemberOrderFooterPojo();
                    memberOrderFooterPojo.setMono(json_obj.getString("mono"));
                    memberOrderFooterPojo.setTtotal(json_obj.getInt("ttotal"));
                    memberOrderFooterPojo.setTprice(json_obj.getInt("tprice"));
                    memberOrderFooterPojo.setOstatus(json_obj.getInt("ostatus"));
                    memberOrderFooterPojo.setOname(json_obj.getString("oname"));
                    memberOrderFooterPojo.setOcolor(json_obj.getString("ocolor"));
                    memberOrderFooterPojo.setAlnum(json_obj.getString("alnum"));
                    memberOrderFooterPojo.setAlchk(json_obj.getInt("alchk"));
                    memberOrderFooterPojo.setAldate(json_obj.getString("aldate"));
                    memberOrderFooterPojo.setPstatus(json_obj.getInt("pstatus"));
                    memberOrderFooterPojo.setPname(json_obj.getString("pname"));
                    memberOrderFooterPojo.setPcolor(json_obj.getString("pcolor"));
                    memberOrderFooterPojo.setLstatus(json_obj.getInt("lstatus"));
                    memberOrderFooterPojo.setLpname(json_obj.getString("lname"));
                    memberOrderFooterPojo.setLcolor(json_obj.getString("lcolor"));
                    try {
                        memberOrderFooterPojo.setLchk(json_obj.getInt("lchk"));
                    } catch (JSONException e) {
                        memberOrderFooterPojo.setLogisticsVal(json_obj.getString("logisticsVal"));
                    }
                    memberOrderFooterPojo.setIstatus(json_obj.getInt("istatus"));
                    memberOrderFooterPojo.setIname(json_obj.getString("iname"));
                    memberOrderFooterPojo.setIcolor(json_obj.getString("icolor"));
                    memberOrderFooterPojo.setCpchk(json_obj.getInt("cpchk"));
                    memberOrderFooterPojo.setCpdate(json_obj.getString("cpdate"));
                    memberOrderFooterPojo.setPinfo(json_obj.getString("pinfo"));
                    arrayList.add(memberOrderFooterPojo);
                }
                return arrayList;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    /**
     * 3.1.3	讀取會員訂單詳情資訊(header+buyer+store+invoice+footer):
     * odate	String	訂單日期
     * ordernum	String	訂單編號
     * logisticsnum	String	物流編號
     * step1	Number	訂單成立 0灰,1綠-, 2-綠, 3-綠-, 4-O-
     * sdate1	String	訂單成立時間
     * step2	Number	已出貨0灰,1綠-, 2-綠, 3-綠-, 4-O-
     * sdate2	String	已出貨時間
     * step3	Number	確認收貨0灰,1綠-, 2-綠, 3-綠-, 4-O-
     * sdate3	String	確認收貨時間
     * step4	Number	申請退換中0灰,1綠-, 2-綠, 3-綠-, 4-O-
     * sdate4	String	申請退換中時間
     * step5	Number	已完成0灰,1綠-, 2-綠, 3-綠-, 4-O-
     * sdate5	String	已完成時間
     * step6	Number	取消0灰,1綠-, 2-綠, 3-綠-, 4-O-
     * sdate6	String	取消時間
     */
    public static MOrderItemPojo getMOrderItem(JSONObject json) {
        try {
            if (json.getBoolean("Success")) {
                MOrderItemPojo mOrderItemPojo;
                JSONObject json_obj = json.getJSONObject("Data").getJSONObject("header");
                mOrderItemPojo = new MOrderItemPojo();
                mOrderItemPojo.setHeader_odate(json_obj.getString("odate"));
                mOrderItemPojo.setHeader_ordernum(json_obj.getString("ordernum"));
                mOrderItemPojo.setHeader_logisticsnum(json_obj.getString("logisticsnum"));
                mOrderItemPojo.setHeader_step1(json_obj.getInt("step1"));
                mOrderItemPojo.setHeader_sdate1(json_obj.getString("sdate1"));
                mOrderItemPojo.setHeader_step2(json_obj.getInt("step2"));
                mOrderItemPojo.setHeader_sdate2(json_obj.getString("sdate2"));
                mOrderItemPojo.setHeader_step3(json_obj.getInt("step3"));
                mOrderItemPojo.setHeader_sdate3(json_obj.getString("sdate3"));
                mOrderItemPojo.setHeader_step4(json_obj.getInt("step4"));
                mOrderItemPojo.setHeader_sdate4(json_obj.getString("sdate4"));
                mOrderItemPojo.setHeader_step5(json_obj.getInt("step5"));
                mOrderItemPojo.setHeader_sdate5(json_obj.getString("sdate5"));
                mOrderItemPojo.setHeader_step6(json_obj.getInt("step6"));
                mOrderItemPojo.setHeader_sdate6(json_obj.getString("sdate6"));
                //buyer
                json_obj = json.getJSONObject("Data").getJSONObject("buyer");
                mOrderItemPojo.setBuyer_logisticsVal(json_obj.getString("logisticsVal"));
                mOrderItemPojo.setBuyer_name(json_obj.getString("name"));
                mOrderItemPojo.setBuyer_mp(json_obj.getString("mp"));
                mOrderItemPojo.setBuyer_address(json_obj.getString("address"));
                //store
                json_obj = json.getJSONObject("Data").getJSONObject("store");
                mOrderItemPojo.setStore_sname(json_obj.getString("sname"));
                mOrderItemPojo.setStore_name(json_obj.getString("name"));
                mOrderItemPojo.setStore_mp(json_obj.getString("mp"));
                mOrderItemPojo.setStore_address(json_obj.getString("address"));
                //invoice
                json_obj = json.getJSONObject("Data").getJSONObject("invoice");
                mOrderItemPojo.setInvoice_intypeVal(json_obj.getString("intypeVal"));
                mOrderItemPojo.setInvoice_inumber(json_obj.getString("inumber"));
                mOrderItemPojo.setInvoice_ctitle(json_obj.getString("ctitle"));
                mOrderItemPojo.setInvoice_vat(json_obj.getString("vat"));
                //footer
                json_obj = json.getJSONObject("Data").getJSONObject("footer");
                mOrderItemPojo.setFooter_note(json_obj.getString("note"));
                mOrderItemPojo.setFooter_mdiscount(json_obj.getInt("mdiscount"));
                mOrderItemPojo.setFooter_lpay(json_obj.getInt("lpay"));
                mOrderItemPojo.setFooter_xmoney(json_obj.getInt("xmoney"));
                mOrderItemPojo.setFooter_ymoney(json_obj.getInt("ymoney"));
                mOrderItemPojo.setFooter_ewallet(json_obj.getInt("ewallet"));
                mOrderItemPojo.setFooter_allpay(json_obj.getInt("allpay"));
                return mOrderItemPojo;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 3.1.3	讀取會員訂單詳情資訊(content):
     * pname	String	商品名稱
     * pimg	String	商品圖
     * color	String	顏色
     * size	String	尺寸
     * oiname	String	訂單細項狀態名
     * oicolor	String	訂單細項狀態顏色色碼
     * price	Number	牌價
     * sprice	Number	售價
     * stotal	Number	訂購數量
     */
    public static ArrayList<MOrderItemContentPojo> getMOrderItemContent(JSONObject json) {
        ArrayList<MOrderItemContentPojo> arrayList = new ArrayList<>();
        try {
            if (json.getBoolean("Success")) {
                MOrderItemContentPojo mOrderItemContentPojo;
                JSONArray jsonArray = json.getJSONObject("Data").getJSONArray("content");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json_obj = jsonArray.getJSONObject(i);
                    mOrderItemContentPojo = new MOrderItemContentPojo();
                    mOrderItemContentPojo.setPname(json_obj.getString("pname"));
                    mOrderItemContentPojo.setPimg(json_obj.getString("pimg"));
                    mOrderItemContentPojo.setColor(json_obj.getString("color"));
                    mOrderItemContentPojo.setSize(json_obj.getString("size"));
                    mOrderItemContentPojo.setOiname(json_obj.getString("oiname"));
                    mOrderItemContentPojo.setOicolor(json_obj.getString("oicolor"));
                    mOrderItemContentPojo.setPrice(json_obj.getInt("price"));
                    mOrderItemContentPojo.setSprice(json_obj.getInt("sprice"));
                    mOrderItemContentPojo.setStotal(json_obj.getInt("stotal"));
                    arrayList.add(mOrderItemContentPojo);
                }

                return arrayList;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    /**
     * 3.1.2	讀取會員訂單付款詳情資訊:
     * pinfo	String	付款方式
     * allpay	Number	合併結帳–繳費金額
     * deadline	String	繳費期限
     * atm_code	String	ATM資訊 – 銀行代號
     * atm_account	String	ATM資訊 – 銀行帳號
     */
    public static MOrderPayPojo getMOrderPay(JSONObject json) {
        try {
            if (json.getBoolean("Success")) {
                MOrderPayPojo mOrderPayPojo;
                JSONObject json_obj = json.getJSONObject("Data");
                mOrderPayPojo = new MOrderPayPojo();
                mOrderPayPojo.setPinfo(json_obj.getString("pinfo"));
                mOrderPayPojo.setAllpay(json_obj.getInt("allpay"));
                mOrderPayPojo.setDeadline(json_obj.getString("deadline"));
                mOrderPayPojo.setAtm_code(json_obj.getString("atm_code"));
                mOrderPayPojo.setAtm_account(json_obj.getString("atm_account"));
                //oArray
                ArrayList<String> arrayList = new ArrayList<>();
                JSONArray jsonArray = json_obj.getJSONArray("oArray");
                for (int i = 0; i < jsonArray.length(); i++) {
                    arrayList.add(jsonArray.getString(i));
                }
                mOrderPayPojo.setOrdernum(arrayList);
                return mOrderPayPojo;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 3.1.2	讀取會員訂單付款詳情資訊:
     * pinfo	String	付款方式
     * allpay	Number	合併結帳–繳費金額
     * deadline	String	繳費期限
     * atm_code	String	ATM資訊 – 銀行代號
     * atm_account	String	ATM資訊 – 銀行帳號
     */
    public static ArrayList<AppreciatePojo> getOrderComment(JSONObject json) {
        ArrayList<AppreciatePojo> arrayList = new ArrayList<>();
        try {
            if (json.getBoolean("Success")) {
                AppreciatePojo appreciatePojo;
                JSONArray jsonArray = json.getJSONArray("Data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json_obj = jsonArray.getJSONObject(i);
                    appreciatePojo = new AppreciatePojo();
                    appreciatePojo.setMoino(json_obj.getString("moino"));
                    appreciatePojo.setPname(json_obj.getString("pname"));
                    try {
                        appreciatePojo.setImg(json_obj.getString("img"));
                    } catch (JSONException e) {
                        appreciatePojo.setImg(json_obj.getString("pimg"));
                    }
                    appreciatePojo.setColor(json_obj.getString("color"));
                    appreciatePojo.setSize(json_obj.getString("size"));
                    appreciatePojo.setComment(json_obj.getString("comment"));
                    appreciatePojo.setComscore(json_obj.getDouble("comscore"));
                    appreciatePojo.setComtimes(json_obj.getInt("comtimes"));
                    appreciatePojo.setComdate(json_obj.getString("comdate"));
                    arrayList.add(appreciatePojo);
                }
                return arrayList;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    /**
     * 3.1.22	退換貨進度 – 讀取訂單商品列表
     * order	Object	訂單資料
     * ordernum	String	訂單編號
     * odate	String	訂單日期
     * store	Object	商家資料
     * sname	String	商家名稱
     * simg	String	商家圖
     * content	Array	商品內容
     * moino	String	訂單細項碼
     * pname	String	商品名稱
     * pimg	String	商品圖
     * color	String	顏色
     * size	String	尺寸
     * oiname	String	訂單細項狀態名
     * oicolor	String	訂單細項狀態顏色色碼
     * price	Number	牌價
     * sprice	Number	售價
     * stotal	Number	訂購數量
     */
    public static ReturnAndRefundHeaderPojo getMOrderReturnHeader(JSONObject json) {
        try {
            if (json.getBoolean("Success")) {
                ReturnAndRefundHeaderPojo returnAndRefundHeaderPojo = new ReturnAndRefundHeaderPojo();
                JSONObject jsonObject = json.getJSONObject("Data");
                JSONObject json_obj = jsonObject.getJSONObject("order");
                returnAndRefundHeaderPojo.setOrdernum(json_obj.getString("ordernum"));
                returnAndRefundHeaderPojo.setOdate(json_obj.getString("odate"));
                json_obj = jsonObject.getJSONObject("store");
                returnAndRefundHeaderPojo.setSname(json_obj.getString("sname"));
                returnAndRefundHeaderPojo.setSimg(json_obj.getString("simg"));
                return returnAndRefundHeaderPojo;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 3.1.22	退換貨進度 – 讀取訂單商品列表
     * order	Object	訂單資料
     * ordernum	String	訂單編號
     * odate	String	訂單日期
     * store	Object	商家資料
     * sname	String	商家名稱
     * simg	String	商家圖
     * content	Array	商品內容
     * moino	String	訂單細項碼
     * pname	String	商品名稱
     * pimg	String	商品圖
     * color	String	顏色
     * size	String	尺寸
     * oiname	String	訂單細項狀態名
     * oicolor	String	訂單細項狀態顏色色碼
     * price	Number	牌價
     * sprice	Number	售價
     * stotal	Number	訂購數量
     */
    public static ArrayList<ReturnAndRefundContentPojo> getMOrderReturnContent(JSONObject json) {
        ArrayList<ReturnAndRefundContentPojo> arrayList = new ArrayList<>();
        try {
            if (json.getBoolean("Success")) {
                JSONObject jsonObject = json.getJSONObject("Data");
                //content
                ReturnAndRefundContentPojo returnAndRefundContentPojo;
                JSONArray jsonArray = jsonObject.getJSONArray("content");
                JSONObject json_obj;
                for (int i = 0; i < jsonArray.length(); i++) {
                    returnAndRefundContentPojo = new ReturnAndRefundContentPojo();
                    json_obj = jsonArray.getJSONObject(i);
                    returnAndRefundContentPojo.setMoino(json_obj.getString("moino"));
                    returnAndRefundContentPojo.setPname(json_obj.getString("pname"));
                    returnAndRefundContentPojo.setPimg(json_obj.getString("pimg"));
                    returnAndRefundContentPojo.setColor(json_obj.getString("color"));
                    returnAndRefundContentPojo.setSize(json_obj.getString("size"));
                    returnAndRefundContentPojo.setOiname(json_obj.getString("oiname"));
                    returnAndRefundContentPojo.setOicolor(json_obj.getString("oicolor"));
                    returnAndRefundContentPojo.setPrice(json_obj.getInt("price"));
                    returnAndRefundContentPojo.setSprice(json_obj.getInt("sprice"));
                    returnAndRefundContentPojo.setStotal(json_obj.getInt("stotal"));
                    returnAndRefundContentPojo.setScount(0);
                    arrayList.add(returnAndRefundContentPojo);
                }
                return arrayList;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
