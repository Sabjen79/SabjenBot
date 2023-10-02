package me.sabjen.discordbot.modules.voice.music.request;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

public class TrackRequestBuilder {
    private TrackRequest request;

    private String url;
    private Guild guild;
    private User user = null;
    private boolean skippable = true;
    private boolean hidden = false;

    private TrackRequestBuilder(String url, Guild g) {
        this.url = url;
        this.guild = g;
    }

    public static TrackRequestBuilder createRequest(String url, Guild g) {
        return new TrackRequestBuilder(url, g);
    }

    public TrackRequestBuilder setUser(User u) {
        user = u;
        return this;
    }

    public TrackRequestBuilder setSkippable(boolean b) {
        skippable = b;
        return this;
    }

    public TrackRequestBuilder setHidden(boolean b) {
        hidden = b;
        return this;
    }

    public TrackRequest build() {
        return new TrackRequest(url, guild, user, skippable, hidden);
    }
}
