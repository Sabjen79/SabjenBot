package me.sabjen.discordbot.modules.commands.channels;

import me.sabjen.discordbot.Bot;
import me.sabjen.discordbot.modules.commands.base.BotCommand;
import me.sabjen.discordbot.database.BotDatabase;
import me.sabjen.discordbot.utils.messages.MessageBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class RestrictChannels extends BotCommand {
    @Override
    public boolean checkEvent(MessageReceivedEvent event) {
        var channel = Bot.getManager(event.getGuild()).getRestrictedChannel();
        if(channel == null || event.getChannel().getIdLong() == channel.getIdLong()) return false;

        for(var role : event.getMember().getRoles()) {
            if(role.getName().contains("carcera")) {
                event.getMessage().delete().queue();

                MessageBuilder.createMessage(BotDatabase.getInstance().getSentences().get("DEFAULT_RESTRICT"))
                        .setChannel(event.getChannel())
                        .replaceMentions(channel)
                        .setInstant(true)
                        .setTemporary(true)
                        .build().send();

                return true;
            }
        }

        return false;
    }
}
