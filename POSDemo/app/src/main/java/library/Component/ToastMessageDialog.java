package library.Component;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.alex.posdemo.R;


public class ToastMessageDialog {
    private AlertDialog dialog;
    private TextView dialog_message;
    private Button confirm;

    public ToastMessageDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View inflate = LayoutInflater.from(context).inflate(R.layout.dialog_login_error, null);
        builder.setView(inflate);
        dialog_message = inflate.findViewById(R.id.dialog_message);
        confirm = inflate.findViewById(R.id.dialog_confirm);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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

    public void confirm(String message) {
        dialog_message.setText(message);
        confirm.setVisibility(View.VISIBLE);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();//显示对话框
    }

}
