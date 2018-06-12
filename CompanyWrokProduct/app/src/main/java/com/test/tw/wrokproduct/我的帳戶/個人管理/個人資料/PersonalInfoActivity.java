package com.test.tw.wrokproduct.我的帳戶.個人管理.個人資料;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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
    Button modify_coverbg, pdata_save, adata_save, phone_bind, email_bind, fb_bind, google_bind;
    SQLiteDatabaseHandler db;
    View personal_info_bg;
    int coverbg;
    CircleImageView select_photo;
    LinearLayout personal_btn_city, personal_btn_area, personal_btn_bankcode, personal_btn_bankname;
    TextView personal_txt_account, personal_txt_birthday, personal_txt_city, personal_txt_area, personal_txt_bankcode, personal_txt_bankname;
    EditText personal_edit_name, personal_edit_memberId, personal_edit_zipcode, personal_edit_address, personal_edit_subbankname, personal_edit_bankaccount, personal_edit_bankaccountname;
    Intent intent;
    String prezipcode;
    Spinner personal_spinner_gender;
    AlertDialog alertDialog;
    DatePicker datePicker;
    String token;
    Map<String, String> pdata, adata, bdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        token = ((GlobalVariable) getApplicationContext()).getToken();
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject json = new MemberJsonData().getPersonData(token);
                pdata = AnalyzeMember.getPersonDataPdata(json);
                adata = AnalyzeMember.getPersonDataAdata(json);
                bdata = AnalyzeMember.getPersonDataBdata(json);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initGenderAndBirthday();
                        initMemberInfoText();
                        initBankInfoText();
                        initBindButton();
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
        select_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(PersonalInfoActivity.this,CameraActivity.class),100);
            }
        });
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
        ((GradientDrawable) pdata_save.getBackground()).setColor(getResources().getColor(R.color.red));
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

        personal_btn_bankcode = findViewById(R.id.personal_btn_bankcode);
        personal_btn_bankcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(PersonalInfoActivity.this, ListVIewActivity.class), 2);
            }
        });
        personal_btn_bankname = findViewById(R.id.personal_btn_bankname);
        personal_btn_bankname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(PersonalInfoActivity.this, ListVIewActivity.class), 2);
            }
        });
        personal_txt_bankcode = findViewById(R.id.personal_txt_bankcode);
        personal_txt_bankname = findViewById(R.id.personal_txt_bankname);
        personal_edit_subbankname = findViewById(R.id.personal_edit_subbankname);
        personal_edit_bankaccount = findViewById(R.id.personal_edit_bankaccount);
        personal_edit_bankaccountname = findViewById(R.id.personal_edit_bankaccountname);
        adata_save = findViewById(R.id.adata_save);


        phone_bind = findViewById(R.id.phone_bind);
        email_bind = findViewById(R.id.email_bind);
        fb_bind = findViewById(R.id.fb_bind);
        google_bind = findViewById(R.id.google_bind);
    }

    private void initMemberInfoText() {
        personal_txt_account.setText(pdata.get("account"));
        personal_edit_name.setText(pdata.get("name"));
        personal_edit_memberId.setText(pdata.get("memberid"));
        personal_spinner_gender.setSelection((Integer.parseInt(pdata.get("sex"))));
        personal_txt_birthday.setText(pdata.get("birthday"));
        personal_edit_zipcode.setText(pdata.get("zipcode"));
        personal_txt_city.setText(pdata.get("city"));
        personal_txt_area.setText(pdata.get("area"));
        personal_edit_address.setText(pdata.get("address"));
    }

    private void closeBillInfo() {
        personal_btn_bankcode.setEnabled(false);
        personal_btn_bankname.setEnabled(false);
        personal_edit_subbankname.setEnabled(false);
        personal_edit_subbankname.setBackgroundResource(0);
        personal_edit_bankaccount.setEnabled(false);
        personal_edit_bankaccount.setBackgroundResource(0);
        personal_edit_bankaccountname.setEnabled(false);
        personal_edit_bankaccountname.setBackgroundResource(0);
        adata_save.setClickable(false);
        adata_save.setText("已綁定");
        ((GradientDrawable) adata_save.getBackground()).setColor(getResources().getColor(R.color.gainsboro));
        findViewById(R.id.arrow3).setVisibility(View.INVISIBLE);
        findViewById(R.id.arrow4).setVisibility(View.INVISIBLE);
    }

    private void initBankInfoText() {
        if (!adata.get("bcode").equals("")) {
            closeBillInfo();
        } else {
            ((GradientDrawable) adata_save.getBackground()).setColor(getResources().getColor(R.color.red));
            adata_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!token.equals("") && !personal_txt_bankcode.getText().toString().equals("") && !personal_edit_subbankname.getText().toString().equals("")
                            && !personal_edit_bankaccount.getText().toString().equals("") && !personal_edit_bankaccountname.getText().toString().equals("")) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                JSONObject json = new MemberJsonData().updateBillingData(token, personal_txt_bankcode.getText().toString(),
                                        personal_edit_subbankname.getText().toString(), personal_edit_bankaccount.getText().toString(), personal_edit_bankaccountname.getText().toString());

                                if (AnalyzeMember.checkSuccess(json)) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            closeBillInfo();
                                        }
                                    });
                                }
                            }
                        }).start();

                    }
                }
            });

        }
        personal_txt_bankcode.setText(adata.get("bcode"));
        personal_txt_bankname.setText(adata.get("bname"));
        personal_edit_subbankname.setText(adata.get("bbank"));
        personal_edit_bankaccount.setText(adata.get("bcard"));
        personal_edit_bankaccountname.setText(adata.get("buname"));

    }

    private void initBindButton() {
        ((GradientDrawable) phone_bind.getBackground()).setColor(getResources().getColor(R.color.colorPrimary));
        ((GradientDrawable) email_bind.getBackground()).setColor(getResources().getColor(R.color.colorPrimary));
        ((GradientDrawable) fb_bind.getBackground()).setColor(getResources().getColor(R.color.colorPrimary));
        ((GradientDrawable) google_bind.getBackground()).setColor(getResources().getColor(R.color.colorPrimary));
        if (bdata.get("mactivate").equals("1")) {
            phone_bind.setText("取消綁定");

        }
        if (bdata.get("eactivate").equals("1")) {
            email_bind.setText("取消綁定");

        }
        if (bdata.get("factivate").equals("1")) {
            fb_bind.setText("取消綁定");

        }
        if (bdata.get("gactivate").equals("1")) {
            google_bind.setText("取消綁定");

        }
    }

    private void initGenderAndBirthday() {
        ArrayAdapter<CharSequence> genderList = ArrayAdapter.createFromResource(this,
                R.array.gender,
                android.R.layout.simple_spinner_dropdown_item);
        personal_spinner_gender.setAdapter(genderList);

        datePicker = new DatePicker(this);
        String[] date = pdata.get("birthday").split("-");
        try {
            datePicker.init(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]), null);
        } catch (Exception e) {
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

            } else if (requestCode == 1) {
                personal_edit_zipcode.setText(null);
                personal_txt_area.setText(data.getStringExtra("area"));
                personal_txt_area.setTextColor(Color.BLACK);
                prezipcode = data.getStringExtra("zipcode");
                personal_edit_zipcode.setText(prezipcode);

            } else if (requestCode == 2) {
                personal_txt_bankcode.setText(data.getStringExtra("bcode"));
                personal_txt_bankname.setText(data.getStringExtra("bname"));
            }else if(requestCode==100){
                byte[] bis = data.getByteArrayExtra("picture");
                Bitmap bitmap = BitmapFactory.decodeByteArray(bis, 0, bis.length);
                select_photo.setImageBitmap(bitmap);
                if(bitmap!=null)
                bitmap.recycle();
            }
        }
    }
}
