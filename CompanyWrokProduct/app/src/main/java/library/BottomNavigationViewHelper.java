package library;

import android.content.Context;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * 去除BottomNavigationView的動畫
 */
public class BottomNavigationViewHelper {


    //此類別在去除BottomNavigationView的動畫
    public void disableShiftMode(BottomNavigationView view) {
        View iconView;
        TextView textView;
        DisplayMetrics displayMetrics = view.getContext().getResources().getDisplayMetrics();
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            BottomNavigationItemView item;
            for (int i = 0; i < menuView.getChildCount(); i++) {
                item = (BottomNavigationItemView) menuView.getChildAt(i);
                //noinspection RestrictedApi
            //    item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                //noinspection RestrictedApi
                item.setChecked(item.getItemData().isChecked());

                //icon
                iconView = item.findViewById(android.support.design.R.id.icon);
                ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
                layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, displayMetrics);
                layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, displayMetrics);
                iconView.setLayoutParams(layoutParams);

                //largelabel
                textView = item.findViewById(android.support.design.R.id.largeLabel);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                //smalllabel
                textView = item.findViewById(android.support.design.R.id.smallLabel);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);

            }
        } catch (NoSuchFieldException e) {
          //  Log.e("BNVHelper", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
          // Log.e("BNVHelper", "Unable to change value of shift mode", e);
        }

    }
}
