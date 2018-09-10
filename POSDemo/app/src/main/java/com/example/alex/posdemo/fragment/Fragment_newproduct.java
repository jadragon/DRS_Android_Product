package com.example.alex.posdemo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.alex.posdemo.R;
import com.example.alex.posdemo.SelectBrandActivity;

/**
 * Created by user on 2017/5/30.
 */

public class Fragment_newproduct extends Fragment {
    View v;
    Button newproduct_btn_newbrand;
    EditText newproduct_edit_code, newproduct_edit_name;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_newproduct_layout, container, false);
        initSearchButton();
        initEditText();
        return v;
    }

    private void initEditText() {
        newproduct_edit_code = v.findViewById(R.id.newproduct_edit_code);
        newproduct_edit_name = v.findViewById(R.id.newproduct_edit_name);
    }

    private void initSearchButton() {
        newproduct_btn_newbrand = v.findViewById(R.id.newproduct_btn_newbrand);
        newproduct_btn_newbrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SelectBrandActivity.class);
                startActivityForResult(intent, 200);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            String code = data.getStringExtra("code");
            String title = data.getStringExtra("title");
            newproduct_edit_code.setText(code);
            newproduct_edit_name.setText(title);

        }
    }
}
