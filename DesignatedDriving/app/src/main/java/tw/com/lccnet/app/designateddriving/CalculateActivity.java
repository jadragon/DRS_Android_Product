package tw.com.lccnet.app.designateddriving;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class CalculateActivity extends ToolbarActivity {
    private Spinner sp_time, sp_type;
    private ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[]{"07:00~23:59", "00:00~06:59"});
    private ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[]{"07:00~23:59", "00:00~06:59"});
    private ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[]{"07:00~23:59", "00:00~06:59"});
    private ArrayAdapter<String> arrayAdapter4 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[]{"07:00~23:59", "00:00~06:59"});

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);
        initToolbar("費用試算", true);
        //   initSpinner();

    }

    private void initSpinner() {
        sp_time = findViewById(R.id.sp_time);
        sp_time.setAdapter(arrayAdapter1);


        sp_type = findViewById(R.id.sp_type);
        sp_type.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[]{"立即", "長途", "鐘點"}));
    }

}
