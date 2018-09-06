package library.Component;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.SurfaceHolder;

import java.util.ArrayList;

public class DotsThread extends Thread {
    private int mCanvasWidth;
    private int mCanvasHeight;
    private ArrayList<dot> Dots= new ArrayList<dot>(); // Dynamic array with dots
    private SurfaceHolder holder;
    private boolean running = false;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final int refresh_rate=100;      // How often we update the screen, in ms

    public DotsThread(SurfaceHolder holder) {
        this.holder = holder;
    }

    @Override
    public void run() {
        int x,y,radius;
        float[] color=new float[3]; // HSV (0..360,0..1,0..1)
        long previousTime, currentTime;
        previousTime = System.currentTimeMillis();
        Canvas canvas = null;
        while(running) {
            // Look if time has past
            currentTime=System.currentTimeMillis();
            while ((currentTime-previousTime)<refresh_rate){
                currentTime=System.currentTimeMillis();
            }
            previousTime=currentTime;
            // ADD ONE MORE DOT TO THE SCREEN
            x=100 + (int)(Math.random() * (mCanvasWidth-200));
            y=100 + (int)(Math.random() * (mCanvasHeight-200));
            radius=1 + (int)(Math.random() * 99);
            color[0]=(float)(Math.random()*360);
            color[1]=1;
            color[2]=1;
            dot mdot=new dot(x,y,radius,Color.HSVToColor(128,color));
            Dots.add(mdot);
            // PAINT
            try {
                canvas = holder.lockCanvas();
                synchronized (holder) {
                    draw(canvas);
                }
            }
            finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
            }
            // WAIT
            try {
                Thread.sleep(refresh_rate-5); // Wait some time till I need to display again
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    // The actual drawing in the Canvas (not the update to the screen).
    private void draw(Canvas canvas)
    {
        dot temp_dot;
        canvas.drawColor(Color.BLACK);
        paint.setStyle(Style.FILL_AND_STROKE);
        for (int i=0;i<Dots.size();i++){
            temp_dot=Dots.get(i);
            paint.setColor(temp_dot.get_color());
            canvas.drawCircle((float)temp_dot.get_x(),
                    (float)temp_dot.get_y(),
                    (float)temp_dot.get_radius(),
                    paint);
        }
    }

    public void setRunning(boolean b) {
        running = b;
    }

    public void setSurfaceSize(int width, int height) {
        synchronized (holder){
            mCanvasWidth = width;
            mCanvasHeight = height;
        }
    }

    private class dot{
        private int x,y,radius,color;

        dot(int x, int y, int radius, int color){
            this.x=x;
            this.y=y;
            this.radius=radius;
            this.color=color;
        }

        public int get_x(){
            return this.x;
        }

        public int get_y(){
            return this.y;
        }

        public int get_radius(){
            return this.radius;
        }

        public int get_color(){
            return this.color;
        }
    }
}
