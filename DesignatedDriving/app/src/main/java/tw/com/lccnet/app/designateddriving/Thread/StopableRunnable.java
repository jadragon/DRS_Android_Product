package tw.com.lccnet.app.designateddriving.Thread;

public abstract class StopableRunnable implements Runnable {
    private volatile boolean shutdown;


    protected abstract void runMethod();

    public void shutdown() {
        shutdown = true;
    }

    public boolean isShutdown() {
        return shutdown;
    }

    @Override
    public void run() {
        while (!shutdown) {
            runMethod();
        }
    }
}