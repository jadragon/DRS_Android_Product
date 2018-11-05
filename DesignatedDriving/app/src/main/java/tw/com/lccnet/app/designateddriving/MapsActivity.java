package tw.com.lccnet.app.designateddriving;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
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

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int REQUEST_ALL_PERMISSION = 0x01;
    private View view;
    private SupportMapFragment mapFragment;
    private ActionBarDrawerToggle actionBarDrawerToggle_main;
    private GoogleMap mgooglemap;
    private double mLatitude, mLongitude;
    private LocationManager mLocationManager;
    String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    // 声明一个集合，在后面的代码中用来存储用户拒绝授权的权
    List<String> mPermissionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        initToo();
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    protected void initToo() {
        //Toolbar 建立
        Toolbar toolbar_main = findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar_main);
        DrawerLayout drawerLayout_main = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle_main = new ActionBarDrawerToggle(this, drawerLayout_main, R.string.app_open, R.string.app_close);
        drawerLayout_main.addDrawerListener(actionBarDrawerToggle_main);
        actionBarDrawerToggle_main.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        map.setTrafficEnabled(true);
        map.setIndoorEnabled(true);
        map.setBuildingsEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mgooglemap.setMyLocationEnabled(true);
        mgooglemap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng latLng = mgooglemap.getCameraPosition().target;
                // mZoom = map.getCameraPosition().zoom;
                if (Math.abs(mLatitude - latLng.latitude) > 0.0005 || Math.abs(mLongitude - latLng.longitude) > 0.0005) {
                    mLatitude = latLng.latitude;
                    mLongitude = latLng.longitude;

                    //  mgooglemap.clear();
                    /*
                    MarkerOptions markerOpt = new MarkerOptions();
                    markerOpt.position(latLng)
                            .title("金正恩")
                            .snippet("★4.9");

                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin));

                    mgooglemap.addMarker(markerOpt).showInfoWindow();
*/
                    //  map.moveCamera(CameraUpdateFactory.zoomTo(16));
                }

            }
        });
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);    //取得系統定位服務
        Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);    //使用GPS定位座標
        LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOpt = new MarkerOptions();
        markerOpt.position(getLatLng(0.002,latlng,0))
                .icon(getMarkerIcon(R.drawable.north_koria,"金正恩","★2.3"));
        mgooglemap.addMarker(markerOpt);
        markerOpt.position(getLatLng(0.002,latlng,60))
                .icon(getMarkerIcon(R.drawable.usa,"歐巴馬","★4.9"));
        mgooglemap.addMarker(markerOpt);
        markerOpt.position(getLatLng(0.002,latlng,120))
                .icon(getMarkerIcon(R.drawable.russia,"普丁","★2.5"));
        mgooglemap.addMarker(markerOpt);
        markerOpt.position(getLatLng(0.002,latlng,180))
                .icon(getMarkerIcon(R.drawable.china,"習近平","★3.2"));
        mgooglemap.addMarker(markerOpt);
        markerOpt.position(getLatLng(0.002,latlng,240))
                .icon(getMarkerIcon(R.drawable.taiwan,"孫中山","★5.0"));
        mgooglemap.addMarker(markerOpt);
        markerOpt.position(getLatLng(0.002,latlng,300))
                .icon(getMarkerIcon(R.drawable.south_koria,"朴槿惠","★2.4"));
        mgooglemap.addMarker(markerOpt);
        mgooglemap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
        mgooglemap.moveCamera(CameraUpdateFactory.zoomTo(16));
        getLocation(location);
    }
    private LatLng getLatLng(double radius,LatLng center,int angle) {
        double cos=Math.cos(Math.toRadians(angle));
        double sin=Math.sin(Math.toRadians(angle));
        return  new LatLng(center.latitude + radius*cos, center.longitude +  radius*sin);
    }
    private BitmapDescriptor getMarkerIcon(int imageRes, String title, String subtitle) {
        if (view == null) {
            view = LayoutInflater.from(this).inflate(R.layout.custominfowindow, null);
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

    private void getLocation(Location location) {    //將定位資訊顯示在畫面中
        if (location != null) {
            mLatitude = location.getLongitude();    //取得經度
            mLongitude = location.getLatitude();    //取得緯度
        } else {
            Toast.makeText(this, "無法定位座標", Toast.LENGTH_LONG).show();
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
}