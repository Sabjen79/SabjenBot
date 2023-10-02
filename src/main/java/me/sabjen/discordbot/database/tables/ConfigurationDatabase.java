package me.sabjen.discordbot.database.tables;

import me.sabjen.discordbot.database.AbstractTable;
import me.sabjen.discordbot.utils.RandomUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class ConfigurationDatabase extends AbstractTable {

    public ConfigurationDatabase(Connection connection) {
        super("configuration", connection);
    }
}
