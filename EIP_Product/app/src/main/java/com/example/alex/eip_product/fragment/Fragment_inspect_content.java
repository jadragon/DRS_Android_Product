package com.example.alex.eip_product.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alex.eip_product.R;

/**
 * Created by user on 2017/5/30.
 */

public class Fragment_inspect_content extends Fragment {
    View v;
TextView title;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_inspect_content, container, false);
        initTextView();
        return v;
    }

    private void initTextView() {
        title=v.findViewById(R.id.inspect_content_txt_title);
        title.setText(getArguments().getString("date")+"驗貨內容");
    }


}
