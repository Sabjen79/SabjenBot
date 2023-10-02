package me.sabjen.discordbot.database.tables;

import me.sabjen.discordbot.database.AbstractTable;
import me.sabjen.discordbot.utils.RandomUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConversationsDatabase extends AbstractTable {

    public ConversationsDatabase(Connection connection) {
        super("conversations", connection);
    }

    public List<String> getAll(String type) {
        try {
            var resultSet = connection.prepareStatement("SELECT TEXT FROM conversations WHERE TYPE LIKE '" + type + "'").executeQuery();
            var results = new ArrayList<String>();

            while(resultSet.next()) {
                results.add(resultSet.getString(1));
            }

            return results;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getRandom(String type) {
        try {
            var resultSet = connection.prepareStatement("SELECT TEXT FROM conversations WHERE TYPE LIKE '" + type + "'").executeQuery();
            var results = new ArrayList<String>();

            while(resultSet.next()) {
                results.add(resultSet.getString(1));
            }

            var result = RandomUtil.randomFrom(results);

            if(result != null) connection.prepareStatement("DELETE FROM CONVERSATIONS WHERE TEXT = '" + result + "'").execute();

            return result;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void add(String type, String text) {
        try {
            connection.prepareStatement("INSERT INTO conversations VALUES ( '" + type + "' , '" + text + "' )").execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
