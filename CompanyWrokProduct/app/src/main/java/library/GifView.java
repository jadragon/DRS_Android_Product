package library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.test.tw.wrokproduct.R;

import java.io.InputStream;

/**
 * Created by user on 2017/5/27.
 */

public class GifView extends View {
    DisplayMetrics dm;
    private InputStream inputStream;    //Gif檔載入
    private Movie movie;                //Gif播放
    private int movieWidth,movieHeigh;  //Gif寬和高
    private long movieDuration;         //Gif延遲時間
    private long movieStart;            //Gif開始

    public GifView(Context context) {
        super(context);
        /**
         * Starting from HONEYCOMB have to turn off HW acceleration to draw
         * Movie on Canvas.
         */
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        init(context);
    }

    public GifView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        /**
         * Starting from HONEYCOMB have to turn off HW acceleration to draw
         * Movie on Canvas.
         */
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        dm = context.getResources().getDisplayMetrics();
        init(context);
    }

    public GifView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        /**
         * Starting from HONEYCOMB have to turn off HW acceleration to draw
         * Movie on Canvas.
         */
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        dm = context.getResources().getDisplayMetrics();
        init(context);
    }
    //alt+Insert
    /*
    Context 為上下文,背景,環境
   Activity就是由Context將內容及物件導入
    */
    private void init(Context context){
        setFocusable(true);
        inputStream=context.getResources().openRawResource(R.raw.yloading);
        movie= Movie.decodeStream(inputStream);
        movieWidth=movie.width();
        movieHeigh=movie.height();
        movieDuration=movie.duration();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //頁面Gif的畫面大小(一定會被執行)
        setMeasuredDimension(dm.widthPixels,dm.heightPixels);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        long startTime= SystemClock.uptimeMillis();
        if(movieStart==0){
            movieStart=startTime;
        }
        if(movie!=null){
            if(movieDuration==0){
                movieDuration=5000;
            }
            int realTime=(int) ((startTime-movieStart)%movieDuration);
            movie.setTime(realTime);
            movie.draw(canvas,(dm.widthPixels-movieWidth)/2,(dm.heightPixels-movieHeigh)/2);
            invalidate();
        }
    }
}
