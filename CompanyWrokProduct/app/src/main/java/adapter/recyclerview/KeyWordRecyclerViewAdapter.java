package adapter.recyclerview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

public class KeyWordRecyclerViewAdapter extends RecyclerView.Adapter<KeyWordRecyclerViewAdapter.RecycleHolder> {


    private Context ctx;
    private JSONObject json;
    private ArrayList<String> list;
    private DisplayMetrics dm;
    private KeyWordRecyclerViewAdapter.RecycleHolder recycleHolder;

    public KeyWordRecyclerViewAdapter(Context ctx, JSONObject json) {
        this.ctx = ctx;
        dm = ctx.getResources().getDisplayMetrics();
        if (json != null) {
        } else {
            list = new ArrayList<>();
            list.add("1111");
            list.add("22222222222222");
            list.add("5555");
            list.add("44444444");
            list.add("3333333");
            list.add("22");
            list.add("111");
            list.add("5555555555555555555555");
        }
    }

    @Override
    public KeyWordRecyclerViewAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView textView = new TextView(ctx);
        textView.setGravity(Gravity.CENTER);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, (int) (40 * dm.density));
        layoutParams.setMargins((int) (10 * dm.density), (int) (10 * dm.density), (int) (10 * dm.density), (int) (10 * dm.density));
        textView.setLayoutParams(new LinearLayout.LayoutParams(layoutParams));
        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadius(10 * dm.density);
        shape.setStroke((int) (2 * dm.density), Color.BLACK);
        textView.setBackground(shape);
        recycleHolder = new KeyWordRecyclerViewAdapter.RecycleHolder(ctx, textView, json);
        return recycleHolder;
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, final int position) {
        holder.text.setText("    " + list.get(position) + "    ");
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //  TextView color, size;
        TextView text;
        Context ctx;
        JSONObject json;

        public RecycleHolder(Context ctx, View view, JSONObject json) {
            super(view);
            this.json = json;
            this.ctx = ctx;

            text = (TextView) view;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            notifyDataSetChanged();

        }
    }

    public void setFilter(JSONObject json) {
        this.json = json;
        notifyDataSetChanged();
    }

}
