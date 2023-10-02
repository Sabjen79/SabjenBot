package me.sabjen.discordbot.ui.controllers.util;

import net.dv8tion.jda.api.entities.Guild;

public class GuildStringWrapper {
    private Guild guild;

    public GuildStringWrapper(Guild guild) {
        this.guild = guild;
    }

    public Guild getGuild() {
        return guild;
    }

    @Override
    public String toString() {
        return guild.getName();
    }

    public String toStringWithID() {
        return guild.getName() + " - " + guild.getId();
    }
}
