package tw.com.lccnet.app.designateddriving;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tw.com.lccnet.app.designateddriving.API.Analyze.AnalyzeUtil;
import tw.com.lccnet.app.designateddriving.API.CallNowApi;
import tw.com.lccnet.app.designateddriving.RecyclerAdapter.MessageRecyclerAdapter;

public class CallNow1_DriverInfoActivity extends ToolbarActivity {
    private Button next;
    private GlobalVariable gv;
    private String pono;
    private JSONObject json;
    private TextView ordernum, uname, vaddress, eaddress, distance, opay;
    private MessageRecyclerAdapter messageRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callnow1_driver_info);
        gv = (GlobalVariable) getApplicationContext();
        pono = getIntent().getStringExtra("pono");
        initToolbar("司機資訊", false);
        initView();
        initButton();
        //  initThread();

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
    }

    public void initData(JSONObject json) {
        try {
            JSONObject order = json.getJSONObject("Data").getJSONObject("order");
            ordernum.setText(order.getString("ordernum"));
            uname.setText(order.getString("uname"));
//            order.getString("img")
//            order.getString("mp")
//            order.getString("score")
            vaddress.setText(order.getString("vaddress"));
            eaddress.setText(order.getString("eaddress"));
            distance.setText(order.getString("distance"));
            opay.setText(order.getString("opay"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

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

    private void initThread() {
        MapsActivity.mThreadHandler.post(r1);
    }


    private void initButton() {
        next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //     startActivity(new Intent(CallNow1_DriverInfoActivity.this, CallNow2_CarCheckActivity.class));
                //    finish();
                sendAgain = false;
                MapsActivity.mThreadHandler.removeCallbacks(r1);
            }
        });
    }


    @Override
    public void onBackPressed() {
    }

    private boolean sendAgain = true;
    Runnable r1 = new Runnable() {
        @Override
        public void run() {
            if (sendAgain) {
                json = CallNowApi.wait_driverInfo(gv.getToken(), pono);
                if (AnalyzeUtil.checkSuccess(json)) {
                    runOnUiThread(u1);
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                MapsActivity.mThreadHandler.post(r1);
            } else {
                MapsActivity.mThreadHandler.removeCallbacks(r1);
            }
        }
    };


    Runnable u1 = new Runnable() {
        @Override
        public void run() {
            try {
                initData(json);
                Toast.makeText(CallNow1_DriverInfoActivity.this, json.getJSONObject("Data").getString("astatus"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}
