package com.example.alex.eip_product;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.eip_product.SoapAPI.API_OrderInfo;
import com.example.alex.eip_product.SoapAPI.Analyze.AnalyzeUtil;
import com.example.alex.eip_product.SoapAPI.Analyze.Analyze_Order;
import com.example.alex.eip_product.pojo.FailItemPojo;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import Utils.AsyncTaskUtils;
import Utils.CommonUtil;
import Utils.StringUtils;
import db.OrderDatabase;

import static db.OrderDatabase.KEY_CheckPass;
import static db.OrderDatabase.KEY_FeedbackDate;
import static db.OrderDatabase.KEY_FeedbackPerson;
import static db.OrderDatabase.KEY_Functions;
import static db.OrderDatabase.KEY_InspectionNumber;
import static db.OrderDatabase.KEY_Inspector;
import static db.OrderDatabase.KEY_InspectorDate;
import static db.OrderDatabase.KEY_Item;
import static db.OrderDatabase.KEY_LineNumber;
import static db.OrderDatabase.KEY_MainMarK;
import static db.OrderDatabase.KEY_OrderQty;
import static db.OrderDatabase.KEY_PONumber;
import static db.OrderDatabase.KEY_POVersion;
import static db.OrderDatabase.KEY_Package;
import static db.OrderDatabase.KEY_Qty;
import static db.OrderDatabase.KEY_ReCheckDate;
import static db.OrderDatabase.KEY_Reject;
import static db.OrderDatabase.KEY_Remarks;
import static db.OrderDatabase.KEY_Rework;
import static db.OrderDatabase.KEY_SalesMan;
import static db.OrderDatabase.KEY_SampleNumber;
import static db.OrderDatabase.KEY_Shipping;
import static db.OrderDatabase.KEY_SideMarK;
import static db.OrderDatabase.KEY_Size;
import static db.OrderDatabase.KEY_Special;
import static db.OrderDatabase.KEY_Surface;
import static db.OrderDatabase.KEY_Uom;
import static db.OrderDatabase.KEY_VendorCode;
import static db.OrderDatabase.KEY_VendorInspector;
import static db.OrderDatabase.KEY_VendorInspectorDate;
import static db.OrderDatabase.KEY_VendorName;

public class PreviewInsepectOrderActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout courseTable, failed_item_layout;
    private ImageView VendorInspector;
    private TextView SalesMan, Shipping, VendorName, VendorCode, PONumber, POVersion, Inspector, InspectorDate, InspectionNumber, FeedbackPerson, FeedbackDate, VendorInspectorDate;
    private DisplayMetrics dm;
    private OrderDatabase db;
    private String key_ponumber;
    private ContentValues Orderslist;
    private ArrayList<ContentValues> OrderDetailslist;
    private Map<String, FailItemPojo> CheckFailedReasonslist = new TreeMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        key_ponumber = getIntent().getStringExtra(KEY_PONumber);
        dm = getResources().getDisplayMetrics();
        db = new OrderDatabase(this);
        setContentView(R.layout.activity_preview_insepect_order);
        initView();
        initListener();
        initData();
        //setAnimation(title);
    }

    private void initData() {
        Orderslist = db.getOrdersByPONumber(key_ponumber);
        OrderDetailslist = db.getOrderDetailsByPONumber(key_ponumber);
        CheckFailedReasonslist = db.getCheckFailedReasonsMapByPONumber(key_ponumber);
        Log.e("Orderslist", Orderslist + "");
        Log.e("OrderDetailslist", OrderDetailslist + "");
        Log.e("CheckFailedReasonslist", CheckFailedReasonslist + "");

        /**
         * Orderslist
         */
        if (Orderslist != null) {
            SalesMan.setText(Orderslist.getAsString(KEY_SalesMan));
            Shipping.setText(Orderslist.getAsString(KEY_Shipping));
            VendorName.setText(Orderslist.getAsString(KEY_VendorName));
            VendorCode.setText(Orderslist.getAsString(KEY_VendorCode));
            PONumber.setText(Orderslist.getAsString(KEY_PONumber));
            POVersion.setText(Orderslist.getAsString(KEY_POVersion));
            Inspector.setText(Orderslist.getAsString(KEY_Inspector));
            InspectionNumber.setText(Orderslist.getAsString(KEY_InspectionNumber));
            InspectorDate.setText(Orderslist.getAsString(KEY_InspectorDate));
            VendorInspectorDate.setText(Orderslist.getAsString(KEY_VendorInspectorDate));

            byte[] bitmapdata = Orderslist.getAsByteArray(KEY_VendorInspector);
            if (bitmapdata != null) {
                Bitmap bitmap = StringUtils.byteArrayToBitmap(bitmapdata);
                VendorInspector.setImageBitmap(bitmap);
            }

            FeedbackPerson.setText(Orderslist.getAsString(KEY_FeedbackPerson));
            FeedbackDate.setText(Orderslist.getAsString(KEY_FeedbackDate));
        }
        /**
         * OrderDetailslist
         */
        new AddColumTask().execute("OrderDetails");
        /**
         * CheckFailedReasonslist
         */

        if (CheckFailedReasonslist.size() > 0) {
            new AddColumTask().execute("CheckFailedReasons");
        }

    }

    private void initView() {
        courseTable = findViewById(R.id.kccx_course_table);
        VendorInspector = findViewById(R.id.VendorInspector);
        failed_item_layout = findViewById(R.id.failed_item_layout);
        SalesMan = findViewById(R.id.SalesMan);
        Shipping = findViewById(R.id.Shipping);
        VendorName = findViewById(R.id.VendorName);
        VendorCode = findViewById(R.id.VendorCode);
        PONumber = findViewById(R.id.PONumber);
        POVersion = findViewById(R.id.POVersion);
        Inspector = findViewById(R.id.Inspector);
        InspectionNumber = findViewById(R.id.InspectionNumber);
        InspectorDate = findViewById(R.id.InspectorDate);
        FeedbackPerson = findViewById(R.id.FeedbackPerson);
        FeedbackDate = findViewById(R.id.FeedbackDate);
        VendorInspectorDate = findViewById(R.id.VendorInspectorDate);
    }

    private void initListener() {
        PONumber.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            /**
             * 採購單號
             */
            case R.id.PONumber:
                Intent intent = new Intent(PreviewInsepectOrderActivity.this, InspectDetailActivity.class);
                intent.putExtra(KEY_PONumber, key_ponumber);
                startActivity(intent);
                break;

        }
    }

    public void setAnimation(View view) {
        //闪烁
        AlphaAnimation alphaAnimation1 = new AlphaAnimation(0.1f, 1.0f);
        alphaAnimation1.setDuration(1000);
        alphaAnimation1.setRepeatCount(Animation.INFINITE);
        alphaAnimation1.setRepeatMode(Animation.REVERSE);
        view.setAnimation(alphaAnimation1);
        alphaAnimation1.start();
    }

    private class AddColumTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            return params[0];
        }

        @Override
        protected void onPostExecute(String result) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 3);
            if (result.equals("OrderDetails")) {
                for (int i = 0; i < OrderDetailslist.size(); i++) {
                    View view = new View(PreviewInsepectOrderActivity.this);
                    params.height = (int) dm.density;
                    view.setLayoutParams(params);
                    view.setBackgroundColor(getResources().getColor(android.R.color.black));
                    courseTable.addView(view);
                    View item_view = LayoutInflater.from(PreviewInsepectOrderActivity.this).inflate(R.layout.item_preview_insepect, null, false);
                    courseTable.addView(item_view);
                    TextView textView = item_view.findViewById(R.id.row1);
                    textView.setText(OrderDetailslist.get(i).getAsString(KEY_LineNumber));
                    textView = item_view.findViewById(R.id.row2);
                    textView.setText(OrderDetailslist.get(i).getAsString(KEY_Item));
                    final int finalI = i;
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(PreviewInsepectOrderActivity.this, ListViewActivity.class);
                             intent.putExtra("PONumber",OrderDetailslist.get(finalI).getAsString(KEY_Item));
                           // intent.putExtra("PONumber", "0689D-096.DC.28");
                            startActivity(intent);
                        }
                    });
                    textView = item_view.findViewById(R.id.row3);
                    textView.setText(OrderDetailslist.get(i).getAsString(KEY_OrderQty));
                    textView = item_view.findViewById(R.id.row4);
                    textView.setText(OrderDetailslist.get(i).getAsString(KEY_Qty));
                    textView = item_view.findViewById(R.id.row5);
                    textView.setText(OrderDetailslist.get(i).getAsString(KEY_SampleNumber));
                    textView = item_view.findViewById(R.id.row6);
                    textView.setText(OrderDetailslist.get(i).getAsString(KEY_Uom));
                    textView = item_view.findViewById(R.id.row7);
                    textView.setText(OrderDetailslist.get(i).getAsString(KEY_Size));
                    textView = item_view.findViewById(R.id.row8);
                    textView.setText(OrderDetailslist.get(i).getAsString(KEY_Functions));
                    textView = item_view.findViewById(R.id.row9);
                    textView.setText(OrderDetailslist.get(i).getAsString(KEY_Surface));
                    textView = item_view.findViewById(R.id.row10);
                    textView.setText(OrderDetailslist.get(i).getAsString(KEY_Package));

                    CheckBox checkBox = item_view.findViewById(R.id.row11);
                    checkBox.setChecked(OrderDetailslist.get(i).getAsBoolean(KEY_MainMarK));
                    checkBox.setEnabled(false);
                    checkBox = item_view.findViewById(R.id.row12);
                    checkBox.setChecked(OrderDetailslist.get(i).getAsBoolean(KEY_SideMarK));
                    checkBox.setEnabled(false);
                    RadioButton radioButton = item_view.findViewById(R.id.row13);
                    radioButton.setChecked(OrderDetailslist.get(i).getAsBoolean(KEY_CheckPass));
                    radioButton.setEnabled(false);
                    radioButton = item_view.findViewById(R.id.row14);
                    radioButton.setChecked(OrderDetailslist.get(i).getAsBoolean(KEY_Special));
                    radioButton.setEnabled(false);
                    radioButton = item_view.findViewById(R.id.row15);
                    radioButton.setChecked(OrderDetailslist.get(i).getAsBoolean(KEY_Rework));
                    radioButton.setEnabled(false);
                    radioButton = item_view.findViewById(R.id.row16);
                    radioButton.setChecked(OrderDetailslist.get(i).getAsBoolean(KEY_Reject));
                    radioButton.setEnabled(false);
                    textView = item_view.findViewById(R.id.row17);
                    textView.setText(OrderDetailslist.get(i).getAsString(KEY_ReCheckDate));
                    textView = item_view.findViewById(R.id.row18);
                    textView.setText(OrderDetailslist.get(i).getAsString(KEY_Remarks));
                }

            } else if (result.equals("CheckFailedReasons")) {
                View view;
                for (String key : CheckFailedReasonslist.keySet()) {
                    view = LayoutInflater.from(PreviewInsepectOrderActivity.this).inflate(R.layout.item_insepect_fail, null, false);
                    FailItemPojo failItemPojo = CheckFailedReasonslist.get(key);
                    TextView textView = view.findViewById(R.id.line);
                    textView.setText(CheckFailedReasonslist.get(key).LineNumber);
                    textView = view.findViewById(R.id.number);
                    textView.setText(CheckFailedReasonslist.get(key).Item);
                    textView.setOnClickListener(PreviewInsepectOrderActivity.this);
                    textView = view.findViewById(R.id.description);
                    textView.setOnClickListener(PreviewInsepectOrderActivity.this);
                    textView.setText(failItemPojo.ReasonCode + "\n" + failItemPojo.ReasonDescr);
                    failed_item_layout.addView(view);
                }
            }
        }

    }

}
