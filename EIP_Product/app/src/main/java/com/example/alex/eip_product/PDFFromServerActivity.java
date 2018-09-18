package com.example.alex.eip_product;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;
import java.io.IOException;

import Utils.CommonUtil;
import liabiry.Http.Downloader;

public class PDFFromServerActivity extends AppCompatActivity {
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 0x01;
    PDFView pdf_view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdffrom_server);
        // Android 6.0相机动态权限检查
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            initDownloader();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // 相机权限
            case REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initDownloader();
                }
                break;
        }
    }

    private void initDownloader() {
        if (CommonUtil.checkWIFI(PDFFromServerActivity.this)) {
            String extStorageDirectory = Environment.getExternalStorageDirectory()
                    .toString();
            File folder = new File(extStorageDirectory, "pdf");
            if (!folder.exists()) {
                folder.mkdirs();
            }
            final File file = new File(folder, "Read.pdf");
            try {
                file.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Downloader.DownloadFile("https://www.efroip.com/efroip/public/resource_view/open.php?file=ed6eb9bae59a2972dfa4848ed615721d.pdf", file);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showPdf();
                        }
                    });
                }
            }).start();
        } else {
            showPdf();
        }

    }

    public void showPdf() {
        pdf_view = findViewById(R.id.pdf_view);
        File file = new File(Environment.getExternalStorageDirectory() + "/pdf/Read.pdf");
        pdf_view.fromFile(file).load();


        /*

        File file = new File(Environment.getExternalStorageDirectory() + "/pdf/Read.pdf");
        Intent intent = new Intent(Intent.ACTION_VIEW);

        Uri contentUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", file);
        intent.setDataAndType(contentUri, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);

        */
    }
}
