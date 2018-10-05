package Component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.alex.eip_product.R;

import java.util.ArrayList;
import java.util.List;

import static java.lang.StrictMath.abs;

public class PaintView extends View {
    List<PointPojo> points = new ArrayList<>();
    Canvas vBitmapCanvas;
    Paint mpaint = new Paint();
    Bitmap vBitmap;
    Context context;
    PointPojo p1, p2;
    //   ViewParent parent;

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
        /* 去鋸齒 */
        mpaint.setAntiAlias(true);
        mpaint.setStyle(Paint.Style.STROKE);
        mpaint.setStrokeWidth(2);
//
//        //取得手機解析度
//        DisplayMetrics dm = new DisplayMetrics();
//        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        windowManager.getDefaultDisplay().getMetrics(dm);
    }


    public void setPenStrokeWidth(float width) {
        mpaint.setStrokeWidth(width);
    }

    public void setPenColor(int color) {
        mpaint.setColor(color);

    }


    /**
     * @Override protected void onAttachedToWindow() {
     * super.onAttachedToWindow();
     * parent = getParent();
     * while (true) {
     * if (parent == null || parent.getParent() instanceof ScrollView) {
     * break;
     * } else {
     * parent = parent.getParent();
     * }
     * }
     * }
     */

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (vBitmapCanvas == null) {
            //設定bitmap大小
            vBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.RGB_565);
            vBitmapCanvas = new Canvas(vBitmap);
            vBitmapCanvas.drawColor(Color.YELLOW);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 1; i < points.size(); i++) {
            p1 = points.get(i - 1);
            p2 = points.get(i);
            if (abs(p1.x - p2.x) < 50 && abs(p1.y - p2.y) < 50) {
                canvas.drawLine(p1.x, p1.y, p2.x, p2.y, mpaint);
                vBitmapCanvas.drawLine(p1.x, p1.y, p2.x, p2.y, mpaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /**
         if (parent != null)
         parent.requestDisallowInterceptTouchEvent(true);
         */
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


    public Bitmap getPaintBitmap() {
        return vBitmap;
    }


}