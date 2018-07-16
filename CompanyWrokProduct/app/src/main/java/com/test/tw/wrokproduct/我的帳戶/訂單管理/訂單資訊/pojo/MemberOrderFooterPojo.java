package com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.pojo;

public class MemberOrderFooterPojo extends Item {
    private String mono;
    private int ttotal;
    private int tprice;
    private int ostatus;
    private String oname;
    private String ocolor;
    private String alnum;
    private int alchk;
    private String aldate;
    private int pstatus;
    private String pname;
    private String pcolor;
    private int lstatus;
    private String lpname;
    private String lcolor;
    private String logisticsVal;
    private int lchk;
    private int istatus;
    private String iname;
    private String icolor;
    private int cpchk;
    private String cpdate;
    private String pinfo;


    public MemberOrderFooterPojo() {
    }

    public MemberOrderFooterPojo(int type) {
        super(type);
    }

    public String getMono() {
        return mono;
    }

    public void setMono(String mono) {
        this.mono = mono;
    }

    public int getTtotal() {
        return ttotal;
    }

    public void setTtotal(int ttotal) {
        this.ttotal = ttotal;
    }

    public int getTprice() {
        return tprice;
    }

    public void setTprice(int tprice) {
        this.tprice = tprice;
    }

    public int getOstatus() {
        return ostatus;
    }

    public void setOstatus(int ostatus) {
        this.ostatus = ostatus;
    }

    public String getOname() {
        return oname;
    }

    public void setOname(String oname) {
        this.oname = oname;
    }

    public String getOcolor() {
        return ocolor;
    }

    public void setOcolor(String ocolor) {
        this.ocolor = ocolor;
    }

    public int getPstatus() {
        return pstatus;
    }

    public void setPstatus(int pstatus) {
        this.pstatus = pstatus;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPcolor() {
        return pcolor;
    }

    public void setPcolor(String pcolor) {
        this.pcolor = pcolor;
    }

    public int getLstatus() {
        return lstatus;
    }

    public void setLstatus(int lstatus) {
        this.lstatus = lstatus;
    }

    public String getLpname() {
        return lpname;
    }

    public void setLpname(String lpname) {
        this.lpname = lpname;
    }

    public String getLcolor() {
        return lcolor;
    }

    public void setLcolor(String lcolor) {
        this.lcolor = lcolor;
    }

    public int getIstatus() {
        return istatus;
    }

    public void setIstatus(int istatus) {
        this.istatus = istatus;
    }

    public String getIname() {
        return iname;
    }

    public void setIname(String iname) {
        this.iname = iname;
    }

    public String getIcolor() {
        return icolor;
    }

    public void setIcolor(String icolor) {
        this.icolor = icolor;
    }

    public String getPinfo() {
        return pinfo;
    }

    public void setPinfo(String pinfo) {
        this.pinfo = pinfo;
    }

    public String getAlnum() {
        return alnum;
    }

    public void setAlnum(String alnum) {
        this.alnum = alnum;
    }

    public int getAlchk() {
        return alchk;
    }

    public void setAlchk(int alchk) {
        this.alchk = alchk;
    }

    public String getAldate() {
        return aldate;
    }

    public void setAldate(String aldate) {
        this.aldate = aldate;
    }

    public int getLchk() {
        return lchk;
    }

    public void setLchk(int lchk) {
        this.lchk = lchk;
    }

    public int getCpchk() {
        return cpchk;
    }

    public void setCpchk(int cpchk) {
        this.cpchk = cpchk;
    }

    public String getCpdate() {
        return cpdate;
    }

    public void setCpdate(String cpdate) {
        this.cpdate = cpdate;
    }

    public String getLogisticsVal() {
        return logisticsVal;
    }

    public void setLogisticsVal(String logisticsVal) {
        this.logisticsVal = logisticsVal;
    }
}
