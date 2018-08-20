package com.example.alex.posdemo.adapter.recylclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alex.posdemo.MainActivity;
import com.example.alex.posdemo.R;
import com.example.alex.posdemo.fragment.Fragment_punch;

import java.util.Map;

import Utils.ComponentUtil;

public class SliderSubMenuAdapter extends RecyclerView.Adapter<SliderSubMenuAdapter.RecycleHolder> {
    private Context ctx;
    DisplayMetrics dm;
    String[] list;
    private TypedArray bg, image;

    public SliderSubMenuAdapter(Context ctx, Map<String, Integer> map) {
        this.ctx = ctx;
        dm = ctx.getResources().getDisplayMetrics();
        if (map != null) {
            bg = ctx.getResources().obtainTypedArray(map.get("background"));
            image = ctx.getResources().obtainTypedArray(map.get("image"));
            list = ctx.getResources().getStringArray(map.get("text"));
            MainActivity.checkMainButtonPojo.isSelectSlide = new boolean[list.length];
        }
    }

    @Override
    public SliderSubMenuAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slideitem, parent, false);
        return new SliderSubMenuAdapter.RecycleHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, final int position) {


            holder.background.setBackgroundResource(bg.getResourceId(position, 0));
            holder.imageView.setImageResource(image.getResourceId(position, 0));
            holder.textView.setText(list[position]);


    }


    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        }
        return list.length;
    }

    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        FrameLayout background;
        ImageView imageView;
        TextView textView;
        ComponentUtil componentUtil;

        public RecycleHolder(View view) {
            super(view);
            componentUtil = new ComponentUtil();
            background = view.findViewById(R.id.slide_item_layout);
            imageView = view.findViewById(R.id.slide_item_image);
            textView = view.findViewById(R.id.slide_item_txt);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            Fragment_punch fragment_punch = new Fragment_punch();

            ((MainActivity) ctx).switchFrament(fragment_punch);


            MainActivity.checkMainButtonPojo.subIsShow = false;
            MainActivity.checkMainButtonPojo.isSelectSlide[MainActivity.checkMainButtonPojo.preClick] = false;
            componentUtil.showSubMenu(((MainActivity) ctx).findViewById(R.id.home_subslide_layout), false);
            componentUtil.showDissMiss(((MainActivity) ctx).findViewById(R.id.home_dismiss), false);

        }

    }


    public void setFilter(Map<String, Integer> map) {
        bg = ctx.getResources().obtainTypedArray(map.get("background"));
        image = ctx.getResources().obtainTypedArray(map.get("image"));
        list = ctx.getResources().getStringArray(map.get("text"));
        notifyDataSetChanged();

    }
}