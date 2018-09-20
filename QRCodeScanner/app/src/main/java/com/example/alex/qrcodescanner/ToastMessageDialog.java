package com.example.alex.qrcodescanner;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ToastMessageDialog {
    private Dialog dialog;
    private View inflate;
    private Button confirm;
    private TextView title, textView;

    public ToastMessageDialog(Context context) {
        if (dialog == null)
            dialog = new Dialog(context);
        if (inflate == null)
            inflate = LayoutInflater.from(context).inflate(R.layout.favorate_layout, null);
        textView = inflate.findViewById(R.id.favor_txt);
        title = inflate.findViewById(R.id.favor_title);
        textView.setText("");
        confirm = inflate.findViewById(R.id.favor_confirm);
        dialog.setContentView(inflate);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public ToastMessageDialog(Context context, String message) {
        if (dialog == null)
            dialog = new Dialog(context);
        if (inflate == null)
            inflate = LayoutInflater.from(context).inflate(R.layout.favorate_layout, null);
        textView = inflate.findViewById(R.id.favor_txt);
        title = inflate.findViewById(R.id.favor_title);
        confirm = inflate.findViewById(R.id.favor_confirm);
        textView.setText(message);
        dialog.setContentView(inflate);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void setTitleText(String message) {
        title.setText(message);
    }

    public void setMessageText(String message) {
        textView.setText(message);
    }

    public void show() {
        confirm.setVisibility(View.INVISIBLE);
        dialog.show();//显示对话框
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 1500);
    }

    public void confirm() {
        confirm.setVisibility(View.VISIBLE);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();//显示对话框
    }

    public boolean isShow() {
        return dialog.isShowing();
    }
}
