package com.example.alex.posdemo.adapter.recylclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.app.Fragment;
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
import com.example.alex.posdemo.fragment.Fragment_album;
import com.example.alex.posdemo.fragment.Fragment_brand;
import com.example.alex.posdemo.fragment.Fragment_count;
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
            int main_position = MainActivity.checkMainButtonPojo.preClick;
            int sub_position = getAdapterPosition();
            switchFragment(main_position, sub_position);
            close();


        }

        private void switchFragment(int main_position, int sub_position) {
            Fragment fragment = null;
            String tag = "";
            switch (main_position) {
                case 1:
                    switch (sub_position) {
                        case 0:
                            fragment = new Fragment_count();
                            tag = "count";
                            break;
                        case 1:
                            break;
                        case 2:
                            fragment = new Fragment_punch();
                            tag = "punch";
                            break;
                    }
                    break;
                case 2:
                    switch (sub_position) {
                        case 0:
                            fragment = new Fragment_album();
                            tag = "album";
                            break;
                        case 1:
                            fragment = new Fragment_brand();
                            tag = "brand";
                            break;
                    }
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
                    break;
                case 8:
                    break;
            }
            if (fragment != null)
                ((MainActivity) ctx).switchFrament(fragment, tag);
        }

        private void close() {
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