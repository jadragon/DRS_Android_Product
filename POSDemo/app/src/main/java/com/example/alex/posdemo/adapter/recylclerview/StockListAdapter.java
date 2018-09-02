package com.example.alex.posdemo.adapter.recylclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.alex.posdemo.R;

import org.json.JSONObject;

import java.util.ArrayList;

import library.AnalyzeJSON.APIpojo.ProductListPojo;
import library.AnalyzeJSON.Analyze_CountInfo;

public class StockListAdapter extends RecyclerView.Adapter<StockListAdapter.RecycleHolder> {
    private Context ctx;
    DisplayMetrics dm;
    ArrayList<ProductListPojo> list;

    public StockListAdapter(Context ctx, JSONObject json) {
        this.ctx = ctx;
        dm = ctx.getResources().getDisplayMetrics();
    }

    @Override
    public StockListAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stocklist, parent, false);
        return new StockListAdapter.RecycleHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, int position) {
        if (position % 2 == 0) {
            holder.background.setBackgroundColor(ctx.getResources().getColor(R.color.punch_gray2));
        } else {
            holder.background.setBackgroundColor(ctx.getResources().getColor(R.color.punch_gray1));
        }
        holder.line.setText("" + (position + 1));

    }

    @Override
    public int getItemCount() {
        // return list.size();
        return 20;
    }

    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout background;
        ImageView img;
        TextView line, store, brand_title, name, pcode, color, size, fprice, price, total, stotal, flaw_total;

        public RecycleHolder(View view) {
            super(view);
            background = view.findViewWithTag("background");
            line = view.findViewWithTag("line");
            store = view.findViewWithTag("store");
            img = view.findViewWithTag("img");
            brand_title = view.findViewWithTag("brand_title");
            name = view.findViewWithTag("name");
            pcode = view.findViewWithTag("pcode");
            color = view.findViewWithTag("color");
            size = view.findViewWithTag("size");
            fprice = view.findViewWithTag("fprice");
            price = view.findViewWithTag("price");
            total = view.findViewWithTag("total");
            stotal = view.findViewWithTag("stotal");
            flaw_total = view.findViewWithTag("flaw_total");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
        }

    }

    public void setFilter(JSONObject json) {
        notifyDataSetChanged();
    }

}