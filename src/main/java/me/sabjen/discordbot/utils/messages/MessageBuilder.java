package me.sabjen.discordbot.utils.messages;

import me.sabjen.discordbot.Bot;
import me.sabjen.discordbot.utils.resource.ResourceManager;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MessageBuilder {
    private String message = null;
    private MessageChannel channel = null;
    private Message reply = null;

    private boolean instant = false;
    private boolean temporary = false;

    private final List<File> files;

    private Consumer<Void> consumer = (v) -> {};

    private MessageBuilder() {
        files = new ArrayList<>();
    }

    public BotMessage build() {
        File[] fa = new File[files.size()];
        for(int i = 0; i < files.size(); i++) fa[i] = files.get(i);

        return new BotMessage(this.message, this.channel, this.reply, this.instant, this.temporary, this.consumer, fa);
    }

    public static MessageBuilder createMessage(String s) {
        MessageBuilder builder = new MessageBuilder();
        builder.message = s;
        return builder;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public MessageBuilder setReply(Message msg) {
        this.reply = msg;
        this.channel = msg.getChannel();
        return this;
    }

    public MessageBuilder setChannel(MessageChannel channel) {
        this.channel = channel;
        return this;
    }

    public MessageBuilder setInstant(boolean instant) {
        this.instant = instant;
        return this;
    }

    public MessageBuilder setTemporary(boolean temporary) {
        this.temporary = temporary;
        return this;
    }

    public MessageBuilder replaceMentions(IMentionable mentionable) {
        this.message = this.message.replaceAll("@@", mentionable.getAsMention());
        return this;
    }

    public MessageBuilder addFile(File f) {
        this.files.add(f);
        return this;
    }

    public MessageBuilder advancedFormatting() {
        this.checkSequencing().checkForImages().checkForReacts();
        return this;
    }

    public MessageBuilder addConsumer(Consumer<Void> voidConsumer) {
        consumer = consumer.andThen(voidConsumer);
        return this;
    }

    private MessageBuilder checkForReacts() {
        while(message.contains("**")) {
            String f = message.substring( message.indexOf("**") );
            f = f.substring( 0, f.indexOf(" ") );

            message = message.replace(f, "");

            String emojiUnicode = f.substring(2);
            consumer = consumer.andThen((v) -> {
                if(reply != null) reply.addReaction(Emoji.fromUnicode(emojiUnicode)).queue();
            });
        }

        return this;
    }

    private MessageBuilder checkForImages() {
        while(message.contains("##")) {
            String f = message.substring( message.indexOf("##") );
            f = f.substring( 0, f.indexOf(" ") );

            message = message.replace(f, "");
            this.addFile(ResourceManager.getInstance().getRandomImage( f.substring(2) ));
        }

        return this;
    }

    private MessageBuilder checkSequencing() {
        if(message.contains("&&")) {
            String nextMessage = message.split("&&", 2)[1];
            message = message.split("&&")[0];

            var newBuilder = clone(nextMessage);

            consumer = (v) -> {
                newBuilder.send();
            };
        }

        return this;
    }

    protected BotMessage clone(String msg) {
        return MessageBuilder.createMessage(msg)
                .setChannel(channel)
                .setReply(reply)
                .setTemporary(temporary)
                .setInstant(instant)
                .addConsumer(consumer)
                .advancedFormatting()
                .build();
    }
}
