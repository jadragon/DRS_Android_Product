package com.example.alex.posdemo.adapter.recylclerview;

import android.content.Context;
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
    DisplayMetrics dm;
    ArrayList<String> list;

    public SimpleTextAdapter(Context ctx, ArrayList<String> list) {
        this.ctx = ctx;
        this.list = list;
        dm = ctx.getResources().getDisplayMetrics();

    }

    @Override
    public SimpleTextAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (36 * dm.density));
        TextView tv = new TextView(ctx);
        tv.setTag("header");
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        //     tv.setBackgroundColor(ctx.getResources().getColor(R.color.default_gray));
        params.setMargins(0, 0, 0, 0);
        tv.setPadding((int) (dm.density * 10), 0, 0, 0);
        tv.setLayoutParams(params);
        return new SimpleTextAdapter.RecycleHolder(tv);
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, final int position) {
        holder.textView.setText(list.get(position));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;

        public RecycleHolder(View view) {
            super(view);
            textView = (TextView) view;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

        }

    }

    public void setFilter(ArrayList<String> list) {
        this.list = list;
        notifyDataSetChanged();

    }
}
