package tw.com.lccnet.app.designateddriving;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {
    private static final int REQUEST_ALL_PERMISSION = 0x01;
    private SupportMapFragment mapFragment;
    private double mLatitude, mLongitude;
    private LocationManager mLocationManager;
    String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    // 声明一个集合，在后面的代码中用来存储用户拒绝授权的权
    List<String> mPermissionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(final GoogleMap map) {
//DO WHATEVER YOU WANT WITH GOOGLEMAP
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setTrafficEnabled(true);
        map.setIndoorEnabled(true);
        map.setBuildingsEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                LatLng latLng = map.getCameraPosition().target;
                // mZoom = map.getCameraPosition().zoom;
                if (Math.abs(mLatitude - latLng.latitude) > 0.001 || Math.abs(mLongitude - latLng.longitude) > 0.001) {
                    mLatitude = latLng.latitude;
                    mLongitude = latLng.longitude;
                    map.clear();
                    map.addMarker(new MarkerOptions().position(latLng).title("這是我家"));
                    map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    //  map.moveCamera(CameraUpdateFactory.zoomTo(16));
                }

            }
        });
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(MapsActivity.this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }
        if (mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了
            map.setMyLocationEnabled(true);
            mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);    //取得系統定位服務
            Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);    //使用GPS定位座標
            LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
            map.addMarker(new MarkerOptions().position(latlng).title("這是我家"));
            map.moveCamera(CameraUpdateFactory.newLatLng(latlng));
            map.moveCamera(CameraUpdateFactory.zoomTo(16));
            getLocation(location);

        } else {//请求权限方法
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(MapsActivity.this, permissions, REQUEST_ALL_PERMISSION);
        }
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_ALL_PERMISSION) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    //判断是否勾选禁止后不再询问
                    boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this, permissions[i]);
                    if (showRequestPermission) {
                        Toast.makeText(this, "權限尚未申請", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void getLocation(Location location) {    //將定位資訊顯示在畫面中
        if (location != null) {
            mLatitude = location.getLongitude();    //取得經度
            mLongitude = location.getLatitude();    //取得緯度
        } else {
            Toast.makeText(this, "無法定位座標", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

}