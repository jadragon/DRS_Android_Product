package library;

import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

public class MainSlideMenuAnimation extends Animation {

    public final static int COLLAPSE = 1;
    public final static int EXPAND = 0;

    private View main_menu, content;
    private int mEndWidth;
    private int mType;
    private LinearLayout.LayoutParams menuParams;
    private LinearLayout.LayoutParams contentParams;
    private DisplayMetrics dm;

    public MainSlideMenuAnimation(DisplayMetrics dm, View main_menu, View content,int mEndWidth, int duration, int type) {
        setDuration(duration);
        main_menu.setVisibility(View.VISIBLE);
        this.main_menu = main_menu;
        this.content = content;
        this.dm = dm;
        this.mEndWidth =mEndWidth;
        contentParams = (LinearLayout.LayoutParams) content.getLayoutParams();
        menuParams = (LinearLayout.LayoutParams) main_menu.getLayoutParams();
        this.mType = type;

    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        if (interpolatedTime < 1.0f) {
            if (mType == EXPAND) {
                //  main_menu.setTranslationX(-(int) (mEndWidth * (1 - interpolatedTime)));
                // menuParams.width = (int) (mEndWidth * interpolatedTime);
                menuParams.leftMargin = -(int) (mEndWidth * (1 - interpolatedTime));
                contentParams.width = dm.widthPixels - (int) (mEndWidth * interpolatedTime);
                content.requestLayout();
            } else {
                //   main_menu.setTranslationX(-(int) (mEndWidth * interpolatedTime));
                //   menuParams.width = (int) (mEndWidth * (1 - interpolatedTime));
                menuParams.leftMargin = -(int) (mEndWidth * interpolatedTime);

                contentParams.width = dm.widthPixels - (int) (mEndWidth * (1 - interpolatedTime));
                content.requestLayout();

            }
            main_menu.requestLayout();
        } else {
            if (mType == EXPAND) {
                //  main_menu.setTranslationX(0);
                menuParams.leftMargin =0;
                main_menu.requestLayout();
                contentParams.width = dm.widthPixels - mEndWidth;
                content.requestLayout();
                main_menu.requestLayout();
            } else {
                menuParams.leftMargin =-mEndWidth;
                contentParams.width = dm.widthPixels;
                content.requestLayout();
                main_menu.requestLayout();
                main_menu.setVisibility(View.GONE);
            }
        }
    }
}