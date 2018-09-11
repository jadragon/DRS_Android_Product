package com.example.alex.eip_product;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.io.File;

public class ImageViewActivity extends AppCompatActivity {
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        image = findViewById(R.id.image);
        image.setImageURI(null);
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "test.png");
        image.setImageURI(Uri.fromFile(file));
    }
}
