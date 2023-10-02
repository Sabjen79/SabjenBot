package me.sabjen.discordbot.modules.commands.base;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class BotCommand {
    public abstract boolean checkEvent(MessageReceivedEvent event);
}
