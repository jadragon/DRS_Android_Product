package com.test.tw.wrokproduct;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import library.GetBitmap;
import library.GetInformationByPHP;

public class TestActivity extends AppCompatActivity {
    JSONObject json;
    TextView test_textview;
    Bitmap bitmap;
    String encoded;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        test_textview = findViewById(R.id.test_textview);

        testJson();
    }

    private void testJson() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                bitmap= GetBitmap.returnBitmap("http://www.rcam.nchu.edu.tw/upload/menu/1397614599.jpg");
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();
                encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

                json = new GetInformationByPHP().updatePortrait("zI6OIYlbhfPKyhbchdOiGg==",encoded);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("TestJSONONONONON", encoded + "");
                        Log.e("TestJSONONONONON", json + "");

                    }
                });
            }
        }).start();

    }
}
