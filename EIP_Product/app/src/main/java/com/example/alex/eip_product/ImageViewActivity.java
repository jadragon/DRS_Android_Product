package com.example.alex.eip_product;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import Component.SimpleRoundProgress;
import db.ImagePojo;
import db.SQLiteDatabaseHandler;
import liabiry.GetJsonData.PhotoApi;

public class ImageViewActivity extends AppCompatActivity {
    ImageView image;
    JSONObject json;
    Button update_button;
    SQLiteDatabaseHandler db;
    TextView total_image;
    ArrayList<ImagePojo> arrayList;
    SimpleRoundProgress progress;
    int t = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        image = findViewById(R.id.image);
        total_image = findViewById(R.id.total_image);
        progress = findViewById(R.id.srp_stroke_0);
        //從SD卡取得圖片
        /*
        image.setImageURI(null);
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "test.png");
        image.setImageURI(Uri.fromFile(file));
        */
        //從資料庫取得圖片


        db = new SQLiteDatabaseHandler(ImageViewActivity.this);
        arrayList = db.getPhotoImage();
        total_image.setText("目前圖片有:" + arrayList.size() + "張  未上傳");

        if (arrayList.size() > 0)
            image.setImageBitmap(BitmapFactory.decodeByteArray(arrayList.get(0).getImage(), 0, arrayList.get(0).getImage().length));


        update_button = findViewById(R.id.update_button);
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        arrayList = db.getPhotoImage();
                        for (final ImagePojo data : arrayList) {
                            int number = new Random().nextInt(50);
                            try {
                                json = new PhotoApi().insert_photo("H9Tv7tBs8Esr914/MB62Aw==", "RalpBDfrS2EPx6t8QzgpcA==", number + "", data.getImage());
                                db.updatePhotoStatus(data.getId(), 1);
                                t++;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progress.setProgress((int) (((float)t / arrayList.size()) * 100));
                                    }
                                });
                            } catch (Exception e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        db.updatePhotoStatus(data.getId(), 0);
                                        Toast.makeText(ImageViewActivity.this, "上傳失敗", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                break;
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                arrayList = db.getPhotoImage();
                                total_image.setText("目前圖片有:" + arrayList.size() + "張  未上傳");
                            }
                        });
                    }
                }).start();
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
