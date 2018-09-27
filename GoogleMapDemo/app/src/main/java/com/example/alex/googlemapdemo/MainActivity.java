package com.example.alex.googlemapdemo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button bt;
    LocationManager locationManager;
    WebView wv;
    private static int REQUEST_PERMISSION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt = findViewById(R.id.button);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        wv = findViewById(R.id.webview);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.loadUrl("file:///android_asset/googlemap.html");

        bt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION);
                    return;
                }
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);//設置為最大精度
                criteria.setAltitudeRequired(false);//不要求海拔資訊
                criteria.setBearingRequired(false);//不要求方位資訊
                criteria.setCostAllowed(true);//是否允許付費
                criteria.setPowerRequirement(Criteria.POWER_LOW);//對電量的要求
                String provider = LocationManager.NETWORK_PROVIDER;
                locationManager.requestLocationUpdates(provider, 2000, 10, locationListener);
                Location location = locationManager.getLastKnownLocation(provider);
                if(location!=null) {
                    String centerURL = "javascript:centerAt(" + location.getLatitude() + "," + location.getLongitude() + ")";
                    wv.loadUrl(centerURL);
                }
            }
        });
    }


    public LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
//Called when a new location is found by the network location provider.


        }


        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };


}