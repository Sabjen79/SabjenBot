package me.sabjen.discordbot.database.tables;

import me.sabjen.discordbot.database.AbstractTable;
import me.sabjen.discordbot.database.BotDatabase;
import me.sabjen.discordbot.utils.RandomUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class SentencesDatabase extends AbstractTable {

    public SentencesDatabase(Connection connection) {
        super("sentences", connection);
    }

}
