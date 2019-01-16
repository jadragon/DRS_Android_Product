package com.example.alex.eip_product.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alex.eip_product.MainActivity;
import com.example.alex.eip_product.R;
import com.example.alex.eip_product.fragment.Fragment_calendar;
import com.example.alex.eip_product.fragment.Fragment_selectFailed;
import com.example.alex.eip_product.fragment.Fragment_setting;

public class MainMenuAdapter extends RecyclerView.Adapter<MainMenuAdapter.RecycleHolder> {
    private String[] list;
    private Context ctx;

    public MainMenuAdapter(Context ctx) {
        this.ctx = ctx;
        initList();
    }

    private void initList() {
        list = ctx.getResources().getStringArray(R.array.menu_item);
    }

    @Override
    public MainMenuAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_menu, null);
        return new MainMenuAdapter.RecycleHolder(view);
    }

    @Override
    public void onBindViewHolder(RecycleHolder holder, final int position) {

        switch (position) {
            case 0:
                holder.image.setImageDrawable(ctx.getResources().getDrawable(R.drawable.date));
                break;
            case 1:
                holder.image.setImageDrawable(ctx.getResources().getDrawable(R.drawable.fail_reason));
                break;
            case 2:
                holder.image.setImageDrawable(ctx.getResources().getDrawable(R.drawable.setting));
                break;
        }
        holder.title.setText(list[position]);

    }

    @Override
    public int getItemCount() {
        return list.length;
    }

    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView image;
        private TextView title;

        public RecycleHolder(View view) {
            super(view);
            image = view.findViewWithTag("image");
            title = view.findViewWithTag("title");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            switch (position) {
                case 0:
                    Fragment calendar = ((FragmentActivity) ctx).getSupportFragmentManager().findFragmentByTag("calendar");
                    if (calendar == null) {
                        calendar = new Fragment_calendar();
                    }
                    ((MainActivity) ctx).switchFrament(calendar, "calendar");
                    break;
                case 1:
                    Fragment selectfailed = ((FragmentActivity) ctx).getSupportFragmentManager().findFragmentByTag("selectfailed");
                    if (selectfailed == null) {
                        selectfailed = new Fragment_selectFailed();
                    }
                    ((MainActivity) ctx).switchFrament(selectfailed, "selectfailed");
                    break;
                case 2:
                    Fragment setting = ((FragmentActivity) ctx).getSupportFragmentManager().findFragmentByTag("setting");
                    if (setting == null) {
                        setting = new Fragment_setting();
                    }
                    ((MainActivity) ctx).switchFrament(setting, "setting");
                    break;
            }

        }
    }

}
