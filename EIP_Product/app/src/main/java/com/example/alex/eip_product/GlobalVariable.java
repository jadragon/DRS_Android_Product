package com.example.alex.eip_product;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import Utils.PreferenceUtil;
import liabiry.Http.HttpUtils;

public class GlobalVariable extends Application {
    private String username, pw, permission;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    private Date current_date;

    public String getCurrent_date() {//取得現在日期名稱
        String date = new SimpleDateFormat("yyyy/MM/dd").format(current_date);
        /*
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        */
        return date;
        //+ "(" + getDayOfWeek(dayOfWeek) + ")";
    }

    public Object getCurrent_date(int type) {
        switch (type) {
            case 0:
                return current_date;
            case 1:
                return current_date.getTime();
            default:
                return current_date;
        }

    }

    public void setCurrent_date(Date current_date) {
        this.current_date = current_date;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initLanguage();
        initImageLoader();
    }

    private void initLanguage() {
        PreferenceUtil.init(this);
        // 保存設置語言的類型
        int language = PreferenceUtil.getInt("language", 0);
        // 設置應用語言類型
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        if (language == 0) {
            // 中文繁體
            config.locale = Locale.TRADITIONAL_CHINESE;
        } else if (language == 1) {
            // 中文簡體
            config.locale = Locale.SIMPLIFIED_CHINESE;
        }
        resources.updateConfiguration(config, dm);
    }

    private void initImageLoader() {
        HttpUtils.setContext(getApplicationContext());
        /*
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
                //  .displayer(new RoundedBitmapDisplayer(20))//不推荐用！！！！是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间，可能会出现闪动
                .build();//构建完成
        //imageloader
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(getApplicationContext())
                .defaultDisplayImageOptions(options)
                .memoryCache(new UsingFreqLimitedMemoryCache(20)).threadPoolSize(3)
                .memoryCacheExtraOptions(480, 800) //保存每個緩存圖片的最大寬高
                .threadPriority(Thread.NORM_PRIORITY - 1) //線池中的緩存數
                .denyCacheImageMultipleSizesInMemory() //禁止緩存多張圖片
                //               .memoryCache(new FIFOLimitedMemoryCache(2 * 1024 * 1024))//缓存策略
//                .memoryCacheSize(50 * 1024 * 1024) //設置內存緩存的大小
                //              .diskCacheFileNameGenerator(new Md5FileNameGenerator()) //缓存文件名的保存方式
//                .diskCacheSize(200 * 1024 * 1024) //緩存大小
                //             .tasksProcessingOrder(QueueProcessingType.LIFO) //工作序列
                .diskCacheFileCount(200) //緩存的文件數量
                .imageDownloader(new AuthImageDownloader(getApplicationContext(), 2000, 2000))
                .build();
        ImageLoader.getInstance().init(config);

*/
    }

    public String getDayOfWeek(int dayofweek) {
        String day = "";
        switch (dayofweek) {
            case Calendar.SUNDAY:
                day = "日";
                break;
            case Calendar.MONDAY:
                day = "一";
                break;
            case Calendar.TUESDAY:
                day = "二";
                break;
            case Calendar.WEDNESDAY:
                day = "三";
                break;
            case Calendar.THURSDAY:
                day = "四";
                break;
            case Calendar.FRIDAY:
                day = "五";
                break;
            case Calendar.SATURDAY:
                day = "六";
                break;
        }
        return day;
    }
}
