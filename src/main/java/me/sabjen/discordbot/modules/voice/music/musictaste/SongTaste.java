package me.sabjen.discordbot.modules.voice.music.musictaste;

import net.dv8tion.jda.api.entities.Guild;

import java.util.Date;

public class SongTaste {
    private Guild guild;
    private String link;
    private int rating;
    private Date lastUpdated;

    public SongTaste(Guild guild, String link) {
        this.guild = guild;
        this.link = link;
        lastUpdated = new Date(0);
        rating = 0;
    }

    public Guild getGuild() {
        return guild;
    }

    public String getLink() {
        return link;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rate) {
        this.rating = rate;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return link + " - " + rating + " - " + lastUpdated.toString();
    }
}
