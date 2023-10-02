package me.sabjen.discordbot.utils;

import me.sabjen.discordbot.Bot;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomUtil {
    private static Random rand = new Random();

    public static long randomLong(int lower, int higher) {
        if(lower >= higher) return lower;
        return rand.nextLong(higher-lower) + lower;
    }

    public static int randomInt(int lower, int higher) {
        if(lower >= higher) return lower;
        return rand.nextInt(higher-lower) + lower;
    }

    public static <T> T randomFrom(T[] items) {
        if(items == null || items.length == 0) return null;
        return items[rand.nextInt(items.length)];
    }

    public static <T> T randomFrom(List<T> items) {
        if(items == null || items.isEmpty()) return null;
        return items.get(rand.nextInt(items.size()));
    }

    public static boolean randomChance(String chance) {
        return randomChance(Double.parseDouble(chance));
    }

    public static boolean randomChance(double chance) {
        return Math.random() <= chance;
    }

    public static User getRandomOnlineUser(Guild g) {
        ArrayList<User> availableUsers = new ArrayList<>();

        for(Member member : g.getMembers()) {
            if(member.getOnlineStatus() != OnlineStatus.OFFLINE && !Bot.isThisBot(member)) availableUsers.add(member.getUser());
        }

        for(VoiceChannel vc : g.getVoiceChannels()) {
            for(Member member : vc.getMembers()) {
                if(!Bot.isThisBot(member) && !availableUsers.contains(member.getUser())) availableUsers.add(member.getUser());
            }
        }

        if(availableUsers.isEmpty()) return null;

        return RandomUtil.randomFrom(availableUsers);
    }
}

