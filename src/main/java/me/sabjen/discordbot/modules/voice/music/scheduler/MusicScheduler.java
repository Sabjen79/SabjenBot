package me.sabjen.discordbot.modules.voice.music.scheduler;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import me.sabjen.discordbot.Bot;
import me.sabjen.discordbot.database.BotDatabase;
import me.sabjen.discordbot.modules.voice.music.MusicManager;
import me.sabjen.discordbot.modules.timers.BotTimers;
import me.sabjen.discordbot.modules.timers.timer.AbstractTimer;
import me.sabjen.discordbot.utils.RandomUtil;
import me.sabjen.discordbot.utils.messages.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class MusicScheduler extends AudioEventAdapter {
    public void addNotifier(MusicSchedulerNotifier n) { notifiers.add(n); }

    private final List<MusicSchedulerNotifier> notifiers;
    private final Guild guild;
    private final AudioPlayer player;
    private final Queue<MusicTrack> queue;
    private MusicTrack currentTrack;
    private boolean loop;

    private final AbstractTimer autoSkipTimer;

    public MusicScheduler(Guild g, AudioPlayer player) {
        notifiers = new ArrayList<>();

        guild = g;
        this.player = player;
        queue = new LinkedBlockingQueue<>();
        currentTrack = null;
        loop = false;

        autoSkipTimer = BotTimers.newDelayedTimer(() -> {
            MusicManager.getForGuild(guild).skipTrack(Bot.getUser());
        }, RandomUtil.randomInt(5, 30), TimeUnit.SECONDS);

        addNotifier(new BotMusicMessages(g));
    }

    public void queue(MusicTrack track) {
        queue.add(track);

        for(var n : notifiers) n.onSongAdded(track);

        if(currentTrack == null) nextTrack();
    }

    public void nextTrack() {
        if(currentTrack != null) {
            player.stopTrack();
        }

        autoSkipTimer.stop();

        currentTrack = queue.poll();
        if(currentTrack == null) return;

        player.startTrack(currentTrack.getTrack(), false);

        for(var n : notifiers) n.onSongPlayed(currentTrack, false);

        if(!Bot.isThisBot(currentTrack.getUser()) && RandomUtil.randomChance(0.03)) autoSkipTimer.start(); //EDIT

        if(Bot.isThisBot(currentTrack.getUser()) && !loop && currentTrack.isHidden() && RandomUtil.randomChance(0.03)) //EDIT
            MusicManager.getForGuild(guild).toggleLoop(Bot.getUser());
    }

    public void skipTrack(User user) {

        if(currentTrack == null) {
            if(!Bot.isThisBot(user))
                MessageBuilder.createMessage(BotDatabase.getInstance().getSentences().get("MUSIC_SKIP_NOSONG"))
                        .setChannel(Bot.getManager(guild).getMusicChannel())
                        .setInstant(true).setTemporary(true)
                        .build().send();

            return;
        }

        if(currentTrack.isSkippable() || Bot.isThisBot(user)) {
            for(var n : notifiers) n.onSongSkipped(currentTrack, user, true);

            nextTrack();
        } else {
            for(var n : notifiers) n.onSongSkipped(currentTrack, user, false);

        }
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if(endReason != AudioTrackEndReason.FINISHED) return;

        if(loop) {
            player.startTrack(track.makeClone(), false);

            for(var n : notifiers) n.onSongPlayed(currentTrack, true);

            return;
        }

        currentTrack = null;
        nextTrack();
    }

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        currentTrack = null;
        nextTrack();
    }

    public MusicTrack getCurrentTrack() {
        return currentTrack;
    }
    public boolean getLoop() { return loop; }
    public void toggleLoop(User looper) {
        loop = !loop;

        for(var n : notifiers) n.onLoopChanged(looper, loop);
    }
    public void clear(User clearer) {
        queue.clear();

        for(var n : notifiers) n.onClear(clearer);
    }
}
