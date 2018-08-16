package com.example.alex.posdemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.alex.posdemo.R;

/**
 * Created by user on 2017/5/30.
 */

public class Fragment_Home extends Fragment {
    View v;
    private View top_itme1, top_itme2, top_itme3, top_itme4;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_home_layout, container, false);
        initTopItem();
        return v;
    }

    private void initTopItem() {

        top_itme1 = v.findViewById(R.id.home_top_item1);
        top_itme2 = v.findViewById(R.id.home_top_item2);
        top_itme3 = v.findViewById(R.id.home_top_item3);
        top_itme4 = v.findViewById(R.id.home_top_item4);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.home_top_item1:
                        Toast.makeText(getContext(), "1", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.home_top_item2:
                        Toast.makeText(getContext(), "2", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.home_top_item3:
                        Toast.makeText(getContext(), "3", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.home_top_item4:
                        Toast.makeText(getContext(), "4", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        top_itme1.setOnClickListener(onClickListener);
        top_itme2.setOnClickListener(onClickListener);
        top_itme3.setOnClickListener(onClickListener);
        top_itme4.setOnClickListener(onClickListener);

    }

}
