package me.sabjen.discordbot.modules.voice;

import me.sabjen.discordbot.Bot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;

public class VoiceAutoChannel {
    private final Guild guild;

    public VoiceAutoChannel(Guild g) {
        guild = g;
    }

    public AudioChannelUnion getBestChannel() {
        AudioChannelUnion channel = null;
        int maxScore = 0;

        for(var c : guild.getVoiceChannels()) {
            int cScore = channelScore((AudioChannelUnion) c);
            if(cScore > maxScore) {
                maxScore = cScore;
                channel = (AudioChannelUnion) c;
            }
        }

        return channel;
    }

    private int channelScore(AudioChannelUnion channel) {
        int score = 0;

        for(var member : channel.getMembers()) {
            var voiceState = member.getVoiceState();

            if(voiceState == null || voiceState.isDeafened() || member.getUser().isBot()) continue;

            score++;
        }

        var botChannel = Bot.getManager(guild).getConnectedChannel();
        if(score > 0 && botChannel != null && channel.getIdLong() == botChannel.getIdLong()) score++;

        return score;
    }
}
