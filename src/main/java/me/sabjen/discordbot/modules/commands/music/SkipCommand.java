package me.sabjen.discordbot.modules.commands.music;

import me.sabjen.discordbot.modules.commands.base.BotCommand;
import me.sabjen.discordbot.modules.commands.base.MusicCommand;
import me.sabjen.discordbot.modules.voice.music.MusicManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class SkipCommand extends BotCommand implements MusicCommand {
    @Override
    public boolean checkEvent(MessageReceivedEvent event) {
        int c = checkMusicCommand(event, "skip");
        if(c != 1) return (c == -1);

        event.getMessage().delete().queue();

        MusicManager.getForGuild(event.getGuild()).skipTrack(event.getAuthor());
        return true;
    }
}
