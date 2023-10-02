package me.sabjen.discordbot.modules.commands.base;

import me.sabjen.discordbot.Bot;
import me.sabjen.discordbot.database.BotDatabase;
import me.sabjen.discordbot.utils.messages.MessageBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface MusicCommand {
    default int checkMusicCommand(MessageReceivedEvent event, String command) {
        var musicChannel = Bot.getManager(event.getGuild()).getMusicChannel();
        if(musicChannel == null) return 0;

        String content = event.getMessage().getContentRaw().toLowerCase().trim();

        if(command.equals("play")) {
            if(!content.startsWith(Bot.prefix + command + " ") && !content.startsWith(Bot.prefix + command.charAt(0) + " ")) return 0;
        } else if(!content.equals(Bot.prefix + command) && !content.equals(Bot.prefix + command.charAt(0))) return 0;


        if(event.getChannel().getIdLong() != musicChannel.getIdLong()) {
            event.getMessage().delete().queue();

            MessageBuilder.createMessage(BotDatabase.getInstance().getSentences().get("MUSIC_WRONG_CHANNEL"))
                    .setChannel(event.getChannel())
                    .setTemporary(true).setInstant(true)
                    .replaceMentions(musicChannel)
                    .build().send();

            return -1;
        }

        return 1;
    }
}
