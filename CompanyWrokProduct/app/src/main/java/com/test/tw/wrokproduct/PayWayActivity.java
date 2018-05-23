package com.test.tw.wrokproduct;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import library.AnalyzeJSON.AnalyzeShopCart;
import library.GetJsonData.ShopCartJsonData;

public class PayWayActivity extends AppCompatActivity implements TextWatcher {
    Toolbar toolbar;
    TextView toolbar_title;
    String token;
    TextView payway_activity_txt_opay, payway_activity_txt_xmoney, payway_activity_txt_ymoney, payway_activity_txt_ewallet,
            payway_activity_txt_pname1, payway_activity_txt_info1, payway_activity_txt_pname2, payway_activity_txt_info2,
            payway_activity_txt_total;
    EditText payway_activity_edit_ekeyin, payway_activity_edit_xkeyin, payway_activity_edit_ykeyin;
    JSONObject json;
    ArrayList<Map<String, String>> data_list, pay_list;
    int xtrans, ytrans;
    int opay, xmoney, xkeyin, ymoney, ykeyin, ewallet, ekeyin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payway);
        payway_activity_txt_opay = findViewById(R.id.payway_activity_txt_opay);
        payway_activity_txt_xmoney = findViewById(R.id.payway_activity_txt_xmoney);
        payway_activity_txt_ymoney = findViewById(R.id.payway_activity_txt_ymoney);
        payway_activity_txt_ewallet = findViewById(R.id.payway_activity_txt_ewallet);
        payway_activity_txt_pname1 = findViewById(R.id.payway_activity_txt_pname1);
        payway_activity_txt_info1 = findViewById(R.id.payway_activity_txt_info1);
        payway_activity_txt_pname2 = findViewById(R.id.payway_activity_txt_pname2);
        payway_activity_txt_info2 = findViewById(R.id.payway_activity_txt_info2);
        payway_activity_edit_ekeyin = findViewById(R.id.payway_activity_edit_ekeyin);
        payway_activity_edit_ekeyin.addTextChangedListener(this);
        payway_activity_edit_xkeyin = findViewById(R.id.payway_activity_edit_xkeyin);
        payway_activity_edit_ykeyin = findViewById(R.id.payway_activity_edit_ykeyin);
        payway_activity_txt_total = findViewById(R.id.payway_activity_txt_total);
        GlobalVariable gv = (GlobalVariable) getApplicationContext();
        gv.setToken("I0JN9@_fTxybt/YuH1j1Ceg==");
        token = gv.getToken();
        initToolbar();
        new Thread(new Runnable() {
            @Override
            public void run() {
                json = new ShopCartJsonData().getMemberPayments(token);
                data_list = AnalyzeShopCart.getMemberPaymentsData(json);
                pay_list = AnalyzeShopCart.getMemberPaymentsPay(json);
                xtrans = Integer.parseInt(pay_list.get(0).get("xtrans"));
                ytrans = Integer.parseInt(pay_list.get(0).get("ytrans"));
                opay = Integer.parseInt(pay_list.get(0).get("opay"));
                xmoney = Integer.parseInt(pay_list.get(0).get("xmoney"));
                ymoney = Integer.parseInt(pay_list.get(0).get("ymoney"));
                ewallet = Integer.parseInt(pay_list.get(0).get("ewallet"));
                ekeyin = Integer.parseInt(pay_list.get(0).get("ekeyin"));
                xkeyin = Integer.parseInt(pay_list.get(0).get("xkeyin"));
                ykeyin = Integer.parseInt(pay_list.get(0).get("ykeyin"));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        initText();
                    }
                });
            }
        }).start();
    }

    private void initToolbar() {
        //Toolbar 建立
        toolbar = findViewById(R.id.include_toolbar);
        toolbar_title = findViewById(R.id.include_toolbar_title);
        toolbar_title.setText("付款方式");
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initText() {
        payway_activity_txt_opay.setText(opay + "");
        payway_activity_txt_xmoney.setText(xmoney + "");
        payway_activity_txt_ymoney.setText(ymoney + "");
        payway_activity_txt_ewallet.setText(ewallet + "");
        payway_activity_txt_pname1.setText(data_list.get(0).get("pname") + "");
        payway_activity_txt_info1.setText(data_list.get(0).get("info") + "");
        payway_activity_txt_pname2.setText(data_list.get(1).get("pname") + "");
        payway_activity_txt_info2.setText(data_list.get(1).get("info") + "");
        payway_activity_edit_ekeyin.setText(ekeyin + "");
        payway_activity_edit_xkeyin.setText(xkeyin + "");
        payway_activity_edit_ykeyin.setText(ykeyin + "");
        payway_activity_txt_total.setText(opay - (xkeyin / xtrans) - (ykeyin / ytrans) - ekeyin + "");
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        ekeyin = Integer.parseInt(payway_activity_edit_ekeyin.getText().toString());
        payway_activity_txt_total.setText(opay - ekeyin + "");
    }
}
