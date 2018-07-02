package library;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONObject;

public abstract class JsonDataThread extends Thread {
    private static final String TAG = "JsonDataThread";
    private Handler handler;
    private JSONObject json;

    public JsonDataThread() {
        this.handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void run() {
        this.json = getJsonData();
        Log.e(TAG, json + "");
        handler.post(new Runnable() {
            @Override
            public void run() {
                runUiThread(json);
            }
        });
    }

    public abstract JSONObject getJsonData();

    public abstract void runUiThread(JSONObject json);

}
