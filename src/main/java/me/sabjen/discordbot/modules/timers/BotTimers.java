package me.sabjen.discordbot.modules.timers;

import me.sabjen.discordbot.Bot;
import me.sabjen.discordbot.modules.chat.ChatManager;
import me.sabjen.discordbot.modules.chat.base.Conversation;
import me.sabjen.discordbot.modules.timers.timer.FixedRateTimer;
import me.sabjen.discordbot.modules.voice.music.MusicManager;
import me.sabjen.discordbot.utils.resource.ResourceManager;
import me.sabjen.discordbot.modules.timers.timer.AbstractTimer;
import me.sabjen.discordbot.modules.timers.timer.DelayedTimer;
import me.sabjen.discordbot.utils.RandomUtil;
import me.sabjen.discordbot.modules.voice.music.request.TrackRequestBuilder;
import net.dv8tion.jda.api.entities.Icon;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class BotTimers {
    public static AbstractTimer newDelayedTimer(Runnable runnable, Supplier<Long> supp, TimeUnit timeUnit) {
        return new DelayedTimer(runnable, supp, timeUnit);
    }
    public static AbstractTimer newDelayedTimer(Runnable runnable, long delay, TimeUnit timeUnit) {
        return new DelayedTimer(runnable, () -> delay, timeUnit);
    }

    public static AbstractTimer newFixedRateTimer(Runnable runnable, Supplier<Long> supp, TimeUnit timeUnit) {
        return new FixedRateTimer(runnable, supp, timeUnit);
    }

    public static AbstractTimer newFixedRateTimer(Runnable runnable, long period, TimeUnit timeUnit) {
        return new FixedRateTimer(runnable, () -> period, timeUnit);
    }

    public static AbstractTimer avatarTimer = newFixedRateTimer(() -> {
        try {
            var files = ResourceManager.getInstance().getFileFromPath("\\resources\\images\\profile").listFiles();
            if(files == null) return;

            Bot.getJDA().getSelfUser().getManager().setAvatar(Icon.from(RandomUtil.randomFrom(files))).queue();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }, () -> RandomUtil.randomLong(15, 20), TimeUnit.MINUTES);

    public static AbstractTimer soundTimer = newFixedRateTimer(() -> {
        File sound = ResourceManager.getInstance().getRandomSound();
        if(sound == null) return;

        for(var guild : Bot.getGuilds()) {
            if(MusicManager.getForGuild(guild).getPlayingTrack() != null || Bot.getManager(guild).getConnectedChannel() == null) continue;

            TrackRequestBuilder.createRequest(sound.getAbsolutePath(), guild)
                    .setUser(Bot.getUser())
                    .setSkippable(false).setHidden(true)
                    .build().accept();
        }
    }, () -> RandomUtil.randomLong(5, 10), TimeUnit.MINUTES);

    public static AbstractTimer musicTimer = newFixedRateTimer(() -> {
        for(var guild : Bot.getGuilds()) {
            var manager = Bot.getManager(guild);
            if(manager == null || manager.getConnectedChannel() == null) continue;

            TrackRequestBuilder.createRequest(manager.getMusicTasteManager().getRandomSong(), guild)
                    .setUser(Bot.getUser())
                    .setSkippable(false).setHidden(false)
                    .build().accept();

        }
    }, () -> RandomUtil.randomLong(15, 30), TimeUnit.MINUTES);

    public static AbstractTimer announceTimer = newFixedRateTimer(() -> {
        Conversation.newAnnounceChat();
    }, () -> RandomUtil.randomLong(15, 30), TimeUnit.MINUTES);

    public static void globalTimers() {
        avatarTimer.start();
        soundTimer.start();
        musicTimer.start();
        announceTimer.start();
    }
}
