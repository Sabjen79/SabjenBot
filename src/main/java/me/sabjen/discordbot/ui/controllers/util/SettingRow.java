package me.sabjen.discordbot.ui.controllers.util;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import me.sabjen.discordbot.database.BotDatabase;
import me.sabjen.discordbot.ui.controllers.MainController;

import java.io.IOException;
import java.util.regex.Pattern;

public class SettingRow {
    private Label label;
    private Label description;
    private TextField field;
    private String database;

    public SettingRow(MainController controller, String sLabel, String sDescription, Pattern regex, String d) {
        try {
            Pane pane = FXMLLoader.load(getClass().getClassLoader().getResource("ui/SettingRow.fxml"));
            controller.getSettingsVbox().getChildren().add(pane);

            label = (Label) pane.lookup("#label");
            label.setText(sLabel);

            description = (Label) pane.lookup("#description");
            description.setText(sDescription);

            field = (TextField) pane.lookup("#field");
            field.setPromptText(sLabel);

            field.setOnKeyTyped(keyEvent -> {
                if(!regex.matcher(field.getText()).matches()) {
                    if(!field.getStyleClass().contains("badText")) field.getStyleClass().add("badText");
                } else {
                    field.getStyleClass().removeAll("badText");
                }

                controller.checkButton();
            });

            database = d;
        } catch (Exception ignored) {

        }
    }

    public TextField getField() {
        return field;
    }

    public void save() {
        var data = BotDatabase.getInstance().getConfig();

        data.set(database, field.getText());
    }

    public void reset() {
        var data = BotDatabase.getInstance().getConfig();

        field.setText(data.get(database));
    }
}
