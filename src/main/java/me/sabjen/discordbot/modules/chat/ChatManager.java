package me.sabjen.discordbot.modules.chat;

import me.sabjen.discordbot.Bot;
import me.sabjen.discordbot.modules.chat.base.Conversation;
import me.sabjen.discordbot.modules.voice.VoiceManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.*;

public class ChatManager extends ListenerAdapter {
    //=======================================================//
    private static ChatManager INSTANCE;
    public static ChatManager getInstance() {
        if(INSTANCE == null) INSTANCE = new ChatManager();
        return INSTANCE;
    }
    //=======================================================//
    private List<Conversation> chatList;
    private Map<Long, Date> cooldowns;

    private ChatManager() {
        chatList = new ArrayList<>();
        cooldowns = new HashMap<>();
        for(var g : Bot.getGuilds()) {
            cooldowns.put(g.getIdLong(), new Date());
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (Bot.isThisBot(event.getAuthor()) || (event.isFromGuild() && !Bot.debugCheck(event.getGuild()))) return;

        long id = event.isFromGuild() ? event.getGuild().getIdLong() : event.getAuthor().getIdLong();

        if (!onCooldown(id)) {
            if (event.isFromGuild()) {
                Conversation.newMentionChat(event);
                Conversation.newImageChat(event);
            } else {
                Conversation.newPrivateChat(event.getChannel(), event.getAuthor());
            }
        }

        chatList = new ArrayList<>(chatList.stream().filter((chat) -> !chat.isFinished()).toList());

        for (var c : chatList) {
            if (c.check(event)) {
                c.advance(event);
                break;
            }
        }
    }

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        var botChannel = Bot.getManager(event.getGuild()).getConnectedChannel();
        if(Bot.isThisBot(event.getMember()) || botChannel == null || onCooldown(event.getGuild().getIdLong())) return;

        if(event.getChannelJoined() == botChannel) {
            Conversation.newVoiceJoinChat(event);
        } else if(event.getChannelLeft() == botChannel) {
            Conversation.newVoiceLeaveChat(event);
        }
    }

    public void addConversation(Conversation c) {
        chatList.add(c);
        putOnCooldown(c.getCooldownID());
    }

    private boolean onCooldown(long id) {
        if(cooldowns.get(id) == null) return false;
        return cooldowns.get(id).after(new Date());
    }

    private void putOnCooldown(long id) {
        cooldowns.put(id, new Date(new Date().getTime() + 1000L*60*10));
    }
}
