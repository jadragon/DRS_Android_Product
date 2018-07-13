package com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.pojo;

public class MemberOrderContentPojo extends Item {
    private String pname;
    private String pimg;
    private String color;
    private String size;
    private String oiname;
    private String oicolor;
    private int price;
    private int sprice;
    private int stotal;

    public MemberOrderContentPojo() {

    }

    public MemberOrderContentPojo(int type) {
        super(type);
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPimg() {
        return pimg;
    }

    public void setPimg(String pimg) {
        this.pimg = pimg;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getOiname() {
        return oiname;
    }

    public void setOiname(String oiname) {
        this.oiname = oiname;
    }

    public String getOicolor() {
        return oicolor;
    }

    public void setOicolor(String oicolor) {
        this.oicolor = oicolor;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSprice() {
        return sprice;
    }

    public void setSprice(int sprice) {
        this.sprice = sprice;
    }

    public int getStotal() {
        return stotal;
    }

    public void setStotal(int stotal) {
        this.stotal = stotal;
    }
}
