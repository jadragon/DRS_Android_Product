package tw.com.lccnet.app.designateddriving;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import tw.com.lccnet.app.designateddriving.API.Analyze.AnalyzeCustomer;
import tw.com.lccnet.app.designateddriving.API.Analyze.AnalyzeUtil;
import tw.com.lccnet.app.designateddriving.API.CustomerApi;
import tw.com.lccnet.app.designateddriving.Thread.AsyncTaskUtils;
import tw.com.lccnet.app.designateddriving.Thread.IDataCallBack;
import tw.com.lccnet.app.designateddriving.Utils.MatchesUtils;
import tw.com.lccnet.app.designateddriving.db.SQLiteDatabaseHandler;

import static tw.com.lccnet.app.designateddriving.db.SQLiteDatabaseHandler.KEY_BIRTHDAY;
import static tw.com.lccnet.app.designateddriving.db.SQLiteDatabaseHandler.KEY_CMP;
import static tw.com.lccnet.app.designateddriving.db.SQLiteDatabaseHandler.KEY_CONTACT;
import static tw.com.lccnet.app.designateddriving.db.SQLiteDatabaseHandler.KEY_EMAIL;
import static tw.com.lccnet.app.designateddriving.db.SQLiteDatabaseHandler.KEY_MP;
import static tw.com.lccnet.app.designateddriving.db.SQLiteDatabaseHandler.KEY_PICTURE;
import static tw.com.lccnet.app.designateddriving.db.SQLiteDatabaseHandler.KEY_SEX;
import static tw.com.lccnet.app.designateddriving.db.SQLiteDatabaseHandler.KEY_UNAME;

public class PersonalActivity extends ToolbarActivity implements View.OnClickListener {
    private static final int CAMERA_PERMISSION = 0x10;
    private static final int WRITE_EXTERNAL_PERMISSION = 0x20;
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
        initJSONData();
        initListener();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        picture.setImageBitmap(getPhotoData());
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
        sex.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[]{"無", "男", "女"}));
    }

    private void initListener() {
        birthday.setOnClickListener(this);
        update.setOnClickListener(this);
        picture.setOnClickListener(this);
    }


    private Bitmap getPhotoData() {
        String path = Environment.getExternalStorageDirectory() + "/DCIM/Camera/"
                + "picture.png";
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeFile(path, options);
    }

    private void initJSONData() {
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public JSONObject onTasking(Void... params) {
                return CustomerApi.getBasicData(gv.getToken());
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                if (AnalyzeUtil.checkSuccess(jsonObject)) {
                    SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(PersonalActivity.this);
                    ContentValues cv = AnalyzeCustomer.getBasicData(jsonObject);
                    uname.setText(cv.getAsString(KEY_UNAME));
                    sex.setSelection(cv.getAsInteger(KEY_SEX));
                    mp.setText(cv.getAsString(KEY_MP));
                    ImageLoader.getInstance().displayImage(cv.getAsString(KEY_PICTURE), picture);
                    dateString = cv.getAsString(KEY_BIRTHDAY);
                    birthday.setText(dateString);
                    email.setText(cv.getAsString(KEY_EMAIL));
                    contact.setText(cv.getAsString(KEY_CONTACT));
                    cmp.setText(cv.getAsString(KEY_CMP));
                    db.addMember(cv);
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
                        byte[] data = null;
                        try {
                            Bitmap bm = getPhotoData();
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.PNG, 75, bos);
                            data = bos.toByteArray();
                            bos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return CustomerApi.updateBasicData(gv.getToken(), uname.getText().toString(), data, sex.getSelectedItemPosition() + "",
                                birthday.getText().toString(), email.getText().toString(), contact.getText().toString(), cmp.getText().toString());
                    }

                    @Override
                    public void onTaskAfter(JSONObject jsonObject) {
                        Toast.makeText(PersonalActivity.this, AnalyzeUtil.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                        initJSONData();
                    }
                });
                break;

            case R.id.picture:
                if (ContextCompat.checkSelfPermission(PersonalActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PersonalActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            CAMERA_PERMISSION);
                } else if (ContextCompat.checkSelfPermission(PersonalActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PersonalActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            WRITE_EXTERNAL_PERMISSION);
                } else {
                    startActivity(new Intent(this, CameraActivity.class));
                }


                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                //判断是否勾选禁止后不再询问
                boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(PersonalActivity.this, permissions[0]);
                if (showRequestPermission) {
                    Toast.makeText(this, "權限尚未申請", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(this, "因權限問題，部分功能無法使用", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        } else if (requestCode == WRITE_EXTERNAL_PERMISSION) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                //判断是否勾选禁止后不再询问
                boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(PersonalActivity.this, permissions[0]);
                if (showRequestPermission) {
                    Toast.makeText(this, "權限尚未申請", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(this, "因權限問題，部分功能無法使用", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
        startActivity(new Intent(this, CameraActivity.class));
    }
}
