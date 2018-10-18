package com.example.alex.ordersystemdemo.RecyclerViewAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alex.ordersystemdemo.R;

import java.util.ArrayList;

public class StoreListAdapter extends RecyclerView.Adapter<StoreListAdapter.RecycleHolder> {
    private Context ctx;
    private DisplayMetrics dm;
    private ArrayList<String> list;

    public StoreListAdapter(Context ctx, ArrayList<String> list) {
        this.ctx = ctx;
        this.list = list;
        dm = ctx.getResources().getDisplayMetrics();

    }

    @Override
    public StoreListAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_storerlist, parent, false);
        return new StoreListAdapter.RecycleHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, final int position) {

    }


    @Override
    public int getItemCount() {
        return 20;
    }

    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RecycleHolder(View view) {
            super(view);
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
