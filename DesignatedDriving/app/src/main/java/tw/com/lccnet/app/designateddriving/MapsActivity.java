package tw.com.lccnet.app.designateddriving;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import tw.com.lccnet.app.designateddriving.API.Analyze.AnalyzeUtil;
import tw.com.lccnet.app.designateddriving.API.CallNowApi;
import tw.com.lccnet.app.designateddriving.API.CustomerApi;
import tw.com.lccnet.app.designateddriving.Component.SlideDialog;
import tw.com.lccnet.app.designateddriving.Thread.AsyncTaskUtils;
import tw.com.lccnet.app.designateddriving.Thread.IDataCallBack;
import tw.com.lccnet.app.designateddriving.Utils.LocationUtils;
import tw.com.lccnet.app.designateddriving.Utils.WidgetUtils;
import tw.com.lccnet.app.designateddriving.db.SQLiteDatabaseHandler;

import static tw.com.lccnet.app.designateddriving.db.SQLiteDatabaseHandler.KEY_PICTURE;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {
    private static final int REQUEST_ALL_PERMISSION = 0x01;
    private static final int AUTO_PLACE_COMPETE_TOOLBAR = 0x02;
    private static final int AUTO_PLACE_COMPETE_END = 0x03;
    public static ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
    public static Handler UiThreadHandler;
    private ScheduledFuture scheduledFuture;

    private View view;
    private TextView toolbar_txt_title;
    private Toolbar toolbar_main;
    private ActionBarDrawerToggle actionBarDrawerToggle_main;
    private double mLatitude, mLongitude;
    private String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    // 聲明一個集合，在後面的代碼中用來存儲用戶拒絕授權的權
    private List<String> mPermissionList = new ArrayList<>();
    private DisplayMetrics dm;
    private SQLiteDatabaseHandler db;
    private GlobalVariable gv;
    private Button btn_long_trip, btn_immediate, btn_deliver;

    private Dialog dialog;
    private TextView start, end, cost;
    private View menu_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            String FRAGMENTS_TAG = "android:support:fragments";
            savedInstanceState.remove(FRAGMENTS_TAG);
        }
        setContentView(R.layout.activity_maps);
        dm = getResources().getDisplayMetrics();
        gv = (GlobalVariable) getApplicationContext();
        db = new SQLiteDatabaseHandler(this);
        if (UiThreadHandler == null)
            UiThreadHandler = new Handler(getMainLooper());
        // initThread();
        checkPermission();
        initView();
        initToolbar();
        initListener();
        initADToast();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        String url = db.getMemberDetail().getAsString(KEY_PICTURE);
        ImageLoader.getInstance().displayImage(url, (ImageView) menu_header.findViewById(R.id.picture));

    }

    private void initView() {
        btn_long_trip = findViewById(R.id.btn_long_trip);
        WidgetUtils.reShapeButton(this, btn_long_trip, R.color.orange1);
        btn_immediate = findViewById(R.id.btn_immediate);
        WidgetUtils.reShapeButton(this, btn_immediate, R.color.royal_blue);
        btn_deliver = findViewById(R.id.btn_deliver);
        WidgetUtils.reShapeButton(this, btn_deliver, R.color.royal_blue_light);
    }

    private void initListener() {
        btn_deliver.setOnClickListener(this);
        btn_immediate.setOnClickListener(this);
        btn_long_trip.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_long_trip:
                break;
            case R.id.btn_immediate:
                dialog = new SlideDialog(this);
                dialog.setContentView(getLayoutInflater().inflate(R.layout.item_slide_dialog, null));
                start = dialog.findViewById(R.id.start);
                end = dialog.findViewById(R.id.end);
                cost = dialog.findViewById(R.id.cost);
                Button confirm = dialog.findViewById(R.id.confirm);
                Button cancel = dialog.findViewById(R.id.cancel);
                //autoCompleteTextView.addTextChangedListener(this);
                //autoCompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
                //autoCompleteTextView.setAdapter(adapter);
                start.setText(toolbar_txt_title.getText().toString());
                end.setOnClickListener(this);
                confirm.setOnClickListener(this);
                cancel.setOnClickListener(this);
                dialog.show();
                break;
            case R.id.btn_deliver:
                startActivity(new Intent(this, OrdermealActivity.class));
                break;
            case R.id.confirm:
                if (TextUtils.isEmpty(end.getText())) {
                    end.setError("請輸入目的地");
                    return;
                }
                // TODO: 2018/12/17 執行續待優化
                final String endString = end.getText().toString();
                AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                    @Override
                    public JSONObject onTasking(Void... params) {
                        return CallNowApi.match_start(gv.getToken(), toolbar_txt_title.getText().toString(), "", "", endString, "", "");
                    }

                    @Override
                    public void onTaskAfter(JSONObject jsonObject) {
                        if (AnalyzeUtil.checkSuccess(jsonObject)) {
                            try {
                                final String pono = jsonObject.getJSONObject("Data").getString("pono");
                                Log.e("CallNow1", "訂單成立" + pono);
                                scheduledFuture = scheduledThreadPool.scheduleAtFixedRate(new Runnable() {
                                    @Override
                                    public void run() {
                                        final JSONObject json = CallNowApi.match_driver(gv.getToken(), pono);
                                        if (AnalyzeUtil.checkSuccess(json)) {
                                            scheduledFuture.cancel(true);
                                            UiThreadHandler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Intent intent = new Intent(MapsActivity.this, CallNow1_DriverInfoActivity.class);
                                                    intent.putExtra("pono", pono);
                                                    startActivity(intent);
                                                    dialog.dismiss();
                                                }
                                            });
                                        }
                                    }
                                }, 0, 5, TimeUnit.SECONDS);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });
                dialog.dismiss();
                dialog = new Dialog(this);
                dialog.setContentView(R.layout.item_wait_dialog);
                dialog.findViewById(R.id.cancel).setOnClickListener(this);
                dialog.setCanceledOnTouchOutside(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                break;
            case R.id.cancel:
                // TODO: 2018/12/17 執行續待優化
                if (scheduledFuture != null && scheduledFuture.isCancelled()) {
                    scheduledFuture.cancel(true);
                }
                dialog.dismiss();
                break;
            case R.id.toolbar_txt_title:
                try {
                    AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                            //  .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                            .setCountry("TW")
                            .build();
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .setFilter(typeFilter)
                                    .build(this);
                    startActivityForResult(intent, AUTO_PLACE_COMPETE_TOOLBAR);
                } catch (GooglePlayServicesRepairableException e) {
                } catch (GooglePlayServicesNotAvailableException e) {
                }
                //startActivityForResult(new Intent(MapsActivity.this, PlaceAutocompleteActivity.class), AUTO_PLACE_COMPETE);
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
        }
    }

    private void checkLocation() {
        if (
                LocationUtils.register(getApplicationContext(), 0, 10, new LocationUtils.OnLocationChangeListener() {
                    @Override
                    public void getLastKnownLocation(Location location) {
                        mLatitude = location.getLatitude();
                        mLongitude = location.getLongitude();
                    }

                    @Override
                    public void onLocationChanged(Location location) {
                        mLatitude = location.getLatitude();
                        mLongitude = location.getLongitude();
                        LatLng latlng = new LatLng(mLatitude, mLongitude);
                        // TODO: 2018/12/17 沒有GoogleServer會出現  CameraUpdateFactory is not initialized
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 17.0f));
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }
                })) {

        } else {
            Toast.makeText(this, "GPS初始化失敗", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkPermission() {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(MapsActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permission);
            }
        }
        if (mPermissionList.isEmpty()) {//未授予的權限為空，表示都授予了
            checkLocation();
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            assert mapFragment != null;
            mapFragment.getMapAsync(this);

        } else {//請求權限方法
            String[] permissions = mPermissionList.toArray(new String[0]);//將List轉為數組
            ActivityCompat.requestPermissions(MapsActivity.this, permissions, REQUEST_ALL_PERMISSION);
        }
    }


    private void caculateCost(final String address1, final String address2, final EditText cost) {
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public JSONObject onTasking(Void... params) {
                return CustomerApi.calculate1("1", address1, address2);
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                if (AnalyzeUtil.checkSuccess(jsonObject)) {
                    try {
                        cost.setText(jsonObject.getJSONObject("Data").getString("pay"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        cost.setText("      ");
                    }
                } else {
                    cost.setText("      ");
                    Toast.makeText(MapsActivity.this, AnalyzeUtil.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    private boolean ToastAgain = true;

    private void initADToast() {
        View ad_info = findViewById(R.id.ad_info);
        ad_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ToastAgain) {
                    ToastAgain = false;
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.toast_layout,
                            (ViewGroup) findViewById(R.id.toast_layout));
                    TextView text = layout.findViewById(R.id.text);
                    text.setText("目前時段10公里內450元");

                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.BOTTOM, 0, (int) (90 * dm.density)); //顯示位置
                    toast.setDuration(Toast.LENGTH_LONG); //顯示時間長短
                    toast.setView(layout);
                    toast.show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000);
                                ToastAgain = true;
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        });

    }

    protected void initToolbar() {
        //Toolbar 建立
        toolbar_main = findViewById(R.id.toolbar_title);
        toolbar_txt_title = findViewById(R.id.toolbar_txt_title);
        toolbar_txt_title.setOnClickListener(this);
        setSupportActionBar(toolbar_main);
        DrawerLayout drawerLayout_main = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle_main = new ActionBarDrawerToggle(this, drawerLayout_main, R.string.app_open, R.string.app_close);
        drawerLayout_main.addDrawerListener(actionBarDrawerToggle_main);
        actionBarDrawerToggle_main.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initNavigation();
    }

    private void initNavigation() {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.header_item1:
                        Intent intent = new Intent(MapsActivity.this, ListViewActivity.class);
                        intent.putExtra("type", "account");
                        startActivity(intent);
                        break;
                    case R.id.header_item2:
                        startActivity(new Intent(MapsActivity.this, CalculateActivity.class));
                        break;
                    case R.id.header_item3:
                        startActivity(new Intent(MapsActivity.this, CouponActivity.class));
                        break;
                }
            }
        };
        NavigationView navigation_view = findViewById(R.id.navigation_view);
        menu_header = navigation_view.getHeaderView(0);
        menu_header.findViewById(R.id.header_item1).setOnClickListener(onClickListener);
        menu_header.findViewById(R.id.header_item2).setOnClickListener(onClickListener);
        menu_header.findViewById(R.id.header_item3).setOnClickListener(onClickListener);
        navigation_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_item1:
                        Intent intent = new Intent(MapsActivity.this, NewsActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.menu_item2:
                        intent = new Intent(MapsActivity.this, OrderListActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.menu_item3:
                        intent = new Intent(MapsActivity.this, ListViewActivity.class);
                        intent.putExtra("type", "about");
                        startActivity(intent);
                        return true;
                    case R.id.menu_item4:
                        db.resetLoginTables();
                        gv.setToken(null);
                        intent = new Intent(MapsActivity.this, LoginActivity.class);
                        startActivity(intent);
                        MapsActivity.this.finish();
                        return true;
                }
                return false;
            }

        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return actionBarDrawerToggle_main.onOptionsItemSelected(item);
    }

    private GoogleMap googleMap;

    @Override
    public void onMapReady(GoogleMap map) {
        this.googleMap = map;
        MapsInitializer.initialize(MapsActivity.this);
        map.setMyLocationEnabled(true);
        //DO WHATEVER YOU WANT WITH GOOGLEMAP
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //  map.setTrafficEnabled(true);//交通
        // map.setIndoorEnabled(true);//室內
        map.setBuildingsEnabled(true);//建築物
        //  map.getUiSettings().setZoomControlsEnabled(true);//縮放按鈕

        //Set Custom InfoWindow Adapter
        /*
        CustomInfoWindowAdapter adapter = new CustomInfoWindowAdapter(MapsActivity.this);
        map.setInfoWindowAdapter(adapter);
        */
        map.setMyLocationEnabled(true);//取得當前位置按鈕
        LatLng latlng = new LatLng(mLatitude, mLongitude);
        showAddress(latlng);
        MarkerOptions markerOpt = new MarkerOptions();
        Random random = new Random();
        DecimalFormat df = new DecimalFormat("#.#");
        markerOpt.position(getLatLng(0.001, latlng, 0))
                .icon(getMarkerIcon(R.drawable.menu_header1, "李司機", df.format(5 * random.nextFloat())));
        map.addMarker(markerOpt);
        markerOpt.position(getLatLng(0.002, latlng, 30))
                .icon(getMarkerIcon(R.drawable.menu_header1, "王司機", df.format(5 * random.nextFloat())));
        map.addMarker(markerOpt);
        markerOpt.position(getLatLng(0.001, latlng, 60))
                .icon(getMarkerIcon(R.drawable.menu_header1, "張司機", df.format(5 * random.nextFloat())));
        map.addMarker(markerOpt);
        markerOpt.position(getLatLng(0.002, latlng, 90))
                .icon(getMarkerIcon(R.drawable.menu_header1, "劉司機", df.format(5 * random.nextFloat())));
        map.addMarker(markerOpt);
        markerOpt.position(getLatLng(0.001, latlng, 120))
                .icon(getMarkerIcon(R.drawable.menu_header1, "陳司機", df.format(5 * random.nextFloat())));
        map.addMarker(markerOpt);
        markerOpt.position(getLatLng(0.002, latlng, 150))
                .icon(getMarkerIcon(R.drawable.menu_header1, "楊司機", df.format(5 * random.nextFloat())));
        map.addMarker(markerOpt);
        markerOpt.position(getLatLng(0.001, latlng, 180))
                .icon(getMarkerIcon(R.drawable.menu_header1, "趙司機", df.format(5 * random.nextFloat())));
        map.addMarker(markerOpt);
        markerOpt.position(getLatLng(0.002, latlng, 210))
                .icon(getMarkerIcon(R.drawable.menu_header1, "黃司機", df.format(5 * random.nextFloat())));
        map.addMarker(markerOpt);
        markerOpt.position(getLatLng(0.001, latlng, 240))
                .icon(getMarkerIcon(R.drawable.menu_header1, "周司機", df.format(5 * random.nextFloat())));
        map.addMarker(markerOpt);
        markerOpt.position(getLatLng(0.002, latlng, 270))
                .icon(getMarkerIcon(R.drawable.menu_header1, "吳司機", df.format(5 * random.nextFloat())));
        map.addMarker(markerOpt);
        markerOpt.position(getLatLng(0.001, latlng, 300))
                .icon(getMarkerIcon(R.drawable.menu_header1, "林司機", df.format(5 * random.nextFloat())));
        map.addMarker(markerOpt);
        markerOpt.position(getLatLng(0.002, latlng, 330))
                .icon(getMarkerIcon(R.drawable.menu_header1, "蔡司機", df.format(5 * random.nextFloat())));
        map.addMarker(markerOpt);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 17.0f));
        map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng latLng = googleMap.getCameraPosition().target;
                showAddress(latLng);
                // mZoom = map.getCameraPosition().zoom;
                if (Math.abs(mLatitude - latLng.latitude) > 0.0005 || Math.abs(mLongitude - latLng.longitude) > 0.0005) {
                }
            }
        });
        //取得toolbar寬度
        toolbar_main.post(new Runnable() {
            @Override
            public void run() {
                googleMap.setPadding(0, (int) (toolbar_main.getHeight() + 10 * dm.density), 0, 0);
            }
        });
    }

    private void showAddress(LatLng latLng) {
        Address address = LocationUtils.getAddress(this, latLng.latitude, latLng.longitude);
        if (address != null) {
            toolbar_txt_title.setText(address.getAddressLine(0));
        } else {
            Toast.makeText(this, "取的定位異常", Toast.LENGTH_SHORT).show();
        }
    }

    private LatLng getLatLng(double radius, LatLng center, int angle) {
        double cos = Math.cos(Math.toRadians(angle));
        double sin = Math.sin(Math.toRadians(angle));
        return new LatLng(center.latitude + radius * cos, center.longitude + radius * sin);
    }

    private BitmapDescriptor getMarkerIcon(int imageRes, String title, String subtitle) {
        if (view == null) {
            view = LayoutInflater.from(this).inflate(R.layout.customer_driverview, null);
        }
        ((ImageView) view.findViewById(R.id.img_head)).setImageResource(imageRes);
        ((TextView) view.findViewById(R.id.tv_title)).setText(title);
        ((TextView) view.findViewById(R.id.tv_subtitle)).setText(subtitle);
        return BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, view));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ALL_PERMISSION) {

            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    //判断是否勾选禁止后不再询问
                    boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this, permissions[i]);
                    if (showRequestPermission) {
                        Toast.makeText(this, "權限尚未申請", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        Toast.makeText(this, "因權限問題，部分功能無法使用", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
            checkLocation();
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            assert mapFragment != null;
            mapFragment.getMapAsync(this);
        }
    }

    private View call_now_view;

    private BitmapDescriptor getCallNowIcon(int imageRes, String title, String subtitle) {
        if (call_now_view == null) {
            call_now_view = LayoutInflater.from(this).inflate(R.layout.call_now_car, null);
        }
        ((ImageView) call_now_view.findViewById(R.id.img_head)).setImageResource(imageRes);
        ((TextView) call_now_view.findViewById(R.id.tv_title)).setText(title);
        ((TextView) call_now_view.findViewById(R.id.tv_subtitle)).setText(subtitle);
        return BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, call_now_view));
    }

    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels,
                displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
                view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }


    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
            return p1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        db.close();
        LocationUtils.unregister();
        if (scheduledFuture != null)
            scheduledFuture.cancel(true);
        scheduledThreadPool.shutdown();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTO_PLACE_COMPETE_TOOLBAR) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                toolbar_txt_title.setText(place.getAddress());
                LatLng latLng = getLocationFromAddress(place.getAddress().toString());
                if (latLng != null)
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.i("place", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        } else if (requestCode == AUTO_PLACE_COMPETE_END) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                end.setText(place.getAddress());
                caculateCost(start.getText().toString(), end.getText().toString(), (EditText) cost);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.i("place", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

}