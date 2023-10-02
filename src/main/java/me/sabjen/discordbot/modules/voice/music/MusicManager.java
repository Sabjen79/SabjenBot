package me.sabjen.discordbot.modules.voice.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;
import me.sabjen.discordbot.Bot;
import me.sabjen.discordbot.database.BotDatabase;
import me.sabjen.discordbot.modules.voice.music.scheduler.MusicTrack;
import me.sabjen.discordbot.modules.voice.music.request.TrackRequest;
import me.sabjen.discordbot.modules.voice.music.scheduler.MusicScheduler;
import me.sabjen.discordbot.utils.messages.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

public class MusicManager {
    public static MusicManager getForGuild(Guild g) {
        return Bot.getManager(g).getMusicManager();
    }

    private static AudioPlayerManager playerManager;
    public static AudioPlayerManager getPlayerManager() {
        if(playerManager == null) playerManager = new DefaultAudioPlayerManager();

        playerManager.getConfiguration().setFrameBufferFactory(NonAllocatingAudioFrameBuffer::new);
        AudioSourceManagers.registerLocalSource(playerManager);
        AudioSourceManagers.registerRemoteSources(playerManager);
        return playerManager;
    }

    private final Guild guild;
    private final AudioPlayer player;
    private final MusicScheduler scheduler;

    public MusicManager(Guild g) {
        guild = g;
        player = getPlayerManager().createPlayer();
        scheduler = new MusicScheduler(guild, player);
        player.addListener(scheduler);

        g.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(player));
    }

    public void loadTrack(TrackRequest request) {
        getPlayerManager().loadItem(request.getUrl(), new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                try {
                    MusicTrack musicTrack = new MusicTrack(track, request.getUser(), request.isSkippable(), request.isHidden());

                    scheduler.queue(musicTrack);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if(request.isHidden()) return;

                MessageBuilder.createMessage(BotDatabase.getInstance().getSentences().get("MUSIC_LOAD_PLAYLIST"))
                        .setChannel(Bot.getManager(guild).getMusicChannel())
                        .setInstant(true)
                        .setTemporary(true)
                        .build().send();
            }

            @Override
            public void noMatches() {
                if(request.isHidden()) return;

                MessageBuilder.createMessage(BotDatabase.getInstance().getSentences().get("MUSIC_LOAD_NO_MATCHES"))
                        .setChannel(Bot.getManager(guild).getMusicChannel())
                        .setInstant(true)
                        .setTemporary(true)
                        .build().send();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                if(request.isHidden()) return;

                MessageBuilder.createMessage(BotDatabase.getInstance().getSentences().get("MUSIC_LOAD_FAILED"))
                        .setChannel(Bot.getManager(guild).getMusicChannel())
                        .setInstant(true).setTemporary(true)
                        .build().send();
            }
        });
    }

    public void skipTrack(User user) {
        scheduler.skipTrack(user);
    }

    public void toggleLoop(User user) {
        scheduler.toggleLoop(user);
    }

    public void clear(User user) {
        scheduler.clear(user);
    }

    public AudioPlayer getPlayer() {
        return player;
    }

    public MusicTrack getPlayingTrack() {
        return scheduler.getCurrentTrack();
    }

    public MusicScheduler getScheduler() {
        return scheduler;
    }
}
