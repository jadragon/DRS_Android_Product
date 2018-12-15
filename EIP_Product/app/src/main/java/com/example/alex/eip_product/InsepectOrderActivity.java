package com.example.alex.eip_product;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import Utils.CommonUtil;
import db.OrderDatabase;

import static db.OrderDatabase.KEY_CheckPass;
import static db.OrderDatabase.KEY_FeedbackDate;
import static db.OrderDatabase.KEY_FeedbackPerson;
import static db.OrderDatabase.KEY_Functions;
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
import static db.OrderDatabase.TYPE_EDIT;
import static db.OrderDatabase.TYPE_NORMAL;

public class InsepectOrderActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener, View.OnFocusChangeListener {
    private final static int SELECT_FAIL_REASON = 110;
    private LinearLayout courseTable, failed_item_layout;
    private Button saveButton, sendButton, editButton, cancelButton;
    private ImageView VendorInspector;
    private TextView title, pdf_view;
    private DisplayMetrics dm;
    private ArrayList<View> ItemList = new ArrayList<>();
    private LinePathView linePathView;
    private AlertDialog dialog;
    private TextView SalesMan, Shipping, VendorName, VendorCode, PONumber, POVersion, Inspector, InspectorDate, FeedbackPerson, FeedbackDate, VendorInspectorDate;
    private OrderDatabase db;
    private String key_ponumber;
    private ContentValues Orderslist;
    private ArrayList<ContentValues> OrderDetailslist, CheckFailedReasonslist;
    private GlobalVariable gv;
    private Bitmap bitmap;
    private Map<String, FailItemPojo> checkreason = new HashMap<>();

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
        setAnimation(title);

    }

    private void initView() {
        courseTable = findViewById(R.id.kccx_course_table);
        VendorInspector = findViewById(R.id.VendorInspector);
        title = findViewById(R.id.title);
        failed_item_layout = findViewById(R.id.failed_item_layout);
        //
        View view = LayoutInflater.from(InsepectOrderActivity.this).inflate(R.layout.item_insepect_fail, null, false);
        view.setTag(0);
        failed_item_layout.addView(view);
        //
        SalesMan = findViewById(R.id.SalesMan);
        Shipping = findViewById(R.id.Shipping);
        VendorName = findViewById(R.id.VendorName);
        VendorCode = findViewById(R.id.VendorCode);
        PONumber = findViewById(R.id.PONumber);
        POVersion = findViewById(R.id.POVersion);
        Inspector = findViewById(R.id.Inspector);
        InspectorDate = findViewById(R.id.InspectorDate);
        FeedbackPerson = findViewById(R.id.FeedbackPerson);
        FeedbackDate = findViewById(R.id.FeedbackDate);
        VendorInspectorDate = findViewById(R.id.VendorInspectorDate);

        saveButton = findViewById(R.id.save);
        sendButton = findViewById(R.id.send);
        editButton = findViewById(R.id.edit);
        cancelButton = findViewById(R.id.cancel);
        pdf_view = findViewById(R.id.pdf_view);
    }

    private void initListener() {
        PONumber.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        sendButton.setOnClickListener(this);
        editButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        pdf_view.setOnClickListener(this);
        VendorInspector.setOnClickListener(this);
    }

    private void initData() {
        Orderslist = db.getOrdersByPONumber(key_ponumber);
        OrderDetailslist = db.getOrderDetailsByPONumber(key_ponumber);
        CheckFailedReasonslist = db.getCheckFailedReasonsByPONumber(key_ponumber);
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

            if (!Orderslist.getAsString(KEY_Inspector).equals("")) {
                Inspector.setText(Orderslist.getAsString(KEY_Inspector));
                InspectorDate.setText(Orderslist.getAsString(KEY_InspectorDate));
            }

            byte[] bitmapdata = Orderslist.getAsByteArray(KEY_VendorInspector);
            if (bitmapdata != null) {
                bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
                VendorInspector.setImageBitmap(bitmap);
            }
            if (!Orderslist.getAsString(KEY_VendorInspectorDate).equals("")) {
                VendorInspectorDate.setText(Orderslist.getAsString(KEY_VendorInspectorDate));
            }
            FeedbackPerson.setText(Orderslist.getAsString(KEY_FeedbackPerson));
            FeedbackDate.setText(Orderslist.getAsString(KEY_FeedbackDate));
        }
        /**
         * OrderDetailslist
         */
        for (int i = 0; i < OrderDetailslist.size(); i++) {
            new AddColumTask(i).execute("OrderDetails");
        }
        /**
         * CheckFailedReasonslist
         */
        Log.e("CheckFailedReasonslist", CheckFailedReasonslist.size() + "\n" + CheckFailedReasonslist);
    }

    private void saveOrder() {
        ContentValues cv = new ContentValues();

        byte[] byteArray = null;
        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray = stream.toByteArray();
        } else {
            Toast.makeText(InsepectOrderActivity.this, "簽名檔沒簽", Toast.LENGTH_SHORT).show();
            return;
        }
        cv.put(KEY_VendorInspector, byteArray);
        if (Inspector.getText().toString().equals("")) {
            cv.put(KEY_Inspector, gv.getUsername());
        } else {
            cv.put(KEY_Inspector, Inspector.getText().toString());
        }
        String today = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        cv.put(KEY_InspectorDate, today);
        cv.put(KEY_VendorInspectorDate, today);
        cv.put(KEY_isOrderEdit, true);
        db.saveOrdersEditBasic(Orderslist.getAsString(KEY_PONumber), cv);
        ArrayList<ContentValues> arrayList = new ArrayList<>();
        for (View item : ItemList) {
            cv = new ContentValues();
            TextView textView = item.findViewById(R.id.row1);
            cv.put(KEY_LineNumber, textView.getText().toString());
            textView = item.findViewById(R.id.row7);
            cv.put(KEY_Size, textView.getText().toString());
            textView = item.findViewById(R.id.row8);
            cv.put(KEY_Functions, textView.getText().toString());
            textView = item.findViewById(R.id.row9);
            cv.put(KEY_Surface, textView.getText().toString());
            textView = item.findViewById(R.id.row10);
            cv.put(KEY_Package, textView.getText().toString());
            CheckBox checkBox = item.findViewById(R.id.row11);
            cv.put(KEY_MainMarK, checkBox.isChecked());
            checkBox = item.findViewById(R.id.row12);
            cv.put(KEY_SideMarK, checkBox.isChecked());
            RadioButton radioButton = item.findViewById(R.id.row13);
            cv.put(KEY_CheckPass, radioButton.isChecked());
            radioButton = item.findViewById(R.id.row14);
            cv.put(KEY_Special, radioButton.isChecked());
            radioButton = item.findViewById(R.id.row15);
            cv.put(KEY_Rework, radioButton.isChecked());
            radioButton = item.findViewById(R.id.row16);
            cv.put(KEY_Reject, radioButton.isChecked());
            textView = item.findViewById(R.id.row17);
            cv.put(KEY_ReCheckDate, textView.getText().toString());
            textView = item.findViewById(R.id.row18);
            cv.put(KEY_Remarks, textView.getText().toString());
            arrayList.add(cv);
        }
        db.saveOrderDetailsEdit(Orderslist.getAsString(KEY_PONumber), arrayList);
        /**
         *checkreason
         */
        arrayList = new ArrayList<>();
        for (String key : checkreason.keySet()) {
            ArrayList<String> array1 = checkreason.get(key).ReasonCode;
            ArrayList<String> array2 = checkreason.get(key).ReasonDescr;
            for (int i = 0; i < array1.size(); i++) {
                cv = new ContentValues();
                cv.put(KEY_PONumber, Orderslist.getAsString(KEY_PONumber));
                cv.put(KEY_POVersion, Orderslist.getAsString(KEY_POVersion));
                cv.put(KEY_Item, key);
                cv.put(KEY_ReasonCode, array1.get(i));
                cv.put(KEY_ReasonDescr, array2.get(i));
                arrayList.add(cv);
            }
        }
        db.saveCheckFailedReasonsEdit(arrayList);
    }

    private void cancel() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("取消");
        builder.setMessage("確定要取消驗貨嗎?");
        builder.setNegativeButton("不儲存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
            }
        });
        builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("儲存後離開", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                saveOrder();
                finish();
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
                saveOrder();
                break;
            /**
             * 上傳
             */
            case R.id.send:

                break;
            /**
             * 編輯
             */
            case R.id.edit:

                break;

            /**
             * PDF
             */
            case R.id.number:
                startActivity(new Intent(InsepectOrderActivity.this, PDFFromServerActivity.class));
                break;

            /**
             * 不合格理由
             */
            case R.id.description:
                Intent intent = new Intent(InsepectOrderActivity.this, SelectFailedActivity.class);
                int index = (int) view.getTag();
                View failView = ItemList.get(index);
                intent.putExtra("index", index);
                TextView textView = failView.findViewById(R.id.row2);
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
             * PDF
             */
            case R.id.pdf_view:
                startActivity(new Intent(InsepectOrderActivity.this, PDFFromServerActivity.class));
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
                    final int bitmapwidth = VendorInspector.getWidth();
                    final int bitmapheight = VendorInspector.getHeight();
                    AlertDialog.Builder builder = new AlertDialog.Builder(InsepectOrderActivity.this).setView(linePathView)
                            .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    bitmap = linePathView.getBitMap(VendorInspector.getWidth(), VendorInspector.getHeight());
                                    VendorInspector.setImageBitmap(bitmap);
                                    dialog.dismiss();
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                    dialog = builder.create();
                    dialog.show();
                    android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
                    p.width = (int) (dm.heightPixels * ((float) bitmapwidth / bitmapheight) / 6 * 5);
                    p.height = (int) ((float) dm.heightPixels / 6 * 5);
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

    //監聽品質不良品數Start
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        checkFailItems();
    }

    private void checkFailItems() {
        for (View view : ItemList) {
            int index = (int) view.getTag();
            EditText editText = view.findViewById(R.id.row7);
            long number = editText.getText().toString().equals("") ? 0 : Long.parseLong(editText.getText().toString());
            if (number > 0) {
                if (index == 0) {
                    new AddColumTask(index).execute("edit");
                } else {
                    if (failed_item_layout.findViewWithTag(index) == null)
                        new AddColumTask(index).execute("item");
                }
            } else {
                editText = view.findViewById(R.id.row8);
                number = editText.getText().toString().equals("") ? 0 : Long.parseLong(editText.getText().toString());
                if (number > 0) {

                    if (index == 0) {
                        new AddColumTask(index).execute("edit");
                    } else {
                        if (failed_item_layout.findViewWithTag(index) == null)
                            new AddColumTask(index).execute("item");
                    }
                } else {
                    editText = view.findViewById(R.id.row9);
                    number = editText.getText().toString().equals("") ? 0 : Long.parseLong(editText.getText().toString());
                    if (number > 0) {

                        if (index == 0) {
                            new AddColumTask(index).execute("edit");
                        } else {
                            if (failed_item_layout.findViewWithTag(index) == null)
                                new AddColumTask(index).execute("item");
                        }
                    } else {
                        editText = view.findViewById(R.id.row10);
                        number = editText.getText().toString().equals("") ? 0 : Long.parseLong(editText.getText().toString());
                        if (number > 0) {
                            if (index == 0) {
                                new AddColumTask(index).execute("edit");
                            } else {
                                if (failed_item_layout.findViewWithTag(index) == null)
                                    new AddColumTask(index).execute("item");
                            }
                        } else {
                            if (index == 0) {
                                new AddColumTask(-1).execute("edit");
                            } else {
                                if (failed_item_layout.findViewWithTag(index) != null)
                                    failed_item_layout.removeView(failed_item_layout.findViewWithTag(index));
                            }
                        }
                    }
                }
            }

        }
    }

    //監聽品質不良品數End

    public void setAnimation(View view) {
        //闪烁
        AlphaAnimation alphaAnimation1 = new AlphaAnimation(0.1f, 1.0f);
        alphaAnimation1.setDuration(1000);
        alphaAnimation1.setRepeatCount(Animation.INFINITE);
        alphaAnimation1.setRepeatMode(Animation.REVERSE);
        view.setAnimation(alphaAnimation1);
        alphaAnimation1.start();
    }

    //save as picture
    public void savePicture(View v) {
        //儲存在SD卡
        /*
        try {
            File newFile = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "test.png");
            newFile.getParentFile().mkdirs();
            try {
                FileOutputStream out = new FileOutputStream(newFile);
                Bitmap vBitmap = convertViewToBitmap(v);
                vBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception x) {
            x.printStackTrace();
        }

        //儲存在資料庫
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap vBitmap = CommonUtil.convertViewToBitmap(v);
        vBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bitmapByte = baos.toByteArray();
        SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(InsepectOrderActivity.this);
        db.addImage(bitmapByte);
        db.close();
        */
    }

    public void saveOrderData() {
        //儲存在SD卡
        /*
        try {
            File newFile = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "test.png");
            newFile.getParentFile().mkdirs();
            try {
                FileOutputStream out = new FileOutputStream(newFile);
                Bitmap vBitmap = convertViewToBitmap(v);
                vBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception x) {
            x.printStackTrace();
        }

        //儲存在資料庫
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap vBitmap = CommonUtil.convertViewToBitmap(v);
        vBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bitmapByte = baos.toByteArray();
        SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(InsepectOrderActivity.this);
        db.addImage(bitmapByte);
        db.close();
        */
    }

    private void toastCheckWIFI() {
        if (!CommonUtil.checkWIFI(InsepectOrderActivity.this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("請確認網路是否為連線狀態?");
            builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (!CommonUtil.checkWIFI(InsepectOrderActivity.this)) {
                        toastCheckWIFI();
                    } else {

                    }
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        } else {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_FAIL_REASON) {
            if (resultCode == 110) {
                ArrayList<String> fail_numList = data.getStringArrayListExtra("ReasonCode");
                ArrayList<String> fail_description = data.getStringArrayListExtra("ReasonDescr");
                checkreason.put(data.getStringExtra("Item"), new FailItemPojo(data.getStringExtra("Item"), fail_numList, fail_description));
                ((TextView) failed_item_layout.findViewWithTag(data.getIntExtra("index", 0)).findViewById(R.id.description)).setText(fail_numList + "\n" + fail_description);
            }
        }
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

    private class FailItemPojo {
        private String Item;
        private ArrayList<String> ReasonCode;
        private ArrayList<String> ReasonDescr;

        public FailItemPojo(String item, ArrayList<String> reasonCode, ArrayList<String> reasonDescr) {
            Item = item;
            ReasonCode = reasonCode;
            ReasonDescr = reasonDescr;
        }
    }

    private class AddColumTask extends AsyncTask<String, Integer, String> {
        int index;

        public AddColumTask(int index) {
            this.index = index;
        }

        @Override
        protected String doInBackground(String... params) {
            return params[0];
        }

        @Override
        protected void onPostExecute(String result) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 3);
            if (result.equals("OrderDetails")) {
                View view = null;
                if (courseTable.getChildCount() % 3 == 0) {
                    view = new View(InsepectOrderActivity.this);
                    view.setLayoutParams(params);
                    view.setBackgroundColor(getResources().getColor(android.R.color.black));
                    courseTable.addView(view);
                } else {
                    view = new View(InsepectOrderActivity.this);
                    params.height = 1;
                    view.setLayoutParams(params);
                    view.setBackgroundColor(getResources().getColor(android.R.color.black));
                    courseTable.addView(view);
                }
                view = LayoutInflater.from(InsepectOrderActivity.this).inflate(R.layout.item_insepect_order, null, false);
                view.setTag(index);
                courseTable.addView(view);
                ItemList.add(view);
                TextView textView = view.findViewById(R.id.row1);
                textView.setText(OrderDetailslist.get(index).getAsString(KEY_LineNumber));
                textView = view.findViewById(R.id.row2);
                textView.setText(OrderDetailslist.get(index).getAsString(KEY_Item));
                textView = view.findViewById(R.id.row3);
                textView.setText(OrderDetailslist.get(index).getAsString(KEY_OrderQty));
                textView = view.findViewById(R.id.row4);
                textView.setText(OrderDetailslist.get(index).getAsString(KEY_Qty));
                textView = view.findViewById(R.id.row5);
                textView.setText(OrderDetailslist.get(index).getAsString(KEY_SampleNumber));
                textView = view.findViewById(R.id.row6);
                textView.setText(OrderDetailslist.get(index).getAsString(KEY_Uom));
                EditText editText = view.findViewById(R.id.row7);
                editText.setText(OrderDetailslist.get(index).getAsString(KEY_Size));
                editText.setOnFocusChangeListener(InsepectOrderActivity.this);
                editText.addTextChangedListener(InsepectOrderActivity.this);
                editText = view.findViewById(R.id.row8);
                editText.setText(OrderDetailslist.get(index).getAsString(KEY_Functions));
                editText.setOnFocusChangeListener(InsepectOrderActivity.this);
                editText.addTextChangedListener(InsepectOrderActivity.this);
                editText = view.findViewById(R.id.row9);
                editText.setText(OrderDetailslist.get(index).getAsString(KEY_Surface));
                editText.setOnFocusChangeListener(InsepectOrderActivity.this);
                editText.addTextChangedListener(InsepectOrderActivity.this);
                editText = view.findViewById(R.id.row10);
                editText.setText(OrderDetailslist.get(index).getAsString(KEY_Package));
                editText.setOnFocusChangeListener(InsepectOrderActivity.this);
                editText.addTextChangedListener(InsepectOrderActivity.this);

                CheckBox checkBox = view.findViewById(R.id.row11);
                checkBox.setChecked(OrderDetailslist.get(index).getAsBoolean(KEY_MainMarK));
                checkBox = view.findViewById(R.id.row12);
                checkBox.setChecked(OrderDetailslist.get(index).getAsBoolean(KEY_SideMarK));
                RadioButton radioButton = view.findViewById(R.id.row13);
                radioButton.setChecked(OrderDetailslist.get(index).getAsBoolean(KEY_CheckPass));
                radioButton = view.findViewById(R.id.row14);
                radioButton.setChecked(OrderDetailslist.get(index).getAsBoolean(KEY_Special));
                radioButton = view.findViewById(R.id.row15);
                radioButton.setChecked(OrderDetailslist.get(index).getAsBoolean(KEY_Rework));
                radioButton = view.findViewById(R.id.row16);
                radioButton.setChecked(OrderDetailslist.get(index).getAsBoolean(KEY_Reject));

                textView = view.findViewById(R.id.row17);
                textView.setText(OrderDetailslist.get(index).getAsString(KEY_ReCheckDate));
                textView = view.findViewById(R.id.row18);
                textView.setText(OrderDetailslist.get(index).getAsString(KEY_Remarks));

            } else if (result.equals("CheckFailedReasons")) {
                View view = LayoutInflater.from(InsepectOrderActivity.this).inflate(R.layout.item_insepect_fail, null, false);
                view.setTag(index);
                TextView textView = view.findViewById(R.id.line);
                textView.setText(CheckFailedReasonslist.get(index).getAsString(KEY_LineNumber));
                textView = view.findViewById(R.id.number);
                textView.setText(CheckFailedReasonslist.get(index).getAsString(KEY_Item));
                textView.setOnClickListener(InsepectOrderActivity.this);
                textView = view.findViewById(R.id.description);
                textView.setOnClickListener(InsepectOrderActivity.this);
                textView.setTag(index);
                failed_item_layout.addView(view);
            } else if (result.equals("item")) {
                View view = LayoutInflater.from(InsepectOrderActivity.this).inflate(R.layout.item_insepect_fail, null, false);
                view.setTag(index);
                TextView textView = view.findViewById(R.id.line);
                textView.setText(OrderDetailslist.get(index).getAsString(KEY_LineNumber));
                textView = view.findViewById(R.id.number);
                textView.setText(OrderDetailslist.get(index).getAsString(KEY_Item));
                textView.setOnClickListener(InsepectOrderActivity.this);
                textView = view.findViewById(R.id.description);
                textView.setOnClickListener(InsepectOrderActivity.this);
                textView.setTag(index);
                failed_item_layout.addView(view);
            } else if (result.equals("edit")) {
                View view = failed_item_layout.findViewWithTag(index);
                if (view != null) {
                    TextView textView = view.findViewById(R.id.line);
                    textView.setText(OrderDetailslist.get(index).getAsString(KEY_LineNumber));
                    textView = view.findViewById(R.id.number);
                    textView.setText(OrderDetailslist.get(index).getAsString(KEY_Item));
                    textView.setOnClickListener(InsepectOrderActivity.this);
                    textView = view.findViewById(R.id.description);
                    textView.setOnClickListener(InsepectOrderActivity.this);
                    textView.setTag(index);
                } else {
                    view = failed_item_layout.findViewWithTag(0);
                    TextView textView = view.findViewById(R.id.line);
                    textView.setText("");
                    textView = view.findViewById(R.id.number);
                    textView.setText("");
                    textView.setOnClickListener(null);
                    textView = view.findViewById(R.id.description);
                    textView.setText("");
                    textView.setOnClickListener(null);
                }
            }
        }

    }

}
