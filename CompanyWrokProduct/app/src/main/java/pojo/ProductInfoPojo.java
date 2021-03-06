package pojo;

/**
 * OBJECT实现SERIALIZABLE
 *
 * @author bixiaopeng 2013-2-18 上午11:32:19
 */
public class ProductInfoPojo
        //implements Serializable
{
    private String title;
    private String image;
    private String pno;
    private String descs;
    private String content;
    private String rprice;
    private String rsprice;
    private String isnew;
    private String ishot;
    private String istime;
    private String discount;
    private String shipping;
    private int score;
    private boolean favorite;
    private String rpolicy;


    public ProductInfoPojo() {
    }

    public ProductInfoPojo(String title, String image, String pno, String descs, String rprice, String rsprice, String isnew, String ishot, String istime, String discount, String shipping, boolean favorite, int score) {
        this.title = title;
        this.image = image;
        this.pno = pno;
        this.descs = descs;
        this.rprice = rprice;
        this.rsprice = rsprice;
        this.isnew = isnew;
        this.ishot = ishot;
        this.istime = istime;
        this.discount = discount;
        this.shipping = shipping;
        this.favorite = favorite;
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPno() {
        return pno;
    }

    public void setPno(String pno) {
        this.pno = pno;
    }

    public String getDescs() {
        return descs;
    }

    public void setDescs(String descs) {
        this.descs = descs;
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

    public String getIsnew() {
        return isnew;
    }

    public void setIsnew(String isnew) {
        this.isnew = isnew;
    }

    public String getIshot() {
        return ishot;
    }

    public void setIshot(String ishot) {
        this.ishot = ishot;
    }

    public String getIstime() {
        return istime;
    }

    public void setIstime(String istime) {
        this.istime = istime;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    public boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public String getRpolicy() {
        return rpolicy;
    }

    public void setRpolicy(String rpolicy) {
        this.rpolicy = rpolicy;
    }

}

