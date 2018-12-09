package tw.com.lccnet.app.designateddriving;

import android.content.Intent;
import android.os.Bundle;

public class CallNow3_DrivingActivity extends ToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callnow3_driving);
        initToolbar("駕駛中", false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(CallNow3_DrivingActivity.this, CallNow5_CountActivity.class));
                finish();
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
    }
}
