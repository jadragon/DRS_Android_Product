package library;

import android.graphics.Bitmap;

import java.util.Map;

/**
 * Bitmap放入Map中(Runnable)
 */

public class UrlToBitmap implements Runnable {

        private String url;
    private String order;
    Bitmap bitmap;
    Map<String, Bitmap> map;

    public UrlToBitmap(String url, String order, Map<String, Bitmap> map) {
        this.url = url;
        this.order = order;
        this.map = map;
    }

    @Override
    public void run() {
        bitmap = new ShowBimapFromURL().getBitmapFromURL(url);
        map.put(order, bitmap);
    }
}
