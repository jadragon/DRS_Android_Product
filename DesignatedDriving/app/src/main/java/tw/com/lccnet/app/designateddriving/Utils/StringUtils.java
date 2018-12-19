package tw.com.lccnet.app.designateddriving.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;

public class StringUtils {

    public static String encodeToUTF8(String s) {
        String out;
        try {
            out = new String(s.getBytes("UTF-8"));
        } catch (java.io.UnsupportedEncodingException e) {
            return "";
        }
        return out;
    }

    public static String encodeTobase64(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 10, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        String encoded = Base64.encodeToString(b, Base64.DEFAULT);
        return encoded;
    }

    // method for base64 to bitmap
    public static Bitmap decodeBase64(String input) {
            byte[] decodedByte = Base64.decode(input.getBytes(Charset.forName("UTF-8")), Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
