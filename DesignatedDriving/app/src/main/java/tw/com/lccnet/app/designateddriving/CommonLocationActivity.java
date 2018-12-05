package tw.com.lccnet.app.designateddriving;

import android.os.Bundle;

public class CommonLocationActivity extends ToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_location);
        initToolbar("常用地點", true);
    }
}
