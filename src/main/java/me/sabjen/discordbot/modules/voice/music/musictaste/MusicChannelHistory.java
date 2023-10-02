package me.sabjen.discordbot.modules.voice.music.musictaste;

import me.sabjen.discordbot.Bot;
import me.sabjen.discordbot.modules.voice.music.youtube.YoutubeManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.Date;

public class MusicChannelHistory {
    private final Guild guild;
    private final TextChannel channel;
    private Date lastDate;

    public MusicChannelHistory(TextChannel channel) {
        this.channel = channel;
        this.guild = channel.getGuild();

        lastDate = MusicTasteDatabase.getInstance().getLastDate(guild);
    }

    public void checkAndAdd() {
        var array = channel.getIterableHistory().cache(false);

        int i = 0;
        for(Message message : array) {
            if(message.getTimeCreated().toEpochSecond() <= lastDate.getTime()) break;

            if(message.getEmbeds().size() > 0 && Bot.isThisBot(message.getAuthor())) {
                String url = YoutubeManager.format( message.getEmbeds().get(0).getUrl() );

                if(url != null) {
                    i++;
                    SongTaste song = new SongTaste(guild, url);
                    MusicTasteDatabase.getInstance().addSong(song);
                }

                if(i % 100 == 0) Bot.logger.info("Am invatat " + i + " melodii noi din " + guild.getName());
            }
        }

        setDate();
        if(i != 0) Bot.logger.info("Am invatat " + i + " melodii noi din " + guild.getName());
    }

    private void setDate() {
        lastDate = new Date();
        MusicTasteDatabase.getInstance().setLastDate(guild, new java.sql.Date(lastDate.getTime()));
    }
}
