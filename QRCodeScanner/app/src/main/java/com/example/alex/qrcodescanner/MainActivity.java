package com.example.alex.qrcodescanner;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.alex.qrcodescanner.adapter.SimpleTextAdapter;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import java.util.ArrayList;
import java.util.List;

import static android.os.Build.VERSION.SDK_INT;

public class MainActivity extends AppCompatActivity {
    CompoundBarcodeView barcodeView;
    DisplayMetrics dm;
    RecyclerView recyclerview;
    ArrayList<String> arrayList = new ArrayList<>();
    SimpleTextAdapter simpleTextAdapter;
    ToastMessageDialog toastMessageDialog;
    Button delete, clear, save;
    TextView classes, process, staff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toastMessageDialog = new ToastMessageDialog(this);
        dm = getResources().getDisplayMetrics();
        if (SDK_INT >= Build.VERSION_CODES.M)
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
            } else {
                initBarcodeView();
                initTextView();
                initRecyclerView();
                initButton();

            }


        //   beepManager = new BeepManager(getActivity());
    }

    private void initTextView() {
        classes = findViewById(R.id.classes);
        process = findViewById(R.id.process);
        staff = findViewById(R.id.staff);
    }

    private void initButton() {
        delete = findViewById(R.id.delete);
        clear = findViewById(R.id.clear);
        save = findViewById(R.id.save);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.delete:
                        if (simpleTextAdapter.getSelectPosition() != -1) {
                            arrayList.remove(simpleTextAdapter.getSelectPosition());
                            simpleTextAdapter.setFilter(arrayList);
                        } else {
                            toastMessageDialog.setMessageText("請選擇要刪除的產品序號");
                            toastMessageDialog.confirm();
                        }

                        break;
                    case R.id.clear:
                        arrayList.clear();
                        simpleTextAdapter.setFilter(arrayList);
                        break;
                    case R.id.save:
                        break;
                }
            }
        };
        delete.setOnClickListener(onClickListener);
        clear.setOnClickListener(onClickListener);
        save.setOnClickListener(onClickListener);
    }

    private void initRecyclerView() {
        recyclerview = findViewById(R.id.recyclerview);
        for (int i = 0; i < 20; i++) {
            arrayList.add(i + "");
        }
        simpleTextAdapter = new SimpleTextAdapter(this, arrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(simpleTextAdapter);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // 相机权限
            case 0:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initBarcodeView();
                }
                break;
        }
    }

    private void initBarcodeView() {
        barcodeView = findViewById(R.id.barcodeView);
        barcodeView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                String scan_result = result.getText();
                if (scan_result != null) {
                    barcodeView.setStatusText(scan_result);
                    if (scan_result.equals("IN")) {
                        classes.setText("進站");
                        return;
                    }
                    if (scan_result.equals("OUT")) {
                        classes.setText("出站");
                        return;
                    }
                    if (scan_result.equals("DF")) {
                        classes.setText("報廢");
                        return;
                    }
                    if (scan_result.equals("RE")) {
                        classes.setText("反修");
                        return;
                    }
                    if (scan_result.contains("ST")) {
                        process.setText(scan_result);
                        return;
                    }

                    if (scan_result.contains("EM")) {
                        staff.setText(scan_result);
                        return;
                    }
                    if (scan_result.contains("PT")) {
                        barcodeView.pause();
                        if (arrayList.contains(scan_result)) {
                            if (!toastMessageDialog.isShow()) {
                                toastMessageDialog.setMessageText("產品序列號重複");
                                toastMessageDialog.show();
                            }
                            barcodeView.resume();
                            return;
                        }
                        arrayList.add(scan_result);
                        simpleTextAdapter.setFilter(arrayList);
                        recyclerview.scrollToPosition(0);
                        barcodeView.resume();
                    }

                }

            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {

            }
        });

    }

    @Override
    public void onResume() {
        if (barcodeView != null)
            barcodeView.resume();
        super.onResume();
    }

    @Override
    public void onPause() {
        if (barcodeView != null)
            barcodeView.pause();
        super.onPause();
    }


}
