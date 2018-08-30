package Utils;

import android.view.View;
import android.view.animation.AlphaAnimation;

import library.MainSlideMenuAnimation;
import library.SubSlideMenuAnimation;

public class ComponentUtil {
    public void showDissMiss(View dismiss, boolean ok) {
        if (ok) {
            dismiss.setAlpha(1);
            dismiss.setClickable(true);
            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
            alphaAnimation.setDuration(500);
            alphaAnimation.setFillAfter(true);
            dismiss.startAnimation(alphaAnimation);
        } else {
            dismiss.setClickable(false);
            AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
            alphaAnimation.setDuration(500);
            alphaAnimation.setFillAfter(true);
            dismiss.startAnimation(alphaAnimation);
        }
    }


    public void showMainMenu(int maxWidth, View menu, View content, boolean ok) {
        if (ok) {
            MainSlideMenuAnimation animation = new MainSlideMenuAnimation(menu, 200, MainSlideMenuAnimation.EXPAND);
            menu.startAnimation(animation);
        } else {
            MainSlideMenuAnimation animation = new MainSlideMenuAnimation(menu, 200, MainSlideMenuAnimation.COLLAPSE);
            menu.startAnimation(animation);
        }
    }

    public void showSubMenu(View submenu, boolean ok) {
        if (ok) {
            SubSlideMenuAnimation animation = new SubSlideMenuAnimation(submenu, 200, SubSlideMenuAnimation.EXPAND);
            submenu.startAnimation(animation);
        } else {
            SubSlideMenuAnimation animation = new SubSlideMenuAnimation(submenu, 200, SubSlideMenuAnimation.COLLAPSE);
            submenu.startAnimation(animation);
        }
    }
}
