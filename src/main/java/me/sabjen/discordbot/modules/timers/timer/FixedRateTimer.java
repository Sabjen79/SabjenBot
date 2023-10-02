package me.sabjen.discordbot.modules.timers.timer;

import me.sabjen.discordbot.Bot;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class FixedRateTimer extends AbstractTimer {
    private int delay;

    public FixedRateTimer(Runnable runnable, Supplier<Long> supplier, TimeUnit unit) {
        super(runnable, supplier, unit);
        delay = 5;
    }

    @Override
    void schedule() throws InterruptedException {
        endDate = new Date(new Date().getTime() + delay);

        Thread.sleep(delay);

        for(int i = 0; i < 10000; i++) {
            Long period = time.get();
            endDate = new Date(new Date().getTime() + timeUnit.toMillis(period));

            runnable.run();

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
