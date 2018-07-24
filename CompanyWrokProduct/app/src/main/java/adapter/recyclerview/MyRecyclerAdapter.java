package adapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.test.tw.wrokproduct.R;

import org.json.JSONObject;

import java.util.ArrayList;

import pojo.ProductInfoPojo;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.RecycleHolder> {
    private Context ctx;
    int type;
    JSONObject json;
    View view;
    ArrayList<ProductInfoPojo> list;
    DisplayMetrics dm;

    private MyRecyclerAdapter.ClickListener clickListener;

    public MyRecyclerAdapter(Context ctx, ArrayList<ProductInfoPojo> list, int type) {
        this.ctx = ctx;
        this.type = type;
        this.list = list;
        dm = ctx.getResources().getDisplayMetrics();

    }

    @Override
    public MyRecyclerAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_home, parent, false);
        return new MyRecyclerAdapter.RecycleHolder(ctx, view, json);
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, final int position) {
        holder.imageView.setImageBitmap(null);
        ImageLoader.getInstance().displayImage(list.get(position).getImage(), holder.imageView);
        holder.tv1.setText(list.get(position).getTitle());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView tv1;
        Context ctx;
        JSONObject json;

        public RecycleHolder(Context ctx, View view, JSONObject json) {
            super(view);
            this.json = json;
            this.ctx = ctx;
            imageView = view.findViewById(R.id.imView);
            tv1 = view.findViewById(R.id.tV1);
            LinearLayout.LayoutParams layoutParams;
            int real_heigh;
            if (type == 0) {
                real_heigh = (int) ((dm.widthPixels - 40 * dm.density) / (float) 3.5);
                layoutParams = new LinearLayout.LayoutParams(real_heigh, real_heigh * 3 / 4);
                layoutParams.setMargins(0, 0, (int) (10 * dm.density), 0);
                view.setLayoutParams(layoutParams);
            } else if (type == 1) {
                real_heigh = (int) ((dm.widthPixels - 10 * dm.density) / (float) 3.5);
                layoutParams = new LinearLayout.LayoutParams(real_heigh, dm.widthPixels / 4);
                layoutParams.setMargins(0, 0, 0, 0);
                view.setLayoutParams(layoutParams);
                tv1.setTextColor(ctx.getResources().getColor(R.color.black));
                tv1.setBackgroundColor(ctx.getResources().getColor(R.color.colorInvisible));
            } else if (type == 2) {
                real_heigh = (int) ((dm.widthPixels - 25 * dm.density) / (float) 4);
                layoutParams = new LinearLayout.LayoutParams(real_heigh, real_heigh / 4 * 3);
                layoutParams.setMargins(0, 0, (int) (5 * dm.density), 0);
                view.setLayoutParams(layoutParams);
                tv1.setVisibility(View.INVISIBLE);
            }

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
        void ItemClicked(View view, int postion, ArrayList<ProductInfoPojo> list);
    }

    public void setFilter(ArrayList<ProductInfoPojo> list) {
        this.list = list;
        notifyDataSetChanged();

    }

}