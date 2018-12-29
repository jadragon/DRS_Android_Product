package com.example.alex.eip_product;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import db.OrderDatabase;

import static db.OrderDatabase.KEY_PONumber;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView home, back;
    private FragmentManager fragmentManager;
    private Button update;
    private ProgressDialog progressDialog;
    private HandlerThread handlerThread;
    private Handler mHandler;
    private OrderDatabase db;
    private GlobalVariable gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new OrderDatabase(this);
        gv = (GlobalVariable) getApplicationContext();
        initHandler();
        initButton();
        initFragment();
        // initRecylcerView();
    }

    private void initButton() {
        update = findViewById(R.id.update_btn);
        update.setOnClickListener(this);
        home = findViewById(R.id.home_btn);
        home.setOnClickListener(this);
        back = findViewById(R.id.back_btn);
        back.setOnClickListener(this);
    }

    private void initFragment() {
        fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {//監聽迴車事件
            @Override
            public void onBackStackChanged() {//監聽fragment返回事件
                if (fragmentManager.findFragmentById(R.id.fragment_content).getTag().equals("home")) {//判斷當前fragment的tag是否為home
                    home.setVisibility(View.INVISIBLE);
                    update.setVisibility(View.VISIBLE);
                    back.setVisibility(View.INVISIBLE);
                } else {
                    home.setVisibility(View.VISIBLE);
                    update.setVisibility(View.INVISIBLE);
                    back.setVisibility(View.VISIBLE);
                }
            }
        });
        Fragment fragment_home = new Fragment_home();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragment_content, fragment_home, "home").commit();
    }

    public void initHandler() {
        handlerThread = new HandlerThread("soap");
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper());
    }

    private Runnable runnable_refresh = new Runnable() {
        @Override
        public void run() {
            try {
                JSONObject json = new API_OrderInfo().getOrderInfo(gv.getUsername(), gv.getPw());
                if (AnalyzeUtil.checkSuccess(json)) {
                    Map<String, List<ContentValues>> map = Analyze_Order.getOrders(json);
                    if (db.addOrders(map.get("Orders"))) {
                        db.addOrderDetails(map.get("OrderDetails"));
                        db.addCheckFailedReasons(map.get("CheckFailedReasons"));
                        db.addOrderComments(map.get("OrderComments"));
                        db.addOrderItemComments(map.get("OrderItemComments"));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, getResources().getString(R.string.refresh_success), Toast.LENGTH_SHORT).show();
                                Log.e("Update", "Orders:" + db.countOrders() + "\nOrderDetails:" + db.countOrderDetails() + "\nCheckFailedReasons:" + db.countCheckFailedReasons() + "\nOrderComments:" + db.countOrderComments() + "\nOrderItemComments:" + db.countOrderItemComments() +
                                        "\nOrdersEdit:" + db.countOrdersEdit() + "\nOrderDetailsEdit:" + db.countOrderDetailsEdit() + "\nCheckFailedReasonsEdit:" + db.countCheckFailedReasonsEdit() + "\nOrderCommentsEdit:" + db.countOrderCommentsEdit() + "\nOrderItemCommentsEdit:" + db.countOrderItemCommentsEdit());
                                progressDialog.dismiss();
                            }
                        });
                    } else {
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
                                            Map<String, JSONObject> map = db.getAllUpdateDataByPONumber();
                                            for (String key : map.keySet()) {
                                                new API_OrderInfo().updateCheckOrder(gv.getUsername(), gv.getPw(), map.get(key));
                                                db.updateOrdersUpdate(key);
                                            }
                                            dialogInterface.dismiss();
                                            mHandler.post(runnable_refresh);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        } catch (XmlPullParserException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        } finally {
                                            progressDialog.dismiss();
                                        }
                                    }
                                });

                            }
                        });
                        builder.show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, AnalyzeUtil.getMessage(json), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, getResources().getString(R.string.exception), Toast.LENGTH_SHORT).show();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, getResources().getString(R.string.exception), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, getResources().getString(R.string.exception), Toast.LENGTH_SHORT).show();
            } finally {
                progressDialog.dismiss();
            }
        }
    };

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
            if (isExit == false) {
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
        handlerThread.quit();
        db.close();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_btn:
                progressDialog = ProgressDialog.show(MainActivity.this, getResources().getString(R.string.loading_data), getResources().getString(R.string.wait), true);
                mHandler.post(runnable_refresh);
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
}