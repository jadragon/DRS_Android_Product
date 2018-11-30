package com.example.alex.lotteryapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.lotteryapp.library.ExcelUtils;
import com.example.alex.lotteryapp.library.SQLiteDatabaseHandler;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class AllListActivity extends ToolbarActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    Button alllist_btn_export, alllist_btn_reset;
    ListView listView;
    SQLiteDatabaseHandler db;
    SimpleAdapter adapter;

    TextView type;
    String[] array;
    ArrayList<Map<String, String>> items;
    private String[] types = {"頭獎", "二獎", "三獎", "四獎", "五獎", "六獎"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_list);
        array = getResources().getStringArray(R.array.type_list);
        initToolbar(true, "項目列表");
        db = new SQLiteDatabaseHandler(this);
        initView();
        initListener();
    }

    private void initView() {
        //btn
        alllist_btn_export = findViewById(R.id.alllist_btn_export);
        alllist_btn_reset = findViewById(R.id.alllist_btn_reset);
        //list
        listView = findViewById(R.id.alllist_listview);
        items = db.getItems();
        initAdapter();


    }

    private void initAdapter() {
        items = db.getItems();
        adapter = new SimpleAdapter(this, items, R.layout.item_list, new String[]{"type", "gift", "number"}, new int[]{R.id.item_list_type, R.id.item_list_gift, R.id.item_list_number});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
    }

    private void initListener() {
        alllist_btn_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.e("Excel", db.getExcelData() + "");
                exportExcel();
                Toast.makeText(AllListActivity.this, "成功!\n輸出路徑:\n" + getSDPath() + "/捷豹尾牙抽獎名單/", Toast.LENGTH_LONG).show();
            }
        });
        alllist_btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AllListActivity.this);
                builder.setTitle("重置");
                builder.setMessage("重置後所有抽獎名單將無法取回，確定要重置?");
                builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ProgressDialog progressDialog = ProgressDialog.show(AllListActivity.this, "重置中", "請稍後", true);
                        db.resetAllAward();
                        initAdapter();
                        progressDialog.dismiss();
                        Toast.makeText(AllListActivity.this, "重置成功", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        initAdapter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String type = items.get(position).get("type");
        ArrayList<String> winners = db.getWinnerNames(type);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(type);
        builder.setMessage(winners + "");
        builder.show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    /**
     * 讀取excel
     */
    /*
    private void loadExcelData() {
     String fileName = getSDPath() + "/Record" + "/" + classes.getText().toString() + "-" + process.getText().toString() + "-" + staff.getText().toString() + "-" + getData(YEAR) + getData(MONTH) + getData(DAY_OF_MONTH) + ".xls";
       exelarratyList = ExcelUtils.read2DB(new File(fileName));
        simpleTextAdapter.setFilter(exelarratyList);
    }
*/
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    private static final int REQUEST_ALL_PERMISSION = 0x01;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ALL_PERMISSION) {

            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                //判断是否勾选禁止后不再询问
                if (ActivityCompat.shouldShowRequestPermissionRationale(AllListActivity.this, permissions[0])) {
                    Toast.makeText(this, "權限尚未申請", Toast.LENGTH_SHORT).show();
                }
            } else {
                exportExcel();
            }
        }

    }

    /**
     * 導出excel
     */
    public void exportExcel() {
        if (ContextCompat.checkSelfPermission(AllListActivity.this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AllListActivity.this, permissions, REQUEST_ALL_PERMISSION);
            return;
        }
        ArrayList<ArrayList<String>> datas = db.getExcelData();
        if (datas.size() > 0) {
            File file = new File(getSDPath() + "/捷豹尾牙抽獎名單");
            makeDir(file);
            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
            String filetitle = "捷豹尾牙抽獎名單" + sdFormat.format(new Date());
            String fileName = file.toString() + "/" + filetitle + ".xls";
            String[] title = {"獎項", "獎品", "得獎人"};
            ExcelUtils.initExcel(fileName, title);
            ExcelUtils.writeObjListToExcel(datas, fileName, this);
        }
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
