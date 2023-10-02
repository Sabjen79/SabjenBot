package me.sabjen.discordbot.modules.commands;

import me.sabjen.discordbot.Bot;
import me.sabjen.discordbot.modules.commands.base.BotCommand;
import me.sabjen.discordbot.modules.commands.channels.ImageReaction;
import me.sabjen.discordbot.modules.commands.channels.NoCommand;
import me.sabjen.discordbot.modules.commands.channels.RestrictChannels;
import me.sabjen.discordbot.modules.commands.channels.RestrictMusicChannel;
import me.sabjen.discordbot.modules.commands.music.ClearCommand;
import me.sabjen.discordbot.modules.commands.music.LoopCommand;
import me.sabjen.discordbot.modules.commands.music.PlayCommand;
import me.sabjen.discordbot.modules.commands.music.SkipCommand;
import me.sabjen.discordbot.utils.AutoListenerAdapter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class CommandManager extends AutoListenerAdapter {
    private final Guild guild;
    private final List<BotCommand> commands;

    public CommandManager(Guild g) {
        super();
        guild = g;

        commands = new ArrayList<>();

        commands.add(new RestrictChannels());

        commands.add(new PlayCommand());
        commands.add(new SkipCommand());
        commands.add(new LoopCommand());
        commands.add(new ClearCommand());
        commands.add(new RestrictMusicChannel());

        commands.add(new NoCommand());
        commands.add(new ImageReaction());
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(!event.isFromGuild() || event.getGuild().getIdLong() != guild.getIdLong() || Bot.isThisBot(event.getAuthor())) return;

        for(var command : commands) {
            boolean stop = command.checkEvent(event);

            if(stop) break;
        }
    }
}
