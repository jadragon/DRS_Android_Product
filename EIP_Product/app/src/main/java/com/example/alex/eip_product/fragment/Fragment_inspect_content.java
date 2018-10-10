package com.example.alex.eip_product.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alex.eip_product.InsepectOrderActivity;
import com.example.alex.eip_product.R;

/**
 * Created by user on 2017/5/30.
 */

public class Fragment_inspect_content extends Fragment {
    private View v;
    private TextView title, wirte_txt_inspect1, wirte_txt_inspect2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_inspect_content, container, false);
        initTextView();
        return v;
    }

    private void initTextView() {
        title = v.findViewById(R.id.inspect_content_txt_title);
        title.setText(getArguments().getString("date") + "驗貨內容");
        wirte_txt_inspect1 = v.findViewById(R.id.wirte_txt_inspect1);
        wirte_txt_inspect2 = v.findViewById(R.id.wirte_txt_inspect2);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                switch (view.getId()) {
                    case R.id.wirte_txt_inspect1:
                        intent = new Intent(getContext(), InsepectOrderActivity.class);
                        getContext().startActivity(intent);
                        break;
                    case R.id.wirte_txt_inspect2:
                        intent = new Intent(getContext(), InsepectOrderActivity.class);
                        getContext().startActivity(intent);
                        break;
                }
            }
        };
        wirte_txt_inspect1.setOnClickListener(clickListener);
        wirte_txt_inspect2.setOnClickListener(clickListener);

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            title.setText(getArguments().getString("date") + "驗貨內容");
        }
    }

}
