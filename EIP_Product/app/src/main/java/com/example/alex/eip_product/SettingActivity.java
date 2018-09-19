package com.example.alex.eip_product;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.Locale;

import Utils.PreferenceUtil;

public class SettingActivity extends AppCompatActivity {
    Spinner spinner;
    boolean init_Activity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initLanguageSpinner();
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
                    Intent it = new Intent(SettingActivity.this, MainActivity.class);
                    it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

}
