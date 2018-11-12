package com.test.tw.wrokproduct.我的帳戶.諮詢管理.聯絡劦譽;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.R;
import com.test.tw.wrokproduct.我的帳戶.個人管理.個人資料.CameraActivity;

import org.json.JSONObject;

import Util.AsyncTaskUtils;
import Util.IDataCallBack;
import library.AnalyzeJSON.AnalyzeUtil;
import library.GetJsonData.ContactJsonData;

public class WriteMailActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText write_mail_title, write_mail_note;
    private ImageView write_mail_photo1, write_mail_photo2, write_mail_photo3, write_mail_photo4, write_mail_photo5, write_mail_photo6;
    private GlobalVariable gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_mail);
        gv = ((GlobalVariable) getApplicationContext());
        initToolbar();
        write_mail_title = findViewById(R.id.write_mail_title);
        write_mail_note = findViewById(R.id.write_mail_note);
        initPhotos();
    }

    private void initPhotos() {
        View.OnClickListener clickListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(WriteMailActivity.this, CameraActivity.class);
                intent.putExtra("Shape", "square");

                switch (view.getId()) {
                    case R.id.write_mail_photo1:
                        startActivityForResult(intent, 100);
                        break;
                    case R.id.write_mail_photo2:
                        startActivityForResult(intent, 200);
                        break;
                    case R.id.write_mail_photo3:
                        startActivityForResult(intent, 300);
                        break;
                    case R.id.write_mail_photo4:
                        startActivityForResult(intent, 400);
                        break;
                    case R.id.write_mail_photo5:
                        startActivityForResult(intent, 500);
                        break;
                    case R.id.write_mail_photo6:
                        startActivityForResult(intent, 600);
                        break;
                }
            }
        };
        write_mail_photo1 = findViewById(R.id.write_mail_photo1);
        write_mail_photo1.setOnClickListener(clickListener);
        write_mail_photo2 = findViewById(R.id.write_mail_photo2);
        write_mail_photo2.setOnClickListener(clickListener);
        write_mail_photo3 = findViewById(R.id.write_mail_photo3);
        write_mail_photo3.setOnClickListener(clickListener);
        write_mail_photo4 = findViewById(R.id.write_mail_photo4);
        write_mail_photo4.setOnClickListener(clickListener);
        write_mail_photo5 = findViewById(R.id.write_mail_photo5);
        write_mail_photo5.setOnClickListener(clickListener);
        write_mail_photo6 = findViewById(R.id.write_mail_photo6);
        write_mail_photo6.setOnClickListener(clickListener);
    }

    private void initToolbar() {
        //Toolbar 建立
        toolbar = findViewById(R.id.include_toolbar);
        ((TextView) findViewById(R.id.include_toolbar_title)).setText("撰寫信件");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchmenu, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_search);
        menuItem.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_send) {

            AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                @Override
                public JSONObject onTasking(Void... params) {
                    return new ContactJsonData().setContact(gv.getToken(), write_mail_title.getText().toString(), write_mail_note.getText().toString());
                }

                @Override
                public void onTaskAfter(JSONObject jsonObject) {

                    if (AnalyzeUtil.checkSuccess(jsonObject)) {
                        finish();
                    }
                }
            });


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                if (data != null) {
                    byte[] bis = data.getByteArrayExtra("picture");
                    write_mail_photo1.setImageBitmap(BitmapFactory.decodeByteArray(bis, 0, bis.length));
                }
                break;
            case 200:
                if (data != null) {
                    byte[] bis = data.getByteArrayExtra("picture");
                    write_mail_photo2.setImageBitmap(BitmapFactory.decodeByteArray(bis, 0, bis.length));
                }
                break;
            case 300:
                if (data != null) {
                    byte[] bis = data.getByteArrayExtra("picture");
                    write_mail_photo3.setImageBitmap(BitmapFactory.decodeByteArray(bis, 0, bis.length));
                }
                break;
            case 400:
                if (data != null) {
                    byte[] bis = data.getByteArrayExtra("picture");
                    write_mail_photo4.setImageBitmap(BitmapFactory.decodeByteArray(bis, 0, bis.length));
                }
                break;
            case 500:
                if (data != null) {
                    byte[] bis = data.getByteArrayExtra("picture");
                    write_mail_photo5.setImageBitmap(BitmapFactory.decodeByteArray(bis, 0, bis.length));
                }
                break;
            case 600:
                if (data != null) {
                    byte[] bis = data.getByteArrayExtra("picture");
                    write_mail_photo6.setImageBitmap(BitmapFactory.decodeByteArray(bis, 0, bis.length));
                }
                break;
        }

    }
}
