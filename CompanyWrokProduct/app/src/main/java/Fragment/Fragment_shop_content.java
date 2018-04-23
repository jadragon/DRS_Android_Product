package Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.test.tw.wrokproduct.R;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import adapter.ShopRecyclerViewAdapter;
import library.ResolveJsonData;

public class Fragment_shop_content extends Fragment {
    RecyclerView recyclerView;
    ShopRecyclerViewAdapter myRecyclerAdapter1;
    ViewPager viewPager;
    JSONObject json1, json2;
    View v;
    Handler handler;
    DisplayMetrics dm;
    com.nostra13.universalimageloader.core.ImageLoader imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();

    public Fragment_shop_content() {
    }

    @SuppressLint("ValidFragment")
    public Fragment_shop_content(JSONObject json1, JSONObject json2) {
        this.json1 = json1;
        this.json2 = json2;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_shop_content_layout, container, false);
        initViewPagerAndRecyclerView();
        return v;
    }

    private void initViewPagerAndRecyclerView() {
        recyclerView = v.findViewById(R.id.shop_conttent_re);
        viewPager = v.findViewById(R.id.adView);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //recycleView
                                dm = getResources().getDisplayMetrics();
                                int real_heigh = (int) ((dm.widthPixels - 10 * dm.density) / (float) 2);
                                myRecyclerAdapter1 = new ShopRecyclerViewAdapter(getActivity().getApplicationContext(), json1, json2, real_heigh, (int) (real_heigh + (110 * dm.density)));
                                GridLayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
                                recyclerView.setLayoutManager(layoutManager);
                                /*
                                recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                                    @Override
                                    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                                        outRect.left = 50;
                                        outRect.right = 50;
                                        outRect.bottom = 50;

                                        // Add top margin only for the first item to avoid double space between items
                                        if (parent.getChildPosition(view) == 0)
                                            outRect.top = 50;
                                    }
                                });
                                */
                                recyclerView.setAdapter(myRecyclerAdapter1);
                                setHeaderView(myRecyclerAdapter1);
                                setFooterView(myRecyclerAdapter1);
                            }
                        });
                    }
                });
                Looper.loop();
            }
        }).start();


    }

    private void setHeaderView(ShopRecyclerViewAdapter adapter) {
        Banner header = (Banner) LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.testbanner, recyclerView, false);
        // Banner header=new Banner(getActivity());
        header.setLayoutParams(new LinearLayout.LayoutParams(dm.widthPixels, dm.widthPixels * 19 / 54));
        header.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        header.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, final Object path, final ImageView imageView) {
                imageLoader.displayImage(path.toString(), imageView);
                com.nostra13.universalimageloader.core.ImageLoader.getInstance().loadImage(path.toString(), getWholeOptions(), new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        super.onLoadingCancelled(imageUri, view);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        imageView.setImageBitmap(loadedImage);
                    }
                });
            }
        });
        List<String> images = new ArrayList<>();
        for (Map<String, String> map : ResolveJsonData.getJSONData(json1))
            images.add(map.get("image"));
        header.setImages(images);
        //banner设置方法全部调用完毕时最后调用
        header.start();

        adapter.setmHeaderView(header);
    }

    private void setFooterView(ShopRecyclerViewAdapter adapter) {
        View footer = new LinearLayout(getActivity().getApplicationContext());
        footer.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50));
        adapter.setmFooterViewView(footer);
    }


    public void setFilter(JSONObject json) {
        if (myRecyclerAdapter1 != null)
            myRecyclerAdapter1.setFilter(json);
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
}
