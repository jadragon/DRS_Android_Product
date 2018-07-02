package library;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.test.tw.wrokproduct.R;

public class LoadingView {
    private static PopupWindow popWin = null; // 弹出窗口
    private static View popView = null; // 保存弹出窗口布局
    private static Context context;

    public static void setContext(Context context) {
        LoadingView.context = context;
    }

    public static void getInstance() {
        if (popView == null || popWin == null) {
            popView = LayoutInflater.from(context).inflate(R.layout.loading, null);
            popWin = new PopupWindow(popView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, false); // 实例化PopupWindow
        }
    }

    public static void setMessage(String message) {
        ((TextView) popView.findViewById(R.id.loading_message)).setText(message);
    }

    public static void show(View viewparent) {
        popWin.showAtLocation(viewparent, Gravity.CENTER, 0, 0); // 显示弹出窗口
    }

    public static void hide() {
        popWin.dismiss();
    }
}
