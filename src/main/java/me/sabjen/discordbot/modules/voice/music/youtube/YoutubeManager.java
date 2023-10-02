package me.sabjen.discordbot.modules.voice.music.youtube;

import me.sabjen.discordbot.Bot;
import me.sabjen.discordbot.utils.JsonReader;

public class YoutubeManager {
    public static String format(String url) {
        String id = getVideoId(url);
        if(id == null) return null;
        return "https://youtu.be/" + id;
    }

    public static String getVideoId(String trackUrl) {
        if(trackUrl == null) return null;
        String s = trackUrl;
        if(s.startsWith("https://youtu.be/")) {
            s = s.substring(17);
            if(s.contains("?t=")) s = s.substring(0, s.indexOf("?t="));
            return s;
        }

        if(s.startsWith("https://www.youtube.com/watch?v=")) {
            s = s.substring(32);
            if(s.contains("&")) s = s.substring(0, s.indexOf("&"));
            return s;
        }

        return null;
    }

    public static String searchForQuery(String query) {
        String url = null;
        try {
            final String searchurl = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=1&order=relevance&q=" + query.replaceAll(" ", "+") + "&key=AIzaSyDCaZDUiC9aT5cArA4jvsUZTgHXGiN1O2c";

            String videoId = JsonReader.readJsonFromUrl(searchurl).getJSONArray("items").getJSONObject(0).getJSONObject("id").getString("videoId");

            url = "https://youtu.be/" + videoId;
        } catch (Exception e) {
            Bot.logger.warn("NU AM GASIT");
            return null;
        }

        return url;
    }
}
