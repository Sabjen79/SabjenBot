package me.sabjen.discordbot.modules.commands.base;

import me.sabjen.discordbot.modules.timers.BotTimers;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public interface Cooldownable {
    Date nextDate = new Date();

    default boolean onCooldown() {
        return nextDate.getTime() > new Date().getTime();
    }

    default void setCooldown(long n, TimeUnit t) {
        nextDate.setTime(new Date().getTime() + TimeUnit.MILLISECONDS.convert(n, t));

        BotTimers.newDelayedTimer(this::onCooldownEnd, n, t).start();
    }

    void onCooldownEnd();
}
