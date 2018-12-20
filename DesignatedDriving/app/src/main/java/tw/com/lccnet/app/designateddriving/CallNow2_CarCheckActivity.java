package tw.com.lccnet.app.designateddriving;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import tw.com.lccnet.app.designateddriving.API.Analyze.AnalyzeUtil;
import tw.com.lccnet.app.designateddriving.API.CallNowApi;
import tw.com.lccnet.app.designateddriving.Component.CarImageView;
import tw.com.lccnet.app.designateddriving.Thread.AsyncTaskUtils;
import tw.com.lccnet.app.designateddriving.Thread.IDataCallBack;

public class CallNow2_CarCheckActivity extends ToolbarActivity implements View.OnClickListener {

    private List<CarImageView> list;
    /*
    private int[] image1 = {R.drawable.car1, R.drawable.car2, R.drawable.car3, R.drawable.car4, R.drawable.car5, R.drawable.car6, R.drawable.car7, R.drawable.car7,
            R.drawable.car9, R.drawable.car9, R.drawable.car11, R.drawable.car12, R.drawable.car13, R.drawable.car14, R.drawable.car15, R.drawable.car16, R.drawable.car16, R.drawable.car16, R.drawable.car16};
    private int[] image2 = {R.drawable.car_red1, R.drawable.car_red2, R.drawable.car_red3, R.drawable.car_red4, R.drawable.car_red5, R.drawable.car_red6, R.drawable.car_red7, R.drawable.car_red8,
            R.drawable.car_red9, R.drawable.car_red10, R.drawable.car_red11, R.drawable.car_red12, R.drawable.car_red13, R.drawable.car_red14, R.drawable.car_red15, R.drawable.car_red16, R.drawable.car_red17, R.drawable.car_red18, R.drawable.car_red19};
  */
    private JSONObject json;
    private Button next, recheck;
    private ScheduledFuture scheduledFuture;
    private GlobalVariable gv;
    private String pono;
    private TextView cmilage, note;
    private Dialog dialog;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callnow2_car_check);
        gv = (GlobalVariable) getApplicationContext();
        pono = getIntent().getStringExtra("pono");
        initToolbar("汽車檢查", true);
        initView();
        scheduledFuture = MapsActivity.scheduledThreadPool.scheduleAtFixedRate(r1, 0, 3, TimeUnit.SECONDS);
        showProgressDialog();
    }

    private void initView() {
        next = findViewById(R.id.next);
        next.setOnClickListener(this);
        recheck = findViewById(R.id.recheck);
        recheck.setOnClickListener(this);
        cmilage = findViewById(R.id.cmilage);
        note = findViewById(R.id.note);

        list = new ArrayList<>();
        list.add((CarImageView) findViewById(R.id.car1));
        list.add((CarImageView) findViewById(R.id.car2));
        list.add((CarImageView) findViewById(R.id.car3));
        list.add((CarImageView) findViewById(R.id.car4));
        list.add((CarImageView) findViewById(R.id.car5));
        list.add((CarImageView) findViewById(R.id.car6));
        list.add((CarImageView) findViewById(R.id.car7));
        list.add((CarImageView) findViewById(R.id.car8));
        list.add((CarImageView) findViewById(R.id.car9));
        list.add((CarImageView) findViewById(R.id.car10));
        list.add((CarImageView) findViewById(R.id.car11));
        list.add((CarImageView) findViewById(R.id.car12));
        list.add((CarImageView) findViewById(R.id.car13));
        list.add((CarImageView) findViewById(R.id.car14));
        list.add((CarImageView) findViewById(R.id.car15));
        list.add((CarImageView) findViewById(R.id.car16));
        list.add((CarImageView) findViewById(R.id.car17));
        list.add((CarImageView) findViewById(R.id.car18));
        list.add((CarImageView) findViewById(R.id.car19));
        /*
        for (CarImageView carImageView : list) {
            carImageView.setOnClickListener(this);
        }
        */
    }

    private void showProgressDialog() {
        if (dialog == null) {
            dialog = new Dialog(this);

            dialog.setContentView(R.layout.item_wait_dialog);
            dialog.findViewById(R.id.cancel).setOnClickListener(this);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            TextView textView = dialog.findViewById(R.id.message);
            dialog.findViewById(R.id.cancel).setVisibility(View.GONE);
            textView.setText("等待司機檢查車輛");
            dialog.show();
        } else {
            dialog.dismiss();
            dialog = new Dialog(this);
            dialog.setContentView(R.layout.item_wait_dialog);
            dialog.findViewById(R.id.cancel).setOnClickListener(this);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            TextView textView = dialog.findViewById(R.id.message);
            dialog.findViewById(R.id.cancel).setVisibility(View.GONE);
            textView.setText("等待司機檢查車輛");
            dialog.show();
        }
    }

    private void hideDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next:
                AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                    @Override
                    public JSONObject onTasking(Void... params) {
                        return CallNowApi.wait_carconfirm(gv.getToken(), pono);
                    }

                    @Override
                    public void onTaskAfter(JSONObject jsonObject) {
                        if (AnalyzeUtil.checkSuccess(jsonObject)) {
                            scheduledFuture.cancel(true);
                            Intent intent = new Intent(CallNow2_CarCheckActivity.this, CallNow3_DrivingActivity.class);
                            intent.putExtra("pono", pono);
                            startActivity(intent);
                        }
                    }
                });
                break;
            case R.id.recheck:
                AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                    @Override
                    public JSONObject onTasking(Void... params) {
                        return CallNowApi.wait_catreset(gv.getToken(), pono);
                    }

                    @Override
                    public void onTaskAfter(JSONObject jsonObject) {
                        if (AnalyzeUtil.checkSuccess(jsonObject)) {
                            showProgressDialog();
                            scheduledFuture.cancel(true);
                            scheduledFuture = MapsActivity.scheduledThreadPool.scheduleAtFixedRate(r1, 0, 3, TimeUnit.SECONDS);
                        }
                    }
                });
                break;
        }

        /*
        if (view instanceof CarImageView) {
            CarImageView carImageView = ((CarImageView) view);
            if (carImageView.isCheck()) {
                carImageView.setBackgroundColor(getResources().getColor(R.color.invisible));
                carImageView.setImageResource(image1[list.indexOf(view)]);
            } else {
                carImageView.setBackgroundColor(getResources().getColor(R.color.invisible_red));
                carImageView.setImageResource(image2[list.indexOf(view)]);
            }
            carImageView.setCheck(!carImageView.isCheck());
        } else {
            startActivity(new Intent(CallNow2_CarCheckActivity.this, CallNow3_DrivingActivity.class));
            finish();
        }
        */
    }

    /**
     * 取得檢查車輛資訊
     */
    Runnable r1 = new Runnable() {
        @Override
        public void run() {
            json = CallNowApi.wait_carcheck(gv.getToken(), pono);
            if (AnalyzeUtil.checkSuccess(json)) {
                scheduledFuture.cancel(true);
                MapsActivity.UiThreadHandler.post(u1);
            }
        }
    };

    Runnable u1 = new Runnable() {
        @Override
        public void run() {
            next.setEnabled(true);
            recheck.setEnabled(true);
            initData(json);
        }

    };

    private void initData(JSONObject json) {
        hideDialog();
        try {
            JSONObject data = json.getJSONObject("Data");
            // TODO: 2018/12/17 解析 car_value
//            data.getString("car_value");
            cmilage.setText(data.getString("cmilage"));
            note.setText(data.getString("note"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
