package com.example.alex.eip_product;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class LinePathView extends View {

    private Context mContext;

    /**
     * 筆畫X座標起點
     */
    private float mX;
    /**
     * 筆畫Y座標起點
     */
    private float mY;
    /**
     * 手寫畫筆
     */
    private final Paint mGesturePaint = new Paint();
    /**
     * 路徑
     */
    private final Path mPath = new Path();
    /**
     * 簽名畫筆
     */
    private Canvas cacheCanvas;
    /**
     * 簽名畫布
     */
    private Bitmap cachebBitmap;
    /**
     * 是否已经簽名
     */
    private boolean isTouched = false;
    /**
     * 畫筆寬度 px；
     */
    private int mPaintWidth = 10;
    /**
     * 前景色
     */
    private int mPenColor = Color.BLACK;
    /**
     * 背景色（指最终簽名結果文件的背景顏色，默認為透明色）
     */
    private int mBackColor = Color.TRANSPARENT;

    public LinePathView(Context context) {
        super(context);
        init(context);
    }

    public LinePathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LinePathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        this.mContext = context;
        //设置抗鋸齒
        mGesturePaint.setAntiAlias(true);
        //设置簽名筆畫樣式
        mGesturePaint.setStyle(Paint.Style.STROKE);
        //设置筆畫寬度
        mGesturePaint.setStrokeWidth(mPaintWidth);
        //设置簽名顏色
        mGesturePaint.setColor(mPenColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //創建跟view一样大的bitmap
        cachebBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        cacheCanvas = new Canvas(cachebBitmap);
        cacheCanvas.drawColor(mBackColor);
        isTouched = false;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.setFocusable(true);
                this.setFocusableInTouchMode(true);
                this.requestFocus();
                this.requestFocusFromTouch();
                touchDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                this.setFocusable(true);
                this.setFocusableInTouchMode(true);
                this.requestFocus();
                this.requestFocusFromTouch();
                isTouched = true;
                touchMove(event);
                break;
            case MotionEvent.ACTION_UP:
                this.setFocusable(true);
                this.setFocusableInTouchMode(true);
                this.requestFocus();
                this.requestFocusFromTouch();
                //將路徑畫到bitmap中，即一次筆畫完成才去更新bitmap，而手势軌跡是實時顯示在畫板上的。
                cacheCanvas.drawPath(mPath, mGesturePaint);
                mPath.reset();
                break;
        }
        // 更新繪製
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //畫此次筆畫之前的簽名
        canvas.drawBitmap(cachebBitmap, 0, 0, mGesturePaint);
        // 通过畫布繪製多點形成的圖形
        canvas.drawPath(mPath, mGesturePaint);
    }

    // 手指點下屏幕時调用
    private void touchDown(MotionEvent event) {
        // 重置繪製路線
        mPath.reset();
        float x = event.getX();
        float y = event.getY();
        mX = x;
        mY = y;
        // mPath繪製的繪製起點
        mPath.moveTo(x, y);
    }

    // 手指在屏幕上滑動時调用
    private void touchMove(MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();
        final float previousX = mX;
        final float previousY = mY;
        final float dx = Math.abs(x - previousX);
        final float dy = Math.abs(y - previousY);
        // 两點之間的距離大於等於3時，生成貝賽爾繪製曲線
        if (dx >= 3 || dy >= 3) {
            // 设置貝賽爾曲線的操作點为起點和终點的一半
            float cX = (x + previousX) / 2;
            float cY = (y + previousY) / 2;
            // 二次貝賽爾，實現平滑曲線；previousX, previousY为操作點，cX, cY为终點
            mPath.quadTo(previousX, previousY, cX, cY);
            // 第二次执行時，第一次结束调用的坐標值將作为第二次调用的初始坐標值
            mX = x;
            mY = y;
        }
    }

    /**
     * 清除畫板
     */
    public void clear() {
        if (cacheCanvas != null) {
            isTouched = false;
            //更新畫板信息
//            mGesturePaint.setColor(mPenColor);
//            cacheCanvas.drawColor(mBackColor);
//            mGesturePaint.setColor(mPenColor);
//            invalidate();

            mPath.reset();
            cacheCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            invalidate();
        }
    }

    /**
     * 保存畫板
     *
     * @param path 保存到路徑
     */
    public void save(String path) throws IOException {
        save(path, false, 0);
    }

    /**
     * 保存畫板
     *
     * @param path       保存到路徑
     * @param clearBlank 是否清除邊缘空白區域
     * @param blank      要保留的邊缘空白距離
     */
    public void save(String path, boolean clearBlank, int blank) throws IOException {

        Bitmap bitmap = cachebBitmap;
        //BitmapUtil.createScaledBitmapByHeight(srcBitmap, 300);//  壓缩圖片
        if (clearBlank) {
            bitmap = clearBlank(bitmap, blank);
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);

        byte[] buffer = bos.toByteArray();
        if (buffer != null) {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            OutputStream outputStream = new FileOutputStream(file);
            outputStream.write(buffer);
            outputStream.close();
        }
    }

    /**
     * 獲取畫板的bitmap
     *
     * @return
     */
    public Bitmap getBitMap(int width,int height) {
        /*
        setDrawingCacheEnabled(true);
        buildDrawingCache();
        Bitmap bitmap=getDrawingCache();
//        setDrawingCacheEnabled(false);
        return bitmap;
        */
        Bitmap newBitmap = Bitmap.createScaledBitmap(cachebBitmap, width,
                height,true);
        return newBitmap;
    }

    /**
     * 逐行掃描 清楚邊界空白。
     *
     * @param bp
     * @param blank 邊距留多少個像素
     * @return
     */
    private Bitmap clearBlank(Bitmap bp, int blank) {
        int HEIGHT = bp.getHeight();
        int WIDTH = bp.getWidth();
        int top = 0, left = 0, right = 0, bottom = 0;
        int[] pixs = new int[WIDTH];
        boolean isStop;
        //掃描上邊距不等於背景顏色的第一個點
        for (int y = 0; y < HEIGHT; y++) {
            bp.getPixels(pixs, 0, WIDTH, 0, y, WIDTH, 1);
            isStop = false;
            for (int pix : pixs) {
                if (pix != mBackColor) {
                    top = y;
                    isStop = true;
                    break;
                }
            }
            if (isStop) {
                break;
            }
        }
        //掃描下邊距不等於背景顏色的第一個點
        for (int y = HEIGHT - 1; y >= 0; y--) {
            bp.getPixels(pixs, 0, WIDTH, 0, y, WIDTH, 1);
            isStop = false;
            for (int pix : pixs) {
                if (pix != mBackColor) {
                    bottom = y;
                    isStop = true;
                    break;
                }
            }
            if (isStop) {
                break;
            }
        }
        pixs = new int[HEIGHT];
        //掃描左邊距不等於背景顏色的第一個點
        for (int x = 0; x < WIDTH; x++) {
            bp.getPixels(pixs, 0, 1, x, 0, 1, HEIGHT);
            isStop = false;
            for (int pix : pixs) {
                if (pix != mBackColor) {
                    left = x;
                    isStop = true;
                    break;
                }
            }
            if (isStop) {
                break;
            }
        }
        //掃描右邊距不等於背景顏色的第一個點
        for (int x = WIDTH - 1; x > 0; x--) {
            bp.getPixels(pixs, 0, 1, x, 0, 1, HEIGHT);
            isStop = false;
            for (int pix : pixs) {
                if (pix != mBackColor) {
                    right = x;
                    isStop = true;
                    break;
                }
            }
            if (isStop) {
                break;
            }
        }
        if (blank < 0) {
            blank = 0;
        }
        //计算加上保留空白距離之後的圖像大小
        left = left - blank > 0 ? left - blank : 0;
        top = top - blank > 0 ? top - blank : 0;
        right = right + blank > WIDTH - 1 ? WIDTH - 1 : right + blank;
        bottom = bottom + blank > HEIGHT - 1 ? HEIGHT - 1 : bottom + blank;
        return Bitmap.createBitmap(bp, left, top, right - left, bottom - top);
    }

    /**
     * 设置畫筆寬度 默認寬度为10px
     *
     * @param mPaintWidth
     */
    public void setPaintWidth(int mPaintWidth) {
        mPaintWidth = mPaintWidth > 0 ? mPaintWidth : 10;
        this.mPaintWidth = mPaintWidth;
        mGesturePaint.setStrokeWidth(mPaintWidth);

    }

    public void setBackColor(@ColorInt int backColor) {
        mBackColor = backColor;
    }

    /**
     * 设置畫筆顏色
     *
     * @param mPenColor
     */
    public void setPenColor(int mPenColor) {
        this.mPenColor = mPenColor;
        mGesturePaint.setColor(mPenColor);
    }

    /**
     * 是否有簽名
     *
     * @return
     */
    public boolean getTouched() {
        return isTouched;
    }
}
