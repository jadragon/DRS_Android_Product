package library;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONObject;

public abstract class JsonDataThread extends Thread {
    public final static Handler handler= new Handler(Looper.getMainLooper());
    @Override
    public void run() {
        synchronized (this) {
            final JSONObject json = getJsonData();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    runUiThread(json);
                }
            });
        }

    }

    public abstract JSONObject getJsonData();

    public abstract void runUiThread(JSONObject json);

}
