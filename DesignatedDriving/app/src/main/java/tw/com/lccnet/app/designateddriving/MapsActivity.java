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
import android.graphics.drawable.GradientDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
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
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import tw.com.lccnet.app.designateddriving.API.Analyze.AnalyzeUtil;
import tw.com.lccnet.app.designateddriving.API.CallNowApi;
import tw.com.lccnet.app.designateddriving.API.CustomerApi;
import tw.com.lccnet.app.designateddriving.Component.SlideDialog;
import tw.com.lccnet.app.designateddriving.Utils.AsyncTaskUtils;
import tw.com.lccnet.app.designateddriving.Utils.IDataCallBack;
import tw.com.lccnet.app.designateddriving.Utils.LocationUtils;
import tw.com.lccnet.app.designateddriving.db.SQLiteDatabaseHandler;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {
    private static final int REQUEST_ALL_PERMISSION = 0x01;
    private static final int AUTO_PLACE_COMPETE_TOOLBAR = 0x02;
    private static final int AUTO_PLACE_COMPETE_END = 0x03;
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
        initThread();
        checkPermission();
        initToolbar();
        initButton();
        initADToast();

    }

    private void initButton() {
        Button btn_long_trip = findViewById(R.id.btn_long_trip);
        btn_long_trip.setOnClickListener(this);
        reShapeButton(btn_long_trip, R.color.orange1);
        Button btn_immediate = findViewById(R.id.btn_immediate);
        btn_immediate.setOnClickListener(this);
        reShapeButton(btn_immediate, R.color.royal_blue);
        Button btn_deliver = findViewById(R.id.btn_deliver);
        btn_deliver.setOnClickListener(this);
        reShapeButton(btn_deliver, R.color.royal_blue_light);
    }

    private void checkLocation() {
        if (
                LocationUtils.register(getApplicationContext(), 0, 10, new LocationUtils.OnLocationChangeListener() {
                    @Override
                    public void getLastKnownLocation(Location location) {
                        mLatitude = location.getLatitude();
                        mLongitude = location.getLongitude();
                        /*
                        LatLng lastPosition = LocationUtils.readData(MapsActivity.this);
                        if (lastPosition != null) {
                            mLatitude = lastPosition.latitude;
                            mLongitude = lastPosition.longitude;
                        } else {
                            mLatitude = location.getLatitude();
                            mLongitude = location.getLongitude();
                        }
                        */
                    }

                    @Override
                    public void onLocationChanged(Location location) {
                        mLatitude = location.getLatitude();
                        mLongitude = location.getLongitude();
                        LatLng latlng = new LatLng(mLatitude, mLongitude);
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 17.0f));
                        Toast.makeText(MapsActivity.this, mLatitude + "\n" + mLongitude, Toast.LENGTH_SHORT).show();
                        //  LocationUtils.saveData(MapsActivity.this, location);
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
            /*
            googleApiClient = new GoogleApiClient.Builder(MapsActivity.this)
                    .addApi(Places.GEO_DATA_API)
                    .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                    .addConnectionCallbacks(this)
                    .build();
            AutocompleteFilter filter = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE)
                    .setCountry("TW")
                    .build();
            adapter = new PlaceAutocompleteAdapter(this, android.R.layout.simple_list_item_1, BOUNDS_MOUNTAIN_VIEW, filter);
            */
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            assert mapFragment != null;
            mapFragment.getMapAsync(this);

        } else {//請求權限方法
            String[] permissions = mPermissionList.toArray(new String[0]);//將List轉為數組
            ActivityCompat.requestPermissions(MapsActivity.this, permissions, REQUEST_ALL_PERMISSION);
        }
    }

    private Dialog dialog;
    private TextView start, end, cost;
    private Thread thread;
    private boolean isSendAgain;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_long_trip:
                break;
            case R.id.btn_immediate:
                // TODO: Handle the error.
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
                // TODO: Handle the error.
                break;
            case R.id.btn_deliver:
                startActivity(new Intent(this, OrdermealActivity.class));
                break;
            case R.id.confirm:
                if (TextUtils.isEmpty(end.getText())) {
                    end.setError("請輸入目的地");
                    return;
                }

                // TODO: Handle the error.
                final String endString = end.getText().toString();
                if (thread != null && thread.isAlive()) {
                    thread.interrupt();
                } else {
                    thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject json = CallNowApi.match_start(gv.getToken(), toolbar_txt_title.getText().toString(), "", "", endString, "", "");
                                if (AnalyzeUtil.checkSuccess(json)) {
                                    String pono = null;
                                    try {
                                        pono = json.getJSONObject("Data").getString("pono");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if (pono != null) {
                                        while (!thread.isInterrupted()) {
                                            Thread.sleep(3000);
                                            Message msg = Message.obtain();
                                            msg.what = 1;
                                            msg.obj = pono;
                                            mThreadHandler.sendMessage(msg);
                                        }
                                    }
                                }

                            } catch (InterruptedException e) {
                                thread.interrupt();
                            }
                        }
                    });
                    thread.start();
                }
                dialog.dismiss();
                dialog = new Dialog(this);
                dialog.setContentView(R.layout.item_wait_dialog);
                dialog.findViewById(R.id.cancel).setOnClickListener(this);
                dialog.setCanceledOnTouchOutside(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                // TODO: Handle the error.
                break;
            case R.id.cancel:
                if (thread != null) {
                    thread.interrupt();
                }
                // TODO: Handle the error.
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

    private void caculateCost() {
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public JSONObject onTasking(Void... params) {
                return CustomerApi.calculate1("1", start.getText().toString(), end.getText().toString());
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                if (jsonObject != null) {
                    if (AnalyzeUtil.checkSuccess(jsonObject)) {
                        try {
                            cost.setText(jsonObject.getJSONObject("Data").getString("pay"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            cost.setText("      ");
                        }
                    } else {
                        cost.setText("      ");
                    }
                }
            }
        });
    }

    /*
        private Handler handler = new Handler();

        @Override
        public void afterTextChanged(Editable s) {
            handler.removeCallbacks(notifier);
            handler.postDelayed(notifier, 1000);
        }

        private Runnable notifier = new Runnable() {
            @Override
            public void run() {
                AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                    @Override
                    public JSONObject onTasking(Void... params) {
                        return CustomerApi.calculate1("1", start.getText().toString(), autoCompleteTextView.getText().toString());
                    }

                    @Override
                    public void onTaskAfter(JSONObject jsonObject) {
                        if (jsonObject != null) {
                            if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                try {
                                    cost.setText(jsonObject.getJSONObject("Data").getString("pay"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    cost.setText("      ");
                                }
                            } else {
                                cost.setText("      ");
                            }
                        }
                    }
                });
            }
        };

        private AdapterView.OnItemClickListener mAutocompleteClickListener
                = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final PlaceAutocompleteAdapter.PlaceAutocomplete item = adapter.getItem(position);
                final String placeId = String.valueOf(item.placeId);
                Log.i(LOG_TAG, "Selected: " + item.description);
                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(googleApiClient, placeId);
                placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
                Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
            }
        };

        private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
                = new ResultCallback<PlaceBuffer>() {
            @Override
            public void onResult(PlaceBuffer places) {
                if (!places.getStatus().isSuccess()) {
                    Log.e(LOG_TAG, "Place query did not complete. Error: " +
                            places.getStatus().toString());
                    return;
                }
                // Selecting the first object buffer.
                final Place place = places.get(0);
                CharSequence attributions = places.getAttributions();
                if (autoCompleteTextView != null) {
                    autoCompleteTextView.setText(Html.fromHtml(place.getAddress().toString()));
                } else {
                    toolbar_txt_title.setText(Html.fromHtml(place.getAddress().toString()));
                }
                if (attributions != null) {
                    if (autoCompleteTextView != null) {
                        autoCompleteTextView.setText(Html.fromHtml(place.getAddress().toString()));
                    } else {
                        toolbar_txt_title.setText(Html.fromHtml(attributions.toString()));
                    }
                }
                // toolbar_txt_title.setVisibility(View.VISIBLE);
            }
        };

        @Override
        public void onConnected(Bundle bundle) {
            adapter.setGoogleApiClient(googleApiClient);
            Log.i(LOG_TAG, "Google Places API connected.");

        }

        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
            Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                    + connectionResult.getErrorCode());

            Toast.makeText(this,
                    "Google Places API connection failed with error code:" +
                            connectionResult.getErrorCode(),
                    Toast.LENGTH_LONG).show();
        }

        @Override
        public void onConnectionSuspended(int i) {
            adapter.setGoogleApiClient(null);
            Log.e(LOG_TAG, "Google Places API connection suspended.");
        }
    */
    private void reShapeButton(Button button, int color) {
        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadius(20);
        shape.setColor(getResources().getColor(color));
        button.setBackgroundDrawable(shape);
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
        // toolbar_txt_title = findViewById(R.id.toolbar_txt_title);
        //  toolbar_edit_title.setAdapter(adapter);
        //toolbar_edit_title.setOnItemClickListener(mAutocompleteClickListener);
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
        View header = navigation_view.getHeaderView(0);
        header.findViewById(R.id.header_item1).setOnClickListener(onClickListener);
        header.findViewById(R.id.header_item2).setOnClickListener(onClickListener);
        header.findViewById(R.id.header_item3).setOnClickListener(onClickListener);
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

    private GoogleMap map;

    @Override
    public void onMapReady(final GoogleMap map) {
        this.map = map;
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
                LatLng latLng = map.getCameraPosition().target;
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
                map.setPadding(0, (int) (toolbar_main.getHeight() + 10 * dm.density), 0, 0);
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

    @Override
    protected void onDestroy() {
        db.close();
        LocationUtils.unregister();
        mThread.quit();
        if (thread != null) {
            thread.interrupt();
        }
        super.onDestroy();
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTO_PLACE_COMPETE_TOOLBAR) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                toolbar_txt_title.setText(place.getAddress());
                LatLng latLng = getLocationFromAddress(place.getAddress().toString());
                if (latLng != null)
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("place", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        } else if (requestCode == AUTO_PLACE_COMPETE_END) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                end.setText(place.getAddress());
                caculateCost();
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("place", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    //=====================================================================================================================
    //宣告特約工人的經紀人
    private static Handler mThreadHandler;
    //宣告特約工人
    private static HandlerThread mThread;

    private void initThread() {
        mThread = new HandlerThread("name");
        //讓Worker待命，等待其工作 (開啟Thread)
        mThread.start();
        //找到特約工人的經紀人，這樣才能派遣工作 (找到Thread上的Handler)
        mThreadHandler = new Handler(mThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        break;
                    case 1:
                        if (msg.obj instanceof JSONObject) {
                            Toast.makeText(MapsActivity.this, "" + msg.obj, Toast.LENGTH_SHORT).show();
                        } else if (msg.obj instanceof String) {
                            Toast.makeText(MapsActivity.this, "" + msg.obj, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 2:
                        break;
                }
            }
        };
    }

    private Marker call_now_marker;
    private int sssss = 0;
    //Ui Thread 1
    private Runnable u1 = new Runnable() {
        public void run() {
            if (call_now_marker != null) {
                call_now_marker.remove();
            }
            LatLng latlng = new LatLng(mLatitude, mLongitude);
            MarkerOptions markerOpt = new MarkerOptions();
            DecimalFormat df = new DecimalFormat("#.#");
            Random random = new Random();
            markerOpt.position(getLatLng(0.002, latlng, 30 * sssss))
                    .icon(getCallNowIcon(R.drawable.menu_header1, "我的司機", df.format(5 * random.nextFloat())));
            call_now_marker = map.addMarker(markerOpt);
            sssss++;
            if (sssss > 12) {
                sssss = 0;
            }
        }
    };

}