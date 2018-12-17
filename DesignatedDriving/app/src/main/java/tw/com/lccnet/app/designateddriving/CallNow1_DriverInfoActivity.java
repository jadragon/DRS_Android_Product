package tw.com.lccnet.app.designateddriving;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import tw.com.lccnet.app.designateddriving.API.Analyze.AnalyzeUtil;
import tw.com.lccnet.app.designateddriving.API.CallNowApi;
import tw.com.lccnet.app.designateddriving.RecyclerAdapter.MessageRecyclerAdapter;
import tw.com.lccnet.app.designateddriving.Thread.AsyncTaskUtils;
import tw.com.lccnet.app.designateddriving.Thread.IDataCallBack;

public class CallNow1_DriverInfoActivity extends ToolbarActivity implements View.OnClickListener {
    private static final int REQUEST_ALL_PERMISSION = 0x10;
    private Button next, cancel, mp, send;
    private GlobalVariable gv;
    private String pono;
    private TextView ordernum, uname, vaddress, eaddress, distance, opay;
    private MessageRecyclerAdapter messageRecyclerAdapter;
    private String phoneNumber = "";
    private EditText edit_msg;
    private ScheduledFuture scheduledFuture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callnow1_driver_info);
        gv = (GlobalVariable) getApplicationContext();
        pono = getIntent().getStringExtra("pono");
        initToolbar("司機資訊", false);
        initView();
        initListener();
        scheduledFuture = MapsActivity.scheduledThreadPool.scheduleAtFixedRate(r1, 0, 3, TimeUnit.SECONDS);
    }

    private void initView() {
        ordernum = findViewById(R.id.ordernum);
        uname = findViewById(R.id.uname);
        vaddress = findViewById(R.id.vaddress);
        eaddress = findViewById(R.id.eaddress);
        distance = findViewById(R.id.distance);
        opay = findViewById(R.id.opay);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        messageRecyclerAdapter = new MessageRecyclerAdapter(this);
        recyclerView.setAdapter(messageRecyclerAdapter);
        next = findViewById(R.id.next);
        cancel = findViewById(R.id.cancel);
        mp = findViewById(R.id.mp);
        send = findViewById(R.id.send);
        edit_msg = findViewById(R.id.edit_msg);
    }

    private void initListener() {
        next.setOnClickListener(this);
        cancel.setOnClickListener(this);
        mp.setOnClickListener(this);
        send.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next:
                scheduledFuture.cancel(true);
                startActivity(new Intent(CallNow1_DriverInfoActivity.this, CallNow2_CarCheckActivity.class));
                finish();
                break;
            case R.id.cancel:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("取消訂單");
                builder.setMessage("確定要取消訂單?");
                builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        scheduledFuture.cancel(true);
                        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                            @Override
                            public JSONObject onTasking(Void... params) {
                                return CallNowApi.cancel_order(gv.getToken(), pono);
                            }

                            @Override
                            public void onTaskAfter(JSONObject jsonObject) {
                                if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                    finish();
                                }
                                Toast.makeText(CallNow1_DriverInfoActivity.this, AnalyzeUtil.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                break;
            case R.id.mp:
                if (ContextCompat.checkSelfPermission(CallNow1_DriverInfoActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CallNow1_DriverInfoActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_ALL_PERMISSION);
                    return;
                }
                if (!phoneNumber.equals("")) {
                    callPhone();
                } else {
                    Toast.makeText(this, "無法取得手機號碼", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.send:
                if (!edit_msg.getText().toString().equals("")) {
                    scheduledFuture.cancel(true);
                    AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                        @Override
                        public JSONObject onTasking(Void... params) {
                            return CallNowApi.wait_setMsg(gv.getToken(), pono, edit_msg.getText().toString());
                        }

                        @Override
                        public void onTaskAfter(JSONObject jsonObject) {
                            if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                scheduledFuture = MapsActivity.scheduledThreadPool.scheduleAtFixedRate(r1, 0, 3, TimeUnit.SECONDS);
                                edit_msg.setText("");
                            }
                            Toast.makeText(CallNow1_DriverInfoActivity.this, AnalyzeUtil.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ALL_PERMISSION) {

            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                //判断是否勾选禁止后不再询问
                if (ActivityCompat.shouldShowRequestPermissionRationale(CallNow1_DriverInfoActivity.this, permissions[0])) {
                    Toast.makeText(this, "權限已被禁止，請於應用程式中啟動權限", Toast.LENGTH_SHORT).show();
                }
            } else {
                callPhone();
            }
        }

    }

    private void callPhone() {
        Intent intentDial = new Intent("android.intent.action.CALL", Uri.parse("tel:" + phoneNumber));
        startActivity(intentDial);
    }

    /**
     * 取得司機資訊
     *
     * @param json
     */
    public void initData(JSONObject json) {
        try {
            JSONObject order = json.getJSONObject("Data").getJSONObject("order");
            ordernum.setText(order.getString("ordernum"));
            uname.setText(order.getString("uname"));
//            order.getString("img")
            phoneNumber = order.getString("mp");
//            order.getString("score")
            vaddress.setText(order.getString("vaddress"));
            eaddress.setText(order.getString("eaddress"));
            distance.setText(order.getString("distance"));
            opay.setText(order.getString("opay"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 取得留言資訊
     *
     * @param json
     * @return
     */
    public ArrayList<Map<String, String>> getMessage(JSONObject json) {
        ArrayList<Map<String, String>> arrayList = new ArrayList<>();
        try {
            JSONArray msg = json.getJSONObject("Data").getJSONArray("msg");
            Map<String, String> map;
            for (int i = 0; i < msg.length(); i++) {
                map = new HashMap<>();
                json = msg.getJSONObject(i);
                map.put("type", json.getString("type"));
                map.put("msg", json.getString("msg"));
                map.put("cdate", json.getString("cdate"));
                arrayList.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }


    private volatile JSONObject json;
    /**
     * 取得司機資訊
     */
    Runnable r1 = new Runnable() {
        @Override
        public void run() {
            json = CallNowApi.wait_driverInfo(gv.getToken(), pono);
            if (AnalyzeUtil.checkSuccess(json)) {
                runOnUiThread(u1);
            }
        }
    };


    Runnable u1 = new Runnable() {
        @Override
        public void run() {
            checkAstatusOrAbstatus(json);
            initData(json);
            ArrayList<Map<String, String>> messageList = getMessage(json);
            if (messageList.size() > 0) {
                messageRecyclerAdapter.setFilter(messageList);
            }
        }
    };

    /**
     * 判斷是否到達或放鳥
     *
     * @param json
     */
    private void checkAstatusOrAbstatus(JSONObject json) {
        try {
            json = json.getJSONObject("Data");
            //司機到達狀態0未到達1已到達
            String astatus = json.getString("astatus");
            if (astatus.equals("1")) {
                scheduledFuture.cancel(true);
                Toast.makeText(this, "司機說你已離開", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            //乘客被遺棄狀態0未遺棄1已被遺棄
            String abstatus = json.getString("abstatus");
            if (abstatus.equals("1")) {
                next.setText("司機已到達");
                next.setEnabled(true);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scheduledFuture.cancel(true);
    }
}
