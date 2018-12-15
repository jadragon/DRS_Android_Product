package com.example.alex.eip_product.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alex.eip_product.R;
import com.example.alex.eip_product.adapter.MainMenuAdapter;

/**
 * Created by user on 2017/5/30.
 */

public class Fragment_home extends Fragment {
    private View v;
    private RecyclerView main_recylcerview;
    private MainMenuAdapter mainMenuAdapter;

    public static Fragment_home newInstance(int index) {
        Fragment_home f = new Fragment_home();
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_home, container, false);
        initRecylcerView();
        return v;
    }

    private void initRecylcerView() {
        main_recylcerview = v.findViewById(R.id.home_recylcerview);
        mainMenuAdapter = new MainMenuAdapter(getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 8);
        //  int imagewidth = 150;
        // gridLayoutManager.setSpanCount(getResources().getDisplayMetrics().widthPixels / imagewidth);
        main_recylcerview.setLayoutManager(gridLayoutManager);
        main_recylcerview.setAdapter(mainMenuAdapter);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
        }
    }
}
