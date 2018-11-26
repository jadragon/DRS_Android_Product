package tw.com.lccnet.app.designateddriving.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

/**
 * author: Blankj
 * blog : http://blankj.com
 * time : 16/11/13
 * desc : 定位相關工具類
 */
public class LocationUtils {

    private static OnLocationChangeListener mListener;
    private static MyLocationListener myLocationListener;
    private static LocationManager mLocationManager;
    private static String PROVIDER;
    private static SharedPreferences settings;

    public static boolean saveData(Context context, Location location) {
        float latitude = (float) location.getLatitude();
        float longitude = (float) location.getLongitude();
        if (latitude != 0 && longitude != 0) {
            settings = context.getSharedPreferences("user_data", 0);
            settings.edit()
                    .putFloat("mLatitude", latitude)
                    .putFloat("mLongitude", longitude)
                    .commit();
            return true;
        }
        return false;
    }

    public static LatLng readData(Context context) {
        settings = context.getSharedPreferences("user_data", 0);
        double mLatitude = settings.getFloat("mLatitude", 0);
        double mLongitude = settings.getFloat("mLongitude", 0);
        if (mLatitude != 0 && mLongitude != 0) {
            return new LatLng(mLatitude, mLongitude);
        }
        return null;
    }


    public LocationUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 判斷Gps是否可用
     *
     * @return {@code true}: 是
     * {@code false}: 否
     */
    public static boolean isGpsEnabled(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 判斷定位是否可用
     *
     * @return {@code true}: 是
     * {@code false}: 否
     */
    public static boolean isLocationEnabled(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 打開Gps設置界面
     */
    public static void openGpsSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 註冊
     * <p>
     * 使用完記得調用{@link #unregister()}
     * <p>
     * <p>
     * <p>
     * 需添加權限 {@code }
     * <p>
     * <p>
     * <p>
     * 需添加權限 {@code }
     * <p>
     * <p>
     * <p>
     * 需添加權限 {@code }
     * <p>
     * <p>
     * <p>
     * 如果{@code minDistance}為0，則通過{@code minTime}來定時更新；
     * <p>
     * <p>
     * <p>
     * {@code minDistance}不為0，則以{@code minDistance}為準；
     * <p>
     * <p>
     * <p>
     * 兩者都為0，則隨時刷新。
     *
     * @param minTime     位置信息更新周期（單位：毫秒）
     * @param minDistance 位置變化最小距離：當位置距離變化超過此值時，將更新位置信息（單位：米）
     * @param listener    位置刷新的回調接口
     * @return {@code true}: 初始化成功
     * {@code false}: 初始化失敗
     */

    @SuppressLint("MissingPermission")
    public static boolean register(Context context, long minTime, long minDistance, OnLocationChangeListener listener) {
        if (listener == null) return false;
        mLocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        mListener = listener;
        if (!isLocationEnabled(context)) {
            Toast.makeText(context, "無法定位，請打開定位服務", Toast.LENGTH_SHORT).show();
            return false;
        }
        //String provider = mLocationManager.getBestProvider(getCriteria(), true);
        // Location location = mLocationManager.getLastKnownLocation(provider);
        Location location = getLastKnownLocation(context);
        if (location != null) listener.getLastKnownLocation(location);
        if (myLocationListener == null) myLocationListener = new MyLocationListener();
        mLocationManager.requestLocationUpdates(PROVIDER, minTime, minDistance, myLocationListener);
        return true;
    }

    private static Location getLastKnownLocation(Context context) {
        mLocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
       PROVIDER = mLocationManager.getBestProvider(criteria, true);
        @SuppressLint("MissingPermission")
        Location location = mLocationManager.getLastKnownLocation(PROVIDER);
        /*
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            @SuppressLint("MissingPermission") Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
                PROVIDER = provider;
            }
        }
        */

        return location;
    }

    /**
     * 註銷
     */
    public static void unregister() {
        if (mLocationManager != null) {
            if (myLocationListener != null) {
                mLocationManager.removeUpdates(myLocationListener);
                myLocationListener = null;
            }
            mLocationManager = null;
        }
    }

    /**
     * 設置定位參數
     *
     * @return {@link Criteria}
     */
    private static Criteria getCriteria() {
        Criteria criteria = new Criteria();
        //設置定位精確度 Criteria.ACCURACY_COARSE比較粗略，Criteria.ACCURACY_FINE則比較精細
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        //設置是否要求速度
        criteria.setSpeedRequired(false);
        // 設置是否允許運營商收費
        criteria.setCostAllowed(false);
        //設置是否需要方位信息
        criteria.setBearingRequired(false);
        //設置是否需要海拔信息
        criteria.setAltitudeRequired(false);
        // 設置對電源的需求
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }

    /**
     * 根據經緯度獲取地理位置
     *
     * @param context   上下文
     * @param latitude  緯度
     * @param longitude 經度
     * @return {@link Address}
     */
    public static Address getAddress(Context context, double latitude, double longitude) {
        Geocoder geo = new Geocoder(context.getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = geo.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) return addresses.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根據經緯度獲取所在國家
     *
     * @param context   上下文
     * @param latitude  緯度
     * @param longitude 經度
     * @return 所在國家
     */
    public static String getCountryName(Context context, double latitude, double longitude) {
        Address address = getAddress(context, latitude, longitude);
        return address == null ? "unknown" : address.getCountryName();
    }

    /**
     * 根據經緯度獲取所在地
     *
     * @param context   上下文
     * @param latitude  緯度
     * @param longitude 經度
     * @return 所在地
     */
    public static String getLocality(Context context, double latitude, double longitude) {
        Address address = getAddress(context, latitude, longitude);
        return address == null ? "unknown" : address.getLocality();
    }

    /**
     * 根據經緯度獲取所在街道
     *
     * @param context   上下文
     * @param latitude  緯度
     * @param longitude 經度
     * @return 所在街道
     */
    public static String getStreet(Context context, double latitude, double longitude) {
        Address address = getAddress(context, latitude, longitude);
        return address == null ? "unknown" : address.getAddressLine(0);
    }

    private static class MyLocationListener
            implements LocationListener {
        /**
         * 當坐標改變時觸發此函數，如果Provider傳進相同的坐標，它就不會被觸發
         *
         * @param location 坐標
         */
        @Override
        public void onLocationChanged(Location location) {
            if (mListener != null) {
                mListener.onLocationChanged(location);
            }
        }

        /**
         * provider的在可用、暫時不可用和無服務三個狀態直接切換時觸發此函數
         *
         * @param provider 提供者
         * @param status   狀態
         * @param extras   provider可選包
         */
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (mListener != null) {
                mListener.onStatusChanged(provider, status, extras);
            }
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("onStatusChanged", "當前GPS狀態為可見狀態");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("onStatusChanged", "當前GPS狀態為服務區外狀態");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("onStatusChanged", "當前GPS狀態為暫停服務狀態");
                    break;
            }
        }

        /**
         * provider被enable時觸發此函數，比如GPS被打開
         */
        @Override
        public void onProviderEnabled(String provider) {
        }

        /**
         * provider被disable時觸發此函數，比如GPS被關閉
         */
        @Override
        public void onProviderDisabled(String provider) {
        }
    }

    public interface OnLocationChangeListener {

        /**
         * 獲取最後一次保留的坐標
         *
         * @param location 坐標
         */
        void getLastKnownLocation(Location location);

        /**
         * 當坐標改變時觸發此函數，如果Provider傳進相同的坐標，它就不會被觸發
         *
         * @param location 坐標
         */
        void onLocationChanged(Location location);

        /**
         * provider的在可用、暫時不可用和無服務三個狀態直接切換時觸發此函數
         *
         * @param provider 提供者
         * @param status   狀態
         * @param extras   provider可選包
         */
        void onStatusChanged(String provider, int status, Bundle extras);//位置狀態發生改變
    }
}