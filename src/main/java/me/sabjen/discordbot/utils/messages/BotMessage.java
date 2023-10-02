package me.sabjen.discordbot.utils.messages;

import me.sabjen.discordbot.utils.RandomUtil;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.utils.FileUpload;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class BotMessage {
    public String message;
    public MessageChannel channel;
    public Message reply;
    public boolean instant;
    public boolean temporary;
    public Consumer<Void> consumer;
    public FileUpload[] attachments;

    protected BotMessage(String message, MessageChannel channel, Message reply, boolean instant, boolean temporary, Consumer<Void> consumer, File... attachments) {
        this.message = message;
        this.channel = channel;
        this.reply = reply;
        this.instant = instant;
        this.temporary = temporary;
        this.consumer = consumer;

        this.attachments = new FileUpload[attachments.length];
        for(int i = 0; i < attachments.length; i++) {
            this.attachments[i] = FileUpload.fromData(attachments[i]);
        }
    }

    public void send() {
        sendMessage(false);
    }

    public void sendAsReply() {
        sendMessage(true);
    }

    private void sendMessage(boolean replyB) {
        Consumer<Void> run = (v) -> {
            var action = (!replyB) ? channel.sendMessage(message) : reply.reply(message);
            action.addFiles(attachments).queueAfter(getTypingSpeed(), TimeUnit.MILLISECONDS, (m) -> {

                if(consumer != null) consumer.accept(null);

                if(temporary) m.delete().queueAfter(5, TimeUnit.SECONDS);
            });
        };

        if(instant) run.accept(null);
        else channel.sendTyping().queueAfter(RandomUtil.randomInt(1000, 2000), TimeUnit.MILLISECONDS, run);
    }

    private int getTypingSpeed() {
        if(instant) return 1;
        return 1000 + Math.min(message.length()/9*1000, 5000);
    }
}
