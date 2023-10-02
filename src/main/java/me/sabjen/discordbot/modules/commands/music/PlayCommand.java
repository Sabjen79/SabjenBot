package me.sabjen.discordbot.modules.commands.music;

import me.sabjen.discordbot.Bot;
import me.sabjen.discordbot.modules.commands.base.BotCommand;
import me.sabjen.discordbot.modules.commands.base.MusicCommand;
import me.sabjen.discordbot.database.BotDatabase;
import me.sabjen.discordbot.modules.voice.music.musictaste.MusicTasteManager;
import me.sabjen.discordbot.utils.RandomUtil;
import me.sabjen.discordbot.utils.messages.MessageBuilder;
import me.sabjen.discordbot.modules.voice.music.request.TrackRequestBuilder;
import me.sabjen.discordbot.modules.voice.music.youtube.YoutubeManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PlayCommand extends BotCommand implements MusicCommand {
    @Override
    public boolean checkEvent(MessageReceivedEvent event) {
        int c = checkMusicCommand(event, "play");
        if(c != 1) return (c == -1);

        event.getMessage().delete().queue();

        String url = event.getMessage().getContentRaw().split(" ", 2)[1];

        if(!url.startsWith("http")) {
            url = YoutubeManager.searchForQuery(url);

            if(url == null) {
                MessageBuilder.createMessage(BotDatabase.getInstance().getSentences().get("MUSIC_LOAD_NO_MATCHES"))
                        .setChannel(Bot.getManager(event.getGuild()).getMusicChannel())
                        .setTemporary(true).setInstant(true)
                        .build().send();

                return true;
            }
        }

        double skippable = Double.parseDouble( BotDatabase.getInstance().getConfig().get("UNSKIPPABLE_CHANCE") );
        var song = MusicTasteManager.getForGuild(event.getGuild()).getSong(url);
        if(song != null) {
            skippable = skippable + Double.parseDouble( BotDatabase.getInstance().getConfig().get("MUSIC_TASTE_SKIP") ) * song.getRating();
        }

        TrackRequestBuilder.createRequest(url, event.getGuild())
                .setUser(event.getAuthor())
                .setSkippable(!RandomUtil.randomChance(skippable))
                .build().accept();

        return true;
    }
}
