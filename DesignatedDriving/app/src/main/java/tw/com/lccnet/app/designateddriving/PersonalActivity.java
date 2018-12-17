package tw.com.lccnet.app.designateddriving;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import tw.com.lccnet.app.designateddriving.API.Analyze.AnalyzeCustomer;
import tw.com.lccnet.app.designateddriving.API.Analyze.AnalyzeUtil;
import tw.com.lccnet.app.designateddriving.API.CustomerApi;
import tw.com.lccnet.app.designateddriving.Thread.AsyncTaskUtils;
import tw.com.lccnet.app.designateddriving.Thread.IDataCallBack;
import tw.com.lccnet.app.designateddriving.Utils.MatchesUtils;

import static tw.com.lccnet.app.designateddriving.db.SQLiteDatabaseHandler.KEY_BIRTHDAY;
import static tw.com.lccnet.app.designateddriving.db.SQLiteDatabaseHandler.KEY_CMP;
import static tw.com.lccnet.app.designateddriving.db.SQLiteDatabaseHandler.KEY_CONTACT;
import static tw.com.lccnet.app.designateddriving.db.SQLiteDatabaseHandler.KEY_EMAIL;
import static tw.com.lccnet.app.designateddriving.db.SQLiteDatabaseHandler.KEY_MP;
import static tw.com.lccnet.app.designateddriving.db.SQLiteDatabaseHandler.KEY_SEX;
import static tw.com.lccnet.app.designateddriving.db.SQLiteDatabaseHandler.KEY_UNAME;

public class PersonalActivity extends ToolbarActivity implements View.OnClickListener {
    private GlobalVariable gv;
    private ImageView picture;
    private TextView mp, birthday;
    private EditText uname, email, contact, cmp;
    private Button update;
    private Spinner sex;
    private String dateString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        gv = (GlobalVariable) getApplicationContext();
        initToolbar("個人資料", true);
        initView();
        initSpinner();
        initListener();
        initData();
    }

    private void initSpinner() {
        sex.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[]{"無", "男", "女"}));
    }

    private void initListener() {
        birthday.setOnClickListener(this);
        update.setOnClickListener(this);
    }

    private void initView() {
        picture = findViewById(R.id.picture);
        uname = findViewById(R.id.uname);
        sex = findViewById(R.id.sex);
        mp = findViewById(R.id.mp);
        birthday = findViewById(R.id.birthday);
        email = findViewById(R.id.email);
        contact = findViewById(R.id.contact);
        cmp = findViewById(R.id.cmp);
        update = findViewById(R.id.update);
    }

    private void initData() {
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public JSONObject onTasking(Void... params) {
                return CustomerApi.getBasicData(gv.getToken());
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                if (AnalyzeUtil.checkSuccess(jsonObject)) {
                    ContentValues cv = AnalyzeCustomer.getBasicData(jsonObject);
                    //      cv.getAsString(KEY_PICTURE);
                    uname.setText(cv.getAsString(KEY_UNAME));
                    sex.setSelection(cv.getAsInteger(KEY_SEX));
                    mp.setText(cv.getAsString(KEY_MP));

                    dateString = cv.getAsString(KEY_BIRTHDAY);
                    birthday.setText(dateString);

                    email.setText(cv.getAsString(KEY_EMAIL));
                    contact.setText(cv.getAsString(KEY_CONTACT));
                    cmp.setText(cv.getAsString(KEY_CMP));
                } else {
                    Toast.makeText(PersonalActivity.this, AnalyzeUtil.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.birthday:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final DatePicker datePicker = new DatePicker(this);
                if (dateString != null && !dateString.equals("")) {
                    datePicker.updateDate(Integer.parseInt(dateString.substring(0, 4)), Integer.parseInt(dateString.substring(5, 7)) - 1, Integer.parseInt(dateString.substring(8, 10)));
                }

                builder.setView(datePicker);
                builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dateString = datePicker.getYear() + "-" + String.format("%02d", (datePicker.getMonth() + 1)) + "-" + String.format("%02d", datePicker.getDayOfMonth());
                        birthday.setText(dateString);
                        dialog.dismiss();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
                break;
            case R.id.update:
                if (!MatchesUtils.matchEmail(email.getText().toString())) {
                    email.setError("請確認信箱是否正確");
                    return;
                } else if (!MatchesUtils.matchPhone(cmp.getText().toString())) {
                    cmp.setError("請確認號碼是否正確");
                    return;
                }
                AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                    @Override
                    public JSONObject onTasking(Void... params) {
                        return CustomerApi.updateBasicData(gv.getToken(), uname.getText().toString(), "", sex.getSelectedItemPosition() + "", birthday.getText().toString(), email.getText().toString(), contact.getText().toString(), cmp.getText().toString());
                    }

                    @Override
                    public void onTaskAfter(JSONObject jsonObject) {

                    }
                });
                break;
        }
    }
}
