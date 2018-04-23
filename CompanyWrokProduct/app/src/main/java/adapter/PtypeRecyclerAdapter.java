package adapter;

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

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
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
    Bitmap[] images_a, images_b;
    PtypeRecyclerAdapter.RecycleHolder recycleHolder;
    LinearLayout.LayoutParams layoutParams;
    private PtypeRecyclerAdapter.ClickListener clickListener;
    int lastposition;

    public PtypeRecyclerAdapter(Context ctx, ArrayList<Map<String, String>> list, int layout_width, int layout_heigh) {
        this.ctx = ctx;
        this.layout_width = layout_width;
        this.layout_heigh = layout_heigh;
        this.list = list;
        images_a = new Bitmap[list.size()];
        images_b = new Bitmap[list.size()];
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
        layoutParams = new LinearLayout.LayoutParams(layout_width, layout_heigh);
        holder.ptype_title_linear.setLayoutParams(layoutParams);
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
            if (lastposition==position) {
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
        holder.tv.setText(list.get(position).get("title"));
    }

    private DisplayImageOptions getWholeOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                //.showImageOnLoading(R.drawable.loading) //设置图片在下载期间显示的图片
                //.showImageForEmptyUri(R.drawable.ic_launcher)//设置图片Uri为空或是错误的时候显示的图片
                // .showImageOnFail(R.drawable.error)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
                //.decodingOptions(BitmapFactory.Options decodingOptions)//设置图片的解码配置
                .delayBeforeLoading(0)//int delayInMillis为你设置的下载前的延迟时间
                //设置图片加入缓存前，对bitmap进行设置
                //.preProcessor(BitmapProcessor preProcessor)
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .displayer(new RoundedBitmapDisplayer(20))//不推荐用！！！！是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间，可能会出现闪动
                .build();//构建完成

        return options;
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
                imageView.setImageBitmap(images_b[position]);
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

    public void setFilter(ArrayList<Map<String, String>> list) {
        this.list = list;
        images_a = new Bitmap[list.size()];
        images_b = new Bitmap[list.size()];
        notifyDataSetChanged();

    }

}