package com.example.alex.eip_product;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    Panel mPanel;
    LinearLayout linearLayout;
    ImageView imageView;
    DisplayMetrics dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dm = getResources().getDisplayMetrics();
        linearLayout = (LinearLayout) findViewById(R.id.main);
        imageView = (ImageView) findViewById(R.id.image);

        //畫布
        mPanel = new Panel(MainActivity.this, dm.widthPixels, (int) (400 * dm.density));
        linearLayout.addView(mPanel);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                mPanel.resetCanvas();
                break;
            case R.id.menu_save:
                mPanel.savePicture();
                //  startActivity(new Intent(MainActivity.this, ImageActivity.class));
                imageView.setImageURI(null);
                File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "test.png");
                imageView.setImageURI(Uri.fromFile(file));
                break;
            case R.id.menu_quit:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}