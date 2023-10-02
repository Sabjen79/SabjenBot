package me.sabjen.discordbot.modules;

import me.sabjen.discordbot.Bot;
import me.sabjen.discordbot.modules.commands.CommandManager;
import me.sabjen.discordbot.modules.voice.music.MusicManager;
import me.sabjen.discordbot.modules.voice.VoiceManager;
import me.sabjen.discordbot.modules.voice.music.musictaste.MusicTasteManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;

public class GuildManager {
    private final Guild guild;

    private final CommandManager commandManager;
    private final VoiceManager voiceManager;
    private final MusicManager musicManager;
    private final MusicTasteManager musicTasteManager;

    private long mainChannel;
    private long musicChannel;
    private long restrictedChannel;

    public GuildManager(Guild g) {
        guild = g;

        mainChannel = musicChannel = restrictedChannel = -1;

        var main = g.getSystemChannel();
        if(main != null) mainChannel = main.getIdLong();

        for(var channel : g.getTextChannels()) {
            if(channel.getName().toLowerCase().contains("music")) musicChannel = channel.getIdLong();
            if(channel.getName().toLowerCase().contains("carcera")) restrictedChannel = channel.getIdLong();
        }

        commandManager = new CommandManager(guild);
        voiceManager = new VoiceManager(guild);
        musicManager = new MusicManager(guild);
        musicTasteManager = new MusicTasteManager(guild, getTextChannel(musicChannel), musicManager);

        Bot.logger.info("Created Guild Manager for: " + guild.getName());
    }

    public Guild getGuild() {
        return guild;
    }

    public TextChannel getMainChannel() {
        return getTextChannel(mainChannel);
    }

    public TextChannel getMusicChannel() {
        return getTextChannel(musicChannel);
    }

    public TextChannel getRestrictedChannel() {
        return getTextChannel(restrictedChannel);
    }

    private TextChannel getTextChannel(long id) {
        if(id == -1) return null;

        return guild.getTextChannelById(id);
    }

    public MusicTasteManager getMusicTasteManager() { return musicTasteManager; }

    public MusicManager getMusicManager() {
        return musicManager;
    }

    public AudioChannelUnion getConnectedChannel() {
        return voiceManager.getConnectedChannel();
    }
}
