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

import java.util.ArrayList;

public class SimpleTextAdapter extends RecyclerView.Adapter<SimpleTextAdapter.RecycleHolder> {
    private Context ctx;
    private ArrayList<Item> arrayList;
    private DisplayMetrics dm;
    int pre_select = 0;

    public SimpleTextAdapter(Context ctx, ArrayList<String> list) {
        this.ctx = ctx;
        dm = ctx.getResources().getDisplayMetrics();
        initList(list);

    }

    private void initList(ArrayList<String> list) {
        arrayList = new ArrayList<>();
        Item item = null;
        for (String code : list) {
            item = new Item();
            item.code = code;
            item.select = false;
            arrayList.add(item);
        }
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

    private void setBackground(View view, int strole_color, int background_color) {
        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadius(10 * dm.density);
        shape.setStroke((int) (1 * dm.density), strole_color);
        shape.setColor(background_color);
        view.setBackgroundDrawable(shape);
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, final int position) {
        holder.text.setText(arrayList.get(position).code);
        if (position == 0) {
            setBackground(holder.text, Color.RED, Color.GREEN);
        } else {
            setBackground(holder.text, Color.RED, Color.WHITE);
        }

        if (arrayList.get(position).select) {
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
        return arrayList.size();
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
                    if (arrayList.get(position).select) {
                        arrayList.get(position).select = false;
                    } else {
                        arrayList.get(position).select = true;
                    }
                } else {
                    arrayList.get(pre_select).select = false;
                    arrayList.get(position).select = true;
                }
                pre_select = position;
                notifyDataSetChanged();
            }
        }
    }


    public void setFilter(ArrayList<String> list) {
        initList(list);
        notifyDataSetChanged();
    }

    public int getSelectPosition() {
        pre_select = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).select) {
                return i;
            }
        }

        return -1;
    }

    private class Item {
        public String code;
        public boolean select;
    }
}
