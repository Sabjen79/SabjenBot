package me.sabjen.discordbot.modules.commands.channels;

import me.sabjen.discordbot.modules.commands.base.Cooldownable;
import me.sabjen.discordbot.modules.commands.base.BotCommand;
import me.sabjen.discordbot.database.BotDatabase;
import me.sabjen.discordbot.utils.RandomUtil;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.concurrent.TimeUnit;

public class ImageReaction extends BotCommand implements Cooldownable {
    @Override
    public boolean checkEvent(MessageReceivedEvent event) {
        if(!event.getMessage().getAttachments().isEmpty()) {
           reactTo(event);
        }

        return false;
    }

    private void reactTo(MessageReceivedEvent event) {
        if(onCooldown()) return;

        event.getMessage()
                .addReaction(Emoji.fromFormatted(BotDatabase.getInstance().getSentences().get("IMAGE_REACT%")))
                .queueAfter(RandomUtil.randomInt(5, 10), TimeUnit.SECONDS);

        setCooldown(RandomUtil.randomInt(20, 30), TimeUnit.MINUTES);
    }

    @Override
    public void onCooldownEnd() {

    }
}
