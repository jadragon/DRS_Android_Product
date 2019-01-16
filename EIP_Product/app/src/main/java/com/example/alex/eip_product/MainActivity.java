package com.example.alex.eip_product;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.alex.eip_product.SoapAPI.API_OrderInfo;
import com.example.alex.eip_product.SoapAPI.Analyze.AnalyzeUtil;
import com.example.alex.eip_product.SoapAPI.Analyze.Analyze_Order;
import com.example.alex.eip_product.fragment.Fragment_home;
import com.example.alex.eip_product.pojo.AllupdateDataPojo;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import Utils.CommonUtil;
import Utils.FileUtils;
import Utils.PreferenceUtil;
import db.OrderDatabase;
import liabiry.Http.pojo.TaskInfo;
import liabiry.Http.pojo.TaskList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 0x01;
    private ImageView home, back;
    private FragmentManager fragmentManager;
    private Button update, download, logout;
    private ProgressDialog progressDialog;
    private static HandlerThread handlerThread;
    private static Handler mHandler;
    private OrderDatabase db;
    private GlobalVariable gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new OrderDatabase(this);
        gv = (GlobalVariable) getApplicationContext();
        getHandler();
        initButton();
        if (savedInstanceState == null) {
            initFragment();
        }

        // initRecylcerView();
    }

    private void initButton() {
        update = findViewById(R.id.update_btn);
        update.setOnClickListener(this);
        home = findViewById(R.id.home_btn);
        home.setOnClickListener(this);
        back = findViewById(R.id.back_btn);
        back.setOnClickListener(this);
        download = findViewById(R.id.download_btn);
        download.setOnClickListener(this);
        logout = findViewById(R.id.logout_btn);
        logout.setOnClickListener(this);

    }

    private void initFragment() {
        fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {//監聽迴車事件
            @Override
            public void onBackStackChanged() {//監聽fragment返回事件
                if (fragmentManager.findFragmentById(R.id.fragment_content).getTag().equals("home")) {//判斷當前fragment的tag是否為home
                    home.setVisibility(View.INVISIBLE);
                    update.setVisibility(View.VISIBLE);
                    download.setVisibility(View.VISIBLE);
                    logout.setVisibility(View.VISIBLE);
                    back.setVisibility(View.INVISIBLE);
                } else {
                    home.setVisibility(View.VISIBLE);
                    update.setVisibility(View.INVISIBLE);
                    download.setVisibility(View.INVISIBLE);
                    logout.setVisibility(View.INVISIBLE);
                    back.setVisibility(View.VISIBLE);
                }
            }
        });
        Fragment fragment_home = new Fragment_home();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragment_content, fragment_home, "home").commit();
    }

    public static Handler getHandler() {
        if (handlerThread == null) {
            handlerThread = new HandlerThread("soap");
            handlerThread.start();
        }
        if (mHandler == null) {
            mHandler = new Handler(handlerThread.getLooper());
        }
        return mHandler;
    }

    private Runnable runnable_refresh = new Runnable() {
        @Override
        public void run() {
            try {
                if (db.checkOrders()) {
                    JSONObject json = new API_OrderInfo().getOrderInfo(gv.getUsername(), gv.getPw());
                    if (AnalyzeUtil.checkSuccess(json)) {
                        Map<String, List<ContentValues>> map = Analyze_Order.getOrders(json);
                        db.addOrders(map.get("Orders"));
                        db.addOrderDetails(map.get("OrderDetails"));
                        db.addCheckFailedReasons(map.get("CheckFailedReasons"));
                        db.addOrderComments(map.get("OrderComments"));
                        db.addOrderItemComments(map.get("OrderItemComments"));
                        json = new API_OrderInfo().getItemDrawing(gv.getUsername(), gv.getPw());
                        if (AnalyzeUtil.checkSuccess(json)) {
                            db.addItemDrawings(Analyze_Order.getItemDrawings(json));
                            json = new API_OrderInfo().getDrawingFile(gv.getUsername(), gv.getPw());
                            if (AnalyzeUtil.checkSuccess(json)) {
                                db.addFiles(Analyze_Order.getFiles(json));
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this, getResources().getString(R.string.refresh_success), Toast.LENGTH_SHORT).show();
                                        Log.e("Update", "Orders:" + db.countOrders() + "\nOrderDetails:" + db.countOrderDetails() + "\nCheckFailedReasons:"
                                                + db.countCheckFailedReasons() + "\nOrderComments:" + db.countOrderComments() + "\nOrderItemComments:" + db.countOrderItemComments());
                                    }
                                });
                            }
                        }

                    } else {
                        CommonUtil.toastErrorMessage(MainActivity.this, getResources().getString(R.string.warning), AnalyzeUtil.getMessage(json));
                    }
                } else {
                    showUpdateDialog();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                CommonUtil.toastErrorMessage(MainActivity.this, getResources().getString(R.string.warning), getResources().getString(R.string.download_fail_info));
            } catch (XmlPullParserException e) {
                e.printStackTrace();
                CommonUtil.toastErrorMessage(MainActivity.this, getResources().getString(R.string.warning), getResources().getString(R.string.download_fail_info));
            } catch (IOException e) {
                e.printStackTrace();
                CommonUtil.toastErrorMessage(MainActivity.this, getResources().getString(R.string.warning), getResources().getString(R.string.download_fail_info));
            } finally {
                progressDialog.dismiss();
            }
        }
    };

    private void showUpdateDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(getResources().getString(R.string.warning));
                builder.setMessage(getResources().getString(R.string.not_update_message));
                builder.setNegativeButton(getResources().getString(R.string.table_button1), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton(getResources().getString(R.string.update), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {
                        progressDialog = ProgressDialog.show(MainActivity.this, getResources().getString(R.string.updating), getResources().getString(R.string.wait), true);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    List<AllupdateDataPojo> pojos = db.getAllUpdateDataByPONumber();
                                    JSONObject json;
                                    for (AllupdateDataPojo pojo : pojos) {
                                        json = new API_OrderInfo().updateCheckOrder(gv.getUsername(), gv.getPw(), pojo.getJsonObject() + "");
                                        if (AnalyzeUtil.checkSuccess(json)) {
                                            db.updateOrdersUpdate(pojo.getPONumber());
                                        } else {
                                            CommonUtil.toastErrorMessage(MainActivity.this, getResources().getString(R.string.warning), AnalyzeUtil.getMessage(json));
                                            return;
                                        }
                                    }
                                    progressDialog.dismiss();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog = ProgressDialog.show(MainActivity.this, getResources().getString(R.string.loading_data), getResources().getString(R.string.wait), true);
                                        }
                                    });
                                    mHandler.post(runnable_refresh);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    progressDialog.dismiss();
                                    CommonUtil.toastErrorMessage(MainActivity.this, getResources().getString(R.string.warning), getResources().getString(R.string.download_fail_info));
                                } catch (XmlPullParserException e) {
                                    e.printStackTrace();
                                    progressDialog.dismiss();
                                    CommonUtil.toastErrorMessage(MainActivity.this, getResources().getString(R.string.warning), getResources().getString(R.string.download_fail_info));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    progressDialog.dismiss();
                                    CommonUtil.toastErrorMessage(MainActivity.this, getResources().getString(R.string.warning), getResources().getString(R.string.download_fail_info));
                                }

                            }
                        });

                    }
                });
                builder.show();
            }
        });

    }

    private void recheckupdate() {
        progressDialog.dismiss();
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getResources().getString(R.string.exception));
        builder.setMessage(getResources().getString(R.string.download_fail_info));
        builder.setPositiveButton(getResources().getString(R.string.reupdate), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog = ProgressDialog.show(MainActivity.this, getResources().getString(R.string.loading_data), getResources().getString(R.string.wait), true);
                        mHandler.post(runnable_refresh);
                    }
                });

            }
        });
        builder.setNegativeButton(getResources().getString(R.string.table_button1), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    /**
     * 切换Fragment
     */
    public void switchFrament(Fragment fragmentto, String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);//跳轉動畫
        Fragment fragmentfrom = fragmentManager.findFragmentByTag(tag);
        if (fragmentfrom == null) {//判斷要跳轉的fragment是否存在
            List<Fragment> fragmentList = fragmentManager.getFragments();
            //隱藏上一個fragment,加入新的fragment並新增於迴車棧中
            transaction.hide(fragmentList.get(fragmentList.size() - 1)).add(R.id.fragment_content, fragmentto, tag).addToBackStack(tag).commit();
        }
    }

    /**
     * 迴車鍵離開程式
     */
    private static Boolean isExit = false;
    private static Boolean hasTask = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK && fragmentManager.getFragments().size() <= 1) {
            Timer tExit = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                    hasTask = false;
                }
            };
            if (!isExit) {
                isExit = true;
                Toast.makeText(this, getResources().getString(R.string.close_app)
                        , Toast.LENGTH_SHORT).show();
                if (!hasTask) {
                    tExit.schedule(task, 2000);
                }
            } else {
                finish();
                System.exit(0);
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout_btn:
                if (db.checkOrders()) {
                    gv.setUsername(null);
                    gv.setPw(null);
                    PreferenceUtil.commitString("username", "");
                    PreferenceUtil.commitString("pw", "");
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.logout_success), Toast.LENGTH_SHORT).show();
                } else {
                    showUpdateDialog();
                }
                break;
            case R.id.update_btn:
                progressDialog = ProgressDialog.show(MainActivity.this, getResources().getString(R.string.loading_data), getResources().getString(R.string.wait), true);
                mHandler.post(runnable_refresh);
                break;
            case R.id.download_btn:
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    initDownloadDialog();
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                            }, REQUEST_WRITE_EXTERNAL_STORAGE);
                }

                break;
            case R.id.home_btn:
                for (int i = 0; i < fragmentManager.getFragments().size() - 1; i++) {
                    fragmentManager.popBackStack();
                }
                break;
            case R.id.back_btn:
                fragmentManager.popBackStack();
                break;

        }
    }

    private void initDownloadDialog() {
        List<TaskInfo> dowloadFiles = db.getDowloadFiles();
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage(getResources().getString(R.string.downloading));
        progressDialog.setMax(dowloadFiles.size());
        progressDialog.setProgress(0);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        mHandler.post(new DownloadRunnable(new TaskList(dowloadFiles, dowloadFiles.size(), 0)));
    }

    public class DownloadRunnable implements Runnable {
        private TaskList list;//下载信息JavaBean

        public DownloadRunnable(TaskList list) {
            this.list = list;
        }

        /**
         * Runnable的run方法，进行文件下载
         */
        public void run() {
            try {
                if (list.getTaskInfos().size() > 0) {
                    FileUtils fileUtils = new FileUtils();
                    HttpURLConnection conn;//http连接对象
                    FileOutputStream f = null;
                    InputStream in = null;
                    fileUtils.creatSDDir("EIP");
                    int len;//每次读取的数组长度
                    byte[] buffer = new byte[1024 * 8];//流读写的缓冲区
                    for (int i = 0; i < list.getTaskInfos().size(); i++) {
                        conn = (HttpURLConnection) new URL(list.getTaskInfos().get(i).getFilePath()).openConnection();
                        conn.setConnectTimeout(5000);//连接超时时间
                        conn.setReadTimeout(5000);//读取超时时间
                        conn.setRequestMethod("GET");//请求类型为GET
                        conn.connect();//连接
                        in = conn.getInputStream();

                        //通过文件路径和文件名实例化File
                        if (fileUtils.isFileExist(list.getTaskInfos().get(i).getFileName() + ".pdf", Integer.parseInt(conn.getHeaderField("content-length")))) {
                            list.setCompletedLen(i);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.setProgress((int) list.getCompletedLen());
                                }
                            });
                        } else {
                            File file = fileUtils.creatSDFile(list.getTaskInfos().get(i).getFileName() + ".pdf");
                            f = new FileOutputStream(file);
                            //从流读取字节数组到缓冲区
                            while ((len = in.read(buffer)) > 0) {
                                f.write(buffer, 0, len);
                            }
                            list.setCompletedLen(i);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.setProgress((int) list.getCompletedLen());
                                }
                            });
                        }
                        conn.disconnect();
                    }
                    if (f != null) {
                        f.flush();
                        f.close();
                    }
                    if (in != null)
                        in.close();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, getResources().getString(R.string.download_success), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            } catch (IOException e) {
                Log.e("download", e.toString());
                e.printStackTrace();
                progressDialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(getResources().getString(R.string.download_fail));
                builder.setMessage(getResources().getString(R.string.download_fail_info));
                builder.setPositiveButton(getResources().getString(R.string.redownload), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initDownloadDialog();
                            }
                        });

                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.table_button1), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            } finally {
                progressDialog.dismiss();
            }
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
                    initDownloadDialog();
                }
                break;
        }
    }

}