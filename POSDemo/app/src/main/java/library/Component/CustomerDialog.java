package library.Component;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;


public class CustomerDialog {
    public AlertDialog init(Context context, View customer) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(customer);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}
