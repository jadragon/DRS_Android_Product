package com.example.alex.eip_product.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alex.eip_product.MainActivity;
import com.example.alex.eip_product.R;
import com.example.alex.eip_product.fragment.Fragment_calendar;
import com.example.alex.eip_product.fragment.Fragment_home;
import com.example.alex.eip_product.fragment.Fragment_inspect_content;

import java.util.ArrayList;

public class MainMenuAdapter extends RecyclerView.Adapter<MainMenuAdapter.RecycleHolder> {
    private ArrayList<String> list;
    private Context ctx;

    public MainMenuAdapter(Context ctx) {
        this.ctx = ctx;
        initList();
    }

    private void initList() {
        list = new ArrayList<>();
        list.add("驗表行程");
        list.add("出貨檢驗表");
    }

    @Override
    public MainMenuAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_menu, null);
        return new MainMenuAdapter.RecycleHolder(view);
    }

    @Override
    public void onBindViewHolder(RecycleHolder holder, final int position) {
        holder.title.setText(list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;

        public RecycleHolder(View view) {
            super(view);
            title = view.findViewWithTag("title");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Fragment fragment_calendar = ((FragmentActivity) ctx).getSupportFragmentManager().findFragmentByTag("calendar");
            if (fragment_calendar == null) {
                fragment_calendar = new Fragment_calendar();
            }
            ((MainActivity) ctx).switchFrament(fragment_calendar, "calendar");
        }
    }

}
