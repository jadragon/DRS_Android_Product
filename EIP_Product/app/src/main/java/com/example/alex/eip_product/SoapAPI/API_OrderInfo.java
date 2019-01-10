package com.example.alex.eip_product.SoapAPI;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import Utils.StringUtils;

public class API_OrderInfo extends SoapSetting {
    private static String METHOD_NAME1 = "GetCheckOrders";//方法名
    private static String METHOD_NAME2 = "UpdateCheckOrder";//方法名
    private static String METHOD_NAME3 = "GetItemDrawing";//方法名
    private static String METHOD_NAME4 = "GetDrawingFile";//方法名
    private static String NAMESPACE = "http://tempuri.org/";//命名空間

    public JSONObject getOrderInfo(String username, String pw) throws JSONException, XmlPullParserException, IOException {
//            创建soapObject,即拼装soap bodyin
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);
//            添加传入参数，根据具体格式测试
        //  request.addProperty("username", "test01");
        // request.addProperty("pw", "Test123456");
        request.addProperty("username", username);
        request.addProperty("pw", pw);
        request.addProperty("secpara", StringUtils.macAddress());
        Log.e("mac",StringUtils.macAddress());
//            创建soap 数据
        return sendAPI(request, NAMESPACE + METHOD_NAME1);
    }

    public JSONObject updateCheckOrder(String username, String pw, String order) throws JSONException, XmlPullParserException, IOException {
//            创建soapObject,即拼装soap bodyin
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME2);
//            添加传入参数，根据具体格式测试
        //  request.addProperty("username", "test01");
        // request.addProperty("pw", "Test123456");
        request.addProperty("username", username);
        request.addProperty("pw", pw);
        request.addProperty("secpara", StringUtils.macAddress());
        Log.e("mac",StringUtils.macAddress());
        request.addProperty("order", order + "");
//            创建soap 数据
        return sendAPI(request, NAMESPACE + METHOD_NAME2);
    }

    public JSONObject getItemDrawing(String username, String pw) throws JSONException, XmlPullParserException, IOException {
//            创建soapObject,即拼装soap bodyin
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME3);
//            添加传入参数，根据具体格式测试
        //  request.addProperty("username", "test01");
        // request.addProperty("pw", "Test123456");
        request.addProperty("username", username);
        request.addProperty("pw", pw);
        request.addProperty("secpara", StringUtils.macAddress());
//            创建soap 数据
        return sendAPI(request, NAMESPACE + METHOD_NAME3);
    }

    public JSONObject getDrawingFile(String username, String pw) throws JSONException, XmlPullParserException, IOException {
//            创建soapObject,即拼装soap bodyin
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME4);
//            添加传入参数，根据具体格式测试
        //  request.addProperty("username", "test01");
        // request.addProperty("pw", "Test123456");
        request.addProperty("username", username);
        request.addProperty("pw", pw);
        request.addProperty("secpara", StringUtils.macAddress());
//            创建soap 数据
        return sendAPI(request, NAMESPACE + METHOD_NAME4);
    }
}
