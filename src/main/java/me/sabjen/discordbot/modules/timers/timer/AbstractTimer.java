package me.sabjen.discordbot.modules.timers.timer;

import javafx.util.Pair;
import me.sabjen.discordbot.Bot;

import java.sql.Time;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public abstract class AbstractTimer {
    private Thread timer;
    protected final Runnable runnable;
    protected final Supplier<Long> time;
    protected final TimeUnit timeUnit;
    protected Date endDate = null;

    AbstractTimer(Runnable runnable, Supplier<Long> supp, TimeUnit timeUnit) {
        this.runnable = runnable;
        this.time = supp;
        this.timeUnit = timeUnit;
    }

    abstract void schedule() throws InterruptedException;

    public void start() {
        if(timer != null) {
            stop();
            start();
            return;
        }

        timer = new Thread(() -> {
            try {
                schedule();
            } catch (InterruptedException e) {
                return;
            }
        });

        timer.start();
    }

    public void stop() {
        if(timer != null) timer.interrupt();
        timer = null;
    }

    public String getTimeLeft() {
        if(endDate == null) return "00:00";

        long millis = Math.abs(endDate.getTime() - new Date().getTime());
        String sec = String.valueOf(TimeUnit.SECONDS.convert(millis, TimeUnit.MILLISECONDS)%60);
        String min = String.valueOf(TimeUnit.MINUTES.convert(millis, TimeUnit.MILLISECONDS));

        return ((min.length() == 1) ? "0" + min : min) + ":" + ((sec.length() == 1) ? "0" + sec : sec);
    }

    public abstract void runForced();
}
