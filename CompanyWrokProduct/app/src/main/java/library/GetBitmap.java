package library;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class GetBitmap {
    public static Bitmap getBitmap(String url) {
        Bitmap bm = null;
        URL iconUrl;
        URLConnection conn;
        int length;
        HttpURLConnection http;
        InputStream is;
        BufferedInputStream bis;
        try {
            iconUrl = new URL(url);
            conn = iconUrl.openConnection();
            http = (HttpURLConnection) conn;

            length = http.getContentLength();

            conn.connect();
            // 获得图像的字符流
            is = conn.getInputStream();
            bis = new BufferedInputStream(is, length);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();// 关闭流
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }
    public static ArrayList<Bitmap> getBitmaps(String ...url) {
        ArrayList<Bitmap> array =new ArrayList<>();
        Bitmap bm = null;
        URL iconUrl;
        URLConnection conn;
        int length;
        HttpURLConnection http;
        InputStream is;
        BufferedInputStream bis;
        for (int i=0;i<url.length;i++) {
            try {
                iconUrl = new URL(url[i]);
                conn = iconUrl.openConnection();
                http = (HttpURLConnection) conn;
                length = http.getContentLength();
                conn.connect();
                // 获得图像的字符流
                is = conn.getInputStream();
                bis = new BufferedInputStream(is, length);
                bm = BitmapFactory.decodeStream(bis);
                array.add(bm);
                bis.close();
                is.close();// 关闭流
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return array;
    }

}
