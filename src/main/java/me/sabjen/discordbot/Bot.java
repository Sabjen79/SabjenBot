package me.sabjen.discordbot;

import me.sabjen.discordbot.database.BotDatabase;
import me.sabjen.discordbot.modules.GuildManager;
import me.sabjen.discordbot.modules.chat.ChatManager;
import me.sabjen.discordbot.modules.timers.BotTimers;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

public class Bot {
    public final static Logger logger = LoggerFactory.getLogger(Bot.class);
    public final static String prefix = "-";
    private static long ID;
    private static JDA jda;
    private static boolean DEBUG;

    private static List<GuildManager> guildManagers;

    private Bot() {}

    public static void start(boolean debugmode) {
        DEBUG = debugmode;
        try {
            BotDatabase.getInstance();

            jda = JDABuilder
                    .createDefault(BotDatabase.getInstance().getConfig().get("TOKEN"))
                    .setChunkingFilter(ChunkingFilter.ALL)
                    .setMemberCachePolicy(MemberCachePolicy.ALL)
                    .enableCache(CacheFlag.ONLINE_STATUS)
                    .enableIntents(EnumSet.allOf(GatewayIntent.class))
                    .setStatus((DEBUG) ? OnlineStatus.INVISIBLE : OnlineStatus.ONLINE)
                    .build().awaitReady();

            ID = jda.getSelfUser().getIdLong();

            guildManagers = new LinkedList<>();

            for(Guild guild : getGuilds()) {
                GuildManager g = new GuildManager(guild);
                guildManagers.add(g);
            }

            jda.addEventListener(ChatManager.getInstance());

            BotTimers.globalTimers();

        } catch (InterruptedException e) { throw new RuntimeException(e); }
    }

    public static boolean checkToken(String tok) {
        try {
            var tmpJda = JDABuilder.createDefault(tok.trim()).setStatus(OnlineStatus.INVISIBLE).build().awaitReady();

            tmpJda.shutdownNow();
        } catch (InvalidTokenException e) {
            return false;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    ///////////////////////////////////////////////////////////////////////
    //  GETTERS
    ///////////////////////////////////////////////////////////////////////

    public static JDA getJDA() { return jda; }

    public static long getId() {
        return ID;
    }

    public static User getUser() {
        return jda.getUserById(getId());
    }

    public static Member getMember(Guild g) {
        return g.getMember(getUser());
    }

    public static List<Guild> getGuilds() {
        if(jda == null) {
            try {
                var tmpJda = JDABuilder.createDefault(BotDatabase.getInstance().getConfig().get("TOKEN")).setStatus(OnlineStatus.INVISIBLE).build().awaitReady();

                var list = tmpJda.getGuilds();
                tmpJda.shutdownNow();

                return list;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        List<Guild> guilds = new ArrayList<>();
        for(var g : jda.getGuilds()) {
            if(debugCheck(g)) guilds.add(g);
        }

        return guilds;
    }

    public static GuildManager getManager(Guild g) {
        for(var manager : guildManagers) {
            if(manager.getGuild().getIdLong() == g.getIdLong()) return manager;
        }

        Bot.logger.warn("Tried to get wrong Guild: " + g.getName());
        throw new RuntimeException();
    }

    ///////////////////////////////////////////////////////////////////////
    //  CHECKERS
    ///////////////////////////////////////////////////////////////////////
    public static boolean isThisBot(Member member) { return isThisBot(member.getUser()); }
    public static boolean isThisBot(User user) {
        return user.getIdLong() == getId();
    }
    public static boolean debugCheck(Guild g) {
        if(!DEBUG) return true;
        else return BotDatabase.getInstance().getConfig().get("DEBUG_SERVER").equals(g.getId());
    }
}
