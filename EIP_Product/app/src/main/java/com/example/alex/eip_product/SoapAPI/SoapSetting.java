package com.example.alex.eip_product.SoapAPI;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class SoapSetting {
    private static String URL = "http://www.cesan.com.tw:3001/ws/wsSCG.asmx?WSDL";//訪問地址
    private static String TAG = "SOAP";

    //送API
    JSONObject sendAPI(SoapObject request, String SOAP_ACTION) throws IOException, XmlPullParserException, JSONException {
//            创建soap 数据
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        soapEnvelope.dotNet = true;
        soapEnvelope.setOutputSoapObject(request);
        HttpTransportSE transport = new HttpTransportSE(URL);
//          soap 协议发送
        transport.call(SOAP_ACTION, soapEnvelope);
//            soap 请求完成后返回数据并转换成字符串
        SoapPrimitive  resultString = (SoapPrimitive) soapEnvelope.getResponse();
        JSONObject jsonObject = null;
        if (resultString != null) {
            jsonObject = new JSONObject(resultString + "");
            Log.e(TAG, "Result Celsius: " + resultString);
        } else {
            Log.e(TAG, "No Response!: ");
        }
        return jsonObject;
    }
}
