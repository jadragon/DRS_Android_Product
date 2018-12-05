package tw.com.lccnet.app.designateddriving.Component;

import android.content.Context;
import android.util.AttributeSet;

public class CarImageView extends android.support.v7.widget.AppCompatImageView {
    private boolean check;

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public CarImageView(Context context) {
        super(context);
    }

    public CarImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CarImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
