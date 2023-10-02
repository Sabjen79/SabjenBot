package me.sabjen.discordbot.modules.voice.music.musictaste;

import me.sabjen.discordbot.Bot;
import me.sabjen.discordbot.database.AbstractTable;
import me.sabjen.discordbot.database.BotDatabase;
import me.sabjen.discordbot.modules.voice.music.musictaste.SongTaste;
import me.sabjen.discordbot.utils.RandomUtil;
import net.dv8tion.jda.api.entities.Guild;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

class MusicTasteDatabase extends AbstractTable {

    private static MusicTasteDatabase INSTANCE;
    public static MusicTasteDatabase getInstance() {
        if(INSTANCE == null) INSTANCE = new MusicTasteDatabase(BotDatabase.getInstance().getConnection());
        return INSTANCE;
    }

    //======================================================================================================//

    private MusicTasteDatabase(Connection connection) {
        super("music_taste", connection);
    }

    public void addSong(SongTaste song) {
        try {
            var results = connection.prepareStatement("SELECT * FROM music_taste WHERE ID LIKE '" + song.getLink() + "' AND GUILD_ID LIKE '" + song.getGuild().getId() + "'").executeQuery();

            if(!results.next()) {
                var p = connection.prepareStatement("INSERT INTO music_taste VALUES ( ?, ?, ?, ? )");
                p.setString(1, song.getGuild().getId());
                p.setString(2, song.getLink());
                p.setInt(3, song.getRating());
                p.setTimestamp(4, new Timestamp(song.getLastUpdated().getTime()));
                p.execute();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getRandomSong(Guild g) {
        if(RandomUtil.randomChance(BotDatabase.getInstance().getConfig().get("MUSIC_TASTE_CHANCE"))) { //musictaste
            try {
                var rateResult = connection.prepareStatement("SELECT MAX(RATING) FROM MUSIC_TASTE WHERE GUILD_ID = '" + g.getId() + "'").executeQuery();
                int maxRating = 0;

                if(rateResult.next()) {
                    maxRating = rateResult.getInt(1);
                }

                var p = connection.prepareStatement("SELECT ID FROM MUSIC_TASTE WHERE GUILD_ID = ? AND RATING = ?");
                p.setString(1, g.getId());
                p.setInt(2, maxRating);
                var result = p.executeQuery();
                List<String> songs = new ArrayList<>();

                while(result.next()) {
                    songs.add(result.getString(1));
                }

                return RandomUtil.randomFrom(songs);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        //else random

        try {
            var p = connection.prepareStatement("SELECT ID FROM MUSIC_TASTE WHERE GUILD_ID = ?");
            p.setString(1, g.getId());
            var result = p.executeQuery();
            List<String> songs = new ArrayList<>();

            while(result.next()) {
                songs.add(result.getString(1));
            }

            return RandomUtil.randomFrom(songs);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public SongTaste getSong(Guild g, String url) {
        try {
            var p = connection.prepareStatement("SELECT * FROM MUSIC_TASTE WHERE GUILD_ID = ? AND ID = ?");
            p.setString(1, g.getId());
            p.setString(2, url);

            var result = p.executeQuery();

            if(result.next()) {
                var song = new SongTaste(g, url);
                song.setRating(result.getInt(3));
                song.setLastUpdated(result.getTimestamp(4));

                return song;
            } else return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void modifySong(SongTaste song) {
        try {
            var p = connection.prepareStatement("UPDATE MUSIC_TASTE SET RATING = ?, LAST_UPDATED = ? WHERE GUILD_ID = ? AND ID = ?");
            p.setInt(1, song.getRating());
            p.setTimestamp(2, new Timestamp( song.getLastUpdated().getTime() ));
            p.setString(3, song.getGuild().getId());
            p.setString(4, song.getLink() );
            p.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Date getLastDate(Guild guild) {
        try {
            var results = connection.prepareStatement("SELECT LAST_UPDATED FROM music_taste_dates WHERE ID LIKE '" + guild.getId() + "'").executeQuery();

            if(results.next()) {
                return new Date(results.getTimestamp(1).getTime());
            } else {
                var pstmt = connection.prepareStatement("INSERT INTO music_taste_dates VALUES ( ?, ? )");
                pstmt.setString(1, guild.getId());
                pstmt.setTimestamp(2, new Timestamp(0));
                pstmt.execute();
                return getLastDate(guild);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void setLastDate(Guild guild, Date date) {
        try {
            var pstmt = connection.prepareStatement("UPDATE music_taste_dates SET LAST_UPDATED = ? WHERE ID = ?");
            pstmt.setString(2, guild.getId());
            pstmt.setTimestamp(1, new Timestamp(date.getTime()));
            pstmt.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
