package com.example.alex.eip_product;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import Utils.CommonUtil;
import db.SQLiteDatabaseHandler;

public class InsepectOrderActivity extends AppCompatActivity implements TextWatcher {
    private final static int SELECT_FAIL_REASON = 110;
    private LinearLayout alltable, courseTable, failed_item_layout;
    private Button academyButton, saveButton;
    private int line = 1;
    private ImageView paintView;
    private TextView title, pdf_view;
    private DisplayMetrics dm;
    private ArrayList<View> ItemList = new ArrayList<>();
    private ArrayList<View> failItemList = new ArrayList<>();
    private LinePathView linePathView;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insepect_order);
        dm = getResources().getDisplayMetrics();
        initPdfView();
        findView();
        initPaintView();
        initFailItem();
        setAnimation(title);
        // Apply the adapter to the spinner

        academyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddColumTask(0).execute("normal");
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePicture(alltable);
                startActivity(new Intent(InsepectOrderActivity.this, ImageViewActivity.class));
            }
        });

    }

    private void initFailItem() {
        for (int i = 0; i < 5; i++) {
            new AddColumTask(i).execute("item");
        }

    }

    private void initPaintView() {

        paintView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linePathView == null)
                    linePathView = new LinePathView(InsepectOrderActivity.this);
                else
                    linePathView.clear();

                if (dialog == null) {
                    final int bitmapwidth = paintView.getWidth();
                    final int bitmapheight = paintView.getHeight();
                    AlertDialog.Builder builder = new AlertDialog.Builder(InsepectOrderActivity.this).setView(linePathView)
                            .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    paintView.setImageBitmap(linePathView.getBitMap(paintView.getWidth(), paintView.getHeight()));
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
                    p.width = (int) (dm.heightPixels * ((float) bitmapwidth / bitmapheight) / 3 * 2);
                    p.height = (int) ((float) dm.heightPixels / 3 * 2);
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
        alltable = findViewById(R.id.alltable);
        courseTable = findViewById(R.id.kccx_course_table);
        academyButton = findViewById(R.id.kccx_chaxun1);
        saveButton = findViewById(R.id.kccx_chaxun2);
        paintView = findViewById(R.id.paintView);
        title = findViewById(R.id.title);
        failed_item_layout = findViewById(R.id.failed_item_layout);
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
                courseTable.addView(view);
                ItemList.add(view);
                ((TextView) view.findViewById(R.id.row1)).setText("" + line++);
                ((EditText) view.findViewById(R.id.row7)).addTextChangedListener(InsepectOrderActivity.this);
                ((EditText) view.findViewById(R.id.row8)).addTextChangedListener(InsepectOrderActivity.this);
                ((EditText) view.findViewById(R.id.row9)).addTextChangedListener(InsepectOrderActivity.this);
                ((EditText) view.findViewById(R.id.row10)).addTextChangedListener(InsepectOrderActivity.this);

            } else if (result.equals("item")) {
                View view = LayoutInflater.from(InsepectOrderActivity.this).inflate(R.layout.item_insepect_fail, null, false);
                ((TextView) view.findViewWithTag("line")).setText(index + "");
                TextView textView = view.findViewWithTag("number");
                textView.setText("102540" + index);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(InsepectOrderActivity.this, PDFFromServerActivity.class));
                    }
                });
                view.findViewWithTag("description").setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(InsepectOrderActivity.this, SelectFailedActivity.class);
                        intent.putExtra("index", index);
                        startActivityForResult(intent, SELECT_FAIL_REASON);
                    }
                });

                failItemList.add(view);
                failed_item_layout.addView(view);

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
                if (!((EditText) view.findViewById(R.id.row7)).getText().toString().equals("")&&Integer.parseInt(((EditText) view.findViewById(R.id.row7)).getText().toString()) > 0) {
                    new AddColumTask(0).execute("item");
                } else {
                    if (!((EditText) view.findViewById(R.id.row8)).getText().toString().equals("")&&Integer.parseInt(((EditText) view.findViewById(R.id.row8)).getText().toString()) > 0) {
                        new AddColumTask(0).execute("item");
                    } else {
                        if (!((EditText) view.findViewById(R.id.row9)).getText().toString().equals("")&&Integer.parseInt(((EditText) view.findViewById(R.id.row9)).getText().toString()) > 0) {
                            new AddColumTask(0).execute("item");
                        } else {
                            if (!((EditText) view.findViewById(R.id.row10)).getText().toString().equals("")&&Integer.parseInt(((EditText) view.findViewById(R.id.row10)).getText().toString()) > 0) {
                                new AddColumTask(0).execute("item");
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
        */
        //儲存在資料庫
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap vBitmap = CommonUtil.convertViewToBitmap(v);
        vBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bitmapByte = baos.toByteArray();
        SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(InsepectOrderActivity.this);
        db.addImage(bitmapByte);
        db.close();
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
                ((TextView) failItemList.get(data.getIntExtra("index", 0)).findViewWithTag("description")).setText(failed_reason);
            }
        }
    }
}
