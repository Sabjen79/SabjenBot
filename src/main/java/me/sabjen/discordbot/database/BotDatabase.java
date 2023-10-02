package me.sabjen.discordbot.database;

import me.sabjen.discordbot.database.tables.ConfigurationDatabase;
import me.sabjen.discordbot.database.tables.ConversationsDatabase;
import me.sabjen.discordbot.database.tables.SentencesDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BotDatabase {
    private static BotDatabase INSTANCE;
    public static BotDatabase getInstance() {
        if(INSTANCE == null) INSTANCE = new BotDatabase();
        return INSTANCE;
    }

    private final Connection connection;
    private SentencesDatabase sentences;
    private ConfigurationDatabase config;
    private ConversationsDatabase conversations;

    private BotDatabase() {
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection("jdbc:h2:./resources/database", "sa", "");

            if(connection == null) return;

            sentences = new SentencesDatabase(connection);
            config = new ConfigurationDatabase(connection);
            conversations = new ConversationsDatabase(connection);

        } catch (SQLException | ClassNotFoundException  e) {
            throw new RuntimeException(e);
        }

    }

    public Connection getConnection() {
        return connection;
    }

    public SentencesDatabase getSentences() {
        return sentences;
    }

    public ConfigurationDatabase getConfig() { return config; }

    public ConversationsDatabase getConversations() {
        return conversations;
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void reset() {
        sentences.reload();
        config.reload();
    }
}
