package library.AnalyzeJSON.APIpojo;

public class BrandListPojo {
    String pb_no;
    String code;
    String title;
    String img;
    String c_time;
    String u_time;
    boolean exist;

    public String getPb_no() {
        return pb_no;
    }

    public void setPb_no(String pb_no) {
        this.pb_no = pb_no;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getC_time() {
        return c_time;
    }

    public void setC_time(String c_time) {
        this.c_time = c_time;
    }

    public String getU_time() {
        return u_time;
    }

    public void setU_time(String u_time) {
        this.u_time = u_time;
    }

    public boolean isExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }
}
