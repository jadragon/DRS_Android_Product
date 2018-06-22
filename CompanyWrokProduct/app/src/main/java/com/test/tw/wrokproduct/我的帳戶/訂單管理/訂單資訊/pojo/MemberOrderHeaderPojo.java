package com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.pojo;

public class MemberOrderHeaderPojo extends Item {
    String ordernum;
    String odate;
    String sname;
    public MemberOrderHeaderPojo() {

    }
    public MemberOrderHeaderPojo(int type) {
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
}
