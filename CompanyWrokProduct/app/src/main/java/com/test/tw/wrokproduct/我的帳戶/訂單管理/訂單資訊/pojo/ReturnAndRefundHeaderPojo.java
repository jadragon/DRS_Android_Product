package com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.pojo;

public class ReturnAndRefundHeaderPojo extends Item {
    //order
    private String ordernum;
    private String odate;
    //store
    private String sname;
    private String simg;

    public ReturnAndRefundHeaderPojo() {
    }

    public ReturnAndRefundHeaderPojo(int type) {
        super(type);
    }

    public String getOrdernum() {
        return ordernum;
    }

    public void setOrdernum(String ordernum) {
        this.ordernum = ordernum;
    }

    public String getOdate() {
        return odate;
    }

    public void setOdate(String odate) {
        this.odate = odate;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getSimg() {
        return simg;
    }

    public void setSimg(String simg) {
        this.simg = simg;
    }

}
