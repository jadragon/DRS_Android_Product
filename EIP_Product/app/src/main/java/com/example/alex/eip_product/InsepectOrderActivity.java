package com.example.alex.eip_product;

import android.app.AlertDialog;
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
    private ArrayList<ContentValues> Orderslist, OrdersDetaillist;
    private GlobalVariable gv;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insepect_order);
        key_ponumber = getIntent().getStringExtra(KEY_PONumber);
        gv = (GlobalVariable) getApplicationContext();
        dm = getResources().getDisplayMetrics();
        db = new OrderDatabase(this);
        initPdfView();
        findView();
        initData();
        initPaintView();
        setAnimation(title);
        // Apply the adapter to the spinner
/*
        academyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddColumTask(0).execute("normal");
            }
        });
        */
        initButton();

    }

    private void initButton() {
        saveButton = findViewById(R.id.save);
        saveButton.setOnClickListener(this);
        sendButton = findViewById(R.id.send);
        sendButton.setOnClickListener(this);
        editButton = findViewById(R.id.edit);
        editButton.setOnClickListener(this);
        cancelButton = findViewById(R.id.cancel);
        cancelButton.setOnClickListener(this);

    }

    private void initData() {

        Orderslist = db.getOrdersByPONumber(key_ponumber);

        SalesMan.setText(Orderslist.get(0).getAsString(KEY_SalesMan));
        Shipping.setText(Orderslist.get(0).getAsString(KEY_Shipping));
        VendorName.setText(Orderslist.get(0).getAsString(KEY_VendorName));
        VendorCode.setText(Orderslist.get(0).getAsString(KEY_VendorCode));
        PONumber.setText(Orderslist.get(0).getAsString(KEY_PONumber));
        POVersion.setText(Orderslist.get(0).getAsString(KEY_POVersion));

        if (!Orderslist.get(0).getAsString(KEY_Inspector).equals("")) {
            Inspector.setText(Orderslist.get(0).getAsString(KEY_Inspector));
            InspectorDate.setText(Orderslist.get(0).getAsString(KEY_InspectorDate));
        }

        byte[] bitmapdata = Orderslist.get(0).getAsByteArray(KEY_VendorInspector);
        if (bitmapdata != null) {
            bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
            VendorInspector.setImageBitmap(bitmap);
        }
        if (!Orderslist.get(0).getAsString(KEY_VendorInspectorDate).equals("")) {
            VendorInspectorDate.setText(Orderslist.get(0).getAsString(KEY_VendorInspectorDate));
        }
        FeedbackPerson.setText(Orderslist.get(0).getAsString(KEY_FeedbackPerson));
        FeedbackDate.setText(Orderslist.get(0).getAsString(KEY_FeedbackDate));
        OrdersDetaillist = db.getOrderDetailsByPONumber(key_ponumber);
        for (int i = 0; i < OrdersDetaillist.size(); i++) {
            new AddColumTask(i).execute("normal");
        }
    }

    private void initPaintView() {
        VendorInspector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

            }
        });

    }

    private void initPdfView() {
        pdf_view = findViewById(R.id.pdf_view);
        pdf_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InsepectOrderActivity.this, PDFFromServerActivity.class));
            }
        });
    }

    void findView() {
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
        PONumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InsepectOrderActivity.this, InspectDetailActivity.class);
                intent.putExtra(KEY_PONumber, key_ponumber);
                startActivity(intent);
            }
        });
        POVersion = findViewById(R.id.POVersion);
        Inspector = findViewById(R.id.Inspector);
        InspectorDate = findViewById(R.id.InspectorDate);
        FeedbackPerson = findViewById(R.id.FeedbackPerson);
        FeedbackDate = findViewById(R.id.FeedbackDate);
        VendorInspectorDate = findViewById(R.id.VendorInspectorDate);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save:
                ContentValues cv = new ContentValues();

                byte[] byteArray = null;
                if (bitmap != null) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byteArray = stream.toByteArray();
                } else {
                    Toast.makeText(InsepectOrderActivity.this, "簽名檔沒簽", Toast.LENGTH_SHORT).show();
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
                db.saveOrdersEditBasic(Orderslist.get(0).getAsString(KEY_PONumber), cv);
                ArrayList<ContentValues> arrayList = new ArrayList<>();
                for (View item : ItemList) {
                    cv = new ContentValues();
                    TextView textView = item.findViewById(R.id.row7);
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
                db.saveOrderDetailsEdit(Orderslist.get(0).getAsString(KEY_PONumber), arrayList);
                break;
            case R.id.send:

                break;
            case R.id.edit:

                break;
            case R.id.cancel:

                break;
            case R.id.number:
                startActivity(new Intent(InsepectOrderActivity.this, PDFFromServerActivity.class));
                break;
            case R.id.description:
                Intent intent = new Intent(InsepectOrderActivity.this, SelectFailedActivity.class);
                int index = (int) view.getTag();
                View failView = ItemList.get(index);
                intent.putExtra("index", index);
                EditText editText = failView.findViewById(R.id.row7);
                long number = editText.getText().toString().equals("") ? 0 : Long.parseLong(editText.getText().toString());
                intent.putExtra("type1", number > 0);
                editText = failView.findViewById(R.id.row8);
                number = editText.getText().toString().equals("") ? 0 : Long.parseLong(editText.getText().toString());
                intent.putExtra("type2", number > 0);
                editText = failView.findViewById(R.id.row9);
                number = editText.getText().toString().equals("") ? 0 : Long.parseLong(editText.getText().toString());
                intent.putExtra("type3", number > 0);
                editText = failView.findViewById(R.id.row10);
                number = editText.getText().toString().equals("") ? 0 : Long.parseLong(editText.getText().toString());
                intent.putExtra("type4", number > 0);

                startActivityForResult(intent, SELECT_FAIL_REASON);
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
            if (result.equals("normal")) {
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
                textView.setText(OrdersDetaillist.get(index).getAsString(KEY_LineNumber));
                textView = view.findViewById(R.id.row2);
                textView.setText(OrdersDetaillist.get(index).getAsString(KEY_Item));
                textView = view.findViewById(R.id.row3);
                textView.setText(OrdersDetaillist.get(index).getAsString(KEY_OrderQty));
                textView = view.findViewById(R.id.row4);
                textView.setText(OrdersDetaillist.get(index).getAsString(KEY_Qty));
                textView = view.findViewById(R.id.row5);
                textView.setText(OrdersDetaillist.get(index).getAsString(KEY_SampleNumber));
                textView = view.findViewById(R.id.row6);
                textView.setText(OrdersDetaillist.get(index).getAsString(KEY_Uom));
                EditText editText = view.findViewById(R.id.row7);
                editText.setText(OrdersDetaillist.get(index).getAsString(KEY_Size));
                editText.setOnFocusChangeListener(InsepectOrderActivity.this);
                editText.addTextChangedListener(InsepectOrderActivity.this);
                editText = view.findViewById(R.id.row8);
                editText.setText(OrdersDetaillist.get(index).getAsString(KEY_Functions));
                editText.setOnFocusChangeListener(InsepectOrderActivity.this);
                editText.addTextChangedListener(InsepectOrderActivity.this);
                editText = view.findViewById(R.id.row9);
                editText.setText(OrdersDetaillist.get(index).getAsString(KEY_Surface));
                editText.setOnFocusChangeListener(InsepectOrderActivity.this);
                editText.addTextChangedListener(InsepectOrderActivity.this);
                editText = view.findViewById(R.id.row10);
                editText.setText(OrdersDetaillist.get(index).getAsString(KEY_Package));
                editText.setOnFocusChangeListener(InsepectOrderActivity.this);
                editText.addTextChangedListener(InsepectOrderActivity.this);

                CheckBox checkBox = view.findViewById(R.id.row11);
                checkBox.setChecked(OrdersDetaillist.get(index).getAsBoolean(KEY_MainMarK));
                checkBox = view.findViewById(R.id.row12);
                checkBox.setChecked(OrdersDetaillist.get(index).getAsBoolean(KEY_SideMarK));
                RadioButton radioButton = view.findViewById(R.id.row13);
                radioButton.setChecked(OrdersDetaillist.get(index).getAsBoolean(KEY_CheckPass));
                radioButton = view.findViewById(R.id.row14);
                radioButton.setChecked(OrdersDetaillist.get(index).getAsBoolean(KEY_Special));
                radioButton = view.findViewById(R.id.row15);
                radioButton.setChecked(OrdersDetaillist.get(index).getAsBoolean(KEY_Rework));
                radioButton = view.findViewById(R.id.row16);
                radioButton.setChecked(OrdersDetaillist.get(index).getAsBoolean(KEY_Reject));

                textView = view.findViewById(R.id.row17);
                textView.setText(OrdersDetaillist.get(index).getAsString(KEY_ReCheckDate));
                textView = view.findViewById(R.id.row18);
                textView.setText(OrdersDetaillist.get(index).getAsString(KEY_Remarks));

            } else if (result.equals("item")) {
                View view = LayoutInflater.from(InsepectOrderActivity.this).inflate(R.layout.item_insepect_fail, null, false);
                view.setTag(index);
                TextView textView = view.findViewById(R.id.line);
                textView.setText(OrdersDetaillist.get(index).getAsString(KEY_LineNumber));
                textView = view.findViewById(R.id.number);
                textView.setText(OrdersDetaillist.get(index).getAsString(KEY_Item));
                textView.setOnClickListener(InsepectOrderActivity.this);
                textView = view.findViewById(R.id.description);
                textView.setOnClickListener(InsepectOrderActivity.this);
                textView.setTag(index);
                failed_item_layout.addView(view);
            } else if (result.equals("edit")) {
                View view = failed_item_layout.findViewWithTag(index);
                if (view != null) {
                    TextView textView = view.findViewById(R.id.line);
                    textView.setText(OrdersDetaillist.get(index).getAsString(KEY_LineNumber));
                    textView = view.findViewById(R.id.number);
                    textView.setText(OrdersDetaillist.get(index).getAsString(KEY_Item));
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

    //監聽品質不良品數Start
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
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

    @Override
    protected void onResume() {
        super.onResume();
        toastCheckWIFI();
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
                String failed_reason = data.getStringExtra("failed_reason");
                ((TextView) failed_item_layout.findViewWithTag(data.getIntExtra("index", 0)).findViewById(R.id.description)).setText(failed_reason);
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (bitmap != null)
            bitmap.recycle();
        db.close();
        super.onDestroy();
    }

}
