package tw.com.lccnet.app.designateddriving.Utils;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.widget.Button;

public class WidgetUtils {
    public static void reShapeButton(Context context, Button button, int color) {
        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadius(20);
        shape.setColor(context.getResources().getColor(color));
        button.setBackgroundDrawable(shape);
    }
}
