package tw.com.lccnet.app.designateddriving;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CallNow1_DriverInfoActivity extends ToolbarActivity {
    private Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callnow1_driver_info);
        initToolbar("司機資訊", false);
        initButton();
    }

    private void initButton() {
        next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CallNow1_DriverInfoActivity.this, CallNow2_CarCheckActivity.class));
                finish();
            }
        });

    }


    @Override
    public void onBackPressed() {
    }
}
