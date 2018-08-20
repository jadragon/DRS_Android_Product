package com.example.alex.posdemo.adapter.recylclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alex.posdemo.R;

import java.util.ArrayList;

public class PunchAdapter extends RecyclerView.Adapter<PunchAdapter.RecycleHolder> {
    private Context ctx;
    DisplayMetrics dm;
    ArrayList<String> list;


    public PunchAdapter(Context ctx, ArrayList<String> list) {
        this.ctx = ctx;
        dm = ctx.getResources().getDisplayMetrics();

        this.list = list;
    }

    @Override
    public PunchAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slideitem, parent, false);
        return new PunchAdapter.RecycleHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, final int position) {

    }


    @Override
    public int getItemCount() {

        return list.size();
    }

    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        FrameLayout background;
        ImageView imageView;
        TextView textView;

        public RecycleHolder(View view) {
            super(view);
            background = view.findViewById(R.id.slide_item_layout);
            imageView = view.findViewById(R.id.slide_item_image);
            textView = view.findViewById(R.id.slide_item_txt);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
        }

    }


    public void setFilter(ArrayList<String> list) {
        notifyDataSetChanged();

    }
}