package tw.com.lccnet.app.designateddriving;

import android.app.Application;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class GlobalVariable extends Application {

    private String token;

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader();
    }

    private void initImageLoader() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                //.showImageOnLoading(R.drawable.loading) //設置圖片在下載期間顯示的圖片
                //.showImageForEmptyUri(R.drawable.ic_launcher)//設置圖片Uri為空或是錯誤的時候顯示的圖片
                // .showImageOnFail(R.drawable.error) //設置圖片加載/解碼過程中錯誤時候顯示的圖片
                .cacheInMemory(true)//設置下載的圖片是否緩存在內存中
                .cacheOnDisk(true)//設置下載的圖片是否緩存在SD卡中
                .considerExifParams(true) //是否考慮JPEG圖像EXIF參數（旋轉，翻轉）
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//設置圖片以如何的編碼方式顯示
                .bitmapConfig(Bitmap.Config.RGB_565)//設置圖片的解碼類型
                //.decodingOptions(BitmapFactory.Options decodingOptions)//設置圖片的解碼配置
                .delayBeforeLoading(0)//int delayInMillis為你設置的下載前的延遲時間
                //設置圖片加入緩存前，對bitmap進行設置
                //.preProcessor(BitmapProcessor preProcessor)
                .resetViewBeforeLoading(true)//設置圖片在下載前是否重置，復位
                // .displayer(new RoundedBitmapDisplayer(20))//不推薦用！ ！ ！ ！是否設置為圓角，弧度為多少
                .displayer(new FadeInBitmapDisplayer(100))//是否圖片加載好後漸入的動畫時間，可能會出現閃動
                .build();//構建完成
        //imageloader
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(getApplicationContext())
                .defaultDisplayImageOptions(options)
                .memoryCache(new UsingFreqLimitedMemoryCache(20)).threadPoolSize(3)
                .memoryCacheExtraOptions(480, 800) //保存每個緩存圖片的最大寬高
                .threadPriority(Thread.NORM_PRIORITY - 1) //線池中的緩存數
                .denyCacheImageMultipleSizesInMemory() //禁止緩存多張圖片
                // .memoryCache(new FIFOLimitedMemoryCache(2 * 1024 * 1024))//緩存策略
                // .memoryCacheSize(50 * 1024 * 1024) //設置內存緩存的大小
                // .diskCacheFileNameGenerator(new Md5FileNameGenerator()) //緩存文件名的保存方式
                // .diskCacheSize(200 * 1024 * 1024) //緩存大小
                // .tasksProcessingOrder(QueueProcessingType.LIFO) //工作序列
                .diskCacheFileCount(200) //緩存的文件數量
                .build();
        ImageLoader.getInstance().init(config);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private double mLatitude, mLongitude;

    public double getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }
}
