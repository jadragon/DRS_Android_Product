package pojo;

import java.io.Serializable;


/**
 * OBJECT实现SERIALIZABLE
 *
 * @author bixiaopeng 2013-2-18 上午11:32:19
 */
public class ProductInfoPojo implements Serializable {

    private String pno;
    private String pname;
    private String descs;
    private String img;
    private String rprice;
    private String rsprice;
    private String score;

    public ProductInfoPojo() {
    }

    public String getPno() {
        return pno;
    }

    public void setPno(String pno) {
        this.pno = pno;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getDescs() {
        return descs;
    }

    public void setDescs(String descs) {
        this.descs = descs;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getRprice() {
        return rprice;
    }

    public void setRprice(String rprice) {
        this.rprice = rprice;
    }

    public String getRsprice() {
        return rsprice;
    }

    public void setRsprice(String rsprice) {
        this.rsprice = rsprice;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public ProductInfoPojo(String pno, String pname, String descs, String img, String rprice, String rsprice, String score) {
        this.pno = pno;
        this.pname = pname;
        this.descs = descs;
        this.img = img;
        this.rprice = rprice;
        this.rsprice = rsprice;
        this.score = score;
    }
}

