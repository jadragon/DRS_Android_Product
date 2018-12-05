package tw.com.lccnet.app.designateddriving.Component;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import tw.com.lccnet.app.designateddriving.R;

public class SlideDialog extends Dialog {
    private View view;

    public SlideDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onStart() {
        super.onStart();

        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        Window window = getWindow();
        window.setWindowAnimations(R.style.bottomShow);
        WindowManager.LayoutParams windowparams = window.getAttributes();
        window.setGravity(Gravity.BOTTOM);
        windowparams.height = (int) (300 * dm.density);
        windowparams.width = dm.widthPixels;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(windowparams);
        view = window.getDecorView();
    }

    float startY;
    float moveY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                moveY = ev.getY() - startY;
                view.scrollBy(0, -(int) moveY);
                startY = ev.getY();
                if (view.getScrollY() > 0) {
                    view.scrollTo(0, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (view.getScrollY() < -this.getWindow().getAttributes().height / 4 && moveY > 0) {
                    this.dismiss();
                }
                view.scrollTo(0, 0);
                break;
        }
        return super.onTouchEvent(ev);
    }

}
