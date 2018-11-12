package Util;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.widget.Button;

public class ComponentUtil {
    Context ctx;

    public ComponentUtil(Context ctx) {
        this.ctx = ctx;
    }

    public void reShapeButton(Button button, int color) {
        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadius(8);
        shape.setColor(ctx.getResources().getColor(color));
        button.setBackgroundDrawable(shape);
    }
}
