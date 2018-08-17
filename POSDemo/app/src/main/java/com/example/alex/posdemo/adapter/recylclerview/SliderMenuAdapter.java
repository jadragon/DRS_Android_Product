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
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.alex.posdemo.MainActivity;
import com.example.alex.posdemo.R;

import Utils.ComponentUtil;

public class SliderMenuAdapter extends RecyclerView.Adapter<SliderMenuAdapter.RecycleHolder> {
    public static int MAIN_SLIDER = 0;
    public static int SUB_SLIDER = 1;
    private Context ctx;
    int type;
    DisplayMetrics dm;
    String[] list;
    private TypedArray bg, image, arrow;

    public SliderMenuAdapter(Context ctx, int bg_array, int image_array, int arrow_array, String[] list, int type) {
        this.ctx = ctx;
        this.type = type;
        this.list = list;
        bg = ctx.getResources().obtainTypedArray(bg_array);
        image = ctx.getResources().obtainTypedArray(image_array);
        arrow = ctx.getResources().obtainTypedArray(arrow_array);
        MainActivity.checkMainButtonPojo.isSelectSlide = new boolean[list.length];
        dm = ctx.getResources().getDisplayMetrics();

    }

    @Override
    public SliderMenuAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slideitem, parent, false);
        return new SliderMenuAdapter.RecycleHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, final int position) {

        if (type == MAIN_SLIDER) {
            if (MainActivity.checkMainButtonPojo.isSelectSlide[position]) {
                holder.background.setBackgroundResource(arrow.getResourceId(position, 0));
                holder.imageView.setImageResource(0);
                holder.textView.setText("");
            } else {
                holder.background.setBackgroundResource(bg.getResourceId(position, 0));
                holder.imageView.setImageResource(image.getResourceId(position, 0));
                holder.textView.setText(list[position]);
            }
        } else {
            holder.background.setBackgroundResource(bg.getResourceId(position, 0));
            holder.imageView.setImageResource(image.getResourceId(position, 0));
            holder.textView.setText(list[position]);
        }

    }


    @Override
    public int getItemCount() {
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
            if (position > 0) {
                checkMenuButton(position);
                if (type == MAIN_SLIDER) {
                    notifyDataSetChanged();

                }
            }
        }

        private void checkMenuButton(int position) {
            MainActivity mainActivity = ((MainActivity) ctx);
            View subview = mainActivity.findViewById(R.id.home_subslide_layout);
            View dismiss = mainActivity.findViewById(R.id.home_dismiss);
            PopupWindow popWin = mainActivity.getPopWin();
            if (MainActivity.checkMainButtonPojo.alertIsShow || MainActivity.checkMainButtonPojo.accOpIsShow) {
                if (popWin != null)
                    popWin.dismiss();

                componentUtil.showSubMenu(subview, true);
                MainActivity.checkMainButtonPojo.alertIsShow = false;
                MainActivity.checkMainButtonPojo.accOpIsShow = false;
                MainActivity.checkMainButtonPojo.subIsShow = true;
                MainActivity.checkMainButtonPojo.isSelectSlide[position] = true;

            } else {
                if (MainActivity.checkMainButtonPojo.subIsShow) {
                    if (MainActivity.checkMainButtonPojo.preClick != position) {
                        MainActivity.checkMainButtonPojo.isSelectSlide[MainActivity.checkMainButtonPojo.preClick] = false;
                        MainActivity.checkMainButtonPojo.isSelectSlide[position] = true;
                    } else {
                        componentUtil.showSubMenu(subview, false);
                        componentUtil.showDissMiss(dismiss, false);
                        MainActivity.checkMainButtonPojo.subIsShow = false;
                        MainActivity.checkMainButtonPojo.isSelectSlide[position] = false;
                    }

                } else {
                    componentUtil.showDissMiss(dismiss, true);
                    componentUtil.showSubMenu(subview, true);
                    MainActivity.checkMainButtonPojo.subIsShow = true;
                    MainActivity.checkMainButtonPojo.isSelectSlide[position] = true;
                }

            }
            MainActivity.checkMainButtonPojo.preClick = position;
        }
    }


    public void setFilter(String[] list) {
        this.list = list;
        notifyDataSetChanged();

    }
}