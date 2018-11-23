package com.example.alex.eip_product.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.alex.eip_product.R;

import static db.OrderDatabase.KEY_PONumber;

/**
 * Created by user on 2017/5/30.
 */

public class Fragment_inspect_content extends Fragment {
    private View v;
    private TextView title, company_name;
    private Bundle bundle;
    private TableLayout tableLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_inspect_content, container, false);
        bundle = getArguments();
        initTextView();
        return v;
    }

    private void initTextView() {
        //標題
        title = v.findViewById(R.id.inspect_content_txt_title);
        title.setText(bundle.getString("date") + "驗貨內容");
        //廠商名稱
        company_name = v.findViewById(R.id.company_name);
        company_name.setText(getArguments().getString("name"));
        tableLayout = v.findViewById(R.id.inspect_content_tableLayout);
        View view;
        for (String number : bundle.getStringArrayList(KEY_PONumber)) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_inspect_content, null);
            ((TextView) view.findViewWithTag("PONumber")).setText(number);
            tableLayout.addView(view);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            title.setText(bundle.getString("date") + "驗貨內容");
        }
    }


}
