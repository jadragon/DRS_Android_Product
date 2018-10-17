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
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (36 * dm.density), 1.0f);
        LinearLayout linearLayout = new LinearLayout(ctx);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(params);

        TextView tv = new TextView(ctx);
        tv.setTag("title");
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        //     tv.setBackgroundColor(ctx.getResources().getColor(R.color.default_gray));
        params.setMargins(0, 0, 0, 0);
        tv.setPadding((int) (dm.density * 10), 0, 0, 0);
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.8f);
        tv.setLayoutParams(params);
        linearLayout.addView(tv);
        tv = new TextView(ctx);
        tv.setTag("price");
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        //     tv.setBackgroundColor(ctx.getResources().getColor(R.color.default_gray));
        params.setMargins(0, 0, 0, 0);
        tv.setPadding((int) (dm.density * 10), 0, 0, 0);
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.2f);
        tv.setLayoutParams(params);
        linearLayout.addView(tv);
        return new SimpleTextAdapter.RecycleHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, final int position) {
        holder.title.setText(list.get(position));
        holder.price.setText(list.get(position));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, price;

        public RecycleHolder(View view) {
            super(view);
            title = view.findViewWithTag("title");
            price = view.findViewWithTag("price");
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
