package com.example.alex.lotteryapp.library;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextPaint;
import android.util.AttributeSet;

/**
 * Created by akitaka on 2017/9/20.
 *
 * @filename FitHeightTextView
 * @describe 根据高度自适应字体文字大小
 * @email 960576866@qq.com
 */

public class AutoVerticalTextView extends AppCompatTextView {
    private Paint mTextPaint;
    private float mMaxTextSize; // 获取当前所设置文字大小作为最大文字大小
    private float mMinTextSize = 8;    //最小的字体大小

    public AutoVerticalTextView(Context context) {
        super(context);
    }

    public AutoVerticalTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setGravity(getGravity());
        setLines(1);
        initialise();
    }

    private void initialise() {
        mTextPaint = new TextPaint();
        mTextPaint.set(this.getPaint());
        //默认的大小是设置的大小，如果撑不下了 就改变
        mMaxTextSize = this.getTextSize();
    }

    //文字改变的时候
    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        refitText(this.getHeight());   //textview视图的高度
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
    }

    private void refitText(int height) {
        if (height > 0) {
            int availableHeight = height - this.getPaddingTop() - this.getPaddingBottom();   //减去边距为字体的实际高度
            float trySize = mMaxTextSize;
            mTextPaint.setTextSize(trySize);
            while (mTextPaint.descent() - mTextPaint.ascent() > availableHeight) {   //测量的字体高度过大，不断地缩放
                trySize -= 5;  //字体不断地减小来适应
                if (trySize < mMinTextSize) {
                    trySize = mMinTextSize;  //最小为这个
                    break;
                }
                mTextPaint.setTextSize(trySize);
            }
            setTextSize(px2sp(getContext(), trySize));
        }
    }

    //大小改变的时候
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (h != oldh) {
            refitText(h);
        }
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     */
    public float px2sp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (pxValue / fontScale);
    }
}