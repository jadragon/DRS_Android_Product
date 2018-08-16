package library;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class MyCustomAnimation extends Animation {

    public final static int COLLAPSE = 1;
    public final static int EXPAND = 0;

    private View mView;
    private int mEndWidth;
    private int mType;

    public MyCustomAnimation(View view, int duration, int type) {
        setDuration(duration);
        view.setVisibility(View.VISIBLE);
        mView = view;
        mEndWidth = view.getLayoutParams().width;
        mType = type;

    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        if (interpolatedTime < 1.0f) {
            if (mType == EXPAND) {
                mView.setTranslationX(-(int) (mEndWidth * (1 - interpolatedTime)));
            } else {
                mView.setTranslationX(-(int) (mEndWidth * interpolatedTime));
            }
        } else {
            if (mType == EXPAND) {
                mView.setTranslationX(0);
                //    mLayoutParams.width =mEndWidth;
                //    mView.requestLayout();
            } else {
                mView.setVisibility(View.GONE);
            }
        }
    }
}