package me.sabjen.discordbot.modules.voice;

import me.sabjen.discordbot.Bot;
import me.sabjen.discordbot.modules.voice.music.MusicManager;
import me.sabjen.discordbot.modules.voice.music.request.TrackRequestBuilder;
import me.sabjen.discordbot.utils.resource.ResourceManager;
import me.sabjen.discordbot.modules.timers.BotTimers;
import me.sabjen.discordbot.utils.AutoListenerAdapter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;

import java.util.concurrent.TimeUnit;

public class VoiceManager extends AutoListenerAdapter {
    private final Guild guild;
    private final VoiceAutoChannel voiceHandler;
    private AudioChannelUnion connectedChannel;

    public AudioChannelUnion getConnectedChannel() { return connectedChannel; }

    public VoiceManager(Guild g) {
        super();
        g.getAudioManager().closeAudioConnection();

        guild = g;
        voiceHandler = new VoiceAutoChannel(guild);
        connectedChannel = null;

        BotTimers.newFixedRateTimer(this::checkForNewChannel, () -> (getConnectedChannel() == null) ? 300L : 5L, TimeUnit.SECONDS)
                .start();
    }

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        if(event.getGuild().getIdLong() != guild.getIdLong()) return;

        if(Bot.isThisBot(event.getMember())) {
            if(event.getChannelJoined() != null) connectedChannel = event.getChannelJoined();
            else connectedChannel = null;
        }

        checkForNewChannel();
    }

    private void checkForNewChannel() {
        if(!Bot.debugCheck(guild) || Bot.getManager(guild) == null) return;

        var newChannel = voiceHandler.getBestChannel();

        if(newChannel == null) {
            disconnect();
            return;
        }
        if(connectedChannel != null && newChannel.getIdLong() == connectedChannel.getIdLong()) return;

        connectTo(newChannel);
    }

    private void connectTo(AudioChannelUnion channel) {
        if(connectedChannel != null && connectedChannel.getIdLong() == channel.getIdLong()) return;

        guild.getAudioManager().openAudioConnection(channel);

        if(MusicManager.getForGuild(guild).getPlayingTrack() != null) return;

        var sound = ResourceManager.getInstance().getRandomSound();
        if(sound == null) return;

        TrackRequestBuilder.createRequest(sound.getAbsolutePath(), guild)
                .setUser(Bot.getUser())
                .setSkippable(false).setHidden(true)
                .build().accept();

    }

    private void disconnect() {
        guild.getAudioManager().closeAudioConnection();
    }
}
