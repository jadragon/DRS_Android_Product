package tw.com.lccnet.app.designateddriving;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import tw.com.lccnet.app.designateddriving.Utils.LocationUtils;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int REQUEST_ALL_PERMISSION = 0x01;
    private View view, ad_info;
    private TextView toolbar_txt_title;
    private Toolbar toolbar_main;
    private SupportMapFragment mapFragment;
    private ActionBarDrawerToggle actionBarDrawerToggle_main;
    private GoogleMap mgooglemap;
    private double mLatitude, mLongitude;
    String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    // 声明一个集合，在后面的代码中用来存储用户拒绝授权的权
    List<String> mPermissionList = new ArrayList<>();
    private GlobalVariable gv;
    private DisplayMetrics dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        gv = (GlobalVariable) getApplicationContext();
        dm = getResources().getDisplayMetrics();
        checkPermission();
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        initToolbar();
        initButton();
        initADToast();
    }

    private void checkPermission() {
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(MapsActivity.this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }
        if (mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了
            if (LocationUtils.isGpsEnabled(this)) {
                LocationUtils.register(this, 5000, 10, new LocationUtils.OnLocationChangeListener() {
                    @Override
                    public void getLastKnownLocation(Location location) {
                        mLatitude = location.getLatitude();
                        mLongitude = location.getLongitude();
                    }

                    @Override
                    public void onLocationChanged(Location location) {
                        Toast.makeText(MapsActivity.this, "地位更新:緯度:" + location.getLatitude() + "\n經度:" + location.getLongitude(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }
                });
            } else {
                LocationUtils.openGpsSettings(this);
            }
        } else {//请求权限方法
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(MapsActivity.this, permissions, REQUEST_ALL_PERMISSION);
        }
    }

    private void initButton() {
        Button btn_long_trip = findViewById(R.id.btn_long_trip);
        reShapeButton(btn_long_trip, R.color.orange1);
        Button btn_immediate = findViewById(R.id.btn_immediate);
        reShapeButton(btn_immediate, R.color.green);
        Button btn_deliver = findViewById(R.id.btn_deliver);
        reShapeButton(btn_deliver, R.color.green1);
    }

    private void reShapeButton(Button button, int color) {
        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadius(20);
        shape.setColor(getResources().getColor(color));
        button.setBackgroundDrawable(shape);
    }

    private boolean ToastAgain = true;

    private void initADToast() {
        ad_info = findViewById(R.id.ad_info);
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
        setSupportActionBar(toolbar_main);
        DrawerLayout drawerLayout_main = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle_main = new ActionBarDrawerToggle(this, drawerLayout_main, R.string.app_open, R.string.app_close);
        drawerLayout_main.addDrawerListener(actionBarDrawerToggle_main);
        actionBarDrawerToggle_main.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //取得toolbar寬度
        toolbar_main.post(new Runnable() {
            @Override
            public void run() {
                mgooglemap.setPadding(0, (int) (toolbar_main.getHeight() + 10 * dm.density), 0, 0);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle_main.onOptionsItemSelected(item)) {
            return true;
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        //DO WHATEVER YOU WANT WITH GOOGLEMAP
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //  map.setTrafficEnabled(true);//交通
        // map.setIndoorEnabled(true);//室內
        map.setBuildingsEnabled(true);//建築物
        //  map.getUiSettings().setZoomControlsEnabled(true);//縮放按鈕
        mgooglemap = map;
        //Set Custom InfoWindow Adapter
        /*
        CustomInfoWindowAdapter adapter = new CustomInfoWindowAdapter(MapsActivity.this);
        map.setInfoWindowAdapter(adapter);
        */
        initGoogleMap();

    }

    private void initGoogleMap() {
        mgooglemap.setMyLocationEnabled(true);//取得當前位置按鈕
/*
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);//高精度
        criteria.setAltitudeRequired(false);//无海拔要求
        criteria.setBearingRequired(false);//无方位要求
        criteria.setCostAllowed(true);//允许产生资费
        criteria.setPowerRequirement(Criteria.POWER_LOW);//低功耗
        // 获取最佳服务对象
        LocationManager locationManager = ((LocationManager) getSystemService(Context.LOCATION_SERVICE));
        String provider = locationManager.getBestProvider(criteria, true);
        Location location = ((LocationManager) getSystemService(Context.LOCATION_SERVICE)).getLastKnownLocation(provider);
        */
        // mLatitude = gv.getmLatitude();
        //  mLongitude = gv.getmLongitude();
        //mLatitude = 25.016633;
        //  mLongitude = 121.300367;
        LatLng latlng = new LatLng(mLatitude, mLongitude);
        showAddress(latlng);
        MarkerOptions markerOpt = new MarkerOptions();
        Random random = new Random();
        DecimalFormat df = new DecimalFormat("#.#");
        markerOpt.position(getLatLng(0.002, latlng, 0))
                .icon(getMarkerIcon(R.drawable.no_photo, "李司機", df.format(5 * random.nextFloat())));
        mgooglemap.addMarker(markerOpt);
        markerOpt.position(getLatLng(0.002, latlng, 30))
                .icon(getMarkerIcon(R.drawable.no_photo, "王司機", df.format(5 * random.nextFloat())));
        mgooglemap.addMarker(markerOpt);
        markerOpt.position(getLatLng(0.002, latlng, 60))
                .icon(getMarkerIcon(R.drawable.no_photo, "張司機", df.format(5 * random.nextFloat())));
        mgooglemap.addMarker(markerOpt);
        markerOpt.position(getLatLng(0.002, latlng, 90))
                .icon(getMarkerIcon(R.drawable.no_photo, "劉司機", df.format(5 * random.nextFloat())));
        mgooglemap.addMarker(markerOpt);
        markerOpt.position(getLatLng(0.002, latlng, 120))
                .icon(getMarkerIcon(R.drawable.no_photo, "陳司機", df.format(5 * random.nextFloat())));
        mgooglemap.addMarker(markerOpt);
        markerOpt.position(getLatLng(0.002, latlng, 150))
                .icon(getMarkerIcon(R.drawable.no_photo, "楊司機", df.format(5 * random.nextFloat())));
        mgooglemap.addMarker(markerOpt);
        markerOpt.position(getLatLng(0.002, latlng, 180))
                .icon(getMarkerIcon(R.drawable.no_photo, "趙司機", df.format(5 * random.nextFloat())));
        mgooglemap.addMarker(markerOpt);
        markerOpt.position(getLatLng(0.002, latlng, 210))
                .icon(getMarkerIcon(R.drawable.no_photo, "黃司機", df.format(5 * random.nextFloat())));
        mgooglemap.addMarker(markerOpt);
        markerOpt.position(getLatLng(0.002, latlng, 240))
                .icon(getMarkerIcon(R.drawable.no_photo, "周司機", df.format(5 * random.nextFloat())));
        mgooglemap.addMarker(markerOpt);
        markerOpt.position(getLatLng(0.002, latlng, 270))
                .icon(getMarkerIcon(R.drawable.no_photo, "吳司機", df.format(5 * random.nextFloat())));
        mgooglemap.addMarker(markerOpt);
        markerOpt.position(getLatLng(0.002, latlng, 300))
                .icon(getMarkerIcon(R.drawable.no_photo, "林司機", df.format(5 * random.nextFloat())));
        mgooglemap.addMarker(markerOpt);
        markerOpt.position(getLatLng(0.002, latlng, 330))
                .icon(getMarkerIcon(R.drawable.no_photo, "蔡司機", df.format(5 * random.nextFloat())));
        mgooglemap.addMarker(markerOpt);
        mgooglemap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 17.0f));

        mgooglemap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng latLng = mgooglemap.getCameraPosition().target;
                // mZoom = map.getCameraPosition().zoom;
                if (Math.abs(mLatitude - latLng.latitude) > 0.0005 || Math.abs(mLongitude - latLng.longitude) > 0.0005) {
                    showAddress(latLng);
                }

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
            initGoogleMap();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void checkCurrentRange(GoogleMap map) {
        VisibleRegion visibleRegion = map.getProjection()
                .getVisibleRegion();

        Point x = map.getProjection().toScreenLocation(
                visibleRegion.farRight);

        Point y = map.getProjection().toScreenLocation(
                visibleRegion.nearLeft);

        Point centerPoint = new Point(x.x / 2, y.y / 2);

        LatLng centerFromPoint = map.getProjection().fromScreenLocation(
                centerPoint);
        Log.e("MapFragment: ", "Center From camera: Long: " + centerFromPoint.longitude
                + " Lat" + centerFromPoint.latitude);

        Log.e("Punto x", "x:" + x.x + "y:" + x.y);
        Log.e("Punto y", "y:" + y.x + "y:" + y.y);

        Log.e("MapFragment: ", "Center From Point: Long: "
                + centerFromPoint.longitude + " Lat"
                + centerFromPoint.latitude);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
}