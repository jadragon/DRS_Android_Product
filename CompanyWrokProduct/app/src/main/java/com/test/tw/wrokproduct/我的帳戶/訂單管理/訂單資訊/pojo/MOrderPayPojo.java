package com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.pojo;

import java.util.ArrayList;

public class MOrderPayPojo extends Item {
    private String pinfo;
    private int allpay;
    private String deadline;
    private String atm_code;
    private String atm_account;
    private ArrayList<String> ordernum;


    public MOrderPayPojo() {
    }

    public MOrderPayPojo(int type) {
        super(type);
    }

    public String getPinfo() {
        return pinfo;
    }

    public void setPinfo(String pinfo) {
        this.pinfo = pinfo;
    }

    public int getAllpay() {
        return allpay;
    }

    public void setAllpay(int allpay) {
        this.allpay = allpay;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getAtm_code() {
        return atm_code;
    }

    public void setAtm_code(String atm_code) {
        this.atm_code = atm_code;
    }

    public String getAtm_account() {
        return atm_account;
    }

    public void setAtm_account(String atm_account) {
        this.atm_account = atm_account;
    }

    public ArrayList<String> getOrdernum() {
        return ordernum;
    }

    public void setOrdernum(ArrayList<String> ordernum) {
        this.ordernum = ordernum;
    }
}
