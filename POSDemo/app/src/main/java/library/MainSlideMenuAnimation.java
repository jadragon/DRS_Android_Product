package library;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

public class MainSlideMenuAnimation extends Animation {

    public final static int COLLAPSE = 1;
    public final static int EXPAND = 0;

    private View main_menu, content;
    private int maxWidth;
    private int mEndWidth;
    private int mType;
    private LinearLayout.LayoutParams menuParams;
    private LinearLayout.LayoutParams contentParams;

    public MainSlideMenuAnimation(int maxWidth, View main_menu, View content, int duration, int type) {
        setDuration(duration);
        main_menu.setVisibility(View.VISIBLE);
        this.maxWidth = maxWidth;
        this.main_menu = main_menu;
        this.content = content;
        this.mType = type;
        menuParams = (LinearLayout.LayoutParams) main_menu.getLayoutParams();
        contentParams = (LinearLayout.LayoutParams) content.getLayoutParams();
        this.mEndWidth = menuParams.width;


    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        if (interpolatedTime < 1.0f) {
            if (mType == EXPAND) {
                menuParams.leftMargin = -(int) (mEndWidth * (1 - interpolatedTime));
                contentParams.width = maxWidth - (int) (mEndWidth * interpolatedTime);
            } else {
                menuParams.leftMargin = -(int) (mEndWidth * interpolatedTime);
                contentParams.width = maxWidth - (int) (mEndWidth * (1 - interpolatedTime));
            }
            content.setLayoutParams(contentParams);
        } else {
            if (mType == EXPAND) {
                menuParams.leftMargin = 0;
                contentParams.width = maxWidth - mEndWidth;
            } else {
                menuParams.leftMargin = -mEndWidth;
                contentParams.width = maxWidth;
                main_menu.setVisibility(View.GONE);
            }
            content.setLayoutParams(contentParams);
        }
    }
}