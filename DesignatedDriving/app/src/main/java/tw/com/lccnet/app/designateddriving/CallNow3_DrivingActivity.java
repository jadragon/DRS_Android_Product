package tw.com.lccnet.app.designateddriving;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import tw.com.lccnet.app.designateddriving.API.Analyze.AnalyzeUtil;
import tw.com.lccnet.app.designateddriving.API.CallNowApi;
import tw.com.lccnet.app.designateddriving.Component.SlideDialog;
import tw.com.lccnet.app.designateddriving.Thread.AsyncTaskUtils;
import tw.com.lccnet.app.designateddriving.Thread.IDataCallBack;

public class CallNow3_DrivingActivity extends ToolbarActivity implements View.OnClickListener {
    private static final int AUTO_PLACE_COMPETE_END = 0x10;
    private Button change;
    private TextView ordernum, uname, vaddress, eaddress, opay;
    private GlobalVariable gv;
    private String pono;
    private ScheduledFuture scheduledFuture, scheduledFuture1;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callnow3_driving);
        gv = (GlobalVariable) getApplicationContext();
        pono = getIntent().getStringExtra("pono");
        initToolbar("駕駛中", false);
        initView();
        initData();
        scheduledFuture1 = MapsActivity.scheduledThreadPool.scheduleAtFixedRate(r2, 0, 5, TimeUnit.SECONDS);
    }

    private void initView() {
        ordernum = findViewById(R.id.ordernum);
        uname = findViewById(R.id.uname);
        vaddress = findViewById(R.id.vaddress);
        eaddress = findViewById(R.id.eaddress);
        opay = findViewById(R.id.opay);
        change = findViewById(R.id.change);
        change.setOnClickListener(this);
    }

    private void initData() {
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public JSONObject onTasking(Void... params) {
                return CallNowApi.wait_driverInfo(gv.getToken(), pono);
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                if (AnalyzeUtil.checkSuccess(jsonObject)) {
                    try {
                        jsonObject = jsonObject.getJSONObject("Data").getJSONObject("order");
                        ordernum.setText("訂單編號:" + jsonObject.getString("ordernum"));
                        uname.setText(jsonObject.getString("uname"));
                        //order.getString("img")
                        //order.getString("score")
                        vaddress.setText(jsonObject.getString("vaddress"));
                        eaddress.setText(jsonObject.getString("eaddress"));
                        opay.setText("總計:" + jsonObject.getString("opay"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

    }

    private Dialog dialog;
    private TextView end;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.change:
                showSlideDialog();
                break;
            case R.id.end:

                try {
                    AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                            //  .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                            .setCountry("TW")
                            .build();
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .setFilter(typeFilter)
                                    .build(this);
                    startActivityForResult(intent, AUTO_PLACE_COMPETE_END);
                } catch (GooglePlayServicesRepairableException e) {
                } catch (GooglePlayServicesNotAvailableException e) {
                }
                break;
            case R.id.confirm:
                if (TextUtils.isEmpty(end.getText())) {
                    end.setError("請輸入目的地");
                    return;
                }
                address = end.getText().toString();
                AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                    @Override
                    public JSONObject onTasking(Void... params) {
                        return CallNowApi.start_change(gv.getToken(), pono, address);
                    }

                    @Override
                    public void onTaskAfter(JSONObject jsonObject) {
                        if (AnalyzeUtil.checkSuccess(jsonObject)) {
                            if (scheduledFuture != null)
                                scheduledFuture.cancel(true);
                            scheduledFuture = MapsActivity.scheduledThreadPool.scheduleAtFixedRate(r1, 0, 5, TimeUnit.SECONDS);
                            showProgressDialog();
                        }
                    }
                });
                hideProgressDialog();
                break;
            case R.id.cancel:
                break;

        }

    }

    private void showSlideDialog() {
        if (dialog == null) {
            dialog = new SlideDialog(this);
            dialog.setContentView(getLayoutInflater().inflate(R.layout.item_slide_dialog, null));
            dialog.findViewById(R.id.start).setVisibility(View.GONE);
            end = dialog.findViewById(R.id.end);
            dialog.findViewById(R.id.layout).setVisibility(View.GONE);
            Button confirm = dialog.findViewById(R.id.confirm);
            Button cancel = dialog.findViewById(R.id.cancel);
            end.setOnClickListener(this);
            confirm.setOnClickListener(this);
            cancel.setOnClickListener(this);
            dialog.show();
        } else {
            dialog.dismiss();
            dialog = new SlideDialog(this);
            dialog.setContentView(getLayoutInflater().inflate(R.layout.item_slide_dialog, null));
            dialog.findViewById(R.id.start).setVisibility(View.GONE);
            end = dialog.findViewById(R.id.end);
            dialog.findViewById(R.id.layout).setVisibility(View.GONE);
            Button confirm = dialog.findViewById(R.id.confirm);
            Button cancel = dialog.findViewById(R.id.cancel);
            end.setOnClickListener(this);
            confirm.setOnClickListener(this);
            cancel.setOnClickListener(this);
            dialog.show();
        }
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
            textView.setText("等待司機確認變更");
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
            textView.setText("等待司機確認變更");
            dialog.show();
        }
    }

    private void hideProgressDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {

    }

    private JSONObject json;
    Runnable r1 = new Runnable() {
        @Override
        public void run() {
            json = CallNowApi.start_watiConfirm(gv.getToken(), pono);
            if (AnalyzeUtil.checkSuccess(json)) {
                //目的地更改狀態(0無需更改,1客戶提出需更改,2司機拒絕更改,3司機同意更改)
                try {
                    String Data = json.getString("Data");
                    if (Data.equals("2")) {
                        scheduledFuture.cancel(true);
                        MapsActivity.UiThreadHandler.post(u1);
                    } else if (Data.equals("3")) {
                        json = CallNowApi.start_newOrder(gv.getToken(), pono, "", gv.getmLongitude() + "", gv.getmLatitude() + "", address, "", "");
                        if (AnalyzeUtil.checkSuccess(json)) {
                            scheduledFuture.cancel(true);
                            MapsActivity.UiThreadHandler.post(u2);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    Runnable r2 = new Runnable() {
        @Override
        public void run() {
            json = CallNowApi.start_arrival(gv.getToken(), pono);
            if (AnalyzeUtil.checkSuccess(json)) {
                //目的地到達狀態(0未到達1已到達)
                try {
                    String Data = json.getString("Data");
                    if (Data.equals("1")) {
                        scheduledFuture.cancel(true);
                        scheduledFuture1.cancel(true);
                        MapsActivity.UiThreadHandler.post(u1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    Runnable u1 = new Runnable() {
        @Override
        public void run() {
            hideProgressDialog();
            Toast.makeText(CallNow3_DrivingActivity.this, AnalyzeUtil.getMessage(json), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CallNow3_DrivingActivity.this, CallNow5_CountActivity.class);
            intent.putExtra("pono", pono);
            startActivity(intent);
            finish();
        }

    };
    Runnable u2 = new Runnable() {
        @Override
        public void run() {
            hideProgressDialog();
            Toast.makeText(CallNow3_DrivingActivity.this, AnalyzeUtil.getMessage(json), Toast.LENGTH_SHORT).show();
        }

    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTO_PLACE_COMPETE_END) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                end.setText(place.getAddress());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.i("place", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (scheduledFuture != null)
            scheduledFuture.cancel(true);
        if (scheduledFuture1 != null)
            scheduledFuture1.cancel(true);
        super.onDestroy();
    }
}
