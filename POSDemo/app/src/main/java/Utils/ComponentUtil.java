package Utils;

import android.view.View;
import android.view.animation.AlphaAnimation;

public class ComponentUtil {
    public void showDissMiss(View dismiss) {
        dismiss.setAlpha(1);
        dismiss.setClickable(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(500);
        alphaAnimation.setFillAfter(true);
        dismiss.startAnimation(alphaAnimation);
    }

    public void hideDissMiss(View dismiss) {
        dismiss.setClickable(false);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(500);
        alphaAnimation.setFillAfter(true);
        dismiss.startAnimation(alphaAnimation);
    }
}
