package com.example.alex.eip_product;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.alex.eip_product.SoapAPI.API_OrderInfo;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class SoapTestAcitivity extends AppCompatActivity {

    private String TAG = "Soap";
    private EditText test_text;
    private Button send;
    private SoapPrimitive resultString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soap_test);
        test_text = findViewById(R.id.test_text);
        send = findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject=new API_OrderInfo().getOrderInfo();
                            test_text.setText(jsonObject+"");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });

    }
/*
    // 摄氏度 转 华氏温度
    public void calculate() {
        String SOAP_ACTION = "http://tempuri.org/GetCheckOrders";//命名空間+方法名
        String METHOD_NAME = "GetCheckOrders";//方法名
        String NAMESPACE = "http://tempuri.org/";//命名空間
        String URL = "http://www.cesan.com.tw:3001/ws/wsSCG.asmx?WSDL";//訪問地址

//            创建soapObject,即拼装soap bodyin
        SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
//            添加传入参数，根据具体格式测试
        Request.addProperty("username", "test01");
        Request.addProperty("pw", "Test123456");
//            创建soap 数据
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.dotNet = true;
        soapEnvelope.setOutputSoapObject(Request);
        try {
            HttpTransportSE transport = new HttpTransportSE(URL);
//          soap 协议发送
            transport.call(SOAP_ACTION, soapEnvelope);
//            soap 请求完成后返回数据并转换成字符串
          resultString=(SoapPrimitive)soapEnvelope.getResponse();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (resultString != null) {
                        try {
                            JSONObject jsonObject=new JSONObject(resultString+"");
                            test_text.setText( jsonObject + "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.e(TAG, "Result Celsius: " + resultString);
                    } else {
                        Toast.makeText(getApplicationContext(), "No Response", Toast.LENGTH_LONG).show();
                    }
                }
            });


        } catch (Exception ex) {
            Log.e(TAG, "Error: " + ex.getMessage());
        }
    }
    */
}
