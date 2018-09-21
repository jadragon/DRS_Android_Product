package com.example.alex.qrcodescanner;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.Toast;

import com.example.alex.qrcodescanner.Utils.ExcelUtils;
import com.example.alex.qrcodescanner.adapter.SimpleTextAdapter;
import com.example.alex.qrcodescanner.pojo.DataPojo;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_ALL_PERMISSION = 0x01;
    CompoundBarcodeView barcodeView;
    DisplayMetrics dm;
    RecyclerView recyclerview;
    List<DataPojo> exelarratyList = new ArrayList<>();
    BeepManager beepManager;

    SimpleTextAdapter simpleTextAdapter;
    ToastMessageDialog toastMessageDialog;
    Button delete, clear, save;
    TextView classes, process, staff;
    String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    // 声明一个集合，在后面的代码中用来存储用户拒绝授权的权
    List<String> mPermissionList = new ArrayList<>();
    String preQRcode_result = "";

    //EXCEL
    private static String[] title = {"类别", "工序", "人员", "ID号", "", "日期", "时间"};
    private static final byte YEAR = 0;
    private static final byte MONTH = 1;
    private static final byte DAY_OF_MONTH = 2;
    private static final byte HOUR_OF_DAY = 3;
    private static final byte MINUTE = 4;
    private static final byte SECOND = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toastMessageDialog = new ToastMessageDialog(this);
        dm = getResources().getDisplayMetrics();

        mPermissionList.clear();
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }
        if (mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了
            initBarcodeView();
            initTextView();
            initRecyclerView();
            initButton();
        } else {//请求权限方法
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(MainActivity.this, permissions, REQUEST_ALL_PERMISSION);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_ALL_PERMISSION) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    //判断是否勾选禁止后不再询问
                    boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissions[i]);
                    if (showRequestPermission) {
                        Toast.makeText(this, "權限尚未申請", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
            initBarcodeView();
            initTextView();
            initRecyclerView();
            initButton();

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
                        int position = simpleTextAdapter.getSelectPosition();
                        if (position != -1) {
                            exelarratyList.remove(position);
                            simpleTextAdapter.setFilter(exelarratyList);
                        } else {
                            toastMessageDialog.setMessageText("請選擇要刪除的產品序號");
                            toastMessageDialog.confirm();
                        }
                        break;
                    case R.id.clear:
                        if (saveExcel()) {
                            exelarratyList.clear();
                            classes.setText(null);
                            process.setText(null);
                            staff.setText(null);
                            simpleTextAdapter.setFilter(exelarratyList);
                        }
                        break;
                    case R.id.save:
                        saveExcel();
                        break;

                }
            }
        };
        delete.setOnClickListener(onClickListener);
        clear.setOnClickListener(onClickListener);
        save.setOnClickListener(onClickListener);
    }

    private boolean saveExcel() {
        if (!classes.getText().toString().equals("") && !process.getText().toString().equals("") && !staff.getText().toString().equals("")) {
            exportExcel();
            return true;
        } else {
            toastMessageDialog.setMessageText("請確認類別、工序、人員是否已取得");
            toastMessageDialog.confirm();
            return false;
        }
    }

    private void initRecyclerView() {

        recyclerview = findViewById(R.id.recyclerview);
        simpleTextAdapter = new SimpleTextAdapter(this, exelarratyList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(simpleTextAdapter);

    }

    private void initBarcodeView() {
        beepManager = new BeepManager(this);
        beepManager.setVibrateEnabled(true);
        barcodeView = findViewById(R.id.barcodeView);
        barcodeView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                String scan_result = result.getText();
                if (scan_result != null) {
                    barcodeView.setStatusText(scan_result);
                    if (!scan_result.equals(preQRcode_result)) {

                        //    beepManager.playBeepSoundAndVibrate();
                        if (scan_result.equals("IN")) {
                            checkClassAndProcessAndStaff();
                            classes.setText("進站");

                        } else if (scan_result.equals("OUT")) {

                            checkClassAndProcessAndStaff();
                            classes.setText("出站");

                        } else if (scan_result.equals("DF")) {

                            checkClassAndProcessAndStaff();
                            classes.setText("報廢");

                        } else if (scan_result.equals("RE")) {

                            checkClassAndProcessAndStaff();
                            classes.setText("反修");

                        } else if (scan_result.contains("ST")) {

                            checkClassAndProcessAndStaff();
                            process.setText(scan_result.substring(3, scan_result.length()));

                        } else if (scan_result.contains("EM")) {

                            checkClassAndProcessAndStaff();
                            staff.setText(scan_result.substring(3, scan_result.length()));

                        } else if (scan_result.contains("PT")) {
                            barcodeView.pause();
                            if (checkSameID(scan_result)) {
                                if (!toastMessageDialog.isShow()) {
                                    toastMessageDialog.setMessageText("產品序列號重複");
                                    toastMessageDialog.show();
                                }
                                barcodeView.resume();
                            } else {
                                DataPojo dataPojo = new DataPojo(classes.getText().toString(), process.getText().toString(), staff.getText().toString(), scan_result, 1, getData(YEAR) + "/" + getData(MONTH) + "/" + getData(DAY_OF_MONTH), getData(HOUR_OF_DAY) + ":" + getData(MINUTE) + ":" + getData(SECOND));
                                exelarratyList.add(0, dataPojo);
                                simpleTextAdapter.setFilter(exelarratyList);
                                recyclerview.scrollToPosition(0);
                                barcodeView.resume();
                            }
                        }
                        preQRcode_result = scan_result;
                    }
                }
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {

            }
        });

    }

    public boolean checkSameID(String scan_result) {
        for (DataPojo dataPojo : exelarratyList) {
            if (dataPojo.id.equals(scan_result)) {
                return true;
            }
        }
        return false;
    }

    public void checkClassAndProcessAndStaff() {
        if (classes.getText().toString().equals("") || process.getText().toString().equals("") || staff.getText().toString().equals("")) {
        } else {
            saveExcel();
            String fileName = getSDPath() + "/Record" + "/" + classes.getText().toString() + "-" + process.getText().toString() + "-" + staff.getText().toString() + "-" + getData(YEAR) + getData(MONTH) + getData(DAY_OF_MONTH) + ".xls";
            exelarratyList = ExcelUtils.read2DB(new File(fileName));
            simpleTextAdapter.setFilter(exelarratyList);
        }
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


    public String getData(byte type) {
        Calendar calendar = Calendar.getInstance();
        switch (type) {
            //获取系统的日期
            case YEAR:
                //年
                return calendar.get(Calendar.YEAR) + "";
            case MONTH:
                //月

                return String.format("%02d", calendar.get(Calendar.MONTH) + 1);
            case DAY_OF_MONTH:
                //日
                return String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)) + "";

            //获取系统时间
            case HOUR_OF_DAY:
                //小时
                return String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + "";

            case MINUTE:
                //分钟
                return String.format("%02d", calendar.get(Calendar.MINUTE)) + "";

            case SECOND:
                //秒
                return String.format("%02d", calendar.get(Calendar.SECOND)) + "";
            default:
                return null;

        }


    }

    /**
     * 导出excel
     */
    public void exportExcel() {
        if (exelarratyList.size() > 0) {
            File file = new File(getSDPath() + "/Record");
            makeDir(file);
            String filetitle = classes.getText().toString() + "-" + process.getText().toString() + "-" + staff.getText().toString() + "-" + getData(YEAR) + getData(MONTH) + getData(DAY_OF_MONTH);
            String fileName = file.toString() + "/" + filetitle + ".xls";
            ExcelUtils.initExcel(fileName, title);
            ExcelUtils.writeObjListToExcel(getRecordData(exelarratyList), fileName, this);
        }
    }

    /**
     * 将数据集合 转化成ArrayList<ArrayList<String>>
     *
     * @return
     */
    private ArrayList<ArrayList<String>> getRecordData(List<DataPojo> dataPojos) {
        ArrayList<ArrayList<String>> recordList = new ArrayList<>();
        for (int i = 0; i < dataPojos.size(); i++) {
            DataPojo dataPojo = dataPojos.get(i);
            ArrayList<String> beanList = new ArrayList<String>();
            beanList.add(dataPojo.classes);
            beanList.add(dataPojo.process);
            beanList.add(dataPojo.staff);
            beanList.add(dataPojo.id);
            beanList.add(dataPojo.number + "");
            beanList.add(dataPojo.date);
            beanList.add(dataPojo.time);
            recordList.add(beanList);
        }
        return recordList;
    }

    private String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
        }
        String dir = sdDir.toString();
        return dir;
    }

    public void makeDir(File dir) {
        if (!dir.getParentFile().exists()) {
            makeDir(dir.getParentFile());
        }
        dir.mkdir();
    }
}
