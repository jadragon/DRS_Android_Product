package com.example.alex.posdemo;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.alex.posdemo.GlobalVariable.UserInfo;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.io.IOException;

import Utils.AsyncTaskUtils;
import Utils.IDataCallBack;
import library.AnalyzeJSON.AnalyzeUtil;
import library.JsonApi.BrandApi;

public class NewBrandActivity extends Activity {
    ImageView imageView;
    EditText ed_code, ed_title;
    Button bt_cancel, bt_confirm;
    UserInfo userInfo;
    Bitmap bitmap;
    String type;
    String image, pb_no, title, code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_brand);
        type = getIntent().getStringExtra("type");
        image = getIntent().getStringExtra("image");
        pb_no = getIntent().getStringExtra("pb_no");
        title = getIntent().getStringExtra("title");
        code = getIntent().getStringExtra("code");
        userInfo = (UserInfo) getApplicationContext();
        initEditText();
        initImageView();
        initButton();
    }

    private void initButton() {
        bt_cancel = findViewById(R.id.new_brand_cancel);
        bt_confirm = findViewById(R.id.new_brand_confirm);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.new_brand_cancel:
                        finish();
                        break;
                    case R.id.new_brand_confirm:

                        if (!ed_code.getText().toString().equals("") && !ed_title.getText().toString().equals("") && bitmap != null) {
                            AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                                @Override
                                public JSONObject onTasking(Void... params) {
                                    if (type.equals("insert")) {
                                        return new BrandApi().insert_brand(userInfo.getDu_no(), ed_code.getText().toString(), ed_title.getText().toString(), bitmap);
                                    } else if (type.equals("update")) {
                                        return new BrandApi().updata_brand(pb_no, userInfo.getDu_no(), ed_code.getText().toString(), bitmap);
                                    } else {
                                        return null;
                                    }
                                }

                                @Override
                                public void onTaskAfter(JSONObject jsonObject) {
                                    if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                        setResult(100);
                                        bitmap.recycle();
                                        finish();
                                    }
                                }
                            });
                        }
                        setResult(100);
                        finish();

                        break;
                }
            }
        };
        bt_cancel.setOnClickListener(onClickListener);
        bt_confirm.setOnClickListener(onClickListener);

    }

    private void initEditText() {
        ed_code = findViewById(R.id.new_brand_code);
        ed_code.setText(code);
        ed_title = findViewById(R.id.new_brand_name);
        ed_title.setText(title);
    }

    private void initImageView() {
        imageView = findViewById(R.id.new_brand_image);
        if (image != null && !image.equals("")) {
            ImageLoader.getInstance().displayImage(image, imageView);
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
                intent.setType("image/*");
                startActivityForResult(intent, 200);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) { //此處的 RESULT_OK 是系統自定義得一個常量
            return;
        }
        //外界的程式訪問ContentProvider所提供數據 可以通過ContentResolver介面
        ContentResolver resolver = getContentResolver();
        //此處的用於判斷接收的Activity是不是你想要的那個
        if (requestCode == 200) {
            try {
                Uri originalUri = data.getData(); //獲得圖片的uri
                bitmap = MediaStore.Images.Media.getBitmap(resolver, originalUri); //顯得到bitmap圖片
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                if (width > height) {
                    bitmap = Bitmap.createBitmap(bitmap, (width - height) / 2, 0, height, height);
                } else if (width < height) {
                    bitmap = Bitmap.createBitmap(bitmap, 0, (height - width) / 2, width, width);
                }
                imageView.setImageBitmap(bitmap);

                /*
                // 這裏開始的第二部分，獲取圖片的路徑：
                String[] proj = {MediaStore.Images.Media.DATA};
//好像是android多媒體數據庫的封裝介面，具體的看Android文檔
                Cursor cursor = managedQuery(originalUri, proj, null, null, null);
//按我個人理解 這個是獲得用戶選擇的圖片的索引值
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//將光標移至開頭 ，這個很重要，不小心很容易引起越界
                cursor.moveToFirst();
//最後根據索引值獲取圖片路徑
                String path = cursor.getString(column_index);
                */
            } catch (IOException e) {
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmap != null)
            bitmap.recycle();
    }
}
