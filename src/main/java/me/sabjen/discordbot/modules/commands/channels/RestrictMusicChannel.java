package me.sabjen.discordbot.modules.commands.channels;

import me.sabjen.discordbot.Bot;
import me.sabjen.discordbot.modules.commands.base.BotCommand;
import me.sabjen.discordbot.database.BotDatabase;
import me.sabjen.discordbot.utils.messages.MessageBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class RestrictMusicChannel extends BotCommand {
    @Override
    public boolean checkEvent(MessageReceivedEvent event) {
        var channel = Bot.getManager(event.getGuild()).getMusicChannel();
        if(channel == null || event.getChannel().getIdLong() != channel.getIdLong()) return false;

        event.getMessage().delete().queue();

        MessageBuilder.createMessage(BotDatabase.getInstance().getSentences().get("DEFAULT_MUSIC_RESTRICT"))
                .setChannel(event.getChannel())
                .setInstant(true)
                .setTemporary(true)
                .build().send();

        return true;
    }
}
