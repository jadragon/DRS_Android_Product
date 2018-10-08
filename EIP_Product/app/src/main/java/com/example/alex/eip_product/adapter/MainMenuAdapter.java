package com.example.alex.eip_product.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alex.eip_product.InsepectOrderActivity;
import com.example.alex.eip_product.R;
import com.example.alex.eip_product.SettingActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainMenuAdapter extends RecyclerView.Adapter<MainMenuAdapter.RecycleHolder> {
    private Context ctx;
    private ArrayList<Map<String, String>> list;
    private DisplayMetrics dm;

    public MainMenuAdapter(Context ctx) {
        this.ctx = ctx;
        dm = ctx.getResources().getDisplayMetrics();
        initList();
    }

    private void initList() {
        list = new ArrayList<>();
        String[] title = ctx.getResources().getStringArray(R.array.menu_item);
        Map<String, String> map = null;

        for (int i = 0; i < title.length; i++) {
            map = new HashMap<>();
            map.put("image", "");
            map.put("title", title[i]);
            list.add(map);
        }
    }


    @Override
    public MainMenuAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_menu, null);
        return new MainMenuAdapter.RecycleHolder(view);
    }

    @Override
    public void onBindViewHolder(RecycleHolder holder, final int position) {
        holder.title.setText(list.get(position).get("title"));

    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView title;

        public RecycleHolder(View view) {
            super(view);
            image = view.findViewWithTag("image");
            title = view.findViewWithTag("title");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = null;


            switch (getAdapterPosition()) {
                case 0:
                    intent = new Intent(ctx, InsepectOrderActivity.class);
                    break;
                case 9:
                    intent = new Intent(ctx, SettingActivity.class);
                    break;
            }

            ctx.startActivity(intent);
        }
    }

}
