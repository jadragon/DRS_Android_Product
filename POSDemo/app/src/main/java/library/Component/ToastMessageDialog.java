package library.Component;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.alex.posdemo.R;


public class ToastMessageDialog {
    public static final byte TYPE_ERROR = 0;
    public static final byte TYPE_WARM = 1;
    public static final byte TYPE_INFO = 2;
    public static final byte TYPE_EDIT = 3;
    private AlertDialog dialog;
    private TextView dialog_message;
    private Button confirm, cancel;

    public ToastMessageDialog(Context context, byte type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View inflate = null;
        if (type == TYPE_ERROR) {
            inflate = LayoutInflater.from(context).inflate(R.layout.dialog_error, null);
        } else if (type == TYPE_INFO) {
            inflate = LayoutInflater.from(context).inflate(R.layout.dialog_info, null);
        } else if (type == TYPE_EDIT) {
            inflate = LayoutInflater.from(context).inflate(R.layout.dialog_edit, null);
        }
        builder.setView(inflate);
        dialog_message = inflate.findViewById(R.id.dialog_message);
        confirm = inflate.findViewById(R.id.dialog_confirm);
        cancel = inflate.findViewById(R.id.dialog_cancel);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
    public void show(String message) {
        confirm.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);
        dialog_message.setText(message);
        dialog.show();//显示对话框
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 1500);
    }
    public void confirm() {
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();//显示对话框
    }

    public void confirm(String message) {
        dialog_message.setText(message);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();//显示对话框
    }

    public void confirm(String message, final OnConfirmListener onConfirmListener) {
        dialog_message.setText(message);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.dialog_confirm:
                        if (onConfirmListener != null)
                            onConfirmListener.onConfirm(dialog);
                        dialog.dismiss();
                        break;
                    case R.id.dialog_cancel:
                        dialog.dismiss();
                        break;
                }
            }
        };
        confirm.setOnClickListener(clickListener);
        cancel.setOnClickListener(clickListener);
        dialog.show();//显示对话框
    }

    public void sendApi(String message, final OnSendApiListener onSendApiListener) {
        dialog_message.setText(message);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.dialog_confirm:
                        if (onSendApiListener != null)
                            onSendApiListener.onConfirm(dialog, dialog_message.getText().toString());
                        dialog.dismiss();
                        break;
                    case R.id.dialog_cancel:
                        dialog.dismiss();
                        break;
                }
            }
        };
        confirm.setOnClickListener(clickListener);
        cancel.setOnClickListener(clickListener);
        dialog.show();//显示对话框
    }

    public interface OnSendApiListener {
        void onConfirm(AlertDialog alertDialog, String note);
    }

    public interface OnConfirmListener {
        void onConfirm(AlertDialog alertDialog);
    }
}
