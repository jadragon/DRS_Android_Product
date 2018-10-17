package com.example.alex.ordersystemdemo.RecyclerViewAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alex.ordersystemdemo.OrderListDetailActivity;
import com.example.alex.ordersystemdemo.R;

import java.util.ArrayList;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.RecycleHolder> {
    private Context ctx;
    private DisplayMetrics dm;
    private ArrayList<String> list;

    public OrderListAdapter(Context ctx, ArrayList<String> list) {
        this.ctx = ctx;
        this.list = list;
        dm = ctx.getResources().getDisplayMetrics();

    }

    @Override
    public OrderListAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orderlist, parent, false);
        return new OrderListAdapter.RecycleHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, final int position) {

    }


    @Override
    public int getItemCount() {
        return 20;
    }

    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView detail;

        public RecycleHolder(View view) {
            super(view);
            detail = view.findViewWithTag("detail");
            detail.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            ctx.startActivity(new Intent(ctx, OrderListDetailActivity.class));
        }

    }

    public void setFilter(ArrayList<String> list) {
        this.list = list;
        notifyDataSetChanged();

    }
}
