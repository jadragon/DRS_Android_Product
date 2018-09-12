package Component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.ScrollView;

import com.example.alex.eip_product.R;

import java.util.ArrayList;
import java.util.List;

import static java.lang.StrictMath.abs;

public class PaintView extends View {
    List<PointPojo> points = new ArrayList<>();
    Canvas vBitmapCanvas;
    Paint mpaint = new Paint();
    Context context;
    PointPojo p1, p2;
    ViewParent parent;

    public PaintView(Context context) {
        this(context, (AttributeSet) null);
    }

    public PaintView(Context context, AttributeSet attrs) {
        this(context, attrs, R.styleable.CustomTheme_paintViewStyle);
    }

    public PaintView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        mpaint.setColor(Color.BLACK);
        mpaint.setStyle(Paint.Style.STROKE);
        mpaint.setStrokeWidth(2);
//
//        //取得手機解析度
//        DisplayMetrics dm = new DisplayMetrics();
//        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        windowManager.getDefaultDisplay().getMetrics(dm);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        parent = getParent();
        while (true) {
            if (parent == null || parent.getParent() instanceof ScrollView) {
                break;
            } else {
                parent = parent.getParent();
            }
        }


    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //設定bitmap大小
        Bitmap  vBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.RGB_565);
        vBitmapCanvas = new Canvas(vBitmap);
        vBitmapCanvas.drawColor(Color.WHITE);
    }

    /*
                    public PaintView(Context context, int width, int heigh) {
                        super(context);
                        this.context = context;
                        mpaint.setColor(Color.BLACK);
                        mpaint.setStyle(Paint.Style.STROKE);
                        mpaint.setStrokeWidth(5);

                        //取得手機解析度
                        DisplayMetrics dm = new DisplayMetrics();
                        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                        windowManager.getDefaultDisplay().getMetrics(dm);

                        //設定bitmap大小
                        vBitmap = Bitmap.createBitmap(width, heigh, Bitmap.Config.RGB_565);
                        vBitmapCanvas = new Canvas(vBitmap);
                        vBitmapCanvas.drawColor(Color.WHITE);

                    }
                */
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 1; i < points.size(); i++) {
            p1 = points.get(i - 1);
            p2 = points.get(i);
            if (abs(p1.x - p2.x) < 10 && abs(p1.y - p2.y) < 10) {
                canvas.drawLine(p1.x, p1.y, p2.x, p2.y, mpaint);
                vBitmapCanvas.drawLine(p1.x, p1.y, p2.x, p2.y, mpaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (parent != null)
            parent.requestDisallowInterceptTouchEvent(true);
        for (int i = 0; i < event.getHistorySize(); i++) {
            points.add(new PointPojo(event.getHistoricalX(i), event.getHistoricalY(i)));
        }
        PaintView.this.invalidate();
        return true;
    }

    //Reset
    public void resetCanvas() {
        points.clear();
        PaintView.this.invalidate();
        vBitmapCanvas.drawColor(Color.WHITE);
    }

    //save as picture
//    public void savePicture() {
//        try {
//            File newFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "test.png");
//            newFile.getParentFile().mkdirs();
//            try {
//                FileOutputStream out = new FileOutputStream(newFile);
//                vBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
//                out.flush();
//                out.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            /*
//            FileOutputStream fout=new FileOutputStream("/sdcard/draw.png");
//            vBitmap.compress(Bitmap.CompressFormat.PNG,100,fout);
//            fout.close();
//            */
//        } catch (Exception x) {
//            x.printStackTrace();
//        }
//    }

}