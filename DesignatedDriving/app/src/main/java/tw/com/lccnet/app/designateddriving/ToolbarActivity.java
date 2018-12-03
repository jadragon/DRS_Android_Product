package tw.com.lccnet.app.designateddriving;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class ToolbarActivity extends AppCompatActivity {


    protected void initToolbar(String title, boolean navigationVisible) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
            ((TextView) findViewById(R.id.toolbar_title)).setText(title);

            if (navigationVisible) {
                toolbar.setNavigationIcon(R.drawable.back);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }
        }

    }
}
