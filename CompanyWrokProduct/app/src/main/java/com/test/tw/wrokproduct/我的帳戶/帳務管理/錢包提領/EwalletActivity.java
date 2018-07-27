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

import library.Component.ToastMessageDialog;
import library.Component.ToolbarActivity;
import library.GetJsonData.BillJsonData;
import library.JsonDataThread;

public class EwalletActivity extends ToolbarActivity {
    GlobalVariable gv;
    TextView exchange_point, exchange_rate1;
    EditText exchange_edit1;
    Button exchange_confirm;
    ToastMessageDialog toastMessageDialog;

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
                    new JsonDataThread() {
                        @Override
                        public JSONObject getJsonData() {
                            return new BillJsonData().getEwalletTrans(gv.getToken(), exchange_edit1.getText().toString());
                        }

                        @Override
                        public void runUiThread(JSONObject json) {
                            try {
                                if (json.getBoolean("Success")) {
                                    exchange_edit1.setText(null);
                                    setText();
                                }
                                toastMessageDialog.setMessageText(json.getString("Message"));
                                toastMessageDialog.show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
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
        new JsonDataThread() {
            @Override
            public JSONObject getJsonData() {
                return new BillJsonData().getEwalletRate(gv.getToken());
            }

            @Override
            public void runUiThread(JSONObject json) {
                if (json != null) {
                    try {
                        String point = json.getJSONObject("Data").getString("point");
                        exchange_point.setText(point);
                        BigDecimal rate = new BigDecimal(json.getJSONObject("Data").getString("cash_rate"));
                        exchange_rate1.setText("1:" + rate);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

    }


}
