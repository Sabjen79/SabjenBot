package me.sabjen.discordbot.modules.voice.music.scheduler;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.User;

public class MusicTrack {
    private AudioTrack track;
    private User user;
    private boolean skippable;
    private boolean hidden;

    public MusicTrack(AudioTrack track, User user, boolean skippable, boolean hidden) {
        this.track = track;
        this.user = user;
        this.skippable = skippable;
        this.hidden = hidden;
    }

    public AudioTrack getTrack() {
        return track;
    }

    public User getUser() {
        return user;
    }

    public boolean isSkippable() {
        return skippable;
    }

    public boolean isHidden() {
        return hidden;
    }
}
