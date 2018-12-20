package com.example.alex.eip_product.pojo;

import java.util.ArrayList;

public class FailItemPojo {
    public String Item;
    public String LineNumber;
    public ArrayList<String> ReasonCode;
    public ArrayList<String> ReasonDescr;

    public FailItemPojo(String LineNumber,String Item, ArrayList<String> reasonCode, ArrayList<String> reasonDescr) {
        this.LineNumber = LineNumber;
        this.Item = Item;
        this.ReasonCode = reasonCode;
        this.ReasonDescr = reasonDescr;
    }
}
