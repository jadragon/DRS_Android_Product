package com.example.alex.eip_product;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import Utils.AsyncTaskUtils;
import Utils.CommonUtil;
import Utils.IDataCallBack;
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
import static db.OrderDatabase.KEY_ReasonCode;
import static db.OrderDatabase.KEY_ReasonDescr;
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
import static db.OrderDatabase.KEY_isOrderEdit;

public class InsepectOrderActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {
    private final static int SELECT_FAIL_REASON = 110;
    private LinearLayout courseTable, failed_item_layout;
    private Button saveButton, sendButton, cancelButton;
    private ImageView VendorInspector;
    private DisplayMetrics dm;
    private ArrayList<View> ItemList = new ArrayList<>();
    private LinePathView linePathView;
    private AlertDialog dialog;
    private TextView SalesMan, Shipping, VendorName, VendorCode, PONumber, POVersion, Inspector, InspectorDate, InspectionNumber, FeedbackPerson, FeedbackDate, VendorInspectorDate;
    private OrderDatabase db;
    private String key_ponumber;
    private ContentValues Orderslist;
    private ArrayList<ContentValues> OrderDetailslist;
    private GlobalVariable gv;
    private Bitmap bitmap;
    private Map<String, FailItemPojo> CheckFailedReasonslist = new TreeMap<>();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insepect_order);
        key_ponumber = getIntent().getStringExtra(KEY_PONumber);
        gv = (GlobalVariable) getApplicationContext();
        dm = getResources().getDisplayMetrics();
        db = new OrderDatabase(this);
        initView();
        initListener();
        initData();
        //   setAnimation(title);

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
        InspectorDate = findViewById(R.id.InspectorDate);
        InspectionNumber = findViewById(R.id.InspectionNumber);
        FeedbackPerson = findViewById(R.id.FeedbackPerson);
        FeedbackDate = findViewById(R.id.FeedbackDate);
        VendorInspectorDate = findViewById(R.id.VendorInspectorDate);

        saveButton = findViewById(R.id.save);
        sendButton = findViewById(R.id.send);
        cancelButton = findViewById(R.id.cancel);
    }

    private void initListener() {
        PONumber.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        sendButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        VendorInspector.setOnClickListener(this);
    }

    private void initData() {
        Orderslist = db.getOrdersByPONumber(key_ponumber);
        OrderDetailslist = db.getOrderDetailsByPONumber(key_ponumber);
        CheckFailedReasonslist = db.getCheckFailedReasonsMapByPONumber(key_ponumber);
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
            InspectionNumber.setText(Orderslist.getAsString(KEY_InspectionNumber));
            if (Orderslist.getAsString(KEY_Inspector).equals("")) {
                Inspector.setText(gv.getUsername());
            } else {
                Inspector.setText(Orderslist.getAsString(KEY_Inspector));
            }
            String inspectorDate = InspectorDate.getText().toString();
            String vendorInspectorDate = VendorInspectorDate.getText().toString();
            if (inspectorDate.equals("") && vendorInspectorDate.equals("")) {
                String today = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
                InspectorDate.setText(today);
                VendorInspectorDate.setText(today);
            } else {
                InspectorDate.setText(Orderslist.getAsString(KEY_InspectorDate));
                VendorInspectorDate.setText(Orderslist.getAsString(KEY_VendorInspectorDate));
            }
            byte[] bitmapdata = Orderslist.getAsByteArray(KEY_VendorInspector);
            if (bitmapdata != null) {
                bitmap = StringUtils.byteArrayToBitmap(bitmapdata);
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

    private boolean saveOrder() {
        try {
            /**
             * OrderDetails
             */
            int count = 0;
            ContentValues cv;
            ArrayList<ContentValues> arrayList1 = new ArrayList<>();
            for (View item : ItemList) {
                cv = new ContentValues();
                TextView textView = item.findViewById(R.id.row1);
                cv.put(KEY_LineNumber, textView.getText().toString());
                textView = item.findViewById(R.id.row3);
                double order_qty = textView.getText().toString().equals("") ? 0 : Double.parseDouble(textView.getText().toString());
                textView = item.findViewById(R.id.row4);
                double qty = textView.getText().toString().equals("") ? 0 : Double.parseDouble(textView.getText().toString());
                if (qty < 0 || qty > order_qty) {
                    CommonUtil.toastErrorMessage(InsepectOrderActivity.this, getResources().getString(R.string.warning), getResources().getString(R.string.qty_more_than_oderqty));
                    return false;
                }
                cv.put(KEY_Qty, qty);
                textView = item.findViewById(R.id.row7);
                int size = Integer.parseInt(textView.getText().toString());
                cv.put(KEY_Size, size);

                textView = item.findViewById(R.id.row8);
                int functions = Integer.parseInt(textView.getText().toString());
                cv.put(KEY_Functions, functions);

                textView = item.findViewById(R.id.row9);
                int surface = Integer.parseInt(textView.getText().toString());
                cv.put(KEY_Surface, surface);

                textView = item.findViewById(R.id.row10);
                int packages = Integer.parseInt(textView.getText().toString());
                cv.put(KEY_Package, packages);
                if (size + functions + surface + packages > 0) {
                    count++;
                }

                CheckBox checkBox = item.findViewById(R.id.row11);
                cv.put(KEY_MainMarK, checkBox.isChecked());
                checkBox = item.findViewById(R.id.row12);
                cv.put(KEY_SideMarK, checkBox.isChecked());
                RadioButton radioButton = item.findViewById(R.id.row14);
                cv.put(KEY_Special, radioButton.isChecked());
                radioButton = item.findViewById(R.id.row15);
                cv.put(KEY_Rework, radioButton.isChecked());
                radioButton = item.findViewById(R.id.row16);
                cv.put(KEY_Reject, radioButton.isChecked());
                //合格可出
                radioButton = item.findViewById(R.id.row13);
                //預定再驗貨日
                textView = item.findViewById(R.id.row17);
                if (!radioButton.isChecked() && textView.getText().toString().equals("")) {
                    CommonUtil.toastErrorMessage(InsepectOrderActivity.this, getResources().getString(R.string.warning), getResources().getString(R.string.unfill_recheckdate1));
                    return false;
                } else if (radioButton.isChecked() && !textView.getText().toString().equals("")) {
                    CommonUtil.toastErrorMessage(InsepectOrderActivity.this, getResources().getString(R.string.warning), getResources().getString(R.string.unfill_recheckdate2));
                    return false;
                }
                cv.put(KEY_CheckPass, radioButton.isChecked());
                cv.put(KEY_ReCheckDate, textView.getText().toString());
                textView = item.findViewById(R.id.row18);
                cv.put(KEY_Remarks, textView.getText().toString());
                arrayList1.add(cv);
            }
            /**
             *checkreason
             */
            ArrayList<ContentValues> arrayList2 = new ArrayList<>();
            if (count != CheckFailedReasonslist.keySet().size()) {
                CommonUtil.toastErrorMessage(InsepectOrderActivity.this, getResources().getString(R.string.warning), getResources().getString(R.string.unfill_recheckdate3));
                return false;
            }
            for (String key : CheckFailedReasonslist.keySet()) {
                ArrayList<String> array1 = CheckFailedReasonslist.get(key).ReasonCode;
                ArrayList<String> array2 = CheckFailedReasonslist.get(key).ReasonDescr;
                for (int i = 0; i < array1.size(); i++) {
                    cv = new ContentValues();
                    cv.put(KEY_PONumber, Orderslist.getAsString(KEY_PONumber));
                    cv.put(KEY_POVersion, Orderslist.getAsString(KEY_POVersion));
                    cv.put(KEY_Item, CheckFailedReasonslist.get(key).Item);
                    cv.put(KEY_LineNumber, CheckFailedReasonslist.get(key).LineNumber);
                    cv.put(KEY_ReasonCode, array1.get(i));
                    cv.put(KEY_ReasonDescr, array2.get(i));
                    arrayList2.add(cv);
                }
            }

            /**
             * OrderDetails
             */
            cv = new ContentValues();
            byte[] byteArray;
            if (bitmap != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byteArray = stream.toByteArray();
            } else {
                CommonUtil.toastErrorMessage(InsepectOrderActivity.this, getResources().getString(R.string.warning), getResources().getString(R.string.nosignin));
                return false;
            }
            cv.put(KEY_VendorInspector, byteArray);
            if (Inspector.getText().toString().equals("")) {
                cv.put(KEY_Inspector, gv.getUsername());
            } else {
                cv.put(KEY_Inspector, Inspector.getText().toString());
            }

            String inspectorDate = InspectorDate.getText().toString();
            String vendorInspectorDate = VendorInspectorDate.getText().toString();
            if (inspectorDate.equals("") && vendorInspectorDate.equals("")) {
                String today = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
                cv.put(KEY_InspectorDate, today);
                cv.put(KEY_VendorInspectorDate, today);
            } else {
                cv.put(KEY_InspectorDate, inspectorDate);
                cv.put(KEY_VendorInspectorDate, vendorInspectorDate);
            }
            cv.put(KEY_isOrderEdit, true);
            db.saveOrdersEditBasic(Orderslist.getAsString(KEY_PONumber), cv);
            db.saveOrderDetailsEdit(Orderslist.getAsString(KEY_PONumber), arrayList1);
            db.saveCheckFailedReasonsEdit(arrayList2, key_ponumber);
            return true;
        } catch (Exception e) {
            CommonUtil.toastErrorMessage(InsepectOrderActivity.this, getResources().getString(R.string.warning), getResources().getString(R.string.savefail));
            return false;
        }

    }

    // TODO: 2018/12/26 當更新完不再顯示
    private void cancel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.table_button1));
        builder.setMessage(getResources().getString(R.string.comfirmcancel));
        builder.setNegativeButton(getResources().getString(R.string.notsave), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
            }
        });
        builder.setNeutralButton(getResources().getString(R.string.table_button1), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton(getResources().getString(R.string.saveleft), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (saveOrder()) {
                    finish();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            /**
             * 取消
             */
            case R.id.cancel:
                cancel();
                break;
            /**
             * 儲存
             */
            case R.id.save:
                if (saveOrder()) {
                    Toast.makeText(this, getResources().getString(R.string.savesuccess), Toast.LENGTH_SHORT).show();
                }
                break;
            /**
             * 上傳
             */
            case R.id.send:
                if (saveOrder()) {
                    AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                        @Override
                        public void onTaskBefore() {
                            progressDialog = ProgressDialog.show(InsepectOrderActivity.this, getResources().getString(R.string.updating), getResources().getString(R.string.wait), true);
                        }

                        // TODO: 2018/12/26 上傳資料 需判斷欄位
                        @Override
                        public JSONObject onTasking(Void... params) {
                            try {
                                return new API_OrderInfo().updateCheckOrder(gv.getUsername(), gv.getPw(), db.getUpdateDataByPONumber(Orderslist.getAsString(KEY_PONumber)).toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (XmlPullParserException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                progressDialog.dismiss();
                            }
                            return null;
                        }

                        @Override
                        public void onTaskAfter(JSONObject jsonObject) {
                            if (jsonObject != null) {
                                if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                    db.updateOrdersUpdate(key_ponumber);
                                    Toast.makeText(InsepectOrderActivity.this, AnalyzeUtil.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                                    AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                                        @Override
                                        public void onTaskBefore() {
                                            progressDialog = ProgressDialog.show(InsepectOrderActivity.this, getResources().getString(R.string.refreshing), getResources().getString(R.string.wait), true);
                                        }

                                        @Override
                                        public JSONObject onTasking(Void... params) {
                                            try {
                                                return new API_OrderInfo().getOrderInfo(gv.getUsername(), gv.getPw());
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            } catch (XmlPullParserException e) {
                                                e.printStackTrace();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            } finally {
                                                progressDialog.dismiss();
                                            }
                                            return null;
                                        }

                                        @Override
                                        public void onTaskAfter(JSONObject jsonObject) {
                                            try {
                                                if (jsonObject != null) {
                                                    if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                                        Map<String, List<ContentValues>> map = Analyze_Order.getOrders(jsonObject, key_ponumber);
                                                        db.updateOrders(map.get("Orders"), key_ponumber);
                                                        db.updateOrderDetails(map.get("OrderDetails"), key_ponumber);
                                                        db.updateCheckFailedReasons(map.get("CheckFailedReasons"), key_ponumber);
                                                        db.updateOrderComments(map.get("OrderComments"), key_ponumber);
                                                        db.updateOrderItemComments(map.get("OrderItemComments"), key_ponumber);
                                                        startActivity(getIntent().setClass(InsepectOrderActivity.this, PreviewInsepectOrderActivity.class));
                                                        finish();
                                                    } else {
                                                        refreash(AnalyzeUtil.getMessage(jsonObject));
                                                    }
                                                } else {
                                                    refreash(null);
                                                }

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                                refreash(null);
                                            }
                                        }
                                    });
                                } else {
                                    CommonUtil.toastErrorMessage(InsepectOrderActivity.this, getResources().getString(R.string.warning), AnalyzeUtil.getMessage(jsonObject));
                                }
                            } else {
                                CommonUtil.toastErrorMessage(InsepectOrderActivity.this, getResources().getString(R.string.exception), getResources().getString(R.string.download_fail_info));
                            }
                        }
                    });

                }
                break;

            /**
             * 不合格理由
             */
            case R.id.description:
                Intent intent = new Intent(InsepectOrderActivity.this, SelectFailedActivity.class);
                int index = (int) view.getTag();
                TextView textView = failed_item_layout.getChildAt(index).findViewById(R.id.line);
                int position = Integer.parseInt(textView.getText().toString());
                intent.putExtra("LineNumber", position + "");
                View failView = ItemList.get(position - 1);
                intent.putExtra("index", index);
                textView = failView.findViewById(R.id.row2);
                intent.putExtra("Item", textView.getText().toString());
                textView = failView.findViewById(R.id.row7);
                long number = textView.getText().toString().equals("") ? 0 : Long.parseLong(textView.getText().toString());
                intent.putExtra("type1", number > 0);
                textView = failView.findViewById(R.id.row8);
                number = textView.getText().toString().equals("") ? 0 : Long.parseLong(textView.getText().toString());
                intent.putExtra("type2", number > 0);
                textView = failView.findViewById(R.id.row9);
                number = textView.getText().toString().equals("") ? 0 : Long.parseLong(textView.getText().toString());
                intent.putExtra("type3", number > 0);
                textView = failView.findViewById(R.id.row10);
                number = textView.getText().toString().equals("") ? 0 : Long.parseLong(textView.getText().toString());
                intent.putExtra("type4", number > 0);

                startActivityForResult(intent, SELECT_FAIL_REASON);
                break;
            /**
             * 採購單號
             */
            case R.id.PONumber:
                intent = new Intent(InsepectOrderActivity.this, InspectDetailActivity.class);
                intent.putExtra(KEY_PONumber, key_ponumber);
                startActivity(intent);
                break;
            /**
             * 簽名檔
             */
            case R.id.VendorInspector:
                if (linePathView == null)
                    linePathView = new LinePathView(InsepectOrderActivity.this);
                else
                    linePathView.clear();

                if (dialog == null) {
                    final int bitmapwidth = (int) (dm.heightPixels * ((float) VendorInspector.getWidth() / VendorInspector.getHeight()) / 6 * 5);
                    final int bitmapheight = (int) ((float) dm.heightPixels / 6 * 5);
                    AlertDialog.Builder builder = new AlertDialog.Builder(InsepectOrderActivity.this).setView(linePathView)
                            .setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    bitmap = linePathView.getBitMap(bitmapwidth / 5, bitmapheight / 5);
                                    VendorInspector.setImageBitmap(bitmap);
                                    dialog.dismiss();
                                }
                            }).setNegativeButton(getResources().getString(R.string.table_button1), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                    dialog = builder.create();
                    dialog.show();
                    android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
                    p.width = bitmapwidth;
                    p.height = bitmapheight;
                    dialog.getWindow().setAttributes(p);     //设置生效
                } else {
                    dialog.show();
                }

                break;
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (!b && view instanceof EditText) {
            EditText editText = ((EditText) view);
            long number = editText.getText().toString().equals("") ? 0 : Long.parseLong(editText.getText().toString());
            if (number == 0)
                editText.setText("0");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_FAIL_REASON) {
            if (resultCode == 110) {
                ArrayList<String> fail_numList = data.getStringArrayListExtra("ReasonCode");
                ArrayList<String> fail_description = data.getStringArrayListExtra("ReasonDescr");
                CheckFailedReasonslist.put(data.getStringExtra("LineNumber"), new FailItemPojo(data.getStringExtra("LineNumber"), data.getStringExtra("Item"), fail_numList, fail_description));
                ((TextView) failed_item_layout.getChildAt(data.getIntExtra("index", 0)).findViewById(R.id.description)).setText(fail_numList + "\n" + fail_description);
            }
        }
    }

    private void refreash(String errormessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(InsepectOrderActivity.this);
        builder.setTitle(getResources().getString(R.string.refresh_fail));
        if (errormessage == null) {
            builder.setMessage(getResources().getString(R.string.download_fail_info));
        } else {
            builder.setMessage(errormessage);
        }
        builder.setPositiveButton(getResources().getString(R.string.reupdate), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                    @Override
                    public void onTaskBefore() {
                        progressDialog = ProgressDialog.show(InsepectOrderActivity.this, getResources().getString(R.string.refreshing), getResources().getString(R.string.wait), true);
                    }

                    @Override
                    public JSONObject onTasking(Void... params) {
                        try {
                            return new API_OrderInfo().getOrderInfo(gv.getUsername(), gv.getPw());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            progressDialog.dismiss();
                        }
                        return null;
                    }

                    @Override
                    public void onTaskAfter(JSONObject jsonObject) {
                        try {
                            if (jsonObject != null) {
                                if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                    Map<String, List<ContentValues>> map = Analyze_Order.getOrders(jsonObject, key_ponumber);
                                    db.updateOrders(map.get("Orders"), key_ponumber);
                                    db.updateOrderDetails(map.get("OrderDetails"), key_ponumber);
                                    db.updateCheckFailedReasons(map.get("CheckFailedReasons"), key_ponumber);
                                    db.updateOrderComments(map.get("OrderComments"), key_ponumber);
                                    db.updateOrderItemComments(map.get("OrderItemComments"), key_ponumber);
                                    startActivity(getIntent().setClass(InsepectOrderActivity.this, PreviewInsepectOrderActivity.class));
                                    finish();
                                } else {
                                    refreash(AnalyzeUtil.getMessage(jsonObject));
                                }
                            } else {
                                refreash(null);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            refreash(null);
                        }
                    }
                });

            }
        });
        builder.setNegativeButton(getResources().getString(R.string.table_button1), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
            }
        });
        builder.show();
    }

    @Override
    public void onBackPressed() {
        cancel();
    }

    @Override
    protected void onDestroy() {
        if (bitmap != null)
            bitmap.recycle();
        db.close();
        super.onDestroy();
    }

    private void checkFailItems(View layout, int position) {
        EditText editText = layout.findViewById(R.id.row7);
        String Item = ((TextView) layout.findViewById(R.id.row1)).getText().toString();
        long number = editText.getText().toString().equals("") ? 0 : Long.parseLong(editText.getText().toString());
        if (number > 0) {
            if (failed_item_layout.findViewWithTag(Item) == null) {
                new AddColumTask(failed_item_layout.getChildCount(), position, Item).execute("item");
            }
        } else {
            editText = layout.findViewById(R.id.row8);
            number = editText.getText().toString().equals("") ? 0 : Long.parseLong(editText.getText().toString());
            if (number > 0) {
                if (failed_item_layout.findViewWithTag(Item) == null) {
                    new AddColumTask(failed_item_layout.getChildCount(), position, Item).execute("item");
                }
            } else {
                editText = layout.findViewById(R.id.row9);
                number = editText.getText().toString().equals("") ? 0 : Long.parseLong(editText.getText().toString());
                if (number > 0) {
                    if (failed_item_layout.findViewWithTag(Item) == null) {
                        new AddColumTask(failed_item_layout.getChildCount(), position, Item).execute("item");
                    }
                } else {
                    editText = layout.findViewById(R.id.row10);
                    number = editText.getText().toString().equals("") ? 0 : Long.parseLong(editText.getText().toString());
                    if (number > 0) {
                        if (failed_item_layout.findViewWithTag(Item) == null) {
                            new AddColumTask(failed_item_layout.getChildCount(), position, Item).execute("item");
                        }
                    } else {
                        if (failed_item_layout.findViewWithTag(Item) != null) {
                            failed_item_layout.removeView(failed_item_layout.findViewWithTag(Item));
                            /*
                            ArrayList<String> items = new ArrayList<>();
                            for (int i = 0; i < failed_item_layout.getChildCount(); i++) {
                                items.add(((TextView) failed_item_layout.getChildAt(i).findViewById(R.id.number)).getText().toString());
                            }
                            */
                            CheckFailedReasonslist.remove(Item);
                            new AddColumTask().execute("reset");
                        }
                    }
                }
            }
        }
    }

    private class AddColumTask extends AsyncTask<String, Integer, String> {
        private int index, position;
        private String tag;

        public AddColumTask() {
        }

        public AddColumTask(int index, int position, String tag) {
            this.index = index;
            this.position = position;
            this.tag = tag;
        }

        @Override
        protected String doInBackground(String... params) {
            return params[0];
        }

        @Override
        protected void onPostExecute(String result) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 3);
            if (result.equals("OrderDetails")) {
                for (int i = 0; i < OrderDetailslist.size(); i++) {
                    final int finalI = i;
                    View view = new View(InsepectOrderActivity.this);
                    params.height = (int) dm.density;
                    view.setLayoutParams(params);
                    view.setBackgroundColor(getResources().getColor(android.R.color.black));
                    courseTable.addView(view);
                    final View item_view = LayoutInflater.from(InsepectOrderActivity.this).inflate(R.layout.item_insepect_order, null, false);
                    item_view.setTag(i);
                    courseTable.addView(item_view);
                    ItemList.add(item_view);
                    TextView textView = item_view.findViewById(R.id.row1);
                    textView.setText(OrderDetailslist.get(i).getAsString(KEY_LineNumber));
                    textView = item_view.findViewById(R.id.row2);
                    textView.setText(OrderDetailslist.get(i).getAsString(KEY_Item));
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(InsepectOrderActivity.this, ListViewActivity.class);
                            intent.putExtra("PONumber", OrderDetailslist.get(finalI).getAsString(KEY_Item));
                            //intent.putExtra("PONumber", "0689D-096.DC.28");
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
                    EditText editText = item_view.findViewById(R.id.row7);
                    editText.setText(OrderDetailslist.get(i).getAsString(KEY_Size));
                    editText.setOnFocusChangeListener(InsepectOrderActivity.this);
                    editText.addTextChangedListener(new TextWatcher() {
                        View layout = item_view;

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            checkFailItems(layout, finalI);
                        }
                    });
                    editText = item_view.findViewById(R.id.row8);
                    editText.setText(OrderDetailslist.get(i).getAsString(KEY_Functions));
                    editText.setOnFocusChangeListener(InsepectOrderActivity.this);
                    editText.addTextChangedListener(new TextWatcher() {
                        View layout = item_view;

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            checkFailItems(layout, finalI);
                        }
                    });
                    editText = item_view.findViewById(R.id.row9);
                    editText.setText(OrderDetailslist.get(i).getAsString(KEY_Surface));
                    editText.setOnFocusChangeListener(InsepectOrderActivity.this);
                    editText.addTextChangedListener(new TextWatcher() {
                        View layout = item_view;

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            checkFailItems(layout, finalI);
                        }
                    });
                    editText = item_view.findViewById(R.id.row10);
                    editText.setText(OrderDetailslist.get(i).getAsString(KEY_Package));
                    editText.setOnFocusChangeListener(InsepectOrderActivity.this);
                    editText.addTextChangedListener(new TextWatcher() {
                        View layout = item_view;

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            checkFailItems(layout, finalI);
                        }
                    });

                    CheckBox checkBox = item_view.findViewById(R.id.row11);
                    checkBox.setChecked(OrderDetailslist.get(i).getAsBoolean(KEY_MainMarK));
                    checkBox = item_view.findViewById(R.id.row12);
                    checkBox.setChecked(OrderDetailslist.get(i).getAsBoolean(KEY_SideMarK));

                    boolean CheckPass = OrderDetailslist.get(i).getAsBoolean(KEY_CheckPass);
                    boolean Special = OrderDetailslist.get(i).getAsBoolean(KEY_Special);
                    boolean Rework = OrderDetailslist.get(i).getAsBoolean(KEY_Rework);
                    boolean Reject = OrderDetailslist.get(i).getAsBoolean(KEY_Reject);
                    RadioButton radioButton = item_view.findViewById(R.id.row13);
                    if (!(CheckPass & Special & Rework & Reject)) {
                        radioButton.setChecked(true);
                    } else {
                        radioButton.setChecked(CheckPass);
                    }
                    radioButton = item_view.findViewById(R.id.row14);
                    radioButton.setChecked(Special);
                    radioButton = item_view.findViewById(R.id.row15);
                    radioButton.setChecked(Rework);
                    radioButton = item_view.findViewById(R.id.row16);
                    radioButton.setChecked(Reject);

                    final TextView dateView = item_view.findViewById(R.id.row17);
                    dateView.setText(OrderDetailslist.get(i).getAsString(KEY_ReCheckDate));
                    dateView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                AlertDialog.Builder builder = new AlertDialog.Builder(InsepectOrderActivity.this);
                                final DatePicker datePicker = new DatePicker(InsepectOrderActivity.this);
                                if (!dateView.getText().toString().equals("")) {
                                    Date date = new SimpleDateFormat("yyyy/MM/dd").parse(dateView.getText().toString());
                                    datePicker.updateDate(date.getYear(), date.getMonth(), date.getDay());
                                }

                                builder.setView(datePicker);
                                builder.setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int which) {
                                        dateView.setText(datePicker.getYear() + "/" + String.format("%02d", (datePicker.getMonth() + 1)) + "/" + String.format("%02d", datePicker.getDayOfMonth()));
                                        dialogInterface.dismiss();
                                    }
                                }).setNeutralButton(getResources().getString(R.string.clear), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dateView.setText("");
                                        dialogInterface.dismiss();
                                    }
                                }).setNegativeButton(getResources().getString(R.string.table_button1), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int which) {
                                        dialogInterface.dismiss();
                                    }
                                }).show();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    textView = item_view.findViewById(R.id.row18);
                    textView.setText(OrderDetailslist.get(i).getAsString(KEY_Remarks));
                }

            } else if (result.equals("CheckFailedReasons")) {
                View view;
                int i = 0;
                for (String key : CheckFailedReasonslist.keySet()) {
                    view = LayoutInflater.from(InsepectOrderActivity.this).inflate(R.layout.item_insepect_fail, null, false);
                    FailItemPojo failItemPojo = CheckFailedReasonslist.get(key);
                    view.setTag(key);
                    TextView textView = view.findViewById(R.id.line);
                    textView.setText(CheckFailedReasonslist.get(key).LineNumber);
                    textView = view.findViewById(R.id.number);
                    textView.setText(CheckFailedReasonslist.get(key).Item);
                    textView.setOnClickListener(InsepectOrderActivity.this);
                    textView = view.findViewById(R.id.description);
                    textView.setOnClickListener(InsepectOrderActivity.this);
                    textView.setText(failItemPojo.ReasonCode + "\n" + failItemPojo.ReasonDescr);
                    textView.setTag(i);
                    failed_item_layout.addView(view);
                    i++;
                }
                /*


                 */
            } else if (result.equals("item")) {
                View view = LayoutInflater.from(InsepectOrderActivity.this).inflate(R.layout.item_insepect_fail, null, false);
                view.setTag(tag);
                TextView textView = view.findViewById(R.id.line);
                textView.setText(OrderDetailslist.get(position).getAsString(KEY_LineNumber));
                textView = view.findViewById(R.id.number);
                textView.setText(OrderDetailslist.get(position).getAsString(KEY_Item));
                textView.setOnClickListener(InsepectOrderActivity.this);
                textView = view.findViewById(R.id.description);
                textView.setOnClickListener(InsepectOrderActivity.this);
                textView.setTag(index);
                failed_item_layout.addView(view);
            } else if (result.equals("reset")) {
                for (int i = 0; i < failed_item_layout.getChildCount(); i++) {
                    failed_item_layout.getChildAt(i).findViewById(R.id.description).setTag(i);
                }
            }
        }
    }
}
