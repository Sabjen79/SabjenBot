package me.sabjen.discordbot.modules.timers.timer;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DelayedTimer extends AbstractTimer {
    private final long delay;
    private final TimeUnit timeUnit;


    public DelayedTimer(Runnable runnable, long delay, TimeUnit unit) {
        super(runnable);

        this.delay = delay;
        this.timeUnit = unit;
    }

    @Override
    void schedule() throws InterruptedException {
        endDate = new Date(new Date().getTime() + timeUnit.toMillis(delay));

        Thread.sleep(timeUnit.toMillis(delay));

        getRunnable().run();
    }

    @Override
    public void runForced() {
        getRunnable().run();

        this.start();
    }
}
