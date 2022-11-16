package utils;

public class Timer {
    private final long nanoSecondsPerSecond = 1000000000;
    private final long nanoSecondsPerMinute = 60000000000L;
    private long stopWatchStartTime = 0;
    private long stopWatchStopTime = 0;
    private boolean stopWatchRunning = false;

    //    STARTS THE TIME COUNT
    public void start() {
        this.stopWatchStartTime = System.nanoTime();
        this.stopWatchRunning = true;
    }

    //    STOPS THE TIME COUNT
    public void stop() {
        this.stopWatchStopTime = System.nanoTime();
        this.stopWatchRunning = false;
    }

    //    TOTAL SECONDS OF GAMEPLAY
    public long getSeconds() {
        long elapsedTime;
        if (stopWatchRunning)
            elapsedTime = (System.nanoTime() - stopWatchStartTime);
        else
            elapsedTime = (stopWatchStopTime - stopWatchStartTime);
        return elapsedTime / nanoSecondsPerSecond;
    }

    //    TOTAL MINUTES OF GAMEPLAY
    public long getMinutes() {
        long elapsedTime;
        if (stopWatchRunning)
            elapsedTime = (System.nanoTime() - stopWatchStartTime);
        else
            elapsedTime = (stopWatchStopTime - stopWatchStartTime);
        return elapsedTime / nanoSecondsPerMinute;
    }

    //    RETURNS THE AMOUNT OF SECONDS, MINUTES EXCLUDED
    public long seconds() {
        if (getSeconds() >= 60) return getSeconds()%60;
        else return getSeconds();
    }
}
