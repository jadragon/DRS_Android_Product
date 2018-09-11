package com.example.alex.eip_product.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alex.eip_product.R;

import org.json.JSONObject;

public class SelectBrandAdapter extends RecyclerView.Adapter<SelectBrandAdapter.RecycleHolder> {
    private Context ctx;
    DisplayMetrics dm;
    public static byte TYPE_HEADER = 0;
    public static byte TYPE_CONTENT = 1;
    int pre_select;

    public SelectBrandAdapter(Context ctx, JSONObject json) {
        this.ctx = ctx;
        dm = ctx.getResources().getDisplayMetrics();
    }

    @Override
    public SelectBrandAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (250 * dm.density), ViewGroup.LayoutParams.WRAP_CONTENT);
        if (viewType == TYPE_HEADER) {
            TextView textView = new TextView(ctx);
            params.setMargins((int) (20 * dm.density), 0, (int) (20 * dm.density), (int) (20 * dm.density));
            textView.setLayoutParams(params);
            textView.setPadding((int) (15 * dm.density), (int) (15 * dm.density), (int) (15 * dm.density), (int) (15 * dm.density));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            view = textView;
        } else if (viewType == TYPE_CONTENT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main, parent, false);
        }
        return new SelectBrandAdapter.RecycleHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, int position) {
    }

    @Override
    public int getItemViewType(int position) {
        if (position == TYPE_HEADER) {
            return TYPE_HEADER;
        } else {
            return TYPE_CONTENT;
        }
    }

    @Override
    public int getItemCount() {
        return 30;
    }


    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        ImageView nike;

        public RecycleHolder(View view, int viewType) {
            super(view);
            if (viewType == TYPE_HEADER) {
                title = (TextView) view;
            } else {
                title = view.findViewWithTag("text");
                nike = view.findViewWithTag("nike");
                itemView.setOnClickListener(this);
            }
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