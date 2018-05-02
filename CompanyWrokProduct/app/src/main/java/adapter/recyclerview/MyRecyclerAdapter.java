package adapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.test.tw.wrokproduct.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.RecycleHolder> {
    private Context ctx;
    int type;
    int layout_width, layout_heigh;
    JSONObject json;
    View view;
    ArrayList<Map<String, String>> list;
    DisplayMetrics dm;
    MyRecyclerAdapter.RecycleHolder recycleHolder;
    LinearLayout.LayoutParams layoutParams;
    private MyRecyclerAdapter.ClickListener clickListener;

    public MyRecyclerAdapter(Context ctx, ArrayList<Map<String, String>> list, int layout_width, int layout_heigh, int type) {
        this.ctx = ctx;
        this.layout_width = layout_width;
        this.layout_heigh = layout_heigh;
        this.type = type;
        this.list = list;
        dm = ctx.getResources().getDisplayMetrics();

    }

    @Override
    public MyRecyclerAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_home, parent, false);
        recycleHolder = new MyRecyclerAdapter.RecycleHolder(ctx, view, json);
        return recycleHolder;
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, final int position) {
        layoutParams = new LinearLayout.LayoutParams(layout_width, layout_heigh);
        if (type == 0) {
            layoutParams.setMargins(0, 0, (int) (10 * dm.density), 0);
        } else if (type == 1) {
            layoutParams.setMargins(0, 0, 0, 0);
            holder.tv1.setTextColor(ctx.getResources().getColor(R.color.colorBlack));
            holder.tv1.setBackgroundColor(ctx.getResources().getColor(R.color.colorInvisible));
        } else if (type == 2) {
            layoutParams.setMargins(0, 0, (int) (5 * dm.density), 0);
            holder.tv1.setVisibility(View.INVISIBLE);
        }
        holder.frameLayout.setLayoutParams(layoutParams);
        holder.imageView.setImageBitmap(null);
        ImageLoader.getInstance().displayImage( list.get(position).get("image"),holder.imageView);
        holder.tv1.setText(list.get(position).get("title"));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        FrameLayout frameLayout;
        TextView tv1;
        Context ctx;
        JSONObject json;

        public RecycleHolder(Context ctx, View view, JSONObject json) {
            super(view);
            this.json = json;
            this.ctx = ctx;
            frameLayout = view.findViewById(R.id.frame_layout);
            imageView = view.findViewById(R.id.imView);
            tv1 = view.findViewById(R.id.tV1);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (clickListener != null) {
                clickListener.ItemClicked(view, position, list);
            }
        }
    }

    public void setClickListener(MyRecyclerAdapter.ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void ItemClicked(View view, int postion, ArrayList<Map<String, String>> list);
    }

    public void setFilter(ArrayList<Map<String, String>> list) {
        this.list = list;
        notifyDataSetChanged();

    }

}