package me.sabjen.discordbot.database;

import me.sabjen.discordbot.Bot;
import me.sabjen.discordbot.utils.RandomUtil;
import org.h2.tools.RunScript;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public abstract class AbstractTable {
    private String name;
    public final Connection connection;

    public AbstractTable(String name, Connection c) {
        this.name = name;
        this.connection = c;

        try {
            var results = connection.prepareStatement("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = '" + name.toUpperCase() + "'").executeQuery();
            if(!results.next()) {
                reload();
                Bot.logger.info("Database Restart: " + name);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void reload() {
        try {
            RunScript.execute(connection, new FileReader("./src/main/resources/database/" + name + ".sql"));
        } catch (FileNotFoundException e) {
            Bot.logger.warn("NU AM GASIT FISIERUL SQL");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String get(String str) {
        try {
            var resultSet = connection.prepareStatement("SELECT TEXT FROM " + name + " WHERE ID LIKE '" + str + "'").executeQuery();
            var results = new ArrayList<String>();

            while(resultSet.next()) {
                results.add(resultSet.getString(1));
            }

            return RandomUtil.randomFrom(results);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void set(String str, String value) {
        try {
            connection.prepareStatement("UPDATE " + name + " SET TEXT = '" + value + "' WHERE ID = '" + str + "'").execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
