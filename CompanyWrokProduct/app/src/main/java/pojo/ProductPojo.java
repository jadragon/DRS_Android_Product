package pojo;

import android.graphics.Bitmap;

/**
 * Created by user on 2017/10/30.
 */

public class ProductPojo {
    private String title;
    private Bitmap bitmap;

    public ProductPojo() {
    }

    public ProductPojo(String title, Bitmap bitmap) {

        this.title = title;
        this.bitmap = bitmap;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
