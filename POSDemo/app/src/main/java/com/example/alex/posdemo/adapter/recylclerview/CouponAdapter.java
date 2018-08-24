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

import org.json.JSONObject;

import java.util.ArrayList;

import library.AnalyzeJSON.APIpojo.CouponPojo;
import library.AnalyzeJSON.Analyze_CountInfo;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.RecycleHolder> {
    private Context ctx;
    DisplayMetrics dm;
    ArrayList<CouponPojo> list;

    public CouponAdapter(Context ctx, JSONObject json) {
        this.ctx = ctx;
        dm = ctx.getResources().getDisplayMetrics();
        list = new Analyze_CountInfo().getPreferential_ContentBD(json);
        list.addAll(new Analyze_CountInfo().getPreferential_ContentFD(json));
        list.addAll(new Analyze_CountInfo().getPreferential_ContentMC(json));
    }

    @Override
    public CouponAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (36 * dm.density), 1.0f);
        LinearLayout linearLayout = new LinearLayout(ctx);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(params);

        TextView tv = new TextView(ctx);
        tv.setTag("title");
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        tv.setPadding((int) (dm.density * 10), 0, 0, 0);
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.8f);
        tv.setLayoutParams(params);
        linearLayout.addView(tv);
        tv = new TextView(ctx);
        tv.setTag("price");
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setPadding((int) (dm.density * 10), 0, 0, 0);
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.2f);
        tv.setLayoutParams(params);
        linearLayout.addView(tv);
        return new CouponAdapter.RecycleHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, final int position) {
        holder.title.setText("＊" + list.get(position).getName());
        holder.price.setText("| " + list.get(position).getDiscount());
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

    public void setFilter(JSONObject json) {
        list = new Analyze_CountInfo().getPreferential_ContentBD(json);
        list.addAll(new Analyze_CountInfo().getPreferential_ContentFD(json));
        list.addAll(new Analyze_CountInfo().getPreferential_ContentMC(json));
        notifyDataSetChanged();

    }
}
