package com.example.alex.eip_product;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.alex.eip_product.SoapAPI.API_OrderInfo;
import com.example.alex.eip_product.SoapAPI.Analyze.Analyze_Order;
import com.example.alex.eip_product.fragment.Fragment_home;

import org.json.JSONException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import db.OrderDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView home, back;
    private FragmentManager fragmentManager;
    private Button update;
    private ProgressDialog progressDialog;
    private HandlerThread handlerThread;
    private Handler mHandler, UiHandler;
    private OrderDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new OrderDatabase(this);
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
        UiHandler = new Handler(getMainLooper());
        handlerThread = new HandlerThread("soap");
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper(), mCallback);
    }

    Handler.Callback mCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            try {
                db.resetTables();
                Map<String, List<ContentValues>> map = Analyze_Order.getOrders(new API_OrderInfo().getOrderInfo());
                db.addOrders(map.get("Orders"));
                db.addOrderDetails(map.get("OrderDetails"));
                db.addOrderComments(map.get("OrderComments"));
                db.addOrderItemComments(map.get("OrderItemComments"));
                UiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Orders:" + db.countOrders() + "\nOrderDetails:" + db.countOrderDetails() + "\nOrderComments:" + db.countOrderComments() + "\nOrderItemComments:" + db.countOrderItemComments(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "JSONException存取異常", Toast.LENGTH_SHORT).show();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "XmlPullParserException存取異常", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "IOException存取異常", Toast.LENGTH_SHORT).show();
            } finally {
                progressDialog.dismiss();
            }
            return false;
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
                Toast.makeText(this, "再按一次後退鍵退出應用程式"
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
        mHandler.removeCallbacksAndMessages(null);
        db.close();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_btn:
                progressDialog = ProgressDialog.show(MainActivity.this, "讀取資料中", "請稍後", true);
                mHandler.sendEmptyMessageDelayed(0, 0);
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