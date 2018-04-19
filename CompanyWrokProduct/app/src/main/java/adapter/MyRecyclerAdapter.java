package adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import library.ResolveJsonData;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.RecycleHolder> {
    private Context ctx;
    int type;
    int layout_width, layout_heigh;
    JSONObject json;
    View view;
    ArrayList<Map<String, String>> list;
    DisplayMetrics dm;
    Bitmap[] images;
    MyRecyclerAdapter.RecycleHolder recycleHolder;
    LinearLayout.LayoutParams layoutParams;

    public MyRecyclerAdapter(Context ctx, JSONObject json, int layout_width, int layout_heigh, int type) {
        this.json = json;
        this.ctx = ctx;
        this.layout_width = layout_width;
        this.layout_heigh = layout_heigh;
        this.type = type;
        list = ResolveJsonData.getJSONData1(json);
        images = new Bitmap[list.size()];
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
        if (images[position] != null) {
            holder.imageView.setImageBitmap(images[position]);
        } else {

            ImageLoader.getInstance().loadImage(list.get(position).get("image"),getWholeOptions(),new SimpleImageLoadingListener() {

                @Override
                public void onLoadingComplete(String imageUri, View view,
                                              Bitmap loadedImage) {
                    super.onLoadingComplete(imageUri, view, loadedImage);
                    images[position] = loadedImage;
                    holder.imageView.setImageBitmap(loadedImage);

                }

            });

        }

        holder.tv1.setText(list.get(position).get("title"));
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
            Toast.makeText(ctx, "" + list.get(position).get("title"), Toast.LENGTH_SHORT).show();
        }

    }

    public void setFilter(JSONObject json) {
        this.json = json;
        list = ResolveJsonData.getJSONData(json);
        images = new Bitmap[list.size()];
        notifyDataSetChanged();

    }

}