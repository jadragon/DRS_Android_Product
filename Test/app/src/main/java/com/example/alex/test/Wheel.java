package com.example.alex.test;

/**
 * Created by user on 2017/9/17.
 */

public class Wheel extends Thread {
    interface WheelListener{
        void newImage(int img);
    }
   // private static int[] imgs={R.drawable.a1,R.drawable.a2,R.drawable.a3,R.drawable.a4,R.drawable.a5,R.drawable.a6,R.drawable.a7};
     private static int[] imgs={0,1,2,3,4,5,6,7,8,9};



    public int currentIndex;
    private WheelListener wheelListener;
    private long frameDuration;
    private long startIn;
    private  boolean isStarted;

    public Wheel(WheelListener wheelListener, long frameDuration, long startIn) {
        this.wheelListener = wheelListener;
        this.frameDuration = frameDuration;
        this.startIn = startIn;
        currentIndex=0;
        isStarted=true;
    }
    public void newImage(){
        currentIndex++;
        if(currentIndex==imgs.length){
            currentIndex=0;
        }
    }

    @Override
    public void run() {
        try {
            Thread.sleep(startIn);
        } catch (InterruptedException e) {
        }
        while (isStarted){
            try {
                Thread.sleep(frameDuration);
                newImage();
                if(wheelListener!=null){
                    wheelListener.newImage(imgs[currentIndex]);
                }
            } catch (InterruptedException e) {}

        }
    }
    public void stopWheel(){
        isStarted=false;
    }
}
