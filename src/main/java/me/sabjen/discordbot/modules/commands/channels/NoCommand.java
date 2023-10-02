package me.sabjen.discordbot.modules.commands.channels;

import me.sabjen.discordbot.modules.commands.base.BotCommand;
import me.sabjen.discordbot.database.BotDatabase;
import me.sabjen.discordbot.utils.messages.MessageBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class NoCommand extends BotCommand {
    @Override
    public boolean checkEvent(MessageReceivedEvent event) {
        if(event.getMessage().getContentRaw().startsWith("-")) {
            executeEvent(event);
            return true;
        }

        return false;
    }

    private void executeEvent(MessageReceivedEvent event) {
        event.getMessage().delete().queue();

        MessageBuilder.createMessage(BotDatabase.getInstance().getSentences().get("DEFAULT_NOCOMMAND"))
                .setChannel(event.getChannel())
                .setInstant(true)
                .setTemporary(true)
                .build().send();
    }
}
