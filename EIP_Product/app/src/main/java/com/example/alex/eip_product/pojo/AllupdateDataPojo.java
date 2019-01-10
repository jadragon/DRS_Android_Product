package com.example.alex.eip_product.pojo;
import org.json.JSONObject;


public class AllupdateDataPojo {
    private JSONObject jsonObject;
    private String PONumber;

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String getPONumber() {
        return PONumber;
    }

    public void setPONumber(String PONumber) {
        this.PONumber = PONumber;
    }
}
