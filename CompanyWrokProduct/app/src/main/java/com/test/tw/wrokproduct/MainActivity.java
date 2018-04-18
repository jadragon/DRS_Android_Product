package com.test.tw.wrokproduct;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import Fragment.Fragment_home;
import Fragment.Fragment_shop;
import library.BottomNavigationViewHelper;

public class MainActivity extends AppCompatActivity {
    private Fragment_home fragment_home;
    private Fragment_shop fragment_shop;
    private Fragment[] fragments;
    private int lastShowFragment = 0;
    BottomNavigationView  navigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        initFragments();
        initBtnNav();
        initImageLoader();
        startActivity(new Intent(MainActivity.this, LoadingPage.class));
    }
    private void initImageLoader() {
        if (ImageLoader.getInstance().isInited()) {
            ImageLoader.getInstance().clearMemoryCache();
            ImageLoader.getInstance().destroy();
        }
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(getApplicationContext())
                .memoryCache(new WeakMemoryCache()).threadPoolSize(5)
                .memoryCacheExtraOptions(480, 800) //保存每個緩存圖片的最大寬高
                .threadPriority(Thread.NORM_PRIORITY) //線池中的緩存數
                .denyCacheImageMultipleSizesInMemory() //禁止緩存多張圖片
                //               .memoryCache(new FIFOLimitedMemoryCache(2 * 1024 * 1024))//缓存策略
//                .memoryCacheSize(50 * 1024 * 1024) //設置內存緩存的大小
                //              .diskCacheFileNameGenerator(new Md5FileNameGenerator()) //缓存文件名的保存方式
//                .diskCacheSize(200 * 1024 * 1024) //緩存大小
                //             .tasksProcessingOrder(QueueProcessingType.LIFO) //工作序列
                .diskCacheFileCount(200) //緩存的文件數量
                .build();
        if (!ImageLoader.getInstance().isInited()) {//偵測如果imagloader已經init，就不再init
            ImageLoader.getInstance().init(config);
        }
        //  ImageLoader.getInstance().displayImage(url, imageView, ImageUsing);

//    UsingFreqLimitedMemoryCache（如果緩存的圖片總量超過限定值，先刪除使用頻率最小的bitmap）
//    LRULimitedMemoryCache（這個也是使用的lru算法，和LruMemoryCache不同的是，他緩存的是bitmap的弱引用）
//    FIFOLimitedMemoryCache（先進先出的緩存策略，當超過設定值，先刪除最先加入緩存的bitmap）
//    LargestLimitedMemoryCache(當超過緩存限定值，先刪除最大的bitmap對象)
//    LimitedAgeMemoryCache（當bitmap加入緩存中的時間超過我們設定的值，將其刪除）

//    Universal-Image-Loader的硬盤緩存策略
//    詳細的硬盤緩存策略可以移步：http://blog.csdn.net/xiaanming/article/details/27525741，下方是總結的結果：

//    FileCountLimitedDiscCache（可以設定緩存圖片的個數，當超過設定值，刪除掉最先加入到硬盤的文件）
//    LimitedAgeDiscCache（設定文件存活的最長時間，當超過這個值，就刪除該文件）
//    TotalSizeLimitedDiscCache（設定緩存bitmap的最大值，當超過這個值，刪除最先加入到硬盤的文件）
//    UnlimitedDiscCache（這個緩存類沒有任何的限制）

//    DisplayImageOptions ImageUsing = new DisplayImageOptions.Builder()
//            .showImageOnLoading(R.drawable.loading)//圖片還沒下載好時跑的臨時圖片
//            .showImageForEmptyUri(R.drawable.loading)
//            .showImageOnFail(R.drawable.loading).cacheInMemory(true)//緩存
//            .cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565)
//            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//            .build();

//.showImageOnLoading(R.drawable.loading)//圖片還沒下載好時跑的臨時圖片
//.showImageOnFail(R.drawable.loading)//圖片讀取失敗時跑的臨時圖片
    }

    protected void initBtnNav() {//BottomLayout
       navigation = findViewById(R.id.tab_layout);
        new BottomNavigationViewHelper().disableShiftMode(navigation);//取消動畫

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {//監聽事件

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        switchFrament(lastShowFragment,0);
                        lastShowFragment=0;
                        return true;
                    case R.id.navigation_shop:
                        switchFrament(lastShowFragment,1);
                        lastShowFragment=1;
                        return true;
                    case R.id.navigation_my_favor:

                        return true;
                    case R.id.navigation_member:

                        return true;

                }
                return false;
            }

        });
        //  navigation.setSelectedItemId(R.id.navigation_home);

    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.e("onStop","onStop");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("onStart","onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("onPause","onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("onRestart","onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("onResume","onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("onDestroy","onDestroy");
    }
    /**
     * 切换Fragment
     *
     * @param lastIndex 上个显示Fragment的索引
     * @param index     需要显示的Fragment的索引
     */
    public void switchFrament(int lastIndex, int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments[lastIndex]);
        if (!fragments[index].isAdded()) {
            transaction.add(R.id.content_layout, fragments[index]);
        }
        transaction.show(fragments[index]).commitAllowingStateLoss();
    }

    private void initFragments() {
        fragment_home = new Fragment_home();
        fragment_shop = new Fragment_shop();
        fragments = new Fragment[]{fragment_home, fragment_shop};
        lastShowFragment = 0;
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content_layout, fragment_home)
                .show(fragment_home)
                .commit();
    }

}