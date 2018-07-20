package Util;

import android.app.Activity;
import android.content.Context;
import android.view.View;

public class LasyUtil {

    public   <T extends View> T f(Context context, int resId) {
        return (T) ((Activity)context).findViewById(resId);
    }
    public  <T extends View> T f(View view, int resId) {
        return (T) view.findViewById(resId);
    }
}
