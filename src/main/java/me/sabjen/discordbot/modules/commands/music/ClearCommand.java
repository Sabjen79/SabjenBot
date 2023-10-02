package me.sabjen.discordbot.modules.commands.music;

import me.sabjen.discordbot.modules.commands.base.BotCommand;
import me.sabjen.discordbot.modules.commands.base.MusicCommand;
import me.sabjen.discordbot.modules.voice.music.MusicManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ClearCommand extends BotCommand implements MusicCommand {
    @Override
    public boolean checkEvent(MessageReceivedEvent event) {
        int c = checkMusicCommand(event, "clear");
        if(c != 1) return (c == -1);

        event.getMessage().delete().queue();

        MusicManager.getForGuild(event.getGuild()).clear(event.getAuthor());

        return true;
    }
}
