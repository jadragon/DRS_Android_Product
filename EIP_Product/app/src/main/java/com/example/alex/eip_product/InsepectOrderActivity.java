package com.example.alex.eip_product;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

import Component.PaintView;
import Utils.CommonUtil;
import Utils.PreferenceUtil;
import db.SQLiteDatabaseHandler;

public class InsepectOrderActivity extends AppCompatActivity {
    private final static int SELECT_FAIL_REASON = 110;
    private LinearLayout alltable, courseTable;
    private Button academyButton, saveButton, resetSignButton;
    int line = 1;
    PaintView paintView;
    TextView title, online, faild_txt_description, pdf_view;
    Spinner spinner;
    boolean init_Activity = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insepect_order);
        initPdfView();
        initLanguageSpinner();
        findView();
        setAnimation(title);
        setAnimation(online);
        // Apply the adapter to the spinner
        academyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddColumTask().execute();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePicture(alltable);
                startActivity(new Intent(InsepectOrderActivity.this, ImageViewActivity.class));
            }
        });
        resetSignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paintView.resetCanvas();
            }
        });
        faild_txt_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(InsepectOrderActivity.this, SelectFailedActivity.class), SELECT_FAIL_REASON);
            }
        });
    }

    private void initPdfView() {
        pdf_view = findViewById(R.id.pdf_view);
        pdf_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InsepectOrderActivity.this, PDFFromServerActivity.class));
            }
        });
    }

    private void initLanguageSpinner() {
        // 初始化PreferenceUtil
        PreferenceUtil.init(this);
        // 依據上次的語言設置，又一次設置語言
        int index = PreferenceUtil.getInt("language", 2);
        switchLanguage(index);
        spinner = findViewById(R.id.language);
        spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.language)));
        spinner.setSelection(index);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switchLanguage(position);
                //更新語言後。destroy當前頁面，又一次繪製
                if (init_Activity) {
                    finish();
                    Intent it = new Intent(InsepectOrderActivity.this, InsepectOrderActivity.class);
                    startActivity(it);
                } else {
                    init_Activity = true;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    void findView() {
        alltable = findViewById(R.id.alltable);
        courseTable = findViewById(R.id.kccx_course_table);
        academyButton = findViewById(R.id.kccx_chaxun1);
        saveButton = findViewById(R.id.kccx_chaxun2);
        resetSignButton = findViewById(R.id.kccx_chaxun3);
        paintView = findViewById(R.id.paintView);
        online = findViewById(R.id.online);
        title = findViewById(R.id.title);
        faild_txt_description = findViewById(R.id.faild_txt_description);
    }


    private class AddColumTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            return "flag";
        }

        @Override
        protected void onPostExecute(String result) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 3);
            if (result.equals("flag")) {
                View view = null;
                if (courseTable.getChildCount() % 3 == 0) {
                    view = new View(InsepectOrderActivity.this);
                    view.setLayoutParams(params);
                    view.setBackgroundColor(getResources().getColor(android.R.color.black));
                    courseTable.addView(view);
                } else {
                    view = new View(InsepectOrderActivity.this);
                    params.height = 1;
                    view.setLayoutParams(params);
                    view.setBackgroundColor(getResources().getColor(android.R.color.black));
                    courseTable.addView(view);
                }
                view = LayoutInflater.from(InsepectOrderActivity.this).inflate(R.layout.item_insepect_order, null, false);

                ((TextView) view.findViewById(R.id.row1)).setText("" + line++);
                //setAnimation(view.findViewById(R.id.row20));
                courseTable.addView(view);


            }
        }


    }

    public void setAnimation(View view) {
        //闪烁
        AlphaAnimation alphaAnimation1 = new AlphaAnimation(0.1f, 1.0f);
        alphaAnimation1.setDuration(1000);
        alphaAnimation1.setRepeatCount(Animation.INFINITE);
        alphaAnimation1.setRepeatMode(Animation.REVERSE);
        view.setAnimation(alphaAnimation1);
        alphaAnimation1.start();
    }

    //save as picture
    public void savePicture(View v) {
        //儲存在SD卡
        /*
        try {
            File newFile = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "test.png");
            newFile.getParentFile().mkdirs();
            try {
                FileOutputStream out = new FileOutputStream(newFile);
                Bitmap vBitmap = convertViewToBitmap(v);
                vBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
        */
        //儲存在資料庫
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap vBitmap = CommonUtil.convertViewToBitmap(v);
        vBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bitmapByte = baos.toByteArray();
        SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(InsepectOrderActivity.this);
        db.addImage(bitmapByte);
        db.close();
    }


    @Override
    protected void onResume() {
        super.onResume();
        toastCheckWIFI();
    }

    private void toastCheckWIFI() {
        if (!CommonUtil.checkWIFI(InsepectOrderActivity.this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("請確認網路是否為連線狀態?");
            builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (!CommonUtil.checkWIFI(InsepectOrderActivity.this)) {
                        toastCheckWIFI();
                    } else {
                        online.setText("線上模式");
                    }
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    online.setText("離線模式");
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        } else {
            online.setText("線上模式");
        }
    }

    /**
     * <切換語言>
     *
     * @param language
     * @see [類、類#方法、類#成員]
     */
    protected void switchLanguage(int language) {
        // 設置應用語言類型
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        if (language == 0) {
            config.locale = Locale.ENGLISH;
        } else if (language == 1) {
            // 中文簡體
            config.locale = Locale.SIMPLIFIED_CHINESE;
        } else {
            // 中文繁體
            config.locale = Locale.TRADITIONAL_CHINESE;
        }
        resources.updateConfiguration(config, dm);

        // 保存設置語言的類型
        PreferenceUtil.commitString("language", language);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_FAIL_REASON) {
            if (resultCode == 110) {
                String failed_reason = data.getStringExtra("failed_reason");
                faild_txt_description.setText(failed_reason);
            }
        }
    }
}
