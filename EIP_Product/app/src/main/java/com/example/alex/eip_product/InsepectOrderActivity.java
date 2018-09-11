package com.example.alex.eip_product;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InsepectOrderActivity extends AppCompatActivity {


    private TableLayout alltable, courseTable;
    private Button academyButton, saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insepect_order);

        findView();


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
    }

    void findView() {
        alltable = findViewById(R.id.alltable);
        courseTable = findViewById(R.id.kccx_course_table);
        academyButton = findViewById(R.id.kccx_chaxun1);
        saveButton = findViewById(R.id.kccx_chaxun2);
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
            TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3);
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
                    for (int i = 1; i < 20; i++) {
                        ((TextView) view.findViewWithTag("row" + i)).setText("123");
                    }
                    courseTable.addView(view);


                }
            }
        }

    }


    //save as picture
    public void savePicture(View v) {
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
            /*
            FileOutputStream fout=new FileOutputStream("/sdcard/draw.png");
            vBitmap.compress(Bitmap.CompressFormat.PNG,100,fout);
            fout.close();
            */
        } catch (Exception x) {
            x.printStackTrace();
        }
    }


    public static Bitmap convertViewToBitmap(View view) {
        // view.setDrawingCacheEnabled(true);
        // view.buildDrawingCache();
        //   Bitmap bitmap = view.getDrawingCache();

        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);


        view.draw(new Canvas(bitmap));
        return bitmap;
    }

}
