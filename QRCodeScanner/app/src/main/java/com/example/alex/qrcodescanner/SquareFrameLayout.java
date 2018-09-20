package com.example.alex.qrcodescanner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;


public class SquareFrameLayout extends FrameLayout {
    float ratio;
    boolean ratio_by_heigh;

    public SquareFrameLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public SquareFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.style.AppTheme);
    }

    public SquareFrameLayout(Context context, AttributeSet attrs,
                             int defStyle) {
        super(context, attrs, defStyle);
        this.setViewAttributes(context, attrs, defStyle);
    }

    @SuppressLint({"NewApi"})
    private void setViewAttributes(Context context, AttributeSet attrs, int defStyle) {
        if (Build.VERSION.SDK_INT >= 11) {
            this.setLayerType(1, (Paint) null);
        }
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SquareFrameLayout, defStyle, R.style.AppTheme);
        this.ratio = array.getFloat(R.styleable.SquareFrameLayout_ratio, 1f);
        this.ratio_by_heigh = array.getBoolean(R.styleable.SquareFrameLayout_ratio_by_heigh, false);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),
                getDefaultSize(0, heightMeasureSpec));

        // 父容器传过来的宽度方向上的模式
        // int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        // 父容器传过来的高度方向上的模式
        //   int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        // 父容器传过来的宽度的值
        int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();


        // 父容器传过来的高度的值
        int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingBottom() - getPaddingTop();

        if (ratio_by_heigh) {
            width = (int) (height / ratio + 0.5f);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(width,
                    MeasureSpec.EXACTLY);
        } else {
            height = (int) (width / ratio + 0.5f);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
                    MeasureSpec.EXACTLY);

        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
