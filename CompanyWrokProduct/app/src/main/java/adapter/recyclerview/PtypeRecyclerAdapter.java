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

import java.util.ArrayList;
import java.util.Map;

public class PtypeRecyclerAdapter extends RecyclerView.Adapter<PtypeRecyclerAdapter.RecycleHolder> {
    private Context ctx;
    private int layout_width, layout_heigh;
    private View view;
    private ArrayList<Map<String, String>> list;
    private DisplayMetrics dm;
    private PtypeRecyclerAdapter.RecycleHolder recycleHolder;
    private LinearLayout.LayoutParams layoutParams;
    private PtypeRecyclerAdapter.ClickListener clickListener;
    private int lastposition;

    public PtypeRecyclerAdapter(Context ctx, ArrayList<Map<String, String>> list, int layout_width, int layout_heigh) {
        this.ctx = ctx;
        this.layout_width = layout_width;
        this.layout_heigh = layout_heigh;
        this.list = list;
        dm = ctx.getResources().getDisplayMetrics();

    }

    @Override
    public PtypeRecyclerAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_ptype, parent, false);
        recycleHolder = new PtypeRecyclerAdapter.RecycleHolder(ctx, view);
        return recycleHolder;
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, final int position) {
        layoutParams = new LinearLayout.LayoutParams((int) (layout_width + 20 * dm.density), layout_heigh);
        //   layoutParams.setMargins((int) (10 * dm.density), 0, (int) (10 * dm.density), 0);
        resizeImageView(holder.imageView, (int) (layout_width), (int) (layout_width));
        holder.ptype_title_linear.setLayoutParams(layoutParams);
        if (lastposition == position) {
            ImageLoader.getInstance().displayImage(list.get(position).get("aimg"), holder.imageView);
        } else {
            ImageLoader.getInstance().displayImage(list.get(position).get("image"), holder.imageView);
        }
        holder.tv.setText(list.get(position).get("title"));
    }

    private void resizeImageView(View view, int width, int heigh) {//重構圖片大小
        ViewGroup.LayoutParams params = view.getLayoutParams();  //需import android.view.ViewGroup.LayoutParams;
        params.width = width;
        params.height = heigh;
        view.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        LinearLayout ptype_title_linear;
        TextView tv;
        Context ctx;

        public RecycleHolder(Context ctx, View view) {
            super(view);
            this.ctx = ctx;
            ptype_title_linear = view.findViewById(R.id.ptype_title_linear);
            imageView = view.findViewById(R.id.ptype_title_image);
            tv = view.findViewById(R.id.ptype_title_tv);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (lastposition != position) {
                notifyItemChanged(lastposition);
                ImageLoader.getInstance().displayImage(list.get(position).get("aimg"), imageView);
                lastposition = position;
            }
            if (clickListener != null) {
                clickListener.ItemClicked(view, position, list);
            }
        }
    }

    public void setClickListener(PtypeRecyclerAdapter.ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void ItemClicked(View view, int postion, ArrayList<Map<String, String>> list);
    }

}