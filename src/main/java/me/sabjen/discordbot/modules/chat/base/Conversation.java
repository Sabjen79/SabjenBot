package me.sabjen.discordbot.modules.chat.base;

import me.sabjen.discordbot.Bot;
import me.sabjen.discordbot.database.BotDatabase;
import me.sabjen.discordbot.modules.GuildManager;
import me.sabjen.discordbot.modules.chat.ChatManager;
import me.sabjen.discordbot.utils.RandomUtil;
import me.sabjen.discordbot.utils.messages.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Date;

public class Conversation {
    public static void newPrivateChat(MessageChannel m, User u) {
        var str = BotDatabase.getInstance().getConversations().getRandom("PRIVATE");
        if(str == null) return;

        Conversation conv = new Conversation(u.getIdLong(), m, u, str);
        ChatManager.getInstance().addConversation(conv);
    }

    public static void newMentionChat(MessageReceivedEvent event) {
        if(!event.getMessage().getContentRaw().contains( Bot.getUser().getName().toLowerCase() )) return;

        var str = BotDatabase.getInstance().getConversations().getRandom("MENTION");
        if(str == null) return;

        Conversation conv = new Conversation(event.getGuild().getIdLong(), event.getChannel(), event.getAuthor(), str);
        ChatManager.getInstance().addConversation(conv);
    }

    public static void newImageChat(MessageReceivedEvent event) {
        if(event.getMessage().getAttachments().size() == 0) return;

        var str = BotDatabase.getInstance().getConversations().getRandom("IMAGE");
        if(str == null) return;

        Conversation conv = new Conversation(event.getGuild().getIdLong(), event.getChannel(), event.getAuthor(), str);
        ChatManager.getInstance().addConversation(conv);
    }

    public static void newVoiceJoinChat(GuildVoiceUpdateEvent event) {
        var str = BotDatabase.getInstance().getConversations().getRandom("VOICE_JOIN");
        if(str == null) return;

        var g = event.getGuild();
        Conversation conv = new Conversation(g.getIdLong(), g.getSystemChannel(), event.getMember().getUser(), str);
        ChatManager.getInstance().addConversation(conv);
        conv.advance(null);
    }

    public static void newVoiceLeaveChat(GuildVoiceUpdateEvent event) {
        var str = BotDatabase.getInstance().getConversations().getRandom("VOICE_LEFT");
        if(str == null) return;

        var g = event.getGuild();
        Conversation conv = new Conversation(g.getIdLong(), g.getSystemChannel(), event.getMember().getUser(), str);
        ChatManager.getInstance().addConversation(conv);
        conv.advance(null);
    }

    public static void newAnnounceChat() {
        var str = BotDatabase.getInstance().getConversations().getRandom("ANNOUNCE");
        if(str == null) return;

        for(var guild : Bot.getGuilds()) {
            Conversation conv = new Conversation(guild.getIdLong(), guild.getSystemChannel(), null, str);
            ChatManager.getInstance().addConversation(conv);
            conv.advance(null);
        }
    }


    ///////////////////////////////////////////////////////////////////////////////////////////
    private Guild guild;
    private long cooldownID;
    private MessageChannel messageChannel;
    private User user;
    private String[] messages;
    private int messagesIndex;
    private Date lastUpdate;
    private boolean paused;

    public Conversation(long cid, MessageChannel m, User u, String msg) {
        this.cooldownID = cid;
        this.messageChannel = m;
        this.user = u;
        this.messages = msg.split("\\|\\|");
        this.messagesIndex = 0;
        this.paused = false;

        guild = Bot.getJDA().getGuildById(cid);
    }

    public boolean check(MessageReceivedEvent event) {
        if(event == null) return true;
        if(user == null) user = event.getAuthor();
        if(event.getAuthor().getIdLong() != user.getIdLong() ||
            event.getChannel().getIdLong() != messageChannel.getIdLong()) return false;

        return true;
    }

    public void advance(MessageReceivedEvent event) {
        if(isFinished() || paused) return;
        paused = true;

        lastUpdate = new Date();

        var builder = MessageBuilder.createMessage( messages[messagesIndex] );

        if(event != null) {
            builder.setReply(event.getMessage());
        }

        builder.setChannel(messageChannel);

        if(user != null) builder.replaceMentions(user);
        else if(guild != null) builder.replaceMentions(Bot.getManager(guild).getRandomUser());

        if(user == null && event != null) user = event.getAuthor();

        builder.addConsumer((v) -> paused = false)
                .advancedFormatting();

        if(messagesIndex == 0 && event != null) {
            builder.build().sendAsReply();
        } else builder.build().send();

        messagesIndex++;
    }

    public boolean isFinished() {
        return messagesIndex >= messages.length;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public long getCooldownID() { return cooldownID; }
}
