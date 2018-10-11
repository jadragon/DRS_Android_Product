package com.example.alex.eip_product.fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.alex.eip_product.MainActivity;
import com.example.alex.eip_product.R;

import java.util.Locale;

import Utils.PreferenceUtil;

/**
 * Created by user on 2017/5/30.
 */

public class Fragment_setting extends Fragment {
    private View v;
    private Spinner spinner;
    private boolean init_Activity = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_setting, container, false);
        initLanguageSpinner();
        return v;
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
            // 中文繁體
            config.locale = Locale.TRADITIONAL_CHINESE;
        } else if (language == 1) {
            // 中文簡體
            config.locale = Locale.SIMPLIFIED_CHINESE;
        }
        resources.updateConfiguration(config, dm);

        // 保存設置語言的類型
        PreferenceUtil.commitString("language", language);
    }

    private void initLanguageSpinner() {
        // 初始化PreferenceUtil
        PreferenceUtil.init(getContext());
        // 依據上次的語言設置，又一次設置語言
        int index = PreferenceUtil.getInt("language", 0);
        switchLanguage(index);
        spinner = v.findViewById(R.id.language);
        spinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.language)));
        spinner.setSelection(index);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switchLanguage(position);
                //更新語言後。destroy當前頁面，又一次繪製
                if (init_Activity) {
                    getActivity().finish();
                    Intent it = new Intent(getContext(), MainActivity.class);
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
