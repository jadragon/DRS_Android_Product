package com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.test.tw.wrokproduct.R;
import com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.pojo.MOrderItemContentPojo;
import com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.pojo.MOrderItemPojo;

import org.json.JSONObject;

import java.util.ArrayList;

import Util.StringUtil;
import library.AnalyzeJSON.AnalyzeOrderInfo;
import library.GetJsonData.OrderInfoJsonData;
import library.GetJsonData.StoreJsonData;

public class OrderInfoDetailActivity extends AppCompatActivity {
    private LinearLayout orderinfo_detail_prolayout;
    private String type, mono, token;
    private JSONObject json;
    private MOrderItemPojo mOrderItemPojo;
    private ArrayList<MOrderItemContentPojo> pojoArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info_detail);
        type = getIntent().getStringExtra("type");
        mono = getIntent().getStringExtra("mono");
        token = getIntent().getStringExtra("token");
        initToolbar();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (type.equals("0"))
                    json = new OrderInfoJsonData().getMOrderItem(token, mono);
                else if (type.equals("1"))
                    json = new StoreJsonData().getSOrderItem(token, mono);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mOrderItemPojo = AnalyzeOrderInfo.getMOrderItem(json);
                        pojoArrayList = AnalyzeOrderInfo.getMOrderItemContent(json);
                        initHeader();
                        initBuyer();
                        initStore();
                        initInvoice();
                        initFooter();
                        initContent();
                    }
                });
            }
        }).start();

        // initProduct();
    }

    private void initToolbar() {
        //Toolbar 建立
        Toolbar toolbar = findViewById(R.id.include_toolbar);
        ((TextView) findViewById(R.id.include_toolbar_title)).setText("定單詳情");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initHeader() {
        ((TextView) findViewById(R.id.orderinfo_detail_header_odate)).setText(mOrderItemPojo.getHeader_odate());
        ((TextView) findViewById(R.id.orderinfo_detail_header_ordernum)).setText(mOrderItemPojo.getHeader_ordernum());
        ((TextView) findViewById(R.id.orderinfo_detail_header_logisticsnum)).setText(mOrderItemPojo.getHeader_logisticsnum());
        ((TextView) findViewById(R.id.orderinfo_detail_header_sdate1)).setText(mOrderItemPojo.getHeader_sdate1());
        ((TextView) findViewById(R.id.orderinfo_detail_header_sdate2)).setText(mOrderItemPojo.getHeader_sdate2());
        ((TextView) findViewById(R.id.orderinfo_detail_header_sdate3)).setText(mOrderItemPojo.getHeader_sdate3());
        ((TextView) findViewById(R.id.orderinfo_detail_header_sdate4)).setText(mOrderItemPojo.getHeader_sdate4());
        ((TextView) findViewById(R.id.orderinfo_detail_header_sdate5)).setText(mOrderItemPojo.getHeader_sdate5());
        ((TextView) findViewById(R.id.orderinfo_detail_header_sdate6)).setText(mOrderItemPojo.getHeader_sdate6());
        //step
        ((ImageView) findViewById(R.id.orderinfo_detail_header_step1)).setImageResource(getResources().obtainTypedArray(R.array.step1).getResourceId(mOrderItemPojo.getHeader_step1(), 0));
        ((ImageView) findViewById(R.id.orderinfo_detail_header_step2)).setImageResource(getResources().obtainTypedArray(R.array.step2).getResourceId(mOrderItemPojo.getHeader_step2(), 0));
        ((ImageView) findViewById(R.id.orderinfo_detail_header_step3)).setImageResource(getResources().obtainTypedArray(R.array.step3).getResourceId(mOrderItemPojo.getHeader_step3(), 0));
        ((ImageView) findViewById(R.id.orderinfo_detail_header_step4)).setImageResource(getResources().obtainTypedArray(R.array.step4).getResourceId(mOrderItemPojo.getHeader_step4(), 0));
        ((ImageView) findViewById(R.id.orderinfo_detail_header_step5)).setImageResource(getResources().obtainTypedArray(R.array.step5).getResourceId(mOrderItemPojo.getHeader_step5(), 0));
        ((ImageView) findViewById(R.id.orderinfo_detail_header_step6)).setImageResource(getResources().obtainTypedArray(R.array.step6).getResourceId(mOrderItemPojo.getHeader_step6(), 0));
    }

    private void initBuyer() {
        ((TextView) findViewById(R.id.orderinfo_detail_buyer_logisticsVal)).setText(mOrderItemPojo.getBuyer_logisticsVal());
        ((TextView) findViewById(R.id.orderinfo_detail_buyer_name)).setText(mOrderItemPojo.getBuyer_name());
        ((TextView) findViewById(R.id.orderinfo_detail_buyer_mp)).setText(mOrderItemPojo.getBuyer_mp());
        ((TextView) findViewById(R.id.orderinfo_detail_buyer_address)).setText(mOrderItemPojo.getBuyer_address());
    }

    private void initStore() {
        ((TextView) findViewById(R.id.orderinfo_detail_store_sname)).setText(mOrderItemPojo.getStore_sname());
        ((TextView) findViewById(R.id.orderinfo_detail_store_name)).setText(mOrderItemPojo.getStore_name());
        ((TextView) findViewById(R.id.orderinfo_detail_store_mp)).setText(mOrderItemPojo.getStore_mp());
        ((TextView) findViewById(R.id.orderinfo_detail_store_address)).setText(mOrderItemPojo.getStore_address());
    }

    private void initInvoice() {
        ((TextView) findViewById(R.id.orderinfo_detail_invoice_intypeVal)).setText(mOrderItemPojo.getInvoice_intypeVal());
        ((TextView) findViewById(R.id.orderinfo_detail_invoice_inumber)).setText(mOrderItemPojo.getInvoice_inumber());
        ((TextView) findViewById(R.id.orderinfo_detail_invoice_ctitle)).setText(mOrderItemPojo.getInvoice_ctitle());
        ((TextView) findViewById(R.id.orderinfo_detail_invoice_vat)).setText(mOrderItemPojo.getInvoice_vat());
    }

    private void initFooter() {
        ((TextView) findViewById(R.id.orderinfo_detail_footer_note)).setText(mOrderItemPojo.getFooter_note());
        ((TextView) findViewById(R.id.orderinfo_detail_footer_mdiscount)).setText("$" + StringUtil.getDeciamlString(mOrderItemPojo.getFooter_mdiscount()));
        ((TextView) findViewById(R.id.orderinfo_detail_footer_lpay)).setText("$" + StringUtil.getDeciamlString(mOrderItemPojo.getFooter_lpay()));
        ((TextView) findViewById(R.id.orderinfo_detail_footer_xmoney)).setText("$" + StringUtil.getDeciamlString(mOrderItemPojo.getFooter_xmoney()));
        ((TextView) findViewById(R.id.orderinfo_detail_footer_ymoney)).setText("$" + StringUtil.getDeciamlString(mOrderItemPojo.getFooter_ymoney()));
        ((TextView) findViewById(R.id.orderinfo_detail_footer_ewallet)).setText("$" + StringUtil.getDeciamlString(mOrderItemPojo.getFooter_ewallet()));
        ((TextView) findViewById(R.id.orderinfo_detail_footer_allpay)).setText("$" + StringUtil.getDeciamlString(mOrderItemPojo.getFooter_allpay()));
    }

    private void initContent() {
        orderinfo_detail_prolayout = findViewById(R.id.orderinfo_detail_prolayout);
        for (int i = 0; i < pojoArrayList.size(); i++) {
            View view = View.inflate(OrderInfoDetailActivity.this, R.layout.viewitem_orderinfo_content, null);
            ((TextView) view.findViewById(R.id.orderinfo_content_pname)).setText(pojoArrayList.get(i).getPname());
            ((TextView) view.findViewById(R.id.orderinfo_content_color)).setText(pojoArrayList.get(i).getColor());
            ((TextView) view.findViewById(R.id.orderinfo_content_size)).setText(pojoArrayList.get(i).getSize());
            ((TextView) view.findViewById(R.id.orderinfo_content_oiname)).setText(pojoArrayList.get(i).getOiname());
            ((TextView) view.findViewById(R.id.orderinfo_content_oiname)).setTextColor(Color.parseColor(("#" + pojoArrayList.get(i).getOicolor())));
            ((TextView) view.findViewById(R.id.orderinfo_content_price)).setText("$" + StringUtil.getDeciamlString(pojoArrayList.get(i).getPrice()));
            ((TextView) view.findViewById(R.id.orderinfo_content_price)).setPaintFlags(((TextView) view.findViewById(R.id.orderinfo_content_price)).getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            ((TextView) view.findViewById(R.id.orderinfo_content_sprice)).setText("$" + StringUtil.getDeciamlString(pojoArrayList.get(i).getSprice()));
            ((TextView) view.findViewById(R.id.orderinfo_content_stotal)).setText("X" + pojoArrayList.get(i).getStotal());
            ImageLoader.getInstance().displayImage(pojoArrayList.get(i).getPimg(), ((ImageView) view.findViewById(R.id.orderinfo_content_pimg)));
            orderinfo_detail_prolayout.addView(view);
        }
    }
}
