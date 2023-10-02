package me.sabjen.discordbot.modules.voice.music.request;

import me.sabjen.discordbot.modules.voice.music.MusicManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

public class TrackRequest {
    private String url;
    private Guild guild;
    private User user;
    private boolean skippable;
    private boolean hidden;

    //////////////////////////////////////////////////////////////////////////////////////////////////////

    public TrackRequest(String url, Guild guild, User user, boolean skippable, boolean hidden) {
        this.url = url;
        this.guild = guild;
        this.user = user;
        this.skippable = skippable;
        this.hidden = hidden;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////

    public void accept() {
        MusicManager.getForGuild(guild).loadTrack(this);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////

    public String getUrl() {
        return url;
    }

    public Guild getGuild() {
        return guild;
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
