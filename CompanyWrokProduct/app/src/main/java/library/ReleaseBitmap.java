package library;

import android.graphics.Bitmap;
import android.view.View;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

public class ReleaseBitmap implements ImageLoadingListener {
    private List<Bitmap> mbitmaps;

    public ReleaseBitmap() {
        mbitmaps = new ArrayList<>();
    }

    public void cleanBitmapList() {
        if (mbitmaps.size() > 0) {
            for (int i = 0; i < mbitmaps.size(); i++) {
                Bitmap b = mbitmaps.get(i);
                if (b != null && !b.isRecycled()) {
                    b.recycle();
                }

            }
        }
    }

    @Override
    public void onLoadingStarted(String imageUri, View view) {

    }

    @Override
    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

    }

    @Override
    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
        mbitmaps.add(loadedImage);
    }

    @Override
    public void onLoadingCancelled(String imageUri, View view) {

    }
}
