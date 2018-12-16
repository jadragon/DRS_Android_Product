package com.example.alex.eip_product.pojo;

import java.util.ArrayList;

public class FailItemPojo {
    public String Item;
    public ArrayList<String> ReasonCode;
    public ArrayList<String> ReasonDescr;

    public FailItemPojo(String item, ArrayList<String> reasonCode, ArrayList<String> reasonDescr) {
        Item = item;
        ReasonCode = reasonCode;
        ReasonDescr = reasonDescr;
    }
}
