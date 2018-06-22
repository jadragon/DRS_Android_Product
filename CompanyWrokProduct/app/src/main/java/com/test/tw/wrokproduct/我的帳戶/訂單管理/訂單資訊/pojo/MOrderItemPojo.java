package com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.pojo;

public class MOrderItemPojo extends Item {
    //header
    String header_odate;
    String header_ordernum;
    String header_logisticsnum;
    int header_step1;
    String header_sdate1;
    int header_step2;
    String header_sdate2;
    int header_step3;
    String header_sdate3;
    int header_step4;
    String header_sdate4;
    int header_step5;
    String header_sdate5;
    int header_step6;
    String header_sdate6;
    //buyer
    String buyer_logisticsVal;
    String buyer_name;
    String buyer_mp;
    String buyer_address;
    //store
    String store_sname;
    String store_name;
    String store_mp;
    String store_address;
    //invoice
    String invoice_intypeVal;
    String invoice_inumber;
    String invoice_ctitle;
    String invoice_vat;
    //footer
    String footer_note;
    int footer_mdiscount;
    int footer_lpay;
    int footer_xmoney;
    int footer_ymoney;
    int footer_ewallet;
    int footer_allpay;

    public MOrderItemPojo() {

    }

    public MOrderItemPojo(int type) {
        super(type);
    }

    public String getHeader_odate() {
        return header_odate;
    }

    public void setHeader_odate(String header_odate) {
        this.header_odate = header_odate;
    }

    public String getHeader_ordernum() {
        return header_ordernum;
    }

    public void setHeader_ordernum(String header_ordernum) {
        this.header_ordernum = header_ordernum;
    }

    public String getHeader_logisticsnum() {
        return header_logisticsnum;
    }

    public void setHeader_logisticsnum(String header_logisticsnum) {
        this.header_logisticsnum = header_logisticsnum;
    }

    public int getHeader_step1() {
        return header_step1;
    }

    public void setHeader_step1(int header_step1) {
        this.header_step1 = header_step1;
    }

    public String getHeader_sdate1() {
        return header_sdate1;
    }

    public void setHeader_sdate1(String header_sdate1) {
        this.header_sdate1 = header_sdate1;
    }

    public int getHeader_step2() {
        return header_step2;
    }

    public void setHeader_step2(int header_step2) {
        this.header_step2 = header_step2;
    }

    public String getHeader_sdate2() {
        return header_sdate2;
    }

    public void setHeader_sdate2(String header_sdate2) {
        this.header_sdate2 = header_sdate2;
    }

    public int getHeader_step3() {
        return header_step3;
    }

    public void setHeader_step3(int header_step3) {
        this.header_step3 = header_step3;
    }

    public String getHeader_sdate3() {
        return header_sdate3;
    }

    public void setHeader_sdate3(String header_sdate3) {
        this.header_sdate3 = header_sdate3;
    }

    public int getHeader_step4() {
        return header_step4;
    }

    public void setHeader_step4(int header_step4) {
        this.header_step4 = header_step4;
    }

    public String getHeader_sdate4() {
        return header_sdate4;
    }

    public void setHeader_sdate4(String header_sdate4) {
        this.header_sdate4 = header_sdate4;
    }

    public int getHeader_step5() {
        return header_step5;
    }

    public void setHeader_step5(int header_step5) {
        this.header_step5 = header_step5;
    }

    public String getHeader_sdate5() {
        return header_sdate5;
    }

    public void setHeader_sdate5(String header_sdate5) {
        this.header_sdate5 = header_sdate5;
    }

    public int getHeader_step6() {
        return header_step6;
    }

    public void setHeader_step6(int header_step6) {
        this.header_step6 = header_step6;
    }

    public String getHeader_sdate6() {
        return header_sdate6;
    }

    public void setHeader_sdate6(String header_sdate6) {
        this.header_sdate6 = header_sdate6;
    }

    public String getBuyer_logisticsVal() {
        return buyer_logisticsVal;
    }

    public void setBuyer_logisticsVal(String buyer_logisticsVal) {
        this.buyer_logisticsVal = buyer_logisticsVal;
    }

    public String getBuyer_name() {
        return buyer_name;
    }

    public void setBuyer_name(String buyer_name) {
        this.buyer_name = buyer_name;
    }

    public String getBuyer_mp() {
        return buyer_mp;
    }

    public void setBuyer_mp(String buyer_mp) {
        this.buyer_mp = buyer_mp;
    }

    public String getBuyer_address() {
        return buyer_address;
    }

    public void setBuyer_address(String buyer_address) {
        this.buyer_address = buyer_address;
    }

    public String getStore_sname() {
        return store_sname;
    }

    public void setStore_sname(String store_sname) {
        this.store_sname = store_sname;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_mp() {
        return store_mp;
    }

    public void setStore_mp(String store_mp) {
        this.store_mp = store_mp;
    }

    public String getStore_address() {
        return store_address;
    }

    public void setStore_address(String store_address) {
        this.store_address = store_address;
    }

    public String getInvoice_intypeVal() {
        return invoice_intypeVal;
    }

    public void setInvoice_intypeVal(String invoice_intypeVal) {
        this.invoice_intypeVal = invoice_intypeVal;
    }

    public String getInvoice_inumber() {
        return invoice_inumber;
    }

    public void setInvoice_inumber(String invoice_inumber) {
        this.invoice_inumber = invoice_inumber;
    }

    public String getInvoice_ctitle() {
        return invoice_ctitle;
    }

    public void setInvoice_ctitle(String invoice_ctitle) {
        this.invoice_ctitle = invoice_ctitle;
    }

    public String getInvoice_vat() {
        return invoice_vat;
    }

    public void setInvoice_vat(String invoice_vat) {
        this.invoice_vat = invoice_vat;
    }

    public String getFooter_note() {
        return footer_note;
    }

    public void setFooter_note(String footer_note) {
        this.footer_note = footer_note;
    }

    public int getFooter_mdiscount() {
        return footer_mdiscount;
    }

    public void setFooter_mdiscount(int footer_mdiscount) {
        this.footer_mdiscount = footer_mdiscount;
    }

    public int getFooter_lpay() {
        return footer_lpay;
    }

    public void setFooter_lpay(int footer_lpay) {
        this.footer_lpay = footer_lpay;
    }

    public int getFooter_xmoney() {
        return footer_xmoney;
    }

    public void setFooter_xmoney(int footer_xmoney) {
        this.footer_xmoney = footer_xmoney;
    }

    public int getFooter_ymoney() {
        return footer_ymoney;
    }

    public void setFooter_ymoney(int footer_ymoney) {
        this.footer_ymoney = footer_ymoney;
    }

    public int getFooter_ewallet() {
        return footer_ewallet;
    }

    public void setFooter_ewallet(int footer_ewallet) {
        this.footer_ewallet = footer_ewallet;
    }

    public int getFooter_allpay() {
        return footer_allpay;
    }

    public void setFooter_allpay(int footer_allpay) {
        this.footer_allpay = footer_allpay;
    }


}
