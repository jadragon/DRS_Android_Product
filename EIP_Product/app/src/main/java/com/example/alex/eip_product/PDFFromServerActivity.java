package com.example.alex.eip_product;

import android.os.Bundle;
import android.os.Environment;

import android.support.v7.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class PDFFromServerActivity extends AppCompatActivity {
    private String file_name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdffrom_server);
        file_name = getIntent().getStringExtra("file_name");
        showPdf();
    }

    public void showPdf() {
        PDFView pdf_view = findViewById(R.id.pdf_view);
        File file = new File(Environment.getExternalStorageDirectory() + "/EIP/" + file_name+".pdf");
        pdf_view.fromFile(file).load();

    }
}
