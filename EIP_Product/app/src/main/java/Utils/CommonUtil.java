package Utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.Toast;

import com.example.alex.eip_product.MainActivity;
import com.example.alex.eip_product.R;
import com.example.alex.eip_product.SoapAPI.API_OrderInfo;
import com.example.alex.eip_product.SoapAPI.Analyze.AnalyzeUtil;
import com.example.alex.eip_product.pojo.AllupdateDataPojo;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

public class CommonUtil {
    public static Bitmap convertViewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(bitmap));
        return bitmap;
    }

    public static boolean checkWIFI(Context context) {
        ConnectivityManager cManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            return true;
            //能上網
        } else {
            return false;
            //不能上網
        }
    }

    public static void toastErrorMessage(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(context.getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
}
