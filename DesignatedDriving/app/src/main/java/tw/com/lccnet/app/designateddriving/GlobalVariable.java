package tw.com.lccnet.app.designateddriving;

import android.app.Application;

public class GlobalVariable extends Application {
    private double mLatitude, mLongitude;

    public double getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }
}
