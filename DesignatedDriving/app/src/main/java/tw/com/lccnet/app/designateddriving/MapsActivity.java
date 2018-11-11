package tw.com.lccnet.app.designateddriving;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        initToolbar();
        initADToast();
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void initADToast() {
        ad_info = findViewById(R.id.ad_info);
        ad_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.toast_layout,
                        (ViewGroup) findViewById(R.id.toast_layout));
                TextView text = layout.findViewById(R.id.text);
                text.setText("目前時段10公里內450元");

                Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.BOTTOM, 0, (int) (80 * dm.density)); //顯示位置
                toast.setDuration(Toast.LENGTH_LONG); //顯示時間長短
                toast.setView(layout);
                toast.show();
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
                mgooglemap.setPadding(0, toolbar_main.getHeight(), 0, 0);
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
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(MapsActivity.this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }
        if (mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了
            initGoogleMap();
        } else {//请求权限方法
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(MapsActivity.this, permissions, REQUEST_ALL_PERMISSION);
        }
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
        mLatitude = gv.getmLatitude();
        mLongitude = gv.getmLongitude();
        LatLng latlng = new LatLng(mLatitude, mLongitude);
        showAddress(latlng);
        MarkerOptions markerOpt = new MarkerOptions();
        markerOpt.position(getLatLng(0.002, latlng, 0))
                .icon(getMarkerIcon(R.drawable.north_koria, "金正恩", "2.3"));
        mgooglemap.addMarker(markerOpt);
        markerOpt.position(getLatLng(0.002, latlng, 60))
                .icon(getMarkerIcon(R.drawable.usa, "歐巴馬", "4.9"));
        mgooglemap.addMarker(markerOpt);
        markerOpt.position(getLatLng(0.002, latlng, 120))
                .icon(getMarkerIcon(R.drawable.russia, "普丁", "2.5"));
        mgooglemap.addMarker(markerOpt);
        markerOpt.position(getLatLng(0.002, latlng, 180))
                .icon(getMarkerIcon(R.drawable.china, "習近平", "3.2"));
        mgooglemap.addMarker(markerOpt);
        markerOpt.position(getLatLng(0.002, latlng, 240))
                .icon(getMarkerIcon(R.drawable.taiwan, "孫中山", "5.0"));
        mgooglemap.addMarker(markerOpt);
        markerOpt.position(getLatLng(0.002, latlng, 300))
                .icon(getMarkerIcon(R.drawable.south_koria, "朴槿惠", "2.4"));
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
        try {
            Geocoder gc = new Geocoder(MapsActivity.this, Locale.TRADITIONAL_CHINESE);
            List<Address> lstAddress = gc.getFromLocation(latLng.latitude, latLng.longitude, 1);
            toolbar_txt_title.setText(lstAddress.get(0).getAddressLine(0));
                        /*
                        Log.e("Address", "returnAddress:" + lstAddress.get(0).getAddressLine(0)
                                + "\ngetCountryName:" + lstAddress.get(0).getCountryName()  //台灣省
                                + "\ngetAdminArea:" + lstAddress.get(0).getAdminArea()  //台北市
                                + "\ngetLocality:" + lstAddress.get(0).getLocality()  //中正區
                                + "\ngetThoroughfare:" + lstAddress.get(0).getThoroughfare()  //信陽街(包含路巷弄)
                                + "\ngetFeatureName:" + lstAddress.get(0).getFeatureName()  //會得到33(號)
                                + "\ngetPostalCode:" + lstAddress.get(0).getPostalCode());  //會得到100(郵遞區號)
*/
        } catch (IOException e) {
            e.printStackTrace();
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