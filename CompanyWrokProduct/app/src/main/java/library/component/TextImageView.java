package library.Component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.test.tw.wrokproduct.R;

/**
 * @author Free
 * @version 1.0
 * @since 2017/5/27
 */
public class TextImageView extends android.support.v7.widget.AppCompatTextView {

    private int mLeftWidth;
    private int mLeftHeight;
    private int mTopWidth;
    private int mTopHeight;
    private int mRightWidth;
    private int mRightHeight;
    private int mBottomWidth;
    private int mBottomHeight;
    private int drawablePadding;
    private boolean drawablefillWidth;
    private boolean drawablefillHeight;

    public TextImageView(Context context) {
        super(context);
    }

    public TextImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TextImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    public void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextImageView);
        mLeftWidth = typedArray.getDimensionPixelOffset(R.styleable.TextImageView_drawableLeftWidth, 0);
        mLeftHeight = typedArray.getDimensionPixelOffset(R.styleable.TextImageView_drawableLeftHeight, 0);
        mTopWidth = typedArray.getDimensionPixelOffset(R.styleable.TextImageView_drawableTopWidth, 0);
        mTopHeight = typedArray.getDimensionPixelOffset(R.styleable.TextImageView_drawableTopHeight, 0);
        mRightWidth = typedArray.getDimensionPixelOffset(R.styleable.TextImageView_drawableRightWidth, 0);
        mRightHeight = typedArray.getDimensionPixelOffset(R.styleable.TextImageView_drawableRightHeight, 0);
        mBottomWidth = typedArray.getDimensionPixelOffset(R.styleable.TextImageView_drawableBottomWidth, 0);
        mBottomHeight = typedArray.getDimensionPixelOffset(R.styleable.TextImageView_drawableBottomHeight, 0);
        drawablePadding = typedArray.getDimensionPixelOffset(R.styleable.TextImageView_drawablePadding, 0);
        drawablefillWidth = typedArray.getBoolean(R.styleable.TextImageView_drawablefillWidth, false);
        drawablefillHeight = typedArray.getBoolean(R.styleable.TextImageView_drawablefillHeight, false);
        typedArray.recycle();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        setDrawablesSize();
    }

    private void setDrawablesSize() {
        Drawable[] compoundDrawables = getCompoundDrawables();
        int width = this.getWidth()-2*drawablePadding;
        int height = this.getHeight()-2*drawablePadding;
        for (int i = 0; i < compoundDrawables.length; i++) {
            if (drawablefillWidth) {
                setDrawableBounds(compoundDrawables[i], width, width);
            } else if (drawablefillHeight) {
                setDrawableBounds(compoundDrawables[i], height, height);
            } else {
                switch (i) {
                    case 0:
                        setDrawableBounds(compoundDrawables[0], mLeftWidth, mLeftHeight);
                        break;
                    case 1:
                        setDrawableBounds(compoundDrawables[1], mTopWidth, mTopHeight);
                        break;
                    case 2:
                        setDrawableBounds(compoundDrawables[2], mRightWidth, mRightHeight);
                        break;
                    case 3:
                        setDrawableBounds(compoundDrawables[3], mBottomWidth, mBottomHeight);
                        break;
                    default:

                        break;
                }
            }
        }
        setCompoundDrawables(compoundDrawables[0], compoundDrawables[1], compoundDrawables[2], compoundDrawables[3]);
    }

    private void setDrawableBounds(Drawable drawable, int width, int height) {
        if (drawable != null) {
            double scale = ((double) drawable.getIntrinsicHeight()) / ((double) drawable.getIntrinsicWidth());
            drawable.setBounds(0, 0, width, height);
            Rect bounds = drawable.getBounds();
            //高宽只给一个值时，自适应
            if (bounds.right != 0 || bounds.bottom != 0) {
                if (bounds.right == 0) {
                    bounds.right = (int) (bounds.bottom / scale);
                    drawable.setBounds(bounds);
                }
                if (bounds.bottom == 0) {
                    bounds.bottom = (int) (bounds.right * scale);
                    drawable.setBounds(bounds);
                }
            }

        }
    }
}