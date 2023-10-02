package me.sabjen.discordbot.modules.timers.timer;

import me.sabjen.discordbot.Bot;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class FixedRateTimer extends AbstractTimer {
    private long delay;
    private final long period;
    private final TimeUnit timeUnit;

    public FixedRateTimer(Runnable runnable, long delay, long period, TimeUnit unit) {
        super(runnable);

        this.delay = delay;
        this.period = period;
        this.timeUnit = unit;
    }

    @Override
    void schedule() throws InterruptedException {
        endDate = new Date(new Date().getTime() + delay);

        Thread.sleep(delay);

        for(int i = 0; i < 10000; i++) {
            getRunnable().run();

            endDate = new Date(new Date().getTime() + timeUnit.toMillis(period));

            Thread.sleep(timeUnit.toMillis(period));
        }

        Bot.logger.warn("A timer timed out...");
    }

    @Override
    public void runForced() {
        delay = 0;

        this.start();
    }
}
