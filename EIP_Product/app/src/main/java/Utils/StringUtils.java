package Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.Enumeration;

public class StringUtils {

    public static String encodeToUTF8(String s) {
        return new String(s.getBytes(Charset.forName("UTF-8")));
    }

    public static String encodeTobase64(byte[] b) {
        // base64 = base64.replace("+", " ");
        return Base64.encodeToString(b, Base64.NO_WRAP);
    }

    // method for base64 to bitmap
    public static byte[] decodeBase64(String input) {
        if (input != null) {
            input = input.replace("data:image/png;base64,", "");
            input = input.replace(" ", "+");
        }
        return Base64.decode(input.getBytes(Charset.forName("UTF-8")), Base64.NO_WRAP);
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

    public static String macAddress() throws SocketException {
        String address = "";
        // 把当前机器上的访问网络接口的存入 Enumeration集合中
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        Log.d("TEST_BUG", " interfaceName = " + interfaces);
        while (interfaces.hasMoreElements()) {
            NetworkInterface netWork = interfaces.nextElement();
            // 如果存在硬件地址并可以使用给定的当前权限访问，则返回该硬件地址（通常是 MAC）。
            byte[] by = netWork.getHardwareAddress();
            if (by == null || by.length == 0) {
                continue;
            }
            StringBuilder builder = new StringBuilder();
            for (byte b : by) {
                builder.append(String.format("%02X:", b));
            }
            if (builder.length() > 0) {
                builder.deleteCharAt(builder.length() - 1);
            }
            String mac = builder.toString();
            Log.d("TEST_BUG", "interfaceName=" + netWork.getName() + ", mac=" + mac);
            // 从路由器上在线设备的MAC地址列表，可以印证设备Wifi的 name 是 wlan0
            if (netWork.getName().equals("wlan0")) {
                Log.d("TEST_BUG", " interfaceName =" + netWork.getName() + ", mac=" + mac);
                address = mac;
            }
        }
        return address;
    }
}
