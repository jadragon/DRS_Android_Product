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
import com.example.alex.posdemo.fragment.Fragment_home;

import java.util.HashMap;
import java.util.Map;

import Utils.ComponentUtil;

public class SliderMainMenuAdapter extends RecyclerView.Adapter<SliderMainMenuAdapter.RecycleHolder> {
    private Context ctx;
    DisplayMetrics dm;
    String[] list;
    private TypedArray bg, image, arrow;

    public SliderMainMenuAdapter(Context ctx, Map<String, Integer> map) {
        this.ctx = ctx;
        dm = ctx.getResources().getDisplayMetrics();
        if (map != null) {
            bg = ctx.getResources().obtainTypedArray(map.get("background"));
            image = ctx.getResources().obtainTypedArray(map.get("image"));
            arrow = ctx.getResources().obtainTypedArray(map.get("arrow"));
            list = ctx.getResources().getStringArray(map.get("text"));
            MainActivity.checkMainButtonPojo.isSelectSlide = new boolean[list.length];
        }
    }

    @Override
    public SliderMainMenuAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slideitem, parent, false);
        return new SliderMainMenuAdapter.RecycleHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, final int position) {


            if (MainActivity.checkMainButtonPojo.isSelectSlide[position]) {
                holder.background.setBackgroundResource(arrow.getResourceId(position, 0));
                holder.imageView.setImageResource(0);
                holder.textView.setText("");
            } else {
                holder.background.setBackgroundResource(bg.getResourceId(position, 0));
                holder.imageView.setImageResource(image.getResourceId(position, 0));
                holder.textView.setText(list[position]);
            }
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
            if (position > 0) {
                //控制遮罩及箭頭
                    checkMenuButton(position);
                    notifyDataSetChanged();
            }else {
                Fragment_home fragment_home = new Fragment_home();

                ((MainActivity) ctx).switchFrament(fragment_home);
            }
        }

        private void checkMenuButton(int position) {
            Map<String, Integer> map = new HashMap<>();
            if (position % 2 == 0) {
                map.put("background", R.array.slider_main_bg);
                map.put("image", R.array.slider_main_img);
                map.put("arrow", R.array.slider_main_arrow);
                map.put("text", R.array.slider_main_txt);
            } else {
                map.put("background", R.array.slider_sub_bg);
                map.put("image", R.array.slider_sub_img);
                map.put("arrow", R.array.slider_sub_arrow);
                map.put("text", R.array.slider_sub_txt);
            }


            MainActivity mainActivity = ((MainActivity) ctx);
            View subview = mainActivity.findViewById(R.id.home_subslide_layout);
            View dismiss = mainActivity.findViewById(R.id.home_dismiss);
            PopupWindow popWin = mainActivity.getPopWin();
            if (MainActivity.checkMainButtonPojo.alertIsShow || MainActivity.checkMainButtonPojo.accOpIsShow) {
                if (popWin != null)
                    popWin.dismiss();

                componentUtil.showSubMenu(subview, true);


                mainActivity.getSubslide_adapter().setFilter(map);


                MainActivity.checkMainButtonPojo.alertIsShow = false;
                MainActivity.checkMainButtonPojo.accOpIsShow = false;
                MainActivity.checkMainButtonPojo.subIsShow = true;
                MainActivity.checkMainButtonPojo.isSelectSlide[position] = true;

            } else {
                if (MainActivity.checkMainButtonPojo.subIsShow) {
                    if (MainActivity.checkMainButtonPojo.preClick != position) {
                        MainActivity.checkMainButtonPojo.isSelectSlide[MainActivity.checkMainButtonPojo.preClick] = false;
                        MainActivity.checkMainButtonPojo.isSelectSlide[position] = true;
                        mainActivity.getSubslide_adapter().setFilter(map);
                    } else {
                        componentUtil.showSubMenu(subview, false);
                        componentUtil.showDissMiss(dismiss, false);
                        MainActivity.checkMainButtonPojo.subIsShow = false;
                        MainActivity.checkMainButtonPojo.isSelectSlide[position] = false;
                    }

                } else {
                    componentUtil.showDissMiss(dismiss, true);
                    componentUtil.showSubMenu(subview, true);

                    mainActivity.getSubslide_adapter().setFilter(map);
                    MainActivity.checkMainButtonPojo.subIsShow = true;
                    MainActivity.checkMainButtonPojo.isSelectSlide[position] = true;
                }

            }
            MainActivity.checkMainButtonPojo.preClick = position;
        }
    }


    public void setFilter(Map<String, Integer> map) {
        bg = ctx.getResources().obtainTypedArray(map.get("background"));
        image = ctx.getResources().obtainTypedArray(map.get("image"));
        arrow = ctx.getResources().obtainTypedArray(map.get("arrow"));
        list = ctx.getResources().getStringArray(map.get("text"));
        notifyDataSetChanged();

    }
}