package library.Component;

import android.databinding.BindingAdapter;
import android.view.View;
import android.view.ViewTreeObserver;

public class DataBindingAdapters {
    // 根据View的高度和宽高比，设置高度
    @BindingAdapter("widthHeightRatio")
    public static void setWidthHeightRatio(final View view, final float ratio) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int height = view.getHeight();
                if (height > 0) {
                    view.getLayoutParams().width = (int) (height * ratio);
                    view.invalidate();
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }
}
