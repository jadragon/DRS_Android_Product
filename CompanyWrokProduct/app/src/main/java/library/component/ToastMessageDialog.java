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
    EditText editText;
    Button confirm, cancel;
    TextView title, textView;
    private ClickListener clickListener;
    private CheckListener checkListener;

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
        editText.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        cancel.setVisibility(View.GONE);
        dialog.show();//显示对话框
    }

    public void choice(ClickListener onclick) {
        this.clickListener = onclick;
        confirm.setVisibility(View.VISIBLE);
        editText.setVisibility(View.VISIBLE);
        textView.setVisibility(View.GONE);
        editText.setText("");
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.ItemClicked(dialog, view, editText.getText().toString());
            }
        });
        cancel.setVisibility(View.VISIBLE);
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

    public interface CheckListener {
        void ItemClicked(Dialog dialog, View view);
    }
    public void setCheckListener(CheckListener onCheck){
        this.checkListener = onCheck;
        confirm.setVisibility(View.VISIBLE);
        editText.setVisibility(View.VISIBLE);
        textView.setVisibility(View.GONE);
        editText.setVisibility(View.GONE);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkListener!=null)
                    checkListener.ItemClicked(dialog, view);
            }
        });
        cancel.setVisibility(View.VISIBLE);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
    public void check() {
        dialog.show();//显示对话框
    }
}
