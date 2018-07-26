package library;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONObject;

public abstract class JsonDataThread extends Thread {
    private static final String TAG = "JsonDataThread";
    private Handler handler;

    public JsonDataThread() {
        this.handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void run() {
        synchronized (this) {
            final JSONObject json = getJsonData();

            handler.post(new Runnable() {
                @Override
                public void run() {
                  //  Log.e(TAG, json + "");
                    runUiThread(json);
                }
            });
        }

    }

    public abstract JSONObject getJsonData();

    public abstract void runUiThread(JSONObject json);

}
