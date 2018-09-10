package com.example.alex.posdemo.adapter.recylclerview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alex.posdemo.R;

import org.json.JSONObject;

import java.util.ArrayList;

import library.AnalyzeJSON.APIpojo.NewBrandListPojo;
import library.AnalyzeJSON.Analyze_NewBrandInfo;

public class SelectBrandAdapter extends RecyclerView.Adapter<SelectBrandAdapter.RecycleHolder> {
    private Context ctx;
    DisplayMetrics dm;
    ArrayList<NewBrandListPojo> list;
    Analyze_NewBrandInfo analyze_newBrandInfo;
    public static byte TYPE_HEADER = 0;
    public static byte TYPE_CONTENT = 1;
    int pre_select;

    public SelectBrandAdapter(Context ctx, JSONObject json) {
        this.ctx = ctx;
        dm = ctx.getResources().getDisplayMetrics();
        analyze_newBrandInfo = new Analyze_NewBrandInfo();
        list = analyze_newBrandInfo.getTypeRel(json);
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
            textView.setTextColor(ctx.getResources().getColor(R.color.white));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            textView.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.background_corner6dp_gray));
            view = textView;
        } else if (viewType == TYPE_CONTENT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_newbrand_textview, parent, false);
        }
        return new SelectBrandAdapter.RecycleHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, int position) {
        holder.title.setText("(" + list.get(position).getCode() + ")" + list.get(position).getTitle());
        if (getItemViewType(position) == TYPE_CONTENT) {
            if (list.get(position).isSelect()) {
                holder.nike.setVisibility(View.VISIBLE);
                holder.title.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.background_edit_green));
                holder.title.setPadding((int) (15 * dm.density), (int) (15 * dm.density), (int) (15 * dm.density), (int) (15 * dm.density));
            } else {
                holder.nike.setVisibility(View.INVISIBLE);
                holder.title.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.background_edit_white));
                holder.title.setPadding((int) (15 * dm.density), (int) (15 * dm.density), (int) (15 * dm.density), (int) (15 * dm.density));
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getType() == TYPE_HEADER) {
            return TYPE_HEADER;
        } else {
            return TYPE_CONTENT;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //將header獨立出來
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == TYPE_HEADER
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
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
            if (pre_select != position) {
                list.get(position).setSelect(true);
                list.get(pre_select).setSelect(false);
            }
            if (pre_select != 0)
                notifyItemChanged(pre_select);
            pre_select = position;
            notifyItemChanged(pre_select);

        }
    }

    public String getSelectTitle() {
        if (pre_select != 0) {
            return list.get(pre_select).getTitle();
        } else {
            return "";
        }
    }

    public String getSelectCode() {
        if (pre_select != 0) {
            return list.get(pre_select).getCode();
        } else {
            return "";
        }
    }

    public void setFilter(JSONObject json) {
        list = analyze_newBrandInfo.getTypeRel(json);
        notifyDataSetChanged();
    }

}