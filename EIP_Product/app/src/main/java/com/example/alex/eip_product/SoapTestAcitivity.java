package com.example.alex.eip_product;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import android.widget.Toast;

import com.example.alex.eip_product.SoapAPI.API_OrderInfo;
import com.example.alex.eip_product.SoapAPI.Analyze.Analyze_Order;

import org.json.JSONException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import db.OrderDatabase;

public class SoapTestAcitivity extends AppCompatActivity implements View.OnClickListener {

    private Button send, count1, count2, count3, count4;
    private ProgressDialog progressDialog;
    private HandlerThread handlerThread;
    private Handler mHandler, UiHandler;
    private OrderDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soap_test);
        db = new OrderDatabase(this);
        count1 = findViewById(R.id.count1);
        count2 = findViewById(R.id.count2);
        count3 = findViewById(R.id.count3);
        count4 = findViewById(R.id.count4);
        send = findViewById(R.id.send);
        initHandler();
        send.setOnClickListener(this);
        count1.setOnClickListener(this);
        count2.setOnClickListener(this);
        count3.setOnClickListener(this);
        count4.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send:
                progressDialog = ProgressDialog.show(SoapTestAcitivity.this, "讀取資料中", "請稍後", true);
                mHandler.sendEmptyMessageDelayed(0, 0);
                break;
            case R.id.count1:
                Toast.makeText(this, db.countOrders() + "", Toast.LENGTH_SHORT).show();
                break;
            case R.id.count2:
                Toast.makeText(this, db.countOrderDetails() + "", Toast.LENGTH_SHORT).show();
                break;
            case R.id.count3:
                Toast.makeText(this, db.countOrderComments() + "", Toast.LENGTH_SHORT).show();
                break;
            case R.id.count4:
                Toast.makeText(this, db.countOrderItemComments() + "", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void initHandler() {
        UiHandler = new Handler(getMainLooper());
        handlerThread = new HandlerThread("camera");
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper(), mCallback);
    }

    Handler.Callback mCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            try {
                db.resetTables();
                Map<String, List<ContentValues>> map = Analyze_Order.getOrders(new API_OrderInfo().getOrderInfo());
                db.addOrders(map.get("Orders"));
                db.addOrderDetails(map.get("OrderDetails"));
                db.addOrderComments(map.get("OrderComments"));
                db.addOrderItemComments(map.get("OrderItemComments"));
                UiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(SoapTestAcitivity.this, "JSONException存取異常", Toast.LENGTH_SHORT).show();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
                Toast.makeText(SoapTestAcitivity.this, "XmlPullParserException存取異常", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(SoapTestAcitivity.this, "IOException存取異常", Toast.LENGTH_SHORT).show();
            } finally {
                progressDialog.dismiss();
            }
            return false;
        }
    };
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

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        db.close();
        super.onDestroy();
    }

}
