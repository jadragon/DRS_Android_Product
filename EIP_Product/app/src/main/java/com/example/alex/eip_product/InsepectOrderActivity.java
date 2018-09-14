package com.example.alex.eip_product;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import Component.PaintView;
import Utils.CommonUtil;
import db.SQLiteDatabaseHandler;

public class InsepectOrderActivity extends AppCompatActivity {
    private LinearLayout alltable, courseTable;
    private Button academyButton, saveButton, resetSignButton;
    int line = 1;
    PaintView paintView;
    TextView title, online, faild_txt_description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insepect_order);
        initFailedTable();
        findView();
        setAnimation(title);
        setAnimation(online);
        // Apply the adapter to the spinner
        academyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddColumTask().execute();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePicture(alltable);
                startActivity(new Intent(InsepectOrderActivity.this, ImageViewActivity.class));
            }
        });
        resetSignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paintView.resetCanvas();
            }
        });
        faild_txt_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           startActivity(new Intent(InsepectOrderActivity.this,SelectFailedActivity.class));
            }
        });
    }

    void findView() {
        alltable = findViewById(R.id.alltable);
        courseTable = findViewById(R.id.kccx_course_table);
        academyButton = findViewById(R.id.kccx_chaxun1);
        saveButton = findViewById(R.id.kccx_chaxun2);
        resetSignButton = findViewById(R.id.kccx_chaxun3);
        paintView = findViewById(R.id.paintView);
        online = findViewById(R.id.online);
        title = findViewById(R.id.title);
        faild_txt_description = findViewById(R.id.faild_txt_description);
    }


    private class AddColumTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            return "flag";
        }

        @Override
        protected void onPostExecute(String result) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 3);
            if (result.equals("flag")) {
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
        if (CommonUtil.checkWIFI(InsepectOrderActivity.this)) {
            online.setText("線上模式");
        } else {
            online.setText("離線模式");
        }
    }


    void initFailedTable() {
        SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(this);
        ArrayList<String> arrayList = db.getFailedDescription(0);
        if (arrayList.size() <= 0) {
            db.initFailTable();
        }
        db.close();
    }
}
