package me.sabjen.discordbot.utils.console;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.OutputStreamAppender;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CustomConsoleAppender extends AppenderBase<ILoggingEvent> {
    public static final List<ConsoleNotifier> notifiers = new ArrayList<>();
    private static final StringBuilder console = new StringBuilder();
    public static String getConsole() {return console.toString();}

    private ConcurrentMap<String, ILoggingEvent> eventMap
            = new ConcurrentHashMap<>();

    @Override
    protected void append(ILoggingEvent event) {
        eventMap.put(String.valueOf(System.currentTimeMillis()), event);

        System.out.println(event.toString());
        console.append(event).append("\n");

        for(var n : notifiers) n.notify(console.toString());
    }

    public Map<String, ILoggingEvent> getEventMap() {
        return eventMap;
    }


}
