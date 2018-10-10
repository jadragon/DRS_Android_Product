package com.example.alex.eip_product;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import Component.PaintView;
import Utils.CommonUtil;
import db.SQLiteDatabaseHandler;

public class InsepectOrderActivity extends AppCompatActivity {
    private final static int SELECT_FAIL_REASON = 110;
    private LinearLayout alltable, courseTable, failed_item_layout;
    private Button academyButton, saveButton;
    private int line = 1;
    private ImageView paintView;
    private TextView title, online, pdf_view;

    private PaintView view;
    private ArrayList<View> failItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insepect_order);
        initPdfView();
        findView();
        initPaintView();
        initFailItem();
        setAnimation(title);
        setAnimation(online);
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
                view = new PaintView(InsepectOrderActivity.this);
                view.setPenStrokeWidth(10);
                AlertDialog.Builder builder = new AlertDialog.Builder(InsepectOrderActivity.this).setView(view)
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                paintView.setImageBitmap(view.getPaintBitmap());
                                dialog.dismiss();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.show();
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
        online = findViewById(R.id.online);
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

                ((TextView) view.findViewById(R.id.row1)).setText("" + line++);
                //setAnimation(view.findViewById(R.id.row20));
                courseTable.addView(view);

            } else if (result.equals("item")) {
                View view = LayoutInflater.from(InsepectOrderActivity.this).inflate(R.layout.item_insepect_fail, null, false);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(InsepectOrderActivity.this, SelectFailedActivity.class);
                        intent.putExtra("index", index);
                        startActivityForResult(intent, SELECT_FAIL_REASON);
                    }
                });
                failed_item_layout.addView(view);
                failItemList.add(view);
            }
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
                        online.setText("線上模式");
                    }
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    online.setText("離線模式");
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        } else {
            online.setText("線上模式");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_FAIL_REASON) {
            if (resultCode == 110) {
                String failed_reason = data.getStringExtra("failed_reason");
                ((TextView) failItemList.get(data.getIntExtra("index", 0)).findViewById(R.id.faild_txt_description)).setText(failed_reason);
            }
        }
    }
}
