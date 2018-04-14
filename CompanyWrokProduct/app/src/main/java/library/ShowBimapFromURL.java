package library;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * 將URL的圖檔轉成Bitmap
 */

public class ShowBimapFromURL {
    Bitmap bitmap;
    public Bitmap getBitmapFromURL(String url) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            // options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeigh);
            options.inSampleSize = 3;
                InputStream is = (InputStream) new URL(url).getContent();
              bitmap = BitmapFactory.decodeStream(is, null, null);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    /*
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        Log.e("aaaaaa","---"+height+"---"+width);
        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both

            // height and width larger than the requested height and width.

            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static ArrayList<Bitmap> decodeBitmap(String[] url) {
        ArrayList<Bitmap> arrayList = new ArrayList<>();
        Bitmap bitmap = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
           // options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeigh);
            options.inSampleSize = 3;
            for (int i = 0; i < url.length; i++) {
                InputStream is = (InputStream) new URL(url[i]).getContent();
                bitmap = BitmapFactory.decodeStream(is, null, options);
                arrayList.add(bitmap);
            }
        } catch (MalformedInputException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }
    */
}
