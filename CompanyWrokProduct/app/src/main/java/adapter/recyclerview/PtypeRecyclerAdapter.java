package adapter.recyclerview;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.tw.wrokproduct.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class PtypeRecyclerAdapter extends RecyclerView.Adapter<PtypeRecyclerAdapter.RecycleHolder> {
    private Context ctx;
    int layout_width, layout_heigh;
    JSONObject json;
    View view;
    ArrayList<Map<String, String>> list;
    DisplayMetrics dm;
    Bitmap[][] bitmaps;
    PtypeRecyclerAdapter.RecycleHolder recycleHolder;
    LinearLayout.LayoutParams layoutParams;
    private PtypeRecyclerAdapter.ClickListener clickListener;
    int lastposition;

    public PtypeRecyclerAdapter(Context ctx, ArrayList<Map<String, String>> list,Bitmap[][] bitmaps, int layout_width, int layout_heigh) {
        this.ctx = ctx;
        this.layout_width = layout_width;
        this.layout_heigh = layout_heigh;
        this.list = list;
        this.bitmaps=bitmaps;
        dm = ctx.getResources().getDisplayMetrics();

    }

    @Override
    public PtypeRecyclerAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_ptype, parent, false);
        recycleHolder = new PtypeRecyclerAdapter.RecycleHolder(ctx, view, json);
        return recycleHolder;
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, final int position) {
        layoutParams = new LinearLayout.LayoutParams((int)(layout_width+20* dm.density), layout_heigh);
     //   layoutParams.setMargins((int) (10 * dm.density), 0, (int) (10 * dm.density), 0);
        resizeImageView(holder.imageView,(int)(layout_width),(int)(layout_width));
        holder.ptype_title_linear.setLayoutParams(layoutParams);
        if (lastposition == position) {
            holder.imageView.setImageBitmap(bitmaps[1][position]);
        } else {
            holder.imageView.setImageBitmap(bitmaps[0][position]);
        }
        /*
        if (images_a[position] == null) {
            ImageLoader.getInstance().loadImage(list.get(position).get("image"), getWholeOptions(), new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view,
                                              Bitmap loadedImage) {
                    super.onLoadingComplete(imageUri, view, loadedImage);
                    images_a[position] = loadedImage;
                    holder.imageView.setImageBitmap(loadedImage);
                }
            });
        } else {
            if (lastposition == position) {
                holder.imageView.setImageBitmap(images_b[position]);
            } else {
                holder.imageView.setImageBitmap(images_a[position]);
            }
        }

        if (images_b[position] == null)
            ImageLoader.getInstance().loadImage(list.get(position).get("aimg"), getWholeOptions(), new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view,
                                              Bitmap loadedImage) {
                    super.onLoadingComplete(imageUri, view, loadedImage);
                    images_b[position] = loadedImage;
                }
            });
            */
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

    public class RecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        LinearLayout ptype_title_linear;
        TextView tv;
        Context ctx;
        JSONObject json;

        public RecycleHolder(Context ctx, View view, JSONObject json) {
            super(view);
            this.json = json;
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
                imageView.setImageBitmap(bitmaps[1][position]);
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