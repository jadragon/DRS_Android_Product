package Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

public class StringUtils {

    public static String encodeToUTF8(String s) {
        return new String(s.getBytes(Charset.forName("UTF-8")));
    }

    public static String encodeTobase64(byte[] b) {
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    // method for base64 to bitmap
    public static byte[] decodeBase64(String input) {
        return Base64.decode(input.getBytes(Charset.forName("UTF-8")), Base64.DEFAULT);
    }

    // method for base64 to bitmap
    public static Bitmap byteArrayToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    // method for base64 to bitmap
    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        byte[] b = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
            b = baos.toByteArray();
            baos.flush();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }
}
