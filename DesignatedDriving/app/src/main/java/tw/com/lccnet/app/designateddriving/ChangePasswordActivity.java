package tw.com.lccnet.app.designateddriving;

import android.os.Bundle;

public class ChangePasswordActivity extends ToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initToolbar("修改密碼", true);
    }
}
