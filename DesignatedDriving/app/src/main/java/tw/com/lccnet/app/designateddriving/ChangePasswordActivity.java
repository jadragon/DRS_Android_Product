package tw.com.lccnet.app.designateddriving;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import tw.com.lccnet.app.designateddriving.Utils.MatchesUtils;

public class ChangePasswordActivity extends ToolbarActivity {
    private EditText old_password, new_password;
    private Button change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initToolbar("修改密碼", true);
        initView();
        initListener();
    }

    private void initListener() {
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(old_password.getText())) {
                    old_password.setError("舊密碼不能為空");
                    return;
                } else if (TextUtils.isEmpty(new_password.getText())) {
                    new_password.setError("新密碼不能為空");
                    return;
                } else if (!MatchesUtils.matchPassword(new_password.getText().toString())) {
                    new_password.setError("密碼為6-12位字母加數字");
                    return;
                } else if (old_password.getText().toString().equals(new_password.getText().toString())) {
                    new_password.setError("新舊密碼不能重複");
                    return;
                }
                Toast.makeText(ChangePasswordActivity.this, "成功但還沒串資料", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        old_password = findViewById(R.id.old_password);
        new_password = findViewById(R.id.new_password);
        change = findViewById(R.id.change);
    }


}
