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
import java.util.List;

import Component.PaintView;
import Utils.CommonUtil;
import db.SQLiteDatabaseHandler;

public class InsepectOrderActivity extends AppCompatActivity {


    private LinearLayout alltable, courseTable;
    private Button academyButton, saveButton, resetSignButton;
    int line = 1;
    PaintView paintView;
    TextView title, online;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insepect_order);

        findView();
        setAnimation(title);
        setAnimation(online);
        // Apply the adapter to the spinner
        academyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyTask1().execute();
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
    }

    /**
     * @author zhuzhp
     * @ClassName: MyTask1
     * @Description: 根据学院名查询课程的AsyncTask内部类
     * @date 2014年4月8日 下午8:24:37
     */
    private class MyTask1 extends AsyncTask<String, Integer, String> {

        List<String> resultList = new ArrayList<>();
        int i = 0;

        @Override
        protected String doInBackground(String... params) {
            resultList.add("11111");
            resultList.add("22222");
            resultList.add("33333");
            resultList.add("44444");
            resultList.add("55555");
            resultList.add("66666");
            resultList.add("77777");
            resultList.add("88888");
            resultList.add("99999");
            return "flag";
        }

        @Override
        /**
         * 该主要完成将查询得到的数据以tablerow形式显示。
         */
        protected void onPostExecute(String result) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 3);
            int rows = resultList.size();
            if (result.equals("flag")) {
                if (rows != 0) {
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
}
