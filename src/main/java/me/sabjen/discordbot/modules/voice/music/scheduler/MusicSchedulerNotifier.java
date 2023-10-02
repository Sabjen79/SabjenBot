package me.sabjen.discordbot.modules.voice.music.scheduler;

import net.dv8tion.jda.api.entities.User;

public interface MusicSchedulerNotifier {
    default void onSongAdded(MusicTrack addedTrack) {}
    default void onSongPlayed(MusicTrack playedTrack, boolean looped) {}
    default void onSongSkipped(MusicTrack skippedTrack, User skipper, boolean success) {}

    default void onLoopChanged(User looper, boolean newLoop) {}

    default void onClear(User clearer) {}
}
