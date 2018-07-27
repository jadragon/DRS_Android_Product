package com.test.tw.wrokproduct;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class LoadingPage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_page);
        ImageView loading_page_image = findViewById(R.id.loading_page_image);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        loading_page_image.setLayoutParams(new LinearLayout.LayoutParams(dm.widthPixels, dm.widthPixels * (2436 / 1125)));
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(2000);
                finish();
            }

        }).start();
    }
}
