package com.example.alex.eip_product;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import db.SQLiteDatabaseHandler;
import liabiry.GetJsonData.UploadFileJsonData;

public class ImageViewActivity extends AppCompatActivity {
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        image = findViewById(R.id.image);
        //從SD卡取得圖片
        /*
        image.setImageURI(null);
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "test.png");
        image.setImageURI(Uri.fromFile(file));
        */
        //從資料庫取得圖片
        SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(ImageViewActivity.this);
        final byte[] bis = db.getPhotoImage();
        image.setImageBitmap(BitmapFactory.decodeByteArray(bis, 0, bis.length));
        new Thread(new Runnable() {
            @Override
            public void run() {
                final JSONObject jsonObject = new UploadFileJsonData().updatePortrait("MNj@_xDWWV6wdtdgBYOKssw==", bis);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if(jsonObject.getBoolean("Success")){
                                Toast.makeText(ImageViewActivity.this, "上傳成功", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();
        db.resetInsepectTables();
        db.close();
    }
}
