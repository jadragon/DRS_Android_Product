package library.component;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.test.tw.wrokproduct.R;

public class ToastMessageDialog {
    private Dialog dialog;
    private View inflate;

    public ToastMessageDialog(Context context) {
        if (dialog == null)
            dialog = new Dialog(context);
        if (inflate == null)
            inflate = LayoutInflater.from(context).inflate(R.layout.favorate_layout, null);
        ((TextView) inflate.findViewById(R.id.favor_txt)).setText("");
        inflate.findViewById(R.id.favor_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(inflate);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public ToastMessageDialog(Context context, String message) {
        if (dialog == null)
            dialog = new Dialog(context);
        if (inflate == null)
            inflate = LayoutInflater.from(context).inflate(R.layout.favorate_layout, null);
        ((TextView) inflate.findViewById(R.id.favor_txt)).setText(message);
        inflate.findViewById(R.id.favor_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(inflate);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void setMessageText(String message) {
        ((TextView) inflate.findViewById(R.id.favor_txt)).setText(message);
    }

    public void show() {
        inflate.findViewById(R.id.favor_btn).setVisibility(View.GONE);
        dialog.show();//显示对话框
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 1500);
    }

    public void confirm() {
        inflate.findViewById(R.id.favor_btn).setVisibility(View.VISIBLE);
        dialog.show();//显示对话框
    }
}
