package library.Component;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.test.tw.wrokproduct.R;

public abstract class ToolbarActivity extends AppCompatActivity {
    protected void initToolbar(boolean showNavigationButton, String toolbarTitle) {
        //Toolbar 建立
        Toolbar toolbar = findViewById(R.id.include_toolbar);
        //title
        // getSupportActionBar().setDisplayShowTitleEnabled(false);
        //setSupportActionBar(toolbar);
        toolbar.setTitle(null);
        ((TextView) findViewById(R.id.include_toolbar_title)).setText(toolbarTitle);
        //NavigationIcon
        if (showNavigationButton) {
            toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }
}
