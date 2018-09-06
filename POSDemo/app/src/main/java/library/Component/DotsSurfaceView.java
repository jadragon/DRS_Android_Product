package library.Component;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

// We extend SurfaceView. Internally (private) SurfaceView creates an object SurfaceHolder
// effectively defining the methods of the SurfaceHolder interface. Notice that it does
// not create a new class or anything, it just defines it right there. When we extend
// the SurfaceView with the SurfaceHolder.Callback interface, we need to add in that extension
// the methods of that interface.

public class DotsSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder holder;     // This is no instantiation. Just saying that the holder
    // will be of a class implementing SurfaceHolder
    private DotsThread DotsThread;// The thread that displays the dots

    public DotsSurfaceView(Context context) {
        super(context);
        holder = getHolder();          // Holder is now the internal/private mSurfaceHolder inherit
        // from the SurfaceView class, which is from an anonymous
        // class implementing SurfaceHolder interface.
        holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    // This is always called at least once, after surfaceCreated
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (DotsThread==null){
            DotsThread = new DotsThread(holder);
            DotsThread.setRunning(true);
            DotsThread.setSurfaceSize(width, height);
            DotsThread.start();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        DotsThread.setRunning(false);
        while (retry) {
            try {
                DotsThread.join();
                retry = false;
            } catch (InterruptedException e) {}
        }
    }

    public Thread getThread() {
        return DotsThread;
    }
}