package me.sabjen.discordbot.modules.voice.music.scheduler;

import me.sabjen.discordbot.Bot;
import me.sabjen.discordbot.database.BotDatabase;
import me.sabjen.discordbot.modules.voice.music.youtube.YoutubeManager;
import me.sabjen.discordbot.utils.messages.MessageBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BotMusicMessages implements MusicSchedulerNotifier {
    private Guild guild;

    public BotMusicMessages(Guild guild) {
        this.guild = guild;
    }

    @Override
    public void onSongAdded(MusicTrack addedTrack) {
        if(addedTrack.isHidden()) return;

        EmbedBuilder eb = new EmbedBuilder();
        var author = addedTrack.getUser();

        if(Bot.isThisBot(author)) {
            eb.setAuthor("Eu am aduagat:", null, author.getEffectiveAvatarUrl());
        } else {
            eb.setAuthor(author.getEffectiveName() + " a aduagat:", null, author.getEffectiveAvatarUrl());
        }
        eb.setTitle(addedTrack.getTrack().getInfo().title, addedTrack.getTrack().getInfo().uri);
        eb.addField("Author", addedTrack.getTrack().getInfo().author, true);
        eb.addField("Duration", new SimpleDateFormat("HH:mm:ss").format(new Date(addedTrack.getTrack().getInfo().length - 1000*60*60*2)), true);

        String id = YoutubeManager.getVideoId(addedTrack.getTrack().getInfo().uri);
        if(id != null) {
            eb.setThumbnail("https://img.youtube.com/vi/" + id + "/0.jpg");
        }
        eb.setColor(Color.WHITE);

        Bot.getManager(guild).getMusicChannel().sendMessageEmbeds(eb.build()).queue();
    }

    @Override
    public void onSongSkipped(MusicTrack skippedTrack, User user, boolean success) {
        if(!success) {
            MessageBuilder.createMessage(BotDatabase.getInstance().getSentences().get("MUSIC_SKIP_FAILED%"))
                    .setChannel(Bot.getManager(guild).getMusicChannel())
                    .setInstant(true).setTemporary(true)
                    .build().send();
            return;
        }

        EmbedBuilder eb = new EmbedBuilder();
        if(Bot.isThisBot(user)) {
            eb.setAuthor("Eu am dat skip la:", null, user.getEffectiveAvatarUrl());
        } else {
            eb.setAuthor(user.getEffectiveName() + " a dat skip la:", null, user.getEffectiveAvatarUrl());
        }

        String id = YoutubeManager.getVideoId(skippedTrack.getTrack().getInfo().uri);
        if(id != null) {
            eb.setThumbnail("https://img.youtube.com/vi/" + id + "/0.jpg");
        }
        eb.setTitle(skippedTrack.getTrack().getInfo().title);
        eb.setColor(Color.RED);

        Bot.getManager(guild).getMusicChannel().sendMessageEmbeds(eb.build()).queue();
    }

    @Override
    public void onLoopChanged(User looper, boolean newLoop) {
        EmbedBuilder eb = new EmbedBuilder();
        if(Bot.isThisBot(looper)) {
            eb.setAuthor("Eu am setat: ", null, looper.getEffectiveAvatarUrl());
        } else {
            eb.setAuthor(looper.getEffectiveName() + " a setat: ", null, looper.getEffectiveAvatarUrl());
        }
        if(newLoop) eb.setTitle("LOOP ON");
        else eb.setTitle("LOOP OFF");
        eb.setColor(Color.BLUE);

        Bot.getManager(guild).getMusicChannel().sendMessageEmbeds(eb.build()).queue();
    }

    @Override
    public void onClear(User clearer) {
        EmbedBuilder eb = new EmbedBuilder();
        if(Bot.isThisBot(clearer)) {
            eb.setAuthor("Eu am dat clear la lista", null, clearer.getEffectiveAvatarUrl());
        } else {
            eb.setAuthor(clearer.getEffectiveName() + " a dat clear la lista.", null, clearer.getEffectiveAvatarUrl());
        }

        eb.setColor(Color.GREEN);

        Bot.getManager(guild).getMusicChannel().sendMessageEmbeds(eb.build()).queue();
    }
}
