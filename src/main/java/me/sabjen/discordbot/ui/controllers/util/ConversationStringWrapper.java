package me.sabjen.discordbot.ui.controllers.util;

public class ConversationStringWrapper {
    private String databaseName;
    private String stringName;

    public ConversationStringWrapper(String a, String b) {
        databaseName = a;
        stringName = b;
    }

    @Override
    public String toString() {
        return stringName;
    }

    public String getType() {
        return databaseName;
    }
}
