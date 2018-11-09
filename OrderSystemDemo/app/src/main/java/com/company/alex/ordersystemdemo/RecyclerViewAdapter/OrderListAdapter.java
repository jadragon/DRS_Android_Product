package com.company.alex.ordersystemdemo.RecyclerViewAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.company.alex.ordersystemdemo.API.Analyze.Analyze_Order;
import com.company.alex.ordersystemdemo.API.Analyze.Pojo.OrderDataPojo;
import com.company.alex.ordersystemdemo.OrderListDetailActivity;
import com.company.alex.ordersystemdemo.R;

import org.json.JSONObject;

import java.util.ArrayList;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.RecycleHolder> {
    private Context ctx;
    private ArrayList<OrderDataPojo> list;
    private String status;

    public OrderListAdapter(Context ctx, JSONObject jsonObject, String status) {
        this.ctx = ctx;
        this.status = status;
        if (jsonObject != null) {
            list = new Analyze_Order().getOrder_data(jsonObject);
        } else {
            list = new ArrayList<>();
        }

    }

    @Override
    public OrderListAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orderlist, parent, false);
        return new OrderListAdapter.RecycleHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, final int position) {
        holder.o_number.setText("單號:"+list.get(position).getO_number());
        holder.s_name.setText(list.get(position).getS_name());
        holder.f_sum.setText(list.get(position).getF_sum() + "元");

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView o_number, s_name, f_sum;

        public RecycleHolder(View view) {
            super(view);
            o_number = view.findViewWithTag("o_number");
            s_name = view.findViewWithTag("s_name");
            f_sum = view.findViewWithTag("f_sum");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Intent intent = new Intent(ctx, OrderListDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("OrderDataPojo", list.get(position));
            intent.putExtras(bundle);
            intent.putExtra("status", status);
            ((FragmentActivity) ctx).startActivityForResult(intent, 100);
        }
    }

    public void setFilter(JSONObject jsonObject) {
        if (jsonObject != null) {
            list = new Analyze_Order().getOrder_data(jsonObject);
        } else {
            list = new ArrayList<>();
        }

        notifyDataSetChanged();

    }
}
