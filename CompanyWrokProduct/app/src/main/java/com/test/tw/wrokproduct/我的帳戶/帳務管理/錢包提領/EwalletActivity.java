package com.test.tw.wrokproduct.我的帳戶.帳務管理.錢包提領;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

import Util.AsyncTaskUtils;
import Util.IDataCallBack;
import library.AnalyzeJSON.AnalyzeUtil;
import library.Component.ToastMessageDialog;
import library.Component.ToolbarActivity;
import library.GetJsonData.BillJsonData;

public class EwalletActivity extends ToolbarActivity {
    private  GlobalVariable gv;
    private   TextView exchange_point, exchange_rate1;
    private  EditText exchange_edit1;
    private   Button exchange_confirm;
    private  ToastMessageDialog toastMessageDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ewallet);
        gv = (GlobalVariable) getApplicationContext();
        toastMessageDialog = new ToastMessageDialog(this);
        initToolbar(true, "錢包提領");
        initID();
        initComponent();
        setText();
        exchange_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String edit1 = exchange_edit1.getText().toString().equals("") ? "0" : exchange_edit1.getText().toString();
                if (!edit1.equals("0")) {
                    AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                        @Override
                        public JSONObject onTasking(Void... params) {
                            return new BillJsonData().getEwalletTrans(gv.getToken(), exchange_edit1.getText().toString());
                        }

                        @Override
                        public void onTaskAfter(JSONObject jsonObject) {

                            if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                exchange_edit1.setText(null);
                                setText();
                            }
                            toastMessageDialog.setMessageText(AnalyzeUtil.getMessage(jsonObject));
                            toastMessageDialog.show();

                        }
                    });

                }

            }
        });
    }

    private void initID() {
        exchange_point = findViewById(R.id.exchange_point);
        exchange_rate1 = findViewById(R.id.exchange_rate1);
        exchange_edit1 = findViewById(R.id.exchange_edit1);
        exchange_confirm = findViewById(R.id.exchange_confirm);
    }

    private void initComponent() {
        ((ImageView) findViewById(R.id.exchange_image)).setImageResource(R.mipmap.wallet_withdrawal);
        ((TextView) findViewById(R.id.exchange_title)).setText("電子錢包");
        ((TextView) findViewById(R.id.exchange_name1)).setText("帳號");
        findViewById(R.id.exchange_group2).setVisibility(View.GONE);
        exchange_confirm.setText("提領");
    }


    public void setText() {

        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public JSONObject onTasking(Void... params) {
                return new BillJsonData().getEwalletRate(gv.getToken());
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                if (AnalyzeUtil.checkSuccess(jsonObject)) {
                    try {
                        String point = jsonObject.getJSONObject("Data").getString("point");
                        exchange_point.setText(point);
                        BigDecimal rate = new BigDecimal(jsonObject.getJSONObject("Data").getString("cash_rate"));
                        exchange_rate1.setText("1:" + rate);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


            }
        });


    }


}
