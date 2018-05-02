package com.test.tw.wrokproduct;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONObject;

import library.GetInformationByPHP;
import library.ResolveJsonData;

public class TestActivity extends AppCompatActivity {
    JSONObject json;
    TextView test_textview;

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
                json = new GetInformationByPHP().getPcontent("URwlZEnZscDdnIJN4vjczw==");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        test_textview.setText("" + ResolveJsonData.getItemArray(json));
                        Log.e("TestJSONONONONON", ResolveJsonData.getPcContentInformation(json) + "");

                    }
                });
            }
        }).start();

    }
}
