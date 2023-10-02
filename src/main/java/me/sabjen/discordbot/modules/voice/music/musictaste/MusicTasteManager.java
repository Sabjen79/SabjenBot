package me.sabjen.discordbot.modules.voice.music.musictaste;

import me.sabjen.discordbot.Bot;
import me.sabjen.discordbot.modules.voice.music.MusicManager;
import me.sabjen.discordbot.modules.voice.music.scheduler.MusicSchedulerNotifier;
import me.sabjen.discordbot.modules.voice.music.scheduler.MusicTrack;
import me.sabjen.discordbot.modules.voice.music.youtube.YoutubeManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.Date;

public class MusicTasteManager implements MusicSchedulerNotifier {
    public static MusicTasteManager getForGuild(Guild g) {
        return Bot.getManager(g).getMusicTasteManager();
    }

    private final Guild guild;
    private final MusicChannelHistory history;

    public MusicTasteManager(Guild guild, TextChannel musicChannel, MusicManager musicManager) {
        this.guild = guild;
        history = new MusicChannelHistory(musicChannel);

        new Thread(history::checkAndAdd).start();

        musicManager.getScheduler().addNotifier(this);
    }

    public String getRandomSong() {
        return MusicTasteDatabase.getInstance().getRandomSong(guild);
    }

    public SongTaste getSong(String url) {
        return MusicTasteDatabase.getInstance().getSong(guild, url);
    }

    public void addToRating(String url, int num) {
        var song = getSong(url);
        var expireDate = new Date(song.getLastUpdated().getTime() + 1000*60*60*3);

        if(song.getRating() + num < 0 || expireDate.after(new Date())) return;

        song.setRating(song.getRating() + num);
        song.setLastUpdated(new Date());

        MusicTasteDatabase.getInstance().modifySong(song);
    }

    public void addSong(String url) {
        if(url == null) return;
        MusicTasteDatabase.getInstance().addSong(new SongTaste(guild, url));
    }

    //========================================================================//


    @Override
    public void onSongAdded(MusicTrack addedTrack) {
        var url = YoutubeManager.format(addedTrack.getTrack().getInfo().uri);
        if(url == null) return;

        addSong(url);

        if(Bot.isThisBot(addedTrack.getUser())) {
            addToRating(url, -1);
        }
    }

    @Override
    public void onSongPlayed(MusicTrack playedTrack, boolean looped) {
        var url = YoutubeManager.format(playedTrack.getTrack().getInfo().uri);
        if(url == null || !looped) return;

        addToRating(url, 1);
    }

    @Override
    public void onSongSkipped(MusicTrack skippedTrack, User skipper, boolean success) {
        var url = YoutubeManager.format(skippedTrack.getTrack().getInfo().uri);
        if(url == null || success) return;

        addToRating(url, 2);
    }
}
