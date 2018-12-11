package tw.com.lccnet.app.designateddriving.Utils;

import org.json.JSONObject;

public class ApiRunnable implements Runnable {
    private JSONObject json;

    public ApiRunnable(JSONObject json) {
        this.json = json;
    }

    @Override
    public void run() {

    }
}
