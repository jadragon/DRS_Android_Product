package library.Component;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.test.tw.wrokproduct.R;

public class ToastMessageDialog {
    private Dialog dialog;
    private View inflate;
    private EditText editText;
    private Button confirm, cancel, other;
    private TextView title, textView;
    private ClickListener clickListener;

    public ToastMessageDialog(Context context) {
        if (dialog == null)
            dialog = new Dialog(context);
        if (inflate == null)
            inflate = LayoutInflater.from(context).inflate(R.layout.favorate_layout, null);
        editText = inflate.findViewById(R.id.favor_edit);
        textView = inflate.findViewById(R.id.favor_txt);
        title = inflate.findViewById(R.id.favor_title);
        textView.setText("");
        confirm = inflate.findViewById(R.id.favor_confirm);
        cancel = inflate.findViewById(R.id.favor_cancel);
        other = inflate.findViewById(R.id.favor_other);
        dialog.setContentView(inflate);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public ToastMessageDialog(Context context, String message) {
        if (dialog == null)
            dialog = new Dialog(context);
        if (inflate == null)
            inflate = LayoutInflater.from(context).inflate(R.layout.favorate_layout, null);
        editText = inflate.findViewById(R.id.favor_edit);
        textView = inflate.findViewById(R.id.favor_txt);
        title = inflate.findViewById(R.id.favor_title);
        confirm = inflate.findViewById(R.id.favor_confirm);
        cancel = inflate.findViewById(R.id.favor_cancel);
        other = inflate.findViewById(R.id.favor_other);
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
        confirm.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);
        other.setVisibility(View.GONE);
        editText.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
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
        cancel.setVisibility(View.GONE);
        other.setVisibility(View.GONE);
        editText.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();//显示对话框
    }

    public void showCheck(boolean showEditText, ClickListener onclick) {
        this.clickListener = onclick;
        confirm.setVisibility(View.VISIBLE);
        other.setVisibility(View.GONE);
        cancel.setVisibility(View.VISIBLE);
        if (showEditText)
            editText.setVisibility(View.VISIBLE);
        else
            editText.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
        editText.setText("");
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.ItemClicked(dialog, view, editText.getText().toString());
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();//显示对话框
    }


    public interface ClickListener {
        void ItemClicked(Dialog dialog, View view, String note);
    }

    public interface OtherClickListener {
        void confirmClicked(Dialog dialog, View view);

        void otherClicked(Dialog dialog, View view);
    }

    public void showOhter(final OtherClickListener otherClickListener) {
        confirm.setVisibility(View.VISIBLE);
        other.setVisibility(View.VISIBLE);
        cancel.setVisibility(View.VISIBLE);
        editText.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otherClickListener.confirmClicked(dialog, view);
            }
        });

        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otherClickListener.otherClicked(dialog, view);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();//显示对话框
    }

    public void setCheckListener(boolean showEditText, ClickListener onCheck) {
        this.clickListener = onCheck;
        confirm.setVisibility(View.VISIBLE);
        other.setVisibility(View.GONE);
        cancel.setVisibility(View.VISIBLE);
        if (showEditText)
            editText.setVisibility(View.VISIBLE);
        else
            editText.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
        editText.setVisibility(View.GONE);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.ItemClicked(dialog, view, editText.getText().toString());
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void showCheck() {
        dialog.show();//显示对话框
    }
}
