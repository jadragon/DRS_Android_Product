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
import library.SubSlideMenuAnimation;

public class SliderMenuAdapter extends RecyclerView.Adapter<SliderMenuAdapter.RecycleHolder> {
    private Context ctx;
    int type;
    DisplayMetrics dm;
    String[] list;
    private TypedArray bg, image;

    public SliderMenuAdapter(Context ctx, int bg_array, int image_array, String[] list, int type) {
        this.ctx = ctx;
        this.type = type;
        this.list = list;
        bg = ctx.getResources().obtainTypedArray(bg_array);
        image = ctx.getResources().obtainTypedArray(image_array);
        dm = ctx.getResources().getDisplayMetrics();

    }

    @Override
    public SliderMenuAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slideitem, parent, false);
        return new SliderMenuAdapter.RecycleHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, final int position) {
        holder.background.setBackgroundResource(bg.getResourceId(position, 0));
        holder.imageView.setImageResource(image.getResourceId(position, 0));
        holder.textView.setText(list[position]);
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

            checkMenuButton();

        }

        private void checkMenuButton() {
            MainActivity mainActivity = ((MainActivity) ctx);
            View subview = mainActivity.findViewById(R.id.home_subslide_layout);
            View dismiss = mainActivity.findViewById(R.id.home_dismiss);
            PopupWindow popWin = mainActivity.getPopWin();
            boolean alertIsShow = mainActivity.isAlertIsShow();
            boolean accOpIsShow = mainActivity.isAccOpIsShow();
            boolean subIsShow = mainActivity.isSubIsShow();
            if (alertIsShow || accOpIsShow || subIsShow) {
                if (popWin != null)
                    popWin.dismiss();
                if (subIsShow) {
                    componentUtil.hideDissMiss(dismiss);
                    SubSlideMenuAnimation a = new SubSlideMenuAnimation(subview, 200, SubSlideMenuAnimation.COLLAPSE);
                    subview.startAnimation(a);
                    mainActivity.setSubIsShow(false);
                } else {
                    SubSlideMenuAnimation a = new SubSlideMenuAnimation(subview, 200, SubSlideMenuAnimation.EXPAND);
                    subview.startAnimation(a);
                    ((MainActivity) ctx).setAlertIsShow(false);
                    ((MainActivity) ctx).setAccOpIsShow(false);
                    ((MainActivity) ctx).setSubIsShow(true);
                }
            } else {
                componentUtil.showDissMiss(dismiss);
                SubSlideMenuAnimation a = new SubSlideMenuAnimation(subview, 200, SubSlideMenuAnimation.EXPAND);
                subview.startAnimation(a);
                ((MainActivity) ctx).setSubIsShow(true);
            }
        }
    }


    public void setFilter(String[] list) {
        this.list = list;
        notifyDataSetChanged();

    }
}