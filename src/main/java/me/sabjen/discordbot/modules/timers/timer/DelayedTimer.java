package me.sabjen.discordbot.modules.timers.timer;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class DelayedTimer extends AbstractTimer {


    public DelayedTimer(Runnable runnable, Supplier<Long> supp, TimeUnit unit) {
        super(runnable, supp, unit);
    }

    @Override
    void schedule() throws InterruptedException {
        Long delay = time.get();
        endDate = new Date(new Date().getTime() + timeUnit.toMillis(delay));

        Thread.sleep(timeUnit.toMillis(delay));

        runnable.run();
    }

    @Override
    public void runForced() {
        runnable.run();

        this.start();
    }
}
