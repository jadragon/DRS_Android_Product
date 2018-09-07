package library.Component;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import com.example.alex.posdemo.R;

public class MuiltiColorTextView extends AppCompatTextView {
    private int mStart;
    private int mEnd;
    private int mTextColor;
    private SpannableStringBuilder mStyle;

    public MuiltiColorTextView(Context context) {
        this(context, null);
    }

    public MuiltiColorTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MuiltiColorTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray tArray = context.obtainStyledAttributes(attrs, R.styleable.MultiTextColor);
        mStart = tArray.getInteger(R.styleable.MultiTextColor_text_start, 0);
        mEnd = tArray.getInteger(R.styleable.MultiTextColor_text_end, 0);
        mTextColor = tArray.getInteger(R.styleable.MultiTextColor_text_color, 0);
        tArray.recycle();
        setOtherTextColor();
    }

    private void setOtherTextColor() {
        if (this.getText().toString() != null)
            mStyle = new SpannableStringBuilder(this.getText().toString());
        if (mEnd <= this.getTextSize()) {
            mStyle.setSpan(new ForegroundColorSpan(mTextColor), mStart, mEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        this.setText(mStyle);
    }
}
