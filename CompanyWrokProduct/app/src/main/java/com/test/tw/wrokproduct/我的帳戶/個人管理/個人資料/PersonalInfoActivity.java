package com.test.tw.wrokproduct.我的帳戶.個人管理.個人資料;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.ListVIewActivity;
import com.test.tw.wrokproduct.R;

import org.json.JSONObject;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import library.AnalyzeJSON.AnalyzeMember;
import library.GetJsonData.MemberJsonData;
import library.SQLiteDatabaseHandler;
import library.component.ToastMessageDialog;

public class PersonalInfoActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button modify_coverbg, pdata_save;
    SQLiteDatabaseHandler db;
    View personal_info_bg;
    int coverbg;
    CircleImageView select_photo;
    LinearLayout personal_btn_city, personal_btn_area;
    TextView personal_txt_account, personal_txt_birthday, personal_txt_city, personal_txt_area;
    EditText personal_edit_name, personal_edit_memberId, personal_edit_zipcode, personal_edit_address;
    Intent intent;
    String prezipcode;
    Spinner personal_spinner_gender;
    AlertDialog alertDialog;
    DatePicker datePicker;
    String token;
    Map<String, String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        token = ((GlobalVariable) getApplicationContext()).getToken();
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject json = new MemberJsonData().getPersonData(token);
                map = AnalyzeMember.getPersonDataPdata(json);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initGenderAndBirthday();
                        initMemberInfoText();
                    }
                });
            }
        }).start();
        getViewById();
        db = new SQLiteDatabaseHandler(getApplicationContext());

        try {
            coverbg = Integer.parseInt(db.getMemberDetail().get("background"));
        } catch (Exception e) {
            coverbg = 0;
        }

        ImageLoader.getInstance().displayImage(db.getMemberDetail().get("photo"), select_photo);
        personal_info_bg.setBackgroundResource(coverbg);
        initToolbar();

        modify_coverbg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PersonalInfoActivity.this, SelectCoverActivity.class));
            }
        });
        initCityAndArea();


    }

    private void getViewById() {
        personal_info_bg = findViewById(R.id.personal_info_bg);
        select_photo = findViewById(R.id.select_photo);
        modify_coverbg = findViewById(R.id.modify_coverbg);

        personal_btn_city = findViewById(R.id.personal_btn_city);
        personal_btn_area = findViewById(R.id.personal_btn_area);

        personal_txt_account = findViewById(R.id.personal_txt_account);
        personal_edit_name = findViewById(R.id.personal_edit_name);
        personal_edit_memberId = findViewById(R.id.personal_edit_memberId);
        personal_spinner_gender = findViewById(R.id.personal_spinner_gender);
        personal_txt_birthday = findViewById(R.id.personal_txt_birthday);
        personal_edit_zipcode = findViewById(R.id.personal_edit_zipcode);
        personal_txt_city = findViewById(R.id.personal_txt_city);
        personal_txt_area = findViewById(R.id.personal_txt_area);
        personal_edit_address = findViewById(R.id.personal_edit_address);

        pdata_save = findViewById(R.id.pdata_save);
        pdata_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new MemberJsonData().updateBasicData(token, personal_edit_name.getText().toString(), personal_edit_memberId.getText().toString(), personal_spinner_gender.getSelectedItemPosition(),
                                personal_txt_birthday.getText().toString(), personal_txt_city.getText().toString(), personal_txt_area.getText().toString(), personal_edit_zipcode.getText().toString(), personal_edit_address.getText().toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new ToastMessageDialog(PersonalInfoActivity.this, "修改完成").show();
                            }
                        });

                    }
                }).start();
            }
        });
    }

    private void initMemberInfoText() {
        personal_txt_account.setText(map.get("account"));
        personal_edit_name.setText(map.get("name"));
        personal_edit_memberId.setText(map.get("memberid"));
        personal_spinner_gender.setSelection((Integer.parseInt(map.get("sex"))));
        personal_txt_birthday.setText(map.get("birthday"));
        personal_edit_zipcode.setText(map.get("zipcode"));
        personal_txt_city.setText(map.get("city"));
        personal_txt_area.setText(map.get("area"));
        personal_edit_address.setText(map.get("address"));
    }

    private void initGenderAndBirthday() {

        ArrayAdapter<CharSequence> genderList = ArrayAdapter.createFromResource(this,
                R.array.gender,
                android.R.layout.simple_spinner_dropdown_item);
        personal_spinner_gender.setAdapter(genderList);

        datePicker = new DatePicker(this);
        String[] date = map.get("birthday").split("-");
        try {
            datePicker.init(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]), null);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("完成", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                personal_txt_birthday.setText(datePicker.getYear() + "-" + String.format("%02d", datePicker.getMonth() + 1) + "-" + String.format("%02d", datePicker.getDayOfMonth()));
                dialogInterface.dismiss();
            }
        });
        builder.setView(datePicker);
        alertDialog = builder.create();  //创建对话框

        personal_txt_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
            }
        });
    }

    private void initToolbar() {
        //Toolbar 建立
        toolbar = findViewById(R.id.include_toolbar);
        ((TextView) findViewById(R.id.include_toolbar_title)).setText("個人資料");
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void initCityAndArea() {

        personal_edit_zipcode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    Map<String, String> map = db.getCityAndAreaByZipcode(personal_edit_zipcode.getText().toString());
                    if (!map.isEmpty()) {
                        personal_txt_area.setText(map.get("area"));
                        personal_txt_area.setTextColor(Color.BLACK);
                        personal_txt_city.setText(map.get("city"));
                        personal_txt_city.setTextColor(Color.BLACK);
                        prezipcode = personal_edit_zipcode.getText().toString();
                    } else {
                        personal_edit_zipcode.setText(prezipcode);
                    }
                }
            }
        });


        personal_btn_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(PersonalInfoActivity.this, ListVIewActivity.class);
                intent.putExtra("land", "0");
                startActivityForResult(intent, 0);
            }
        });

        personal_btn_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(PersonalInfoActivity.this, ListVIewActivity.class);
                intent.putExtra("city", personal_txt_city.getText().toString());
                startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        try {
            coverbg = Integer.parseInt(db.getMemberDetail().get("background"));
        } catch (Exception e) {
            coverbg = 0;
        }
        personal_info_bg.setBackgroundResource(coverbg);
        Log.e("onRestart", "onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == 0) {
                personal_txt_area.setText("請選擇鄉鎮市區");
                personal_txt_area.setTextColor(getResources().getColor(R.color.gainsboro));
                personal_edit_zipcode.setText(null);
                personal_txt_city.setText(data.getStringExtra("city"));
                personal_txt_city.setTextColor(Color.BLACK);
            } else if (requestCode == 1) {
                personal_edit_zipcode.setText(null);
                personal_txt_area.setText(data.getStringExtra("area"));
                personal_txt_area.setTextColor(Color.BLACK);
                prezipcode = data.getStringExtra("zipcode");
                personal_edit_zipcode.setText(prezipcode);

            }
        }
    }
}
