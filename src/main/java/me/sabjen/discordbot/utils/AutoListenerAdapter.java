package me.sabjen.discordbot.utils;

import me.sabjen.discordbot.Bot;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public abstract class AutoListenerAdapter extends ListenerAdapter {
    public AutoListenerAdapter() {
        Bot.getJDA().addEventListener(this);
    }
}
