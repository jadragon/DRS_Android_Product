package com.example.alex.posdemo.adapter.recylclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alex.posdemo.R;

import java.util.Map;

import Utils.ComponentUtil;

public class QuickMenuAdapter extends RecyclerView.Adapter<QuickMenuAdapter.RecycleHolder> {
    private Context ctx;
    DisplayMetrics dm;
    String[] list;
    private TypedArray image;

    public QuickMenuAdapter(Context ctx, Map<String, Integer> map) {
        this.ctx = ctx;
        dm = ctx.getResources().getDisplayMetrics();
        if (map != null) {
            image = ctx.getResources().obtainTypedArray(map.get("image"));
            list = ctx.getResources().getStringArray(map.get("text"));
        }
    }

    @Override
    public QuickMenuAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quickmenu, parent, false);
        return new QuickMenuAdapter.RecycleHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, final int position) {
        holder.imageView.setImageResource(image.getResourceId(position, 0));
        holder.textView.setText(list[position]);
    }


    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        }
        return list.length;
    }

    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView textView;
        ComponentUtil componentUtil;

        public RecycleHolder(View view) {
            super(view);
            componentUtil = new ComponentUtil();
            imageView = view.findViewWithTag("image");
            textView = view.findViewWithTag("text");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            switch (position) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
            }
        }

    }


    public void setFilter(Map<String, Integer> map) {
        image = ctx.getResources().obtainTypedArray(map.get("image"));
        list = ctx.getResources().getStringArray(map.get("text"));
        notifyDataSetChanged();

    }
}