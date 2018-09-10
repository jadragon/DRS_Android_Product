package library.AnalyzeJSON.APIpojo;

public class NewBrandListPojo {
    String pt_no;
    String title;
    String code;
    byte type;
    boolean select;

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public NewBrandListPojo(byte type) {
        this.type = type;
    }

    public String getPt_no() {
        return pt_no;
    }

    public void setPt_no(String pt_no) {
        this.pt_no = pt_no;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public byte getType() {
        return type;
    }
}
