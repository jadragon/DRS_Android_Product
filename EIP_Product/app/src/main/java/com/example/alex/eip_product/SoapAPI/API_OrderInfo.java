package com.example.alex.eip_product.SoapAPI;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class API_OrderInfo extends SoapSetting {
    private static String METHOD_NAME = "GetCheckOrders";//方法名
    private static String NAMESPACE = "http://tempuri.org/";//命名空間

    public JSONObject getOrderInfo() throws JSONException, XmlPullParserException, IOException {
//            创建soapObject,即拼装soap bodyin
        SoapObject  request = new SoapObject(NAMESPACE, NAMESPACE + METHOD_NAME);
//            添加传入参数，根据具体格式测试
        request.addProperty("username", "test01");
        request.addProperty("pw", "Test123456");
//            创建soap 数据
        return sendAPI(request, NAMESPACE + METHOD_NAME);
    }
}
