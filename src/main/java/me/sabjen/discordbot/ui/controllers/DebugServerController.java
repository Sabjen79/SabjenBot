package me.sabjen.discordbot.ui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import me.sabjen.discordbot.Bot;
import me.sabjen.discordbot.database.BotDatabase;
import me.sabjen.discordbot.ui.FXMLController;
import me.sabjen.discordbot.ui.UI;
import me.sabjen.discordbot.ui.controllers.util.GuildStringWrapper;

public class DebugServerController extends FXMLController {

    @FXML
    private ChoiceBox<GuildStringWrapper> choice;

    @FXML
    private Button submitButton;

    public void onSubmit() {
        var id = choice.getSelectionModel().getSelectedItem().getGuild().getId();

        BotDatabase.getInstance().getConfig().set("DEBUG_SERVER", id);

        UI.mainView();
    }

    @Override
    public void initialize() {
        submitButton.setDisable(true);

        ObservableList<GuildStringWrapper> list = FXCollections.observableArrayList();

        for(var g : Bot.getGuilds()) {
            list.add(new GuildStringWrapper(g));
        }

        choice.setItems(list);

        choice.getSelectionModel().selectedIndexProperty().addListener((value, b, index) -> {
            submitButton.setDisable(false);
        });
    }
}
