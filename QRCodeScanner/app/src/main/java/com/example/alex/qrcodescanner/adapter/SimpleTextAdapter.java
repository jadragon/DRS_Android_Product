package com.example.alex.qrcodescanner.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alex.qrcodescanner.pojo.DataPojo;

import java.util.List;

public class SimpleTextAdapter extends RecyclerView.Adapter<SimpleTextAdapter.RecycleHolder> {
    private Context ctx;
    private List<DataPojo> list;
    private DisplayMetrics dm;
    int pre_select = 0;

    public SimpleTextAdapter(Context ctx, List<DataPojo> list) {
        this.ctx = ctx;
        this.list = list;
        dm = ctx.getResources().getDisplayMetrics();


    }


    @Override
    public SimpleTextAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView textView = new TextView(ctx);
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setPadding((int) (20 * dm.density), (int) (10 * dm.density), (int) (20 * dm.density), (int) (10 * dm.density));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        textView.setTextColor(Color.RED);
        return new SimpleTextAdapter.RecycleHolder(ctx, textView);
    }

    private void setBackground(View view, int stroke_color, int background_color) {
        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadius(10 * dm.density);
        shape.setStroke((int) (1 * dm.density), stroke_color);
        shape.setColor(background_color);
        view.setBackgroundDrawable(shape);
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, final int position) {
        holder.text.setText(list.get(position).id);
        if (position == 0) {
            setBackground(holder.text, Color.RED, Color.GREEN);
        } else {
            setBackground(holder.text, Color.RED, Color.WHITE);
        }

        if (list.get(position).select) {
            setBackground(holder.text, Color.RED, Color.GRAY);
        } else {
            if (position == 0) {
                setBackground(holder.text, Color.RED, Color.GREEN);
            } else {
                setBackground(holder.text, Color.RED, Color.WHITE);
            }
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //  TextView color, size;
        TextView text;
        Context ctx;

        public RecycleHolder(Context ctx, View view) {
            super(view);

            this.ctx = ctx;

            text = (TextView) view;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != -1) {
                if (position == pre_select) {
                    if (list.get(position).select) {
                        list.get(position).select = false;
                    } else {
                        list.get(position).select = true;
                    }
                } else {
                    list.get(pre_select).select = false;
                    list.get(position).select = true;
                }
                pre_select = position;
                notifyDataSetChanged();
            }
        }
    }


    public void setFilter(List<DataPojo> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public int getSelectPosition() {
        pre_select = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).select) {
                return i;
            }
        }

        return -1;
    }

}
